package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ChangeInfoDao;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class ChangeInfoService extends BaseService<ChangeInfoDao, ChangeInfo> {

    private final ChangeInfoMapper changeInfoMapper;

    private final SysUserService sysUserService;

    private final ChangeDetailMapper changeDetailMapper;

    private final SysUserMapper sysUserMapper;

    private final SysPostMapper sysPostMapper;

    private final AssetsManageService assetsManageService;

    private final CommonDataTools commonDataTools;

    private final SysDepartmentService sysDepartmentService;

    private final SysPositionService sysPositionService;

    private final OperationRecordService operationRecordService;

    private final AssetsManageMapper assetsManageMapper;

    private final AssetsTypeMapper assetsTypeMapper;

    private final ProcessMemberService processMemberService;

    private final SysDictService sysDictService;

    private final BrandManageMapper brandManageMapper;

    private final ProcessMemberHistoryService processMemberHistoryService;

    /**
     * 分页展示符合条件得资产
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public ResultInfo getLeaveUnused(Integer pageIndex, Integer pageSize, AssetsManage assetsManage, List<ReceiveAssets> assets){

        PageHelper.startPage(pageIndex, pageSize);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.in("asset_state", 0,1);
        if (!CollectionUtils.isEmpty(assets)){
            for (ReceiveAssets temp : assets){
                queryWrapper.ne("id",temp.getAssets_id());
            }
        }
        if (StringUtils.isNotBlank(assetsManage.getAsset_name())){
            queryWrapper.like("asset_name",assetsManage.getAsset_name());
        }
        if (null != assetsManage.getAsset_type_id() && 0 != assetsManage.getAsset_type_id()){
            queryWrapper.eq("asset_type_id",assetsManage.getAsset_type_id());
        }
        if (null != assetsManage.getDept_id() && 0 != assetsManage.getDept_id()){
            queryWrapper.eq("dept_id",assetsManage.getDept_id());
        }
        if (null != assetsManage.getUse_people() && 0 != assetsManage.getUse_people()){
            queryWrapper.eq("use_people",assetsManage.getUse_people());
        }
        List<AssetsManage> list = assetsManageMapper.selectList(queryWrapper);

        Map<Integer, SysDict> brandType = commonDataTools.getSysDictMap("brand_type");
        for (AssetsManage temp : list){
            if (null != temp.getAsset_type_id()){
                AssetsType assetsType = assetsTypeMapper.selectById(temp.getAsset_type_id());
                if (null != assetsType){
                    temp.setAsset_type_name(assetsType.getAssets_type_name());
                }
            }
            if (null != temp.getBrand_id()){
                temp.setBrand_name(commonDataTools.getValue(brandType, temp.getBrand_id(), "label"));
            }
            if (null != temp.getAdmin_user()){
                SysUser sysUser = sysUserMapper.selectById(temp.getAdmin_user());
                if (null != sysUser){
                    temp.setAdmin_user_name(sysUser.getUser_name());
                }
            }


        }
        PageInfo<AssetsManage> pageInfo = new PageInfo<>(list);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 根据ID查询变更单信息
     * @param id
     * @return
     */

    public ChangeInfo getOneInfoById(Integer id){
        ChangeInfo changeInfo =  changeInfoMapper.selectById(id);
        if (null != changeInfo){
            if (null != changeInfo.getUse_id()){
                SysUser sysUser = sysUserMapper.selectById(changeInfo.getUse_id());
                if (null != sysUser){
                    changeInfo.setUse_name(sysUser.getUser_name());
                }
            }
            if (null != changeInfo.getDepartment_id()){
                SysDepartment department = sysDepartmentService.selectById(changeInfo.getDepartment_id());
                if (null != department){
                    changeInfo.setDepartment_name(department.getDept_name());
                }
            }
            if (null != changeInfo.getPosition()){
                SysPosition sysPosition = sysPositionService.selectById(changeInfo.getPosition());
                if (null != sysPosition){
                    changeInfo.setPlace_name(sysPosition.getPosition_name());
                }

            }

            if (null != changeInfo.getApply_user()){
                SysUser sysUser = sysUserMapper.selectById(changeInfo.getApply_user());
                if (null != sysUser){
                    changeInfo.setApply_name(sysUser.getUser_name());
                    Integer dept_id = sysUser.getDepartment();
                    if (null != dept_id){
                        SysDepartment sysDepartment = sysDepartmentService.selectById(dept_id);
                        changeInfo.setApply_dept_name(sysDepartment.getDept_name());
                    }
                }
            }
            if (null != changeInfo.getApply_date()){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                changeInfo.setReal_apply_date(simpleDateFormat.format(changeInfo.getApply_date()));
            }
            if (null != changeInfo.getStatus()){
                switch (changeInfo.getStatus()) {
                    case "1":
                        changeInfo.setStatus_name("闲置");
                        break;
                    case "2":
                        changeInfo.setStatus_name("使用");
                        break;
                    case "3":
                        changeInfo.setStatus_name("领用");
                        break;
                    case "4":
                        changeInfo.setStatus_name("借用");
                        break;
                    case "5":
                        changeInfo.setStatus_name("调拨");
                        break;
                    case "6":
                        changeInfo.setStatus_name("故障");
                        break;
                    case "7":
                        changeInfo.setStatus_name("处置");
                        break;
                }
            }
        }
        return changeInfo;

    }


    /**
     * 根据变更单Id查询资产信息
     * @param id
     * @return
     */
    public List<ChangeDetail> getInfoByChangeId(int id){
        List<ChangeDetail> changeDetail =changeDetailMapper.getDetailById(id);
        Map<Integer, SysDict> brandType = commonDataTools.getSysDictMap("brand_type");
        Integer count = 0;
        for (ChangeDetail temp : changeDetail){
            AssetsManage assetsManage = assetsManageMapper.selectById(temp.getAssets_id());
            if (null == assetsManage){
                continue;
            }
            BrandManage brandManage = brandManageMapper.selectById(assetsManage.getBrand_id());
            temp.setBrand_name(null == brandManage?"":brandManage.getBrand_name());
            count++;
            temp.setNum(count);
        }
        return changeDetail;
    }



    public PageInfo<ChangeInfo> getChangeListByPage(ChangeInfo changeInfo, int pageIndex, int pageSize){
        SysUser current = sysUserService.getUser();
        return getChangeListByPage(changeInfo, pageIndex, pageSize,current);
    }

    /**
     * 描述：变更 分页展示当前登陆用户的变更申请单
     * 作者：xuaolong
     * @param changeInfo
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<ChangeInfo> getChangeListByPage(ChangeInfo changeInfo, int pageIndex, int pageSize,SysUser current){
        Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
        Map<Integer, SysPosition> positionMap = commonDataTools.getPositionMap();
        PageHelper.startPage(pageIndex, pageSize);
        List<ChangeInfo> list = changeInfoMapper.getInfoByPage(changeInfo, changeInfo.getCreate_user());

        for (ChangeInfo temp : list) {
            temp.setUse_name(commonDataTools.getValue(userMap, temp.getUse_id(), "user_name"));
            temp.setApply_name(commonDataTools.getValue(userMap, temp.getApply_user(), "user_name"));

            temp.setPlace_name(commonDataTools.getValue(positionMap, temp.getPosition(), "position_name"));
            temp.setDetail(changeDetailMapper.getDetailById(temp.getId()));

            // 得到当前流程节点
            processMemberService.getProcessDataByBusId(temp.getId(), G.PROCESS_ZCBG, current, temp);
        }

        return new PageInfo<>(list);
    }

    /**
     * 已审批数据
     * @return
     */
    public PageInfo<ChangeInfo> finishAdopt(ChangeInfo changeInfo, Integer pageIndex, Integer pageSize) {
        Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
        Map<Integer, SysPosition> positionMap = commonDataTools.getPositionMap();
        PageHelper.startPage(pageIndex, pageSize);
        List<ChangeInfo> list = changeInfoMapper.getInfoByPage(ChangeInfo.builder()
                .ids(changeInfo.getIds())
                .change_code(changeInfo.getChange_code())
                .timeStr(changeInfo.getTimeStr())
                .apply_name(changeInfo.getApply_name())
                .build(), null);

        for (ChangeInfo temp : list) {
            temp.setUse_name(commonDataTools.getValue(userMap, temp.getUse_id(), "user_name"));
            temp.setApply_name(commonDataTools.getValue(userMap, temp.getApply_user(), "user_name"));

            temp.setPlace_name(commonDataTools.getValue(positionMap, temp.getPosition(), "position_name"));
            temp.setDetail(changeDetailMapper.getDetailById(temp.getId()));
        }

        return new PageInfo<>(list);
    }

    /**
     * 新增变更单(变更申请)
     * @param changeInfo
     * @return
     */
    public ResultInfo addChangeInfo(ChangeInfo changeInfo, List<ChangeDetail> list ){
        return addChangeInfo(changeInfo, list, sysUserService.getUser());
    }
    public ResultInfo addChangeInfo(ChangeInfo changeInfo, List<ChangeDetail> list , SysUser current){
        //BG+变更单来源（变更申请：SQ；手动创建：SD）+日期+三位自增码（每日重置）
        changeInfo.setSource(G.CHANGE_APPLICATION);
        String code = commonDataTools.getNo(DataType.CHANGE.getType(), changeInfo);

        changeInfo.setApply_user(current.getId());
        changeInfo.setChange_code(code);
        changeInfo.setApply_date(new Date());
        changeInfo.setCreate_time(new Date());
        changeInfo.setCreate_user(current.getId());
        changeInfo.setIs_del(G.ISDEL_NO);
        changeInfo.setApprove_status(G.NOT_SUBMITTED);
        changeInfo.setSource(G.CHANGE_APPLICATION);
        Integer userId = changeInfo.getUse_id();
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser != null){
            Integer department  = sysUser.getDepartment();
            changeInfo.setDepartment_id(department);
            if (null != department ){
                SysDepartment sysDepartment = sysDepartmentService.selectById(department);
                if (sysDepartment != null){
                    changeInfo.setPosition(sysDepartment.getDept_position());
                }
            }
        }

        insert(changeInfo);

        operationRecordService.addOtherAppOperationRecord(changeInfo.getId(),null,"变更申请-新增变更单","2", current.getId());

        //关联的资产
        for (ChangeDetail temp : list) {
            int id = temp.getAssets_id();
            ChangeDetail changeDetail = new ChangeDetail();
            changeDetail.setChange_id(changeInfo.getId());
            changeDetail.setAssets_id(id);
            changeDetail.setIs_del(G.ISDEL_NO);
            changeDetail.setCreate_time(new Date());
            changeDetail.setCreate_user(current.getId());
            changeDetailMapper.insert(changeDetail);
//            AssetsManage assetsManage = assetsManageMapper.selectById(id);
//            operationRecordService.addOperationRecord(changeInfo.getId(), assetsManage.getAsset_code(),"变更申请-新增变更单","6", current);
        }

        return ResultInfo.success();
    }

    /**
     * 新增变更单(变更申请)
     * @param changeInfo
     * @return
     */
    public ResultInfo addChangeInfoAndSub(ChangeInfo changeInfo, List<ChangeDetail> list ){
        return addChangeInfoAndSub(changeInfo, list, sysUserService.getUser());
    }
    public ResultInfo addChangeInfoAndSub(ChangeInfo changeInfo, List<ChangeDetail> list, SysUser current){
        //BG+变更单来源（变更申请：SQ；手动创建：SD）+日期+三位自增码（每日重置）
        changeInfo.setSource(G.CHANGE_APPLICATION);
        String code = commonDataTools.getNo(DataType.CHANGE.getType(), changeInfo);

        changeInfo.setApply_user(current.getId());
        changeInfo.setChange_code(code);
        changeInfo.setApply_date(new Date());
        changeInfo.setCreate_time(new Date());
        changeInfo.setCreate_user(current.getId());
        changeInfo.setIs_del(G.ISDEL_NO);
        //changeInfo.setApprove_status(G.NOT_APPROVED);
        changeInfo.setApprove_status(G.PASS_APPROVED);
        changeInfo.setSource(G.CHANGE_APPLICATION);
        Integer userId = changeInfo.getUse_id();
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser != null){
            Integer department  = sysUser.getDepartment();
            changeInfo.setDepartment_id(department);
            if (null != department ){
                SysDepartment sysDepartment = sysDepartmentService.selectById(department);
                if (sysDepartment != null){
                    changeInfo.setPosition(sysDepartment.getDept_position());
                }
            }
        }
        insert(changeInfo);
        operationRecordService.addOtherOperationRecord(changeInfo.getId(),null,"变更申请-新增变更单","2", current);

        //关联的资产
        for (ChangeDetail temp : list) {
            int id = temp.getAssets_id();
            ChangeDetail changeDetail = new ChangeDetail();
            changeDetail.setChange_id(changeInfo.getId());
            changeDetail.setAssets_id(id);
            changeDetail.setIs_del(G.ISDEL_NO);
            changeDetail.setCreate_time(new Date());
            changeDetail.setCreate_user(current.getId());
            changeDetailMapper.insert(changeDetail);
            AssetsManage assetsManage = assetsManageMapper.selectById(id);
            operationRecordService.addOperationRecord(changeInfo.getId(), assetsManage.getAsset_code(),current.getUser_name() + " 进行资产变更","6", current);
        }
        // 启动流程
        //processMemberService.startProcess(G.PROCESS_ZCBG, changeInfo.getId(), current.getId());

        return ResultInfo.success();
    }

    /**
     * 修改变更单
     * @param changeInfo
     * @return
     */
    public ResultInfo updateChangeInfo(ChangeInfo changeInfo, List<Integer> list){
        if (!G.NOT_SUBMITTED.equals(changeInfo.getApprove_status()) && !G.REJECTION_APPROVAL.equals(changeInfo.getApprove_status()) ) {
            return ResultInfo.error("变更单状态必须是未送审或者被驳回");
        }
        changeInfo.setUpdate_time(new Date());
        changeInfo.setUpdate_user(sysUserService.getUser().getId());

        Integer userId = changeInfo.getUse_id();
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser != null){
            Integer department  = sysUser.getDepartment();
            changeInfo.setDepartment_id(department);
            if (null != department ){
                SysDepartment sysDepartment = sysDepartmentService.selectById(department);
                if (sysDepartment != null){
                    changeInfo.setPosition(sysDepartment.getDept_position());
                }
            }
        }
        updateById(changeInfo);

        operationRecordService.addOtherOperationRecord(changeInfo.getId(),null,"变更申请-编辑变更单","2");

        changeDetailMapper.update(null, Wrappers.<ChangeDetail>lambdaUpdate().eq(ChangeDetail::getChange_id, changeInfo.getId()).set(ChangeDetail::getIs_del, G.ISDEL_YES));
        //关联的资产
        Set<Integer> set = new HashSet<>();
        for (Integer temp : list){
            if (null != temp){
                set.add(temp);
            }
        }
        for (Integer temp : set) {
            int id = temp;
            ChangeDetail changeDetail = new ChangeDetail();
            changeDetail.setChange_id(changeInfo.getId());
            changeDetail.setAssets_id(id);
            changeDetail.setIs_del(G.ISDEL_NO);
            changeDetail.setCreate_time(new Date());
            changeDetail.setCreate_user(sysUserService.getUser().getId());
            changeDetailMapper.insert(changeDetail);
//            AssetsManage assetsManage = assetsManageMapper.selectById(id);
//            operationRecordService.addOperationRecord(changeInfo.getId(), assetsManage.getAsset_code(),"变更申请-修改变更单","6");
        }
        return ResultInfo.success();
    }


    /**
     * 修改变更单并送审
     * @param changeInfo
     * @return
     */
    public ResultInfo updateChangeInfoAndSub(ChangeInfo changeInfo, List<Integer> list){
        if (!G.NOT_SUBMITTED.equals(changeInfo.getApprove_status())&& !G.REJECTION_APPROVAL.equals(changeInfo.getApprove_status())) {
            return ResultInfo.error("变更单状态必须是未送审或者被驳回");
        }
        SysUser current = sysUserService.getUser();
        changeInfo.setUpdate_time(new Date());
        changeInfo.setUpdate_user(current.getId());
        changeInfo.setApprove_status(G.NOT_APPROVED);

        Integer userId = changeInfo.getUse_id();
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser != null){
            Integer department  = sysUser.getDepartment();
            changeInfo.setDepartment_id(department);
            if (null != department ){
                SysDepartment sysDepartment = sysDepartmentService.selectById(department);
                if (sysDepartment != null){
                    changeInfo.setPosition(sysDepartment.getDept_position());
                }
            }
        }
        updateById(changeInfo);

        operationRecordService.addOtherOperationRecord(changeInfo.getId(),null,"变更申请-编辑变更单并送审","2");

        changeDetailMapper.update(null, Wrappers.<ChangeDetail>lambdaUpdate().eq(ChangeDetail::getChange_id, changeInfo.getId()).set(ChangeDetail::getIs_del, G.ISDEL_YES));
        //关联的资产
        Set<Integer> set = new HashSet<>();
        for (Integer temp : list){
            if (null != temp){
                set.add(temp);
            }
        }
        for (Integer temp : set) {
            int id = temp;
            ChangeDetail changeDetail = new ChangeDetail();
            changeDetail.setChange_id(changeInfo.getId());
            changeDetail.setAssets_id(id);
            changeDetail.setIs_del(G.ISDEL_NO);
            changeDetail.setCreate_time(new Date());
            changeDetail.setCreate_user(sysUserService.getUser().getId());
            changeDetailMapper.insert(changeDetail);
            AssetsManage assetsManage = assetsManageMapper.selectById(id);
            operationRecordService.addOperationRecord(changeInfo.getId(), assetsManage.getAsset_code(),current.getUser_name() + " 进行资产变更送审","6");
        }

        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCBG, changeInfo.getId(), sysUserService.getUser().getId());
        return ResultInfo.success();
    }
    /**
     * 删除变更单
     * @param id
     * @return
     */
    public ResultInfo deleteChange(Integer id){
        ChangeInfo changeInfo = changeInfoMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(changeInfo.getApprove_status())&& !G.REJECTION_APPROVAL.equals(changeInfo.getApprove_status())) {
            return ResultInfo.error("变更单状态必须是未送审或者被驳回");
        }
        changeInfo.setIs_del(G.ISDEL_YES);
        updateById(changeInfo);

        UpdateWrapper<ChangeDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("change_id",changeInfo.getId());
        updateWrapper.set("is_del",G.ISDEL_YES);
        changeDetailMapper.update(null,updateWrapper);
        return ResultInfo.success();
    }


    /**
     * 变更单送审
     * @param id
     * @return
     */
    public ResultInfo submitChangeInfo(Integer id) {
        return submitChangeInfo(id, sysUserService.getUser());
    }
    public ResultInfo submitChangeInfo(Integer id, SysUser current) {
        ChangeInfo changeInfo = changeInfoMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(changeInfo.getApprove_status())&& !G.REJECTION_APPROVAL.equals(changeInfo.getApprove_status())) {
            return ResultInfo.error("变更单状态必须是未送审或者被驳回");
        }
        changeInfo.setApprove_status(G.NOT_APPROVED);
        updateById(changeInfo);


        operationRecordService.addOtherAppOperationRecord(changeInfo.getId(),null,"变更申请-变更单送审","2",current.getId());

        QueryWrapper<ChangeDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("change_id",changeInfo.getId());
        queryWrapper.eq("is_del","0");
        List<ChangeDetail> list = changeDetailMapper.selectList(queryWrapper);
        for (ChangeDetail temp : list){
            Integer tempId = temp.getAssets_id();
            AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
            operationRecordService.addOperationRecord(changeInfo.getId(), assetsManage.getAsset_code(),current.getUser_name() + " 进行资产变更送审","6", current);
        }

        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCBG, id, current.getId());

        return ResultInfo.success();
    }

    /**
     * 变更单撤回
     * @param id
     * @return
     */
    public ResultInfo backChangeInfo(Integer id) {
        ChangeInfo changeInfo = changeInfoMapper.selectById(id);
        if (G.NOT_APPROVED.equals(changeInfo.getApprove_status()) ) {
            // 查询当前流程 查询字典表
            SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCBG);
            ProcessMember processMember = processMemberService.getProcessMemberByBusId(changeInfo.getId(), dictionary.getValue());
            if (null == processMember) {
                throw new RuntimeException("未查询到流程");
            }
            if (processMember.getIs_revoke() != 1){
                return ResultInfo.error("当前流程已经审批，不能撤回!");
            }
            processMember.setIsCH(1);
            processMemberService.removeProcessMember(processMember, JSON.toJSONString(changeInfo));

            changeInfo.setApprove_status(G.NOT_SUBMITTED);
            changeInfo.setUpdate_time(new Date());
            changeInfo.setUpdate_user(sysUserService.getUser().getId());
            updateById(changeInfo);

            operationRecordService.addOtherOperationRecord(changeInfo.getId(),null,"变更申请-变更单撤回","2");

//            QueryWrapper<ChangeDetail> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("change_id",changeInfo.getId());
//            queryWrapper.eq("is_del","0");
//            List<ChangeDetail> list = changeDetailMapper.selectList(queryWrapper);
//            for (ChangeDetail temp : list){
//                Integer tempId = temp.getAssets_id();
//                AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
//                operationRecordService.addOperationRecord(changeInfo.getId(), assetsManage.getAsset_code(),"变更申请-变更单撤回","6");
//            }

            return ResultInfo.success();
        }

        return ResultInfo.error("撤回失败");
    }


    /**
     * （变更管理）根据登陆用户权限分页展示变更信息
     * @param changeInfo
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultInfo collectionChangeByPage(ChangeInfo changeInfo, int pageIndex, int pageSize){
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

        Map<Integer, SysPosition> positionMap = commonDataTools.getPositionMap();

        List<ChangeInfo> list = new ArrayList<>();

        Map<Integer, SysUser> userMap = commonDataTools.getUserMap();
        PageHelper.startPage(pageIndex, pageSize);
        //仅查看个人通过审核数据
        if (G.DATAPERMISSION_PERSONAL == data_permission) {
            list = changeInfoMapper.collectionChangeByPage(changeInfo,userId,null);
            //查看本部门数据
        } else if (G.DATAPERMISSION_DEPT == data_permission) {
            int department_id = sysUser.getDepartment();
            list = changeInfoMapper.collectionChangeByPage(changeInfo, null, department_id);
            //查看所有部门数据
        } else {
            list = changeInfoMapper.collectionChangeByPage(changeInfo, null, null);
        }

        if (!CollectionUtils.isEmpty(userMap)) {
            for (ChangeInfo temp : list) {
                temp.setUse_name(StringUtils.EMPTY);
                temp.setApply_name(StringUtils.EMPTY);
                if (null == temp.getUse_id() || userMap.get(temp.getUse_id()) == null && null == temp.getApply_user() || userMap.get(temp.getApply_user()) == null) {
                    continue;
                }
                temp.setUse_name(userMap.get(temp.getUse_id()) == null?"":userMap.get(temp.getUse_id()).getUser_name());
                temp.setApply_name(userMap.get(temp.getApply_user()).getUser_name());

                temp.setPlace_name(commonDataTools.getValue(positionMap, temp.getPosition(), "position_name"));
                int id = temp.getId();
                List<ChangeDetail> detail = changeDetailMapper.getDetailById(id);
                if (detail != null) {
                    temp.setDetail(detail);
                }
            }
        }
        PageInfo<ChangeInfo> pageInfo = new PageInfo<>(list);
        return ResultInfo.success(pageInfo);
    }



    /**
     * 新增变更单(手动添加)
     * @param changeInfo
     * @return
     */
    public ResultInfo addChangeInfoByManual(ChangeInfo changeInfo,List<ChangeDetail> list){
        //BG+变更单来源（变更申请：SQ；手动创建：SD）+日期+三位自增码（每日重置）
        changeInfo.setSource(G.CHANGE_MANUAL);
        String code = commonDataTools.getNo(DataType.CHANGE.getType(), changeInfo);
        changeInfo.setChange_code(code);
        changeInfo.setApply_date(new Date());
        changeInfo.setCreate_time(new Date());
        changeInfo.setApply_user(sysUserService.getUser().getId());
        changeInfo.setCreate_user(sysUserService.getUser().getId());
        changeInfo.setIs_del(G.ISDEL_NO);
        changeInfo.setApprove_status(G.PASS_APPROVED);
        changeInfo.setSource(G.CHANGE_MANUAL);


        Integer userId = changeInfo.getUse_id();
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser != null){
            Integer department  = sysUser.getDepartment();
            changeInfo.setDepartment_id(department);
            if (null != department ){
                SysDepartment sysDepartment = sysDepartmentService.selectById(department);
                if (sysDepartment != null){
                    changeInfo.setPosition(sysDepartment.getDept_position());
                }
            }
        }
        insert(changeInfo);

        operationRecordService.addOtherOperationRecord(changeInfo.getId(),null,"变更管理-手动添加变更单","2");

        //关联的资产
        for (ChangeDetail temp : list) {
            //获取资产ID
            int id = temp.getAssets_id();
            ChangeDetail changeDetail = new ChangeDetail();
            changeDetail.setChange_id(changeInfo.getId());
            changeDetail.setAssets_id(id);
            changeDetail.setIs_del(G.ISDEL_NO);
            changeDetail.setCreate_time(new Date());
            changeDetail.setCreate_user(sysUserService.getUser().getId());
            changeDetailMapper.insert(changeDetail);
            //因为状态为审批通过 所以需要同步消息至资产管理表
            AssetsManage assetsManage =assetsManageService.selectById(id);
            assetsManage.setDept_id(changeInfo.getDepartment_id());

            assetsManage.setUse_people(changeInfo.getUse_id());
            assetsManage.setPosition_id(changeInfo.getPosition());
            assetsManage.setUpdate_time(new Date());
            assetsManage.setUpdate_user(sysUserService.getUser().getId());
            assetsManageService.updateById(assetsManage);
//            AssetsManage assetsManageTemp = assetsManageMapper.selectById(id);
//            operationRecordService.addOperationRecord(changeInfo.getId(), assetsManageTemp.getAsset_code(),"变更管理-手动添加变更单","6");
        }

        return ResultInfo.success();
    }


    //查询单条记录日志信息
    public  List<OperationRecord> getOperationRecordList (Integer id){
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id",id);
        queryWrapper.eq("is_del","0");
        queryWrapper.eq("kind","2");
        queryWrapper.orderByAsc("create_time");
        return operationRecordService.selectList(queryWrapper);
    }


    //审批通过后改变主表的资产信息
    public String changeAssManageInfo(Integer id){
        ChangeInfo changeInfo = changeInfoMapper.selectById(id);
        QueryWrapper<ChangeDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        queryWrapper.eq("change_id",id);
        List<ChangeDetail> list = changeDetailMapper.selectList(queryWrapper);
        for (ChangeDetail temp : list){
            Integer assest_id = temp.getAssets_id();
            AssetsManage assetsManage = assetsManageMapper.selectById(assest_id);
            assetsManage.setDept_id(changeInfo.getDepartment_id());

            assetsManage.setUse_people(changeInfo.getUse_id());
            assetsManage.setPosition_id(changeInfo.getPosition());
            assetsManage.setUpdate_time(new Date());
            assetsManage.setUpdate_user(sysUserService.getUser().getId());
            assetsManageService.updateById(assetsManage);
        }
        return "变更成功!!!";
    }

    //审批通过后改变主表的资产信息
    public String changeAssManageInfo(Integer id,Integer UserId){
        ChangeInfo changeInfo = changeInfoMapper.selectById(id);
        QueryWrapper<ChangeDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        queryWrapper.eq("change_id",id);
        List<ChangeDetail> list = changeDetailMapper.selectList(queryWrapper);
        for (ChangeDetail temp : list){
            Integer assest_id = temp.getAssets_id();
            AssetsManage assetsManage = assetsManageMapper.selectById(assest_id);
            assetsManage.setDept_id(changeInfo.getDepartment_id());

            assetsManage.setUse_people(changeInfo.getUse_id());
            assetsManage.setPosition_id(changeInfo.getPosition());
            assetsManage.setUpdate_time(new Date());
            assetsManage.setUpdate_user(UserId);
            assetsManageService.updateById(assetsManage);
        }
        return "变更成功!!!";
    }


}
