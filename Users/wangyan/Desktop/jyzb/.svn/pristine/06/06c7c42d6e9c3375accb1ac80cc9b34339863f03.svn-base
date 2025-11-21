package com.jsdc.rfid.service;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.common.utils.DateTimeUtils;
import com.jsdc.rfid.dao.RepairApplyDao;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.AssetsTypeMapper;
import com.jsdc.rfid.mapper.BrandManageMapper;
import com.jsdc.rfid.mapper.RepairApplyMemberMapper;
import com.jsdc.rfid.mapper.SysPostMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.ResultInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class RepairApplyService extends BaseService<RepairApplyDao, RepairApply> {

    @Autowired
    private CommonDataTools commonDataTools;

    @Autowired
    private AssetsManageService assetsManageService;

    @Autowired
    private AssetsTypeMapper assetsTypeMapper;

    @Autowired
    private BrandManageMapper brandManageMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private RepairApplyMemberMapper repairApplyMemberMapper;

    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private SysDictService sysDictService;

    /**
     * 维修 - 选择资产列表
     *
     * @return
     */
    public ResultInfo getLeaveUnused(Integer page, Integer limit, AssetsManage assetsManage, List<ReceiveAssets> assets, SysUser currentUser) {

        // 增加数据权限
        Integer post_id = currentUser.getPost();
        SysPost post = sysPostMapper.selectById(post_id);
        if (null == post) {
            return ResultInfo.error("当前用户岗位不存在");
        }
        Integer permission = post.getData_permission();
        List<Integer> userIds = new ArrayList<>();
        // 0-个人 1-本部门 2-全部
        if (0 == permission) {
            userIds.add(currentUser.getId());
        } else if (1 == permission) {
            sysUserService.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDepartment, currentUser.getDepartment())).forEach(user -> {
                userIds.add(user.getId());
            });
        }

        PageHelper.startPage(page, limit);

        LambdaQueryWrapper<AssetsManage> queryWrapper = Wrappers.<AssetsManage>lambdaQuery().eq(AssetsManage::getIs_del, G.ISDEL_NO);
//        queryWrapper.in("asset_state", 0,1);
        // 排除已经选择过的资产
        if (!CollectionUtils.isEmpty(assets)) {
            queryWrapper.notIn(AssetsManage::getId, assets.stream().map(ReceiveAssets::getAssets_id).collect(Collectors.toList()));
        }
        // 资产名称 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(assetsManage.getAsset_name()), AssetsManage::getAsset_name, assetsManage.getAsset_name());
        // 品类
        queryWrapper.eq(null != assetsManage.getAsset_type_id() && 0 != assetsManage.getAsset_type_id(), AssetsManage::getAsset_type_id, assetsManage.getAsset_type_id());
        // 如果 permission 不等于 2, 则增加数据权限, 符合条件的用户id,匹配使用人或者管理员
        if (2 != permission) {
            queryWrapper.and(wp -> wp.in(AssetsManage::getUse_people, userIds).or().in(AssetsManage::getAdmin_user, userIds));
        }

        // 得到集合
        List<AssetsManage> list = assetsManageService.selectList(queryWrapper);

        for (AssetsManage temp : list) {
            if (null != temp.getAsset_type_id()) {
                AssetsType assetsType = assetsTypeMapper.selectById(temp.getAsset_type_id());
                if (null != assetsType) {
                    temp.setAsset_type_name(assetsType.getAssets_type_name());
                }
            }
            if (null != temp.getBrand_id()) {
                BrandManage brand = brandManageMapper.selectById(temp.getBrand_id());
                temp.setBrand_name(brand == null ? "" : brand.getBrand_name());
            }
            if (null != temp.getAdmin_user()) {
                SysUser sysUser = sysUserService.selectById(temp.getAdmin_user());
                if (null != sysUser) {
                    temp.setAdmin_user_name(sysUser.getUser_name());
                }
            }
        }
        PageInfo<AssetsManage> pageInfo = new PageInfo<>(list);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 维修 - 根据id获取维修申请单
     *
     * @param id
     * @return
     */
    public RepairApply getRepairApplyById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        RepairApply repairApply = null;
        try {
            repairApply = selectById(Integer.parseInt(id));
            if (null == repairApply) {
                return null;
            }
            // 设置申请人
            SysUser user = sysUserService.selectById(repairApply.getRepair_user());
            if (null != user) {
                repairApply.setRepair_user_name(user.getUser_name());
            }
            // 找部门名称
            SysDepartment department = sysDepartmentService.selectById(repairApply.getDepartment_id());
            if (null != department) {
                repairApply.setDepartment_name(department.getDept_name());
            }
            // 创建日期 转换 str
            repairApply.setCreate_time_str(DateTimeUtils.formatDateChoseSimple(repairApply.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
            // 得到资产关联表
            List<RepairApplyMember> members = repairApplyMemberMapper.selectList(Wrappers.<RepairApplyMember>lambdaQuery()
                    .eq(RepairApplyMember::getRepair_apply_id, repairApply.getId())
                    .eq(RepairApplyMember::getIs_del, G.ISDEL_NO)
            );
            for (RepairApplyMember member : members) {
                AssetsManage assetsManage = assetsManageService.getById(member.getAsset_id());
                member.setAssetsManage(assetsManage);
            }
            repairApply.setApplyMembers(members);
        } catch (Exception e) {
            log.error("根据id获取维修申请单异常", e.getMessage());
        }
        return repairApply;
    }

    /**
     * 维修 - 保存或者更新维修申请单
     *
     * @param repairApply
     */
    public void saveOrUpdateRepairApply(RepairApply repairApply) {
        if (null != repairApply.getId() && selectById(repairApply.getId()) != null) {
            // 更新
            repairApply.setUpdate_time(new Date());
            repairApply.setUpdate_user(sysUserService.getUser().getId());
            updateById(repairApply);
            // 删除原来的关联表
            repairApplyMemberMapper.update(null, Wrappers.<RepairApplyMember>lambdaUpdate()
                    .set(RepairApplyMember::getIs_del, G.ISDEL_YES)
                    .eq(RepairApplyMember::getRepair_apply_id, repairApply.getId())
            );

            operationRecordService.addOtherOperationRecord(repairApply.getId(), null, "维修申请-编辑维修单", "5");
        } else {
            // 保存
            repairApply.setCreate_time(new Date());
            repairApply.setCreate_user(sysUserService.getUser().getId());
            repairApply.setIs_del(G.ISDEL_NO);
            // 设置初始状态
            repairApply.setStatus("1");
            // 设置单号
            String code = commonDataTools.getNo(DataType.CONS_WXZC_CODE.getType(), null);
            repairApply.setRepair_code(code);
            insert(repairApply);
            operationRecordService.addOtherOperationRecord(repairApply.getId(), null, "维修申请-新增维修单", "5");
        }
        if (!CollectionUtils.isEmpty(repairApply.getApplyMembers())) {
            for (RepairApplyMember member : repairApply.getApplyMembers()) {
                member.setRepair_apply_id(repairApply.getId());
                member.setCreate_time(new Date());
                member.setCreate_user(sysUserService.getUser().getId());
                member.setIs_del(G.ISDEL_NO);
                repairApplyMemberMapper.insert(member);

//                AssetsManage assetsManage = assetsManageService.getById(member.getAsset_id());
//                operationRecordService.addOperationRecord(repairApply.getId(), assetsManage.getAsset_code(), "暂存维修-修改领用单", "4");
            }
        }
    }

    /**
     * 维修 - 保存并提交维修申请单
     *
     * @param repairApply
     */
    public void saveAndSubmit(RepairApply repairApply, SysUser currentUser) {
        // 设置初始状态
        //repairApply.setStatus("2");
        repairApply.setStatus(G.PASS_APPROVED);
        if (null != repairApply.getId() && selectById(repairApply.getId()) != null) {
            // 更新
            repairApply.setUpdate_time(new Date());
            repairApply.setUpdate_user(currentUser.getId());
            updateById(repairApply);
            // 删除原来的关联表
            repairApplyMemberMapper.update(null, Wrappers.<RepairApplyMember>lambdaUpdate()
                    .set(RepairApplyMember::getIs_del, G.ISDEL_YES)
                    .eq(RepairApplyMember::getRepair_apply_id, repairApply.getId())
            );
            operationRecordService.addOtherOperationRecord(repairApply.getId(), null, "维修申请-编辑维修单", "5", currentUser);
        } else {
            // 保存
            repairApply.setCreate_time(new Date());
            repairApply.setCreate_user(currentUser.getId());
            repairApply.setIs_del(G.ISDEL_NO);
            // 设置单号
            String code = commonDataTools.getNo(DataType.CONS_WXZC_CODE.getType(), null);
            repairApply.setRepair_code(code);
            insert(repairApply);
            operationRecordService.addOtherOperationRecord(repairApply.getId(), null, "维修申请-新增维修单", "5", currentUser);
        }
        if (!CollectionUtils.isEmpty(repairApply.getApplyMembers())) {
            for (RepairApplyMember member : repairApply.getApplyMembers()) {
                member.setRepair_apply_id(repairApply.getId());
                member.setCreate_time(new Date());
                member.setCreate_user(currentUser.getId());
                member.setIs_del(G.ISDEL_NO);
                repairApplyMemberMapper.insert(member);

                AssetsManage assetsManage = assetsManageService.getById(member.getAsset_id());
                operationRecordService.addOperationRecord(repairApply.getId(), assetsManage.getAsset_code(), currentUser.getUser_name() + " 进行资产维修", "4", currentUser);
            }
        }
        // 启动流程
        //processMemberService.startProcess(G.PROCESS_WXZC, repairApply.getId(), currentUser.getId());
    }

    /**
     * 维修 - 删除维修申请单
     *
     * @param id
     */
    public void deleteRepairApply(String id) {
        if (StringUtils.isBlank(id)) {
            return;
        }
        Integer tempId = Integer.parseInt(id);
        update(null, Wrappers.<RepairApply>lambdaUpdate()
                .set(RepairApply::getIs_del, G.ISDEL_YES)
                .eq(RepairApply::getId, tempId)
        );
        operationRecordService.addOtherOperationRecord(tempId, null, "删除维修申请单", "5");
        // 删除关联表
        repairApplyMemberMapper.update(null, Wrappers.<RepairApplyMember>lambdaUpdate()
                .set(RepairApplyMember::getIs_del, G.ISDEL_YES)
                .eq(RepairApplyMember::getRepair_apply_id, tempId)
        );
    }

    /**
     * 维修 - 获取维修申请单列表
     *
     * @param page
     * @param limit
     * @param repairApply
     * @return
     */
    public ResultInfo getRepairApplyList(Integer page, Integer limit, RepairApply repairApply, SysUser currentUser) {
        // 增加数据权限
        Integer post_id = currentUser.getPost();
        SysPost post = sysPostMapper.selectById(post_id);
        if (null == post) {
            return ResultInfo.error("当前用户岗位不存在");
        }
        Integer permission = post.getData_permission();
        List<Integer> userIds = new ArrayList<>();
        // 0-个人 1-本部门 2-全部
        if (0 == permission) {
            userIds.add(currentUser.getId());
        } else if (1 == permission) {
            sysUserService.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDepartment, currentUser.getDepartment())).forEach(user -> {
                userIds.add(user.getId());
            });
        }

        LambdaQueryWrapper<RepairApply> queryWrapper = Wrappers.<RepairApply>lambdaQuery().eq(RepairApply::getIs_del, G.ISDEL_NO);
        // 单号
        queryWrapper.like(StringUtils.isNotBlank(repairApply.getRepair_code()), RepairApply::getRepair_code, repairApply.getRepair_code());
        // 如果 permission 不等于 2, 则增加数据权限, 符合条件的用户id,匹配使用人或者管理员
        if (2 != permission) {
            queryWrapper.in(RepairApply::getRepair_user, userIds);
        }
        // 申请人
        if (StringUtils.isNotBlank(repairApply.getRepair_user_name())){
            List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .like(SysUser::getUser_name, repairApply.getRepair_user_name())
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
            );
            List<Integer> tempIds = sysUsers.stream().map(SysUser::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(tempIds)){
                tempIds.add(-1);
            }
            queryWrapper.in(RepairApply::getRepair_user, tempIds);
        }
        // 申请日期
        if (StringUtils.isNotBlank(repairApply.getTimeStr())){
            String[] split = repairApply.getTimeStr().split(" - ");
            queryWrapper.ge(RepairApply::getCreate_time, split[0] + " 00:00:00");
            queryWrapper.le(RepairApply::getCreate_time, split[1] + " 23:59:59");
        }
        // 部门
        if (null != repairApply.getDepartment_id()){
            queryWrapper.eq(RepairApply::getDepartment_id, repairApply.getDepartment_id());
        }
//        queryWrapper.orderByDesc(RepairApply::getCreate_time);

        PageHelper.startPage(page, limit, "create_time desc");
        List<RepairApply> list = selectList(queryWrapper);

        PageInfo<RepairApply> pageInfo = new PageInfo<>(list);
        pageInfo.setList(afterList(list, currentUser));
        return ResultInfo.success(pageInfo);
    }

    /**
     * 维修 - 日志信息
     *
     * @param id
     * @return
     */
    public List<OperationRecord> getOperationList(Integer id) {
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("kind", "5");
        queryWrapper.orderByAsc("create_time");
        return operationRecordService.selectList(queryWrapper);
    }

    /**
     * 维修单送审
     *
     * @param id
     * @return
     */
    public ResultInfo submitRepairApply(Integer id) {
        RepairApply repairApply = selectById(id);
        if (!G.NOT_SUBMITTED.equals(repairApply.getStatus()) && !G.REJECTION_APPROVAL.equals(repairApply.getStatus())) {
            return ResultInfo.error("申请单状态必须是未送审或者被驳回");
        }
        repairApply.setStatus(G.NOT_APPROVED);
        updateById(repairApply);

        operationRecordService.addOtherOperationRecord(repairApply.getId(), null, "维修申请-维修单送审", "5");

        List<RepairApplyMember> list = repairApplyMemberMapper.selectList(Wrappers.<RepairApplyMember>lambdaQuery()
                .eq(RepairApplyMember::getRepair_apply_id, id)
                .eq(RepairApplyMember::getIs_del, G.ISDEL_NO)
        );
        for (RepairApplyMember temp : list) {
            int tempId = temp.getAsset_id();
            AssetsManage assetsManage = assetsManageService.selectById(tempId);
            operationRecordService.addOperationRecord(repairApply.getId(), assetsManage.getAsset_code(), sysUserService.getUser().getUser_name() + " 进行资产维修送审", "4");
        }
        // 启动流程
        processMemberService.startProcess(G.PROCESS_WXZC, id, sysUserService.getUser().getId());
        return ResultInfo.success();
    }

    /**
     * 维修单审批
     *
     * @param bean
     * @param page
     * @param limit
     * @return
     */
    public PageInfo<RepairApply> getListByPage(RepairApply bean, Integer page, Integer limit, SysUser currentUser) {
        PageHelper.startPage(page, limit, "create_time desc");
        LambdaQueryWrapper<RepairApply> wrapper = Wrappers.<RepairApply>lambdaQuery().eq(RepairApply::getIs_del, G.ISDEL_NO);
        // 审批状态
        if (!StringUtils.equals("-1", bean.getStatus())) {
            wrapper.eq(RepairApply::getStatus, bean.getStatus());
        }
        // 单号
        wrapper.like(StringUtils.isNotBlank(bean.getRepair_code()), RepairApply::getRepair_code, bean.getRepair_code());
        // 过滤后的ids
        wrapper.in(RepairApply::getId, bean.getIds());
        // 申请人
        if (StringUtils.isNotBlank(bean.getRepair_user_name())){
            List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .like(SysUser::getUser_name, bean.getRepair_user_name())
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
            );
            List<Integer> userIds = sysUsers.stream().map(SysUser::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIds)){
                userIds.add(-1);
            }
            wrapper.in(RepairApply::getRepair_user, userIds);
        }
        // 申请日期
        if (StringUtils.isNotBlank(bean.getTimeStr())){
            String[] split = bean.getTimeStr().split(" - ");
            wrapper.ge(RepairApply::getCreate_time, split[0] + " 00:00:00");
            wrapper.le(RepairApply::getCreate_time, split[1] + " 23:59:59");
        }
        // 部门
        if (null != bean.getDepartment_id()){
            wrapper.eq(RepairApply::getDepartment_id, bean.getDepartment_id());
        }
//        wrapper.orderByDesc(RepairApply::getCreate_time);
        List<RepairApply> list = selectList(wrapper);
        PageInfo<RepairApply> pageInfo = new PageInfo<>(list);
        pageInfo.setList(afterList(list, currentUser));
        return pageInfo;
    }

    /**
     * 处理集合后的数据
     *
     * @param list
     * @return
     */
    private List<RepairApply> afterList(List<RepairApply> list, SysUser currentUser) {
        List<RepairApply> newList = new ArrayList<>();
        for (RepairApply temp : list) {
            // 设置申请人
            SysUser user = sysUserService.selectById(temp.getRepair_user());
            if (null != user) {
                temp.setRepair_user_name(user.getUser_name());
            }
            // 找部门名称
            SysDepartment department = sysDepartmentService.selectById(temp.getDepartment_id());
            if (null != department) {
                temp.setDepartment_name(department.getDept_name());
            }

            // 得到当前流程节点
            processMemberService.getProcessDataByBusId(temp.getId(), G.PROCESS_WXZC, currentUser, temp);
            newList.add(temp);
        }
        return newList;
    }

    /**
     * 流程撤回
     *
     * @param id
     */
    public ResultInfo revoke(Integer id, SysUser currentUser) {
        RepairApply bean = selectById(id);
        if (G.NOT_APPROVED.equals(bean.getStatus())) {
            // 查询当前流程 查询字典表
            SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_WXZC);
            ProcessMember processMember = processMemberService.getProcessMemberByBusId(bean.getId(), dictionary.getValue());
            if (null == processMember) {
                throw new RuntimeException("未查询到流程");
            }
            if (processMember.getIs_revoke() != 1) {
                return ResultInfo.error("当前流程已经审批，不能撤回!");
            }
            processMember.setIsCH(1);
            processMemberService.removeProcessMember(processMember, JSON.toJSONString(bean));

            // 修改状态
            bean.setStatus(G.NOT_SUBMITTED);
            bean.setUpdate_time(new Date());
            bean.setUpdate_user(currentUser.getId());
            updateById(bean);

            operationRecordService.addOtherOperationRecord(bean.getId(), null, "维修申请-维修单撤回", "5");

//            List<RepairApplyMember> list = repairApplyMemberMapper.selectList(Wrappers.<RepairApplyMember>lambdaQuery()
//                    .eq(RepairApplyMember::getRepair_apply_id, id)
//                    .eq(RepairApplyMember::getIs_del, G.ISDEL_NO)
//            );
//            for (RepairApplyMember temp : list) {
//                int tempId = temp.getAsset_id();
//                AssetsManage assetsManage = assetsManageService.selectById(tempId);
//                operationRecordService.addOperationRecord(bean.getId(), assetsManage.getAsset_code(), "维修申请-维修单撤回", "4");
//            }
            return ResultInfo.success();
        }
        return ResultInfo.error("操作失败");
    }

    /**
     * 已审批列表
     *
     * @param bean
     * @param page
     * @param limit
     * @param sysUser
     * @return
     */
    public PageInfo<RepairApply> finishAdopt(RepairApply bean, Integer page, Integer limit, SysUser sysUser) {

        LambdaQueryWrapper<RepairApply> wrapper = Wrappers.<RepairApply>lambdaQuery().eq(RepairApply::getIs_del, G.ISDEL_NO);
        // 单号
        wrapper.like(StringUtils.isNotBlank(bean.getRepair_code()), RepairApply::getRepair_code, bean.getRepair_code());
        // 过滤后的ids
        wrapper.in(RepairApply::getId, bean.getIds());
        // 申请人
        if (StringUtils.isNotBlank(bean.getRepair_user_name())){
            List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                    .like(SysUser::getUser_name, bean.getRepair_user_name())
                    .eq(SysUser::getIs_del, G.ISDEL_NO)
            );
            List<Integer> userIds = sysUsers.stream().map(SysUser::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIds)){
                userIds.add(-1);
            }
            wrapper.in(RepairApply::getRepair_user, userIds);
        }
        // 申请日期
        if (StringUtils.isNotBlank(bean.getTimeStr())){
            String[] split = bean.getTimeStr().split(" - ");
            wrapper.ge(RepairApply::getCreate_time, split[0] + " 00:00:00");
            wrapper.le(RepairApply::getCreate_time, split[1] + " 23:59:59");
        }
        // 部门
        if (null != bean.getDepartment_id()){
            wrapper.eq(RepairApply::getDepartment_id, bean.getDepartment_id());
        }
//        wrapper.orderByDesc(RepairApply::getCreate_time);
        PageHelper.startPage(page, limit, "create_time desc");
        List<RepairApply> list = selectList(wrapper);
        PageInfo<RepairApply> pageInfo = new PageInfo<>(list);
        pageInfo.setList(afterList(list, sysUser));
        return pageInfo;
    }


    /**
     * 微信小程序—待审批分页
     *
     * @param bean
     * @param page
     * @param limit
     * @param currentUser
     * @return
     */
    public PageInfo<RepairApply> getWxListByPage(RepairApply bean, Integer page, Integer limit, SysUser currentUser) {
        PageHelper.startPage(page, limit, "create_time desc");
        LambdaQueryWrapper<RepairApply> wrapper = Wrappers.<RepairApply>lambdaQuery().eq(RepairApply::getIs_del, G.ISDEL_NO);
        // 审批状态
        wrapper.eq(RepairApply::getStatus, bean.getStatus());
        // 单号
        wrapper.like(StringUtils.isNotBlank(bean.getRepair_code()), RepairApply::getRepair_code, bean.getRepair_code());
        // 过滤后的ids
        wrapper.in(RepairApply::getId, bean.getIds());

//        wrapper.orderByDesc(RepairApply::getCreate_time);
        List<RepairApply> list = selectList(wrapper);
        for (RepairApply temp : list) {
            // 设置申请人
            SysUser user = sysUserService.selectById(temp.getRepair_user());
            if (null != user) {
                temp.setRepair_user_name(user.getUser_name());
            }
            // 找部门名称
            SysDepartment department = sysDepartmentService.selectById(temp.getDepartment_id());
            if (null != department) {
                temp.setDepartment_name(department.getDept_name());
            }
        }
        return new PageInfo<>(list);
    }
}
