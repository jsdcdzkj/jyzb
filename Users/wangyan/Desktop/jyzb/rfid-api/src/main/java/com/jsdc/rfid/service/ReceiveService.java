package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ReceiveDao;
import com.jsdc.rfid.enums.AssetsStatusEnums;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import vo.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReceiveService extends BaseService<ReceiveDao, Receive> {

    @Autowired
    private ReceiveMapper receiveMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ReceiveAssetsMapper receiveAssetsMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPostMapper sysPostMapper;
    @Autowired
    private AssetsManageMapper assetsManageMapper;
    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;
    @Autowired
    private CommonDataTools commonDataTools;

    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private AssetsTypeMapper assetsTypeMapper;

    @Autowired
    private BrandManageMapper brandManageMapper;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private ProcessMemberHistoryService processMemberHistoryService;

    @Autowired
    private ProcessConfigInfoService processConfigInfoService;

    @Autowired
    private ProcessConfigService processConfigService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysPostService sysPostService;

    /**
     * 分页展示闲置状态得资产
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public ResultInfo getLeaveUnused(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, AssetsManage assetsManage, List<ReceiveAssets> assets) {
        PageHelper.startPage(pageIndex, pageSize);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("asset_state", AssetsStatusEnums.IDLE.getType());
        if (!CollectionUtils.isEmpty(assets)) {
            queryWrapper.notIn("id", assets.stream().map(ReceiveAssets::getAssets_id).distinct().collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(assetsManage.getAsset_name())) {
            queryWrapper.like("asset_name", assetsManage.getAsset_name());
        }
        if (null != assetsManage.getAsset_type_id() && 0 != assetsManage.getAsset_type_id()) {
            queryWrapper.eq("asset_type_id", assetsManage.getAsset_type_id());
        }
        List<AssetsManage> list = assetsManageMapper.selectList(queryWrapper);

//        Map<Integer, SysDict> brandType = commonDataTools.getSysDictMap("brand_type");
        for (AssetsManage temp : list) {
            if (null != temp.getAsset_type_id()) {
                AssetsType assetsType = assetsTypeMapper.selectById(temp.getAsset_type_id());
                if (null != assetsType) {
                    temp.setAsset_type_name(assetsType.getAssets_type_name());
                }
            }

            if (null != temp.getBrand_id()) {
                BrandManage brandManage = brandManageMapper.selectById(temp.getBrand_id());
                temp.setBrand_name(null == brandManage ? "" : brandManage.getBrand_name());
            }
            if (null != temp.getAdmin_user()) {
                SysUser sysUser = sysUserMapper.selectById(temp.getAdmin_user());
                if (null != sysUser) {
                    temp.setAdmin_user_name(sysUser.getUser_name());
                }
            }


        }
        PageInfo<AssetsManage> pageInfo = new PageInfo<>(list);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 得到用户的键值对Map<id,SysUser>
     */
    public Map<Integer, SysUser> getUserNameMap() {
        List<SysUser> users = sysUserService.selectList(null);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyMap();
        }
        return users.stream().collect(Collectors.toMap(SysUser::getId, Function.identity(), (key1, key2) -> key2));
    }


    /**
     * 描述：领用申请 分页展示当前登陆用户的资产领用单
     * 作者：xuaolong
     *
     * @param receive
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<Receive> getListByPage(Receive receive, int pageIndex, int pageSize) {

        SysUser currentUser = null;
        if (Base.notEmpty(receive.getUserId())){
            currentUser = sysUserService.selectById(receive.getUserId());
        } else {
            currentUser = sysUserService.getUser();
        }
        // 数据权限
        if (null == receive|| null == receive.getIs_adopt() || 0 == receive.getIs_adopt()) {
            SysPost sysPost = sysPostService.selectById(currentUser.getPost());
            if (sysPost.getData_permission() == G.DATAPERMISSION_PERSONAL) {
                receive.setCreate_user(currentUser.getId());
            } else if (sysPost.getData_permission() == G.DATAPERMISSION_DEPT) {
                receive.setDepartment_id(currentUser.getDepartment());
            }
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<Receive> list = receiveMapper.selectReceiveByPage(receive, receive.getUse_id());
        Map<Integer, SysUser> userMap = getUserNameMap();
        if (!CollectionUtils.isEmpty(userMap)) {
            for (Receive temp : list) {

                if (null != temp.getStatus()) {
                    switch (temp.getStatus()) {
                        case "1":
                            temp.setStatusName("未送审");
                            break;
                        case "2":
                            temp.setStatusName("未审批");
                            break;
                        case "3":
                            temp.setStatusName("审批中");
                            break;
                        case "4":
                            temp.setStatusName("审批通过");
                            break;
                        case "5":
                            temp.setStatusName("审批驳回");
                            break;
                    }
                }

                temp.setUse_name(StringUtils.EMPTY);
                temp.setHandle_name(StringUtils.EMPTY);
                if (null == temp.getUse_id() || userMap.get(temp.getUse_id()) == null) {
                    continue;
                }
                temp.setUse_name(userMap.get(temp.getUse_id()).getUser_name());
//                temp.setHandle_name(userMap.get(temp.getHandle_id()).getUser_name());

                int id = temp.getId();
                List<ReceiveAssets> detail = receiveAssetsMapper.selectReceiveAssets(id);
                if (detail != null) {
                    temp.setDetail(detail);
                }
                // 得到当前流程节点
                processMemberService.getProcessDataByBusId(temp.getId(), G.PROCESS_ZCSL, currentUser, temp);
            }
        }
        return new PageInfo<>(list);
    }

    /**
     * 已审批数据
     *
     * @return
     */
    public PageInfo<Receive> finishAdopt(Receive receive, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Receive> list = receiveMapper.selectReceiveByPage(Receive.builder()
                .ids(receive.getIds())
                .receive_code(receive.getReceive_code())
                .use_name(receive.getUse_name())
                .department_id(receive.getDepartment_id())
                .timeStr(receive.getTimeStr())
                .build(), null);
        Map<Integer, SysUser> userMap = getUserNameMap();

        if (!CollectionUtils.isEmpty(userMap)) {
            for (Receive temp : list) {

                if (null != temp.getStatus()) {
                    switch (temp.getStatus()) {
                        case "1":
                            temp.setStatusName("未送审");
                            break;
                        case "2":
                            temp.setStatusName("未审批");
                            break;
                        case "3":
                            temp.setStatusName("审批中");
                            break;
                        case "4":
                            temp.setStatusName("审批通过");
                            break;
                        case "5":
                            temp.setStatusName("审批驳回");
                            break;
                    }
                }

                temp.setUse_name(StringUtils.EMPTY);
                temp.setHandle_name(StringUtils.EMPTY);
                if (null == temp.getUse_id() || userMap.get(temp.getUse_id()) == null) {
                    continue;
                }
                temp.setUse_name(userMap.get(temp.getUse_id()).getUser_name());

                int id = temp.getId();
                List<ReceiveAssets> detail = receiveAssetsMapper.selectReceiveAssets(id);
                if (detail != null) {
                    temp.setDetail(detail);
                }
            }
        }
        return new PageInfo<>(list);
    }

    public Receive getOneByReceiveId(Integer id) {
        Receive receive = receiveMapper.selectById(id);
        Integer use_id = receive.getUse_id();
        if (null != use_id) {
            SysUser sysUser = sysUserMapper.selectById(use_id);
            receive.setUse_name(sysUser.getUser_name());
        }
        Integer department_id = receive.getDepartment_id();
        if (null != department_id) {
            SysDepartment sysDepartment = sysDepartmentMapper.selectById(department_id);
            receive.setDepartment_name(sysDepartment.getDept_name());
        }
        Integer handle_id = receive.getHandle_id();
        if (null != handle_id) {
            SysUser sysUser = sysUserMapper.selectById(handle_id);
            receive.setHandle_name(sysUser.getUser_name());
        } else {
            receive.setHandle_name("");
        }
        if (null != receive.getCreate_time()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String real_date = simpleDateFormat.format(receive.getCreate_time());
            receive.setReal_create_time(real_date);
        } else {
            receive.setReal_create_time("");
        }
        if (null != receive.getUse_date()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String real_use_date = simpleDateFormat.format(receive.getUse_date());
            receive.setReal_use_date(real_use_date);
        } else {
            receive.setReal_use_date("");
        }
        if (null != receive.getStatus()) {
            switch (receive.getStatus()) {
                case "1":
                    receive.setStatusName("未送审");
                    break;
                case "2":
                    receive.setStatusName("未审批");
                    break;
                case "3":
                    receive.setStatusName("审批中");
                    break;
                case "4":
                    receive.setStatusName("审批通过");
                    break;
                case "5":
                    receive.setStatusName("审批驳回");
                    break;
            }
        }
        return receive;
    }

    /**
     * 新增领用单
     * 作者：xuaolong
     *
     * @param receive
     * @return
     */
    public ResultInfo addReceive(List<ReceiveAssets> list, Receive receive) {
        SysUser sysUser = sysUserService.getUser();

        String code = commonDataTools.getNo(DataType.RECEIVE.getType(), null);
        receive.setReceive_code(code);
        receive.setCreate_time(new Date());
        receive.setCreate_user(sysUser.getId());
        receive.setUse_id(sysUser.getId());
        receive.setDepartment_id(sysUser.getDepartment());
        receive.setIs_del(G.ISDEL_NO);
        receive.setStatus(G.NOT_SUBMITTED);
        insert(receive);
        operationRecordService.addOtherOperationRecord(receive.getId(), null, "领用申请-新增领用单", "1");
        //关联的资产
        for (ReceiveAssets temp : list) {
            int id = temp.getAssets_id();
            ReceiveAssets receiveAssets = new ReceiveAssets();
            receiveAssets.setAssets_id(id);
            receiveAssets.setReceive_id(receive.getId());

            receiveAssets.setIs_del(G.ISDEL_NO);
            receiveAssets.setCreate_time(new Date());
            receiveAssets.setCreate_user(sysUserService.getUser().getId());
            receiveAssetsMapper.insert(receiveAssets);
//            AssetsManage assetsManage = assetsManageMapper.selectById(id);
//            operationRecordService.addOperationRecord(receive.getId(), assetsManage.getAsset_code(), "领用申请-新增领用单", "4");
        }

        return ResultInfo.success();
    }

    /**
     * 新增领用单并送审
     * 作者：xuaolong
     *
     * @param receive
     * @return
     */
    public ResultInfo addReceiveAndSub(List<ReceiveAssets> list, Receive receive) {
        SysUser sysUser = sysUserService.getUser();

        String code = commonDataTools.getNo(DataType.RECEIVE.getType(), null);
        receive.setReceive_code(code);
        receive.setCreate_time(new Date());
        receive.setCreate_user(sysUser.getId());
        receive.setUse_id(sysUser.getId());
        receive.setDepartment_id(sysUser.getDepartment());
        receive.setIs_del(G.ISDEL_NO);
        //receive.setStatus(G.NOT_APPROVED);
        receive.setStatus(G.PASS_APPROVED);
        insert(receive);
        operationRecordService.addOtherOperationRecord(receive.getId(), null, "领用申请-新增领用单", "1");
        //关联的资产
        for (ReceiveAssets temp : list) {
            int id = temp.getAssets_id();
            ReceiveAssets receiveAssets = new ReceiveAssets();
            receiveAssets.setAssets_id(id);
            receiveAssets.setReceive_id(receive.getId());

            receiveAssets.setIs_del(G.ISDEL_NO);
            receiveAssets.setReceive_status(G.TO_BE_COLLECTED);
            receiveAssets.setCreate_time(new Date());
            receiveAssets.setCreate_user(sysUserService.getUser().getId());
            receiveAssetsMapper.insert(receiveAssets);
            AssetsManage assetsManage = assetsManageMapper.selectById(id);
            operationRecordService.addOperationRecord(receive.getId(), assetsManage.getAsset_code(), sysUser.getUser_name() + " 进行资产申领送审", "4");
        }
        // 启动流程
        //processMemberService.startProcess(G.PROCESS_ZCSL, receive.getId(), sysUser.getId());
        return ResultInfo.success();
    }


    /**
     * 修改领用单
     * 作者：xuaolong
     *
     * @param receive
     * @return
     */
    public ResultInfo updateReceive(List<Integer> list, Receive receive) {
        if (!G.NOT_SUBMITTED.equals(receive.getStatus()) && !G.REJECTION_APPROVAL.equals(receive.getStatus())) {
            return ResultInfo.error("领用单状态必须是未送审或者被驳回");
        }
        receive.setUpdate_time(new Date());
        receive.setUpdate_user(sysUserService.getUser().getId());
        updateById(receive);
        UpdateWrapper<ReceiveAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("receive_id", receive.getId());
        updateWrapper.set("is_del", G.ISDEL_YES);
        receiveAssetsMapper.update(null, updateWrapper);


        operationRecordService.addOtherOperationRecord(receive.getId(), null, "领用申请-编辑领用单", "1");
        //关联的资产
        Set<Integer> set = new HashSet<>();
        for (Integer temp : list) {
            if (null != temp) {
                set.add(temp);
            }
        }
        for (Integer temp : set) {
            int id = temp;
            ReceiveAssets receiveAssets = new ReceiveAssets();
            receiveAssets.setAssets_id(id);
            receiveAssets.setReceive_id(receive.getId());
            receiveAssets.setReceive_status(G.TO_BE_COLLECTED);
            receiveAssets.setIs_del(G.ISDEL_NO);
            receiveAssets.setCreate_time(new Date());
            receiveAssets.setCreate_user(sysUserService.getUser().getId());
            receiveAssetsMapper.insert(receiveAssets);
            AssetsManage assetsManage = assetsManageMapper.selectById(id);
//            operationRecordService.addOperationRecord(receive.getId(), assetsManage.getAsset_code(), "领用申请-修改领用单", "4");
        }
        return ResultInfo.success();
    }


    /**
     * 修改领用单
     * 作者：xuaolong
     *
     * @param receive
     * @return
     */
    public ResultInfo updateReceiveAndSub(List<Integer> list, Receive receive) {
        if (!G.NOT_SUBMITTED.equals(receive.getStatus()) && !G.REJECTION_APPROVAL.equals(receive.getStatus())) {
            return ResultInfo.error("领用单状态必须是未送审或者被驳回");
        }
        receive.setUpdate_time(new Date());
        receive.setUpdate_user(sysUserService.getUser().getId());
        receive.setStatus(G.NOT_APPROVED);
        updateById(receive);
        UpdateWrapper<ReceiveAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("receive_id", receive.getId());
        updateWrapper.set("is_del", G.ISDEL_YES);
        receiveAssetsMapper.update(null, updateWrapper);

        operationRecordService.addOtherOperationRecord(receive.getId(), null, "领用申请-编辑领用单并送审", "1");

        //关联的资产
        Set<Integer> set = new HashSet<>();
        for (Integer temp : list) {
            if (null != temp) {
                set.add(temp);
            }
        }
        for (Integer temp : set) {
            int id = temp;
            ReceiveAssets receiveAssets = new ReceiveAssets();
            receiveAssets.setAssets_id(id);
            receiveAssets.setReceive_id(receive.getId());
            receiveAssets.setReceive_status(G.TO_BE_COLLECTED);
            receiveAssets.setIs_del(G.ISDEL_NO);
            receiveAssets.setCreate_time(new Date());
            receiveAssets.setCreate_user(sysUserService.getUser().getId());
            receiveAssetsMapper.insert(receiveAssets);
            AssetsManage assetsManage = assetsManageMapper.selectById(id);
            operationRecordService.addOperationRecord(receive.getId(), assetsManage.getAsset_code(), sysUserService.getUser().getUser_name() + " 进行资产申领送审", "4");
        }
        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCSL, receive.getId(), sysUserService.getUser().getId());
        return ResultInfo.success();
    }

    /**
     * 删除领用单
     *
     * @param id
     * @return
     */
    public Boolean deleteReceive(Integer id) {
        Receive receive = receiveMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(receive.getStatus()) && !G.REJECTION_APPROVAL.equals(receive.getStatus())) {
            return false;
        }
        receive.setIs_del(G.ISDEL_YES);
        updateById(receive);
        UpdateWrapper<ReceiveAssets> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_del", G.ISDEL_YES);
        updateWrapper.eq("receive_id", receive.getId());
        receiveAssetsMapper.update(null, updateWrapper);
        return true;
    }

    /**
     * 领用单送审
     *
     * @param id
     * @return
     */
    public ResultInfo submitReceive(Integer id) {
        Receive receive = receiveMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(receive.getStatus()) && !G.REJECTION_APPROVAL.equals(receive.getStatus())) {
            return ResultInfo.error("领用单状态必须是未送审或者被驳回");
        }
        receive.setStatus(G.NOT_APPROVED);
        updateById(receive);

        operationRecordService.addOtherOperationRecord(receive.getId(), null, "领用申请-领用单送审", "1");


        QueryWrapper<ReceiveAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_id", receive.getId());
        queryWrapper.eq("is_del", "0");
        List<ReceiveAssets> list = receiveAssetsMapper.selectList(queryWrapper);
        for (ReceiveAssets temp : list) {
            int tempId = temp.getAssets_id();
            AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
            operationRecordService.addOperationRecord(receive.getId(), assetsManage.getAsset_code(), sysUserService.getUser().getUser_name() + " 进行资产申领送审", "4");
        }
        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCSL, id, sysUserService.getUser().getId());
        return ResultInfo.success();
    }


    /**
     * APP领用单送审
     *
     * @param id
     * @return
     */
    public ResultInfo submitAppReceive(Integer id, Integer userId) {
        Receive receive = receiveMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(receive.getStatus()) && !G.REJECTION_APPROVAL.equals(receive.getStatus())) {
            return ResultInfo.error("领用单状态必须是未送审或者被驳回");
        }
        receive.setStatus(G.NOT_APPROVED);
        updateById(receive);

        operationRecordService.addOtherAppOperationRecord(receive.getId(), null, "领用申请-领用单送审", "1", userId);


        QueryWrapper<ReceiveAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_id", receive.getId());
        queryWrapper.eq("is_del", "0");
        List<ReceiveAssets> list = receiveAssetsMapper.selectList(queryWrapper);
        for (ReceiveAssets temp : list) {
            int tempId = temp.getAssets_id();
            AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
//            operationRecordService.addOperationRecord(receive.getId(), assetsManage.getAsset_code(), "领用申请-领用单送审","4");
        }

        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCSL, id, userId);

        return ResultInfo.success();
    }

    /**
     * 领用单撤回
     * 进行事务回滚
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultInfo backReceive(Integer id) {
        Receive receive = receiveMapper.selectById(id);
        if (G.NOT_APPROVED.equals(receive.getStatus())) {
            // 查询当前流程 查询字典表
            SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCSL);
            ProcessMember processMember = processMemberService.getProcessMemberByBusId(receive.getId(), dictionary.getValue());
            if (null == processMember) {
                // 修改状态
                receive.setStatus(G.NOT_SUBMITTED);
                receive.setUpdate_time(new Date());
                receive.setUpdate_user(sysUserService.getUser().getId());
                updateById(receive);
                throw new RuntimeException("未查询到流程, 正在重置流程状态...");
            }
            if (processMember.getIs_revoke() != 1){
                return ResultInfo.error("当前流程已经审批，不能撤回!");
            }
            processMember.setIsCH(1);
            processMemberService.removeProcessMember(processMember, JSON.toJSONString(receive));

            // 修改状态
            receive.setStatus(G.NOT_SUBMITTED);
            receive.setUpdate_time(new Date());
            receive.setUpdate_user(sysUserService.getUser().getId());
            updateById(receive);

            operationRecordService.addOtherOperationRecord(receive.getId(), null, "领用申请-领用单撤回", "1");

//            QueryWrapper<ReceiveAssets> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("receive_id", receive.getId());
//            queryWrapper.eq("is_del", "0");
//            List<ReceiveAssets> list = receiveAssetsMapper.selectList(queryWrapper);
//            for (ReceiveAssets temp : list) {
//                int tempId = temp.getAssets_id();
//                AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
//                operationRecordService.addOperationRecord(receive.getId(), assetsManage.getAsset_code(), "领用申请-领用单撤回", "4");
//            }

            return ResultInfo.success();
        }

        return ResultInfo.error("操作失败");
    }

    /**
     * 打印单据信息
     *
     * @param receive
     * @return
     */
    public ResultInfo printBill(Receive receive) {
        if (G.NOT_SUBMITTED.equals(receive.getStatus()) || G.REJECTION_APPROVAL.equals(receive.getStatus())) {
            return ResultInfo.error("领用单状态不能是未送审或者被退回的");
        }
        return ResultInfo.success(receive);
    }


    /**
     * 分页展示领用管理
     *
     * @param receive
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultInfo collectionManageByPage(Receive receive, int pageIndex, int pageSize) {
        SysUser sysUser = sysUserService.getUser();
        int userId = sysUser.getId();
        Integer postId = sysUserMapper.selectById(userId).getPost();
        if (null == postId) {
            return ResultInfo.error("没有权限查看");
        }
        SysPost sysPost = sysPostMapper.selectById(postId);
        if (null == sysPost) {
            return ResultInfo.error("没有权限查看");
        }
        Integer data_permission = sysPost.getData_permission();



        //仅查看个人通过审核数据
        Integer userTemp = null;
        Integer departmentTemp = null;
        if (G.DATAPERMISSION_PERSONAL == data_permission) {
            //查看本部门数据
            userTemp = userId;
        } else if (G.DATAPERMISSION_DEPT == data_permission) {
            //查看所有部门数据
            departmentTemp = sysUser.getDepartment();
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<Receive> list = selectList(collectionManageByPageWrapper(receive, userTemp, departmentTemp));

        Map<Integer, SysUser> userMap = getUserNameMap();
        if (!CollectionUtils.isEmpty(userMap)) {
            for (Receive temp : list) {
                StringBuilder assets_name = new StringBuilder();
                temp.setUse_name(StringUtils.EMPTY);
                temp.setHandle_name(StringUtils.EMPTY);
                if (null == temp.getUse_id() || userMap.get(temp.getUse_id()) == null) {
                    continue;
                }
                temp.setUse_name(userMap.get(temp.getUse_id()).getUser_name());


                int id = temp.getId();
                List<ReceiveAssets> detail = receiveAssetsMapper.selectReceiveAssets(id);
                if (detail != null) {
                    temp.setDetail(detail);
                }
                if (detail != null) {
                    for (ReceiveAssets receiveAssets : detail) {
                        if (StringUtils.isNotBlank(receiveAssets.getAsset_name())) {
                            assets_name.append(receiveAssets.getAsset_name()).append(",");
                        }
                    }
                }
                if (StringUtils.isNotBlank(assets_name.toString())) {
                    temp.setAssets_names(assets_name.substring(0, assets_name.length() - 1));
                }
            }
        }
        PageInfo<Receive> pageInfo = new PageInfo<>(list);
        return ResultInfo.success(pageInfo);
    }

    private QueryWrapper<Receive> collectionManageByPageWrapper(Receive receive, Integer userId, Integer department_id){
        QueryWrapper<Receive> wrapper = Wrappers.<Receive>query().eq("is_del", "0");
        wrapper.eq("status", "4");
        wrapper.eq(null != userId, "create_user", userId);
        wrapper.eq(null != department_id, "department_id", department_id);
        if (null != receive) {
            if (StringUtils.isNotBlank(receive.getReceive_code())) {
                wrapper.like("receive_code", receive.getReceive_code());
            }
            // 资产名称
            if (StringUtils.isNotBlank(receive.getAssets_names())) {
                wrapper.like("assets_names", receive.getAssets_names());
            }

            // 日期区间
            if (StringUtils.isNotBlank(receive.getTimeStr())) {
                String[] split = receive.getTimeStr().split(" - ");
                String startTime = split[0] + " 00:00:00";
                String endTime = split[1] + " 23:59:59";
                wrapper.between("use_date", startTime, endTime);
            }
            // 领用人
            if (StringUtils.isNotBlank(receive.getUse_name())) {
                wrapper.inSql("use_id", "select id from sys_user where user_name like '%" + receive.getUse_name() + "%'");
            }

            // 部门
            if (null != receive.getDepartment_id()) {
                wrapper.eq("department_id", receive.getDepartment_id());
            }
        }
        wrapper.orderByDesc("create_time");
        return wrapper;
    }


    /**
     * 根据领用单ID 查询详细信息
     *
     * @param id
     * @return
     */
    public List<ReceiveAssets> getOneById(Integer id) {
        List<ReceiveAssets> list = receiveAssetsMapper.selectReceiveAssets(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<Integer, SysDict> brandType = commonDataTools.getSysDictMap("brand_type");
        for (ReceiveAssets temp : list) {

            if (null != temp.getReal_date()) {
                temp.setRealUseDate(simpleDateFormat.format(temp.getReal_date()));
            } else {
                temp.setRealUseDate("");
            }
            if (null != temp.getReturn_date()) {
                temp.setRealBackDate(simpleDateFormat.format(temp.getReturn_date()));
            } else {
                temp.setRealBackDate("");
            }
            if (null != temp.getReceive_status()) {
                if (temp.getReceive_status().equals("1")) {
                    temp.setStatusName("待领用确认");
                } else if (temp.getReceive_status().equals("2")) {
                    temp.setStatusName("待归还");
                } else {
                    temp.setStatusName("已归还");
                }
            } else {
                temp.setStatusName("");
            }
            if (null != temp.getReturn_deal_user()) {
                SysUser sysUser = sysUserService.selectById(temp.getReturn_deal_user());
                if (sysUser != null) {
                    temp.setReturn_deal_name(sysUser.getUser_name());
                } else {
                    temp.setReturn_deal_name("");
                }
            } else {
                temp.setReturn_deal_name("");
            }
            AssetsManage assetsManage = assetsManageMapper.selectById(temp.getAssets_id());
            BrandManage brandManage = brandManageMapper.selectById(temp.getBrand_id());
            temp.setBrand_name(null != brandManage ? brandManage.getBrand_name() : "");
        }
        return list;
    }


    /**
     * 根据领用单ID 分页查询详细信息
     *
     * @param id
     * @return
     */
    public PageInfo<ReceiveAssets> getOneById(Integer id, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ReceiveAssets> list = receiveAssetsMapper.selectReceiveAssets(id);
        for (ReceiveAssets temp : list) {
            Integer return_deal_user = temp.getReturn_deal_user();
            if (null != return_deal_user) {
                SysUser sysUser = sysUserMapper.selectById(return_deal_user);
                if (null != sysUser) {
                    temp.setReturn_deal_name(sysUser.getUser_name());
                }
            }
        }
        return new PageInfo<>(list);
    }


    /**
     * 领用确认
     *
     * @param id
     * @return
     */
    public ResultInfo confirm(Integer id) {
        ReceiveAssets receiveAssets = receiveAssetsMapper.selectById(id);
        if (!G.TO_BE_COLLECTED.equals(receiveAssets.getReceive_status())) {
            ResultInfo.error("状态必须是待领用确认");
        }
        AssetsManage assetsManageTemp1 = assetsManageMapper.selectById(receiveAssets.getAssets_id());
        if (assetsManageTemp1.getAsset_state().equals(AssetsStatusEnums.DISPOSE_OF.getType()) || assetsManageTemp1.getAsset_state().equals(AssetsStatusEnums.ABNORMAL.getType())) {
            receiveAssets.setReceive_status(G.RETURNED);
            receiveAssetsMapper.updateById(receiveAssets);

            Integer receive_id = receiveAssets.getReceive_id();
            QueryWrapper<ReceiveAssets> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_del", "0");
            queryWrapper.eq("receive_id", receive_id);
            List<ReceiveAssets> list = receiveAssetsMapper.selectList(queryWrapper);
            boolean flag = true;
            for (ReceiveAssets temp : list) {
                if (!G.RETURNED.equals(temp.getReceive_status())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Receive receive = receiveMapper.selectById(receive_id);
                receive.setFinish_sign("1");
                receive.setCancel_sign("0");
                receiveMapper.updateById(receive);
            }


            return ResultInfo.error("资产处于异常或者处置状态，不能被领用");
        }
        Date newDate = new Date();
        receiveAssets.setReceive_status(G.TO_BE_RETURNED);
        receiveAssets.setReal_date(newDate);
        receiveAssetsMapper.updateById(receiveAssets);

        Integer receive_id = receiveAssets.getReceive_id();
        Receive receive = selectById(receive_id);
        int use_id = receive.getUse_id();
        int department_id = receive.getDepartment_id();

        //更改资产管理的信息
        int assets_id = receiveAssets.getAssets_id();
        AssetsManage assetsManage = assetsManageMapper.selectById(assets_id);
        //修改状态为使用
        assetsManage.setAsset_state(AssetsStatusEnums.USE.getType());
        //存放部门
        assetsManage.setDept_id(department_id);
        //使用人
        assetsManage.setUse_people(use_id);
        //存放位置
        SysDepartment sysDepartment = sysDepartmentMapper.selectById(department_id);
        if (sysDepartment != null) {
            Integer dept_position = sysDepartment.getDept_position();
            assetsManage.setPosition_id(dept_position);
        }
        assetsManageMapper.updateById(assetsManage);

        receiveMapper.updateById(Receive.builder().id(receiveAssets.getReceive_id()).use_date(newDate).cancel_sign("0").build());


        int tempId = receiveAssets.getAssets_id();
        AssetsManage assetsManageTemp = assetsManageMapper.selectById(tempId);
        operationRecordService.addOtherOperationRecord(receive.getId(), assetsManageTemp.getAsset_code(), sysUserService.getUser().getUser_name() + " 进行资产申领领用", "1");
//        operationRecordService.addOtherOperationRecord(receive.getId(), assetsManageTemp.getAsset_code(), "领用管理-领用编号为" + assetsManageTemp.getAsset_code() + "的资产", "1");
        return ResultInfo.success();

    }


    /**
     * 领用归还
     *
     * @param id
     * @return
     */
    public ResultInfo returnAssets(Integer id) {
        SysUser sysUser = sysUserService.getUser();
        int userId = sysUser.getId();


        ReceiveAssets receiveAssets = receiveAssetsMapper.selectById(id);

        if (!G.TO_BE_RETURNED.equals(receiveAssets.getReceive_status())) {
            ResultInfo.error("状态必须是待归还");
        }
        receiveAssets.setReceive_status(G.RETURNED);
        receiveAssets.setReturn_date(new Date());
        receiveAssets.setReturn_deal_user(userId);
        receiveAssetsMapper.updateById(receiveAssets);
        //更改资产管理的信息
        int assets_id = receiveAssets.getAssets_id();
//        AssetsManage assetsManage = assetsManageMapper.selectById(assets_id);
//        assetsManage.setAsset_state(AssetsStatusEnums.IDLE.getType());
//        assetsManage.setUse_people(null);
//        assetsManageMapper.updateById(assetsManage);
        assetsManageMapper.update(null, Wrappers.<AssetsManage>lambdaUpdate()
                .set(AssetsManage::getUse_people, null)
                .set(AssetsManage::getAsset_state, AssetsStatusEnums.IDLE.getType())
                .eq(AssetsManage::getId, assets_id));


        Integer receive_id = receiveAssets.getReceive_id();
        QueryWrapper<ReceiveAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("receive_id", receive_id);
        List<ReceiveAssets> list = receiveAssetsMapper.selectList(queryWrapper);
        boolean flag = true;
        for (ReceiveAssets temp : list) {
            if (!G.RETURNED.equals(temp.getReceive_status())) {
                flag = false;
            }
        }
        if (flag) {
            Receive receive = receiveMapper.selectById(receive_id);
            receive.setFinish_sign("1");
            receiveMapper.updateById(receive);
        }


        int tempId = receiveAssets.getAssets_id();
        AssetsManage assetsManageTemp = assetsManageMapper.selectById(tempId);

        operationRecordService.addOtherOperationRecord(receive_id, assetsManageTemp.getAsset_code(), "领用管理-归还编号为" + assetsManageTemp.getAsset_code() + "的资产", "1");


        return ResultInfo.success();

    }

    /**
     * 获取当前用户所有的领用单
     *
     * @return
     */
    public List<Receive> getAllReceive(Integer userId) {
        List<Receive> list = receiveMapper.getAllReceive(userId);
        for (Receive temp : list) {
            if (null != temp.getUse_id()) {
                SysUser sysUser = sysUserMapper.selectById(temp.getUse_id());
                if (null != sysUser) {
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null != temp.getHandle_id()) {
                SysUser sysUser = sysUserMapper.selectById(temp.getHandle_id());
                if (null != sysUser) {
                    temp.setHandle_name(sysUser.getUser_name());
                }
            }
            if (null != temp.getUse_date()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String tempTime = simpleDateFormat.format(temp.getUse_date());
                temp.setReal_use_date(tempTime);
            } else {
                temp.setReal_use_date("");
            }
            if (null != temp.getStatus()) {
                if (temp.getStatus().equals("1")) {
                    temp.setStatusName("未送审");
                } else if (temp.getStatus().equals("2")) {
                    temp.setStatusName("未审批");
                } else if (temp.getStatus().equals("3")) {
                    temp.setStatusName("审批中");
                } else if (temp.getStatus().equals("4")) {
                    temp.setStatusName("审批通过");
                } else if (temp.getStatus().equals("5")) {
                    temp.setStatusName("审批驳回");
                }
            }
        }
        return list;
    }

    /**
     * 微信小程序
     * 获取当前用户所有的领用单
     */
    public List<Receive> getWxReceiveList(Receive bean) {
        List<Receive> list = receiveMapper.getWxReceiveList(bean);
        for (Receive temp : list) {
            if (null != temp.getUse_id()) {
                SysUser sysUser = sysUserMapper.selectById(temp.getUse_id());
                if (null != sysUser) {
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null != temp.getHandle_id()) {
                SysUser sysUser = sysUserMapper.selectById(temp.getHandle_id());
                if (null != sysUser) {
                    temp.setHandle_name(sysUser.getUser_name());
                }
            }
            if (null != temp.getUse_date()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String tempTime = simpleDateFormat.format(temp.getUse_date());
                temp.setReal_use_date(tempTime);
            } else {
                temp.setReal_use_date("");
            }
            if (null != temp.getStatus()) {
                switch (temp.getStatus()) {
                    case "1":
                        temp.setStatusName("未送审");
                        break;
                    case "2":
                        temp.setStatusName("未审批");
                        break;
                    case "3":
                        temp.setStatusName("审批中");
                        break;
                    case "4":
                        temp.setStatusName("审批通过");
                        break;
                    case "5":
                        temp.setStatusName("审批驳回");
                        break;
                }
            }
        }
        return list;
    }


    public void addAPPReceive(String ids, String remark, Integer userId, Integer type) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        String code = commonDataTools.getNo(DataType.RECEIVE.getType(), null);
        Receive receive = new Receive();
        receive.setUse_id(sysUser.getId());
        receive.setCreate_user(sysUser.getId());
        receive.setDepartment_id(sysUser.getDepartment());
        receive.setRemark(remark);
        receive.setReceive_code(code);
        receive.setCreate_time(new Date());
        receive.setIs_del(G.ISDEL_NO);
        if (1 == type) {
            receive.setStatus(G.NOT_SUBMITTED);
        } else {
            receive.setStatus(G.NOT_APPROVED);
        }

        receiveMapper.insert(receive);

        if (1 == type) {
            operationRecordService.addOtherAppOperationRecord(receive.getId(), null, "领用申请-新增领用单", "1", sysUser.getId());
        } else {
            operationRecordService.addOtherAppOperationRecord(receive.getId(), null, "领用申请-新增领用单并送审", "1", sysUser.getId());
            // 启动流程
            processMemberService.startProcess(G.PROCESS_ZCSL, receive.getId(), userId);
        }

        String[] idList = ids.split("-");
        for (String temp : idList) {
            System.out.println(temp);
            Integer assetsId = Integer.parseInt(temp);
            ReceiveAssets receiveAssets = new ReceiveAssets();
            receiveAssets.setAssets_id(assetsId);
            receiveAssets.setReceive_id(receive.getId());
            receiveAssets.setReceive_status(G.TO_BE_COLLECTED);
            receiveAssets.setIs_del(G.ISDEL_NO);
            receiveAssets.setCreate_time(new Date());
            receiveAssets.setCreate_user(sysUser.getId());
            AssetsManage assetsManage = assetsManageMapper.selectById(assetsId);
//            operationRecordService.addOperationRecord(receive.getId(), assetsManage.getAsset_code(), "领用申请-新增领用单","4");
            receiveAssetsMapper.insert(receiveAssets);
        }
    }

    //查询单条记录日志信息
    public List<OperationRecord> getOperationRecordList(Integer id) {
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("kind", "1");
        queryWrapper.orderByAsc("create_time");
        return operationRecordService.selectList(queryWrapper);
    }

    //查询最新单条记录日志信息
    public OperationRecord getOperationRecordData(Integer id) {
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("kind", "1");
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("limit 1");
        return operationRecordService.selectOne(queryWrapper);
    }

    /**
     */
    public PageInfo<Receive> getUserListByPage(Receive receive, int pageIndex, int pageSize) {

        SysUser currentUser = null;
        if (Base.notEmpty(receive.getUserId())){
            currentUser = sysUserService.selectById(receive.getUserId());
        } else {
            currentUser = sysUserService.getUser();
        }
        // 数据权限
//        if (null == receive|| null == receive.getIs_adopt() || 0 == receive.getIs_adopt()) {
//            SysPost sysPost = sysPostService.selectById(currentUser.getPost());
//            if (sysPost.getData_permission() == G.DATAPERMISSION_PERSONAL) {
//                receive.setCreate_user(currentUser.getId());
//            } else if (sysPost.getData_permission() == G.DATAPERMISSION_DEPT) {
//                receive.setDepartment_id(currentUser.getDepartment());
//            }
//        }
        receive.setCreate_user(currentUser.getId());
        PageHelper.startPage(pageIndex, pageSize);
        List<Receive> list = receiveMapper.selectUserReceiveByPage(receive, receive.getUse_id());
        Map<Integer, SysUser> userMap = getUserNameMap();
        if (!CollectionUtils.isEmpty(userMap)) {
            for (Receive temp : list) {

                if (null != temp.getStatus()) {
                    switch (temp.getStatus()) {
                        case "1":
                            temp.setStatusName("未送审");
                            break;
                        case "2":
                            temp.setStatusName("未审批");
                            break;
                        case "3":
                            temp.setStatusName("审批中");
                            break;
                        case "4":
                            temp.setStatusName("审批通过");
                            break;
                        case "5":
                            temp.setStatusName("审批驳回");
                            break;
                    }
                }

                temp.setUse_name(StringUtils.EMPTY);
                temp.setHandle_name(StringUtils.EMPTY);
                if (null == temp.getUse_id() || userMap.get(temp.getUse_id()) == null) {
                    continue;
                }
                temp.setUse_name(userMap.get(temp.getUse_id()).getUser_name());
//                temp.setHandle_name(userMap.get(temp.getHandle_id()).getUser_name());

                int id = temp.getId();
                List<ReceiveAssets> detail = receiveAssetsMapper.selectReceiveAssets(id);
                if (detail != null) {
                    temp.setDetail(detail);
                }
                // 得到当前流程节点
                ProcessConfig process = processMemberService.getInfoByBusId(temp.getId(), G.PROCESS_ZCSL);
                if (null != process && !CollectionUtils.isEmpty(process.getCurrentInfos())) {
                    for (ProcessConfigInfo info : process.getCurrentInfos()) {
                        if (info.getNode_type() == 0 && currentUser.getPost().equals(info.getNode_handler())) {
                            temp.setCurrentTaskName(info.getNode_name());
                            temp.setInfo(info);
                        } else if (info.getNode_type() == 1 && currentUser.getId().equals(info.getNode_handler())) {
                            temp.setCurrentTaskName(info.getNode_name());
                            temp.setInfo(info);
                        } else if (info.getNode_type() == 2 && process.getProcessMember().getApply_dept_leader_id().equals(currentUser.getId())) {
                            temp.setCurrentTaskName(info.getNode_name());
                            temp.setInfo(info);
                        }
                    }
                }
            }
        }
        return new PageInfo<>(list);
    }


    /**
     * 微信小程序
     * 资产申领中数据
     */
    public List<Receive> getWxReceivingList(Receive bean) {
        return receiveMapper.getWxReceivingList(bean);
    }
}
