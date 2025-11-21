package com.jsdc.rfid.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ManagementDao;
import com.jsdc.rfid.enums.AssetsStatusEnums;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.enums.ManagementStatusEnums;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.utils.CommonDataTools;
import com.jsdc.rfid.utils.Convert;
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
public class ManagementService extends BaseService<ManagementDao, Management> {

    @Autowired
    private ManagementMapper managementMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CommonDataTools commonDataTools;
    @Autowired
    private ManagementAssetsMapper managementAssetsMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPostMapper sysPostMapper;
    @Autowired
    private AssetsManageMapper assetsManageMapper;

    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private AssetsTypeMapper assetsTypeMapper;

    @Autowired
    private ManagementAssetsFileMemberMapper managementAssetsFileMemberMapper;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private ProcessMemberHistoryService processMemberHistoryService;

    /**
     * 分页展示符合条件得资产
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public ResultInfo getLeaveUnused(@RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam(defaultValue = "10") Integer pageSize, AssetsManage assetsManage, List<ReceiveAssets> assets) {
        PageHelper.startPage(pageIndex, pageSize);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.ne("asset_state", "6");
        if (!CollectionUtils.isEmpty(assets)) {
            for (ReceiveAssets temp : assets) {
                queryWrapper.ne("id", temp.getAssets_id());
            }
        }
        if (StringUtils.isNotBlank(assetsManage.getAsset_name())) {
            queryWrapper.like("asset_name", assetsManage.getAsset_name());
        }
        if (null != assetsManage.getAsset_type_id() && 0 != assetsManage.getAsset_type_id()) {
            queryWrapper.eq("asset_type_id", assetsManage.getAsset_type_id());
        }
        List<AssetsManage> list = assetsManageMapper.selectList(queryWrapper);

        Map<Integer, SysDict> brandType = commonDataTools.getSysDictMap("brand_type");
        for (AssetsManage temp : list) {
            if (null != temp.getAsset_type_id()) {
                AssetsType assetsType = assetsTypeMapper.selectById(temp.getAsset_type_id());
                if (null != assetsType) {
                    temp.setAsset_type_name(assetsType.getAssets_type_name());
                }
            }
            if (null != temp.getBrand_id()) {
                temp.setBrand_name(commonDataTools.getValue(brandType, temp.getBrand_id(), "label"));
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
     * 根据ID查询信息
     *
     * @param id
     * @return
     */
    public Management getOneInfoById(int id) {
        Management management = managementMapper.getOneManById(id);
        if (null != management.getApply_date()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            management.setReal_apply_date(simpleDateFormat.format(management.getApply_date()));
        }
        return management;
    }


    /**
     * 处置 展示当前登陆用户的所有处置单
     *
     * @param management
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<Management> getPageByUserId(Management management, int pageIndex, int pageSize) {
        // 得到当前用户
        SysUser current;
        if (Base.empty(management.getUserId())) {
            current = sysUserService.getUser();
        } else {
            current = sysUserService.selectById(management.getUserId());
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<Management> list = managementMapper.getPageByUserId(management, management.getCreate_user());
        for (Management temp : list) {
            // 得到当前流程节点
            processMemberService.getProcessDataByBusId(temp.getId(), G.PROCESS_ZCCZ, current, temp);
        }
        return new PageInfo<>(list);
    }

    /**
     * 处置已审批
     *
     * @return
     */
    public PageInfo<Management> finishAdopt(Management management, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Management> list = managementMapper.getPageByUserId(Management.builder()
                .ids(management.getIds())
                .management_code(management.getManagement_code())
                .apply_name(management.getApply_name())
                .timeStr(management.getTimeStr())
                .department_id(management.getDepartment_id())
                .build(), null);
        return new PageInfo<>(list);
    }


    /**
     * 新增处置单
     *
     * @param management
     * @return
     */
    public ResultInfo addManagement(Management management) {
        SysUser sysUser = sysUserService.getUser();
        //处置单号：CZ+处置单来源（处置申请：SQ；盘亏：PK；盘点异常：PY）+日期+三位自增码（每日重置）
        if (null == management.getSource()) {
            management.setSource(G.DISPOSAL_APPLICATION);
        }
        String code = commonDataTools.getNo(DataType.DISPOSE_OF.getType(), management);
        management.setManagement_code(code);
        management.setCreate_time(new Date());
        management.setApply_date(new Date());
        management.setCreate_user(sysUser.getId());
        management.setApply_user(sysUser.getId());
        management.setDepartment_id(sysUser.getDepartment());
        management.setIs_del(G.ISDEL_NO);
        management.setStatus(G.NOT_SUBMITTED);
        management.setManagement_finish(G.FINISH_NO);
        insert(management);
        operationRecordService.addOtherOperationRecord(management.getId(), null, "处置申请-新增处置单", "3");

        if (null != management.getAssetsManageList()) {
            List<AssetsManage> assetsManageList = management.getAssetsManageList();
            for (AssetsManage temp : assetsManageList) {
                int id = temp.getId();
                ManagementAssets managementAssets = new ManagementAssets();
                managementAssets.setAssets_id(id);
                managementAssets.setStatus("2");
                managementAssets.setManagement_id(management.getId());
                managementAssets.setCreate_user(sysUserService.getUser().getId());
                management.setCreate_time(new Date());
                managementAssets.setIs_del(G.ISDEL_NO);
                managementAssetsMapper.insert(managementAssets);
            }
        }

        if (null != management.getDetail()) {
            List<ManagementAssets> detail = management.getDetail();
            for (ManagementAssets temp : detail) {
                int id = temp.getAssets_id();
                ManagementAssets managementAssets = new ManagementAssets();
                managementAssets.setStatus("2");
                managementAssets.setProposal(temp.getProposal());
                managementAssets.setAssets_id(id);
                managementAssets.setManagement_id(management.getId());
                managementAssets.setCreate_user(sysUserService.getUser().getId());
                management.setCreate_time(new Date());
                managementAssets.setIs_del(G.ISDEL_NO);
                managementAssetsMapper.insert(managementAssets);
//                AssetsManage assetsManage = assetsManageMapper.selectById(id);
//                operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(), "处置申请-新增处置单", "7");
            }
        }
        return ResultInfo.success();
    }


    /**
     * 新增处置单
     *
     * @param management
     * @return
     */
    public ResultInfo addManagementAndSub(Management management) {
        SysUser sysUser = sysUserService.getUser();
        //处置单号：CZ+处置单来源（处置申请：SQ；盘亏：PK；盘点异常：PY）+日期+三位自增码（每日重置）
        if (null == management.getSource()) {
            management.setSource(G.DISPOSAL_APPLICATION);
        }
        String code = commonDataTools.getNo(DataType.DISPOSE_OF.getType(), management);
        management.setManagement_code(code);
        management.setCreate_time(new Date());
        management.setApply_date(new Date());
        management.setCreate_user(sysUser.getId());
        management.setApply_user(sysUser.getId());
        management.setDepartment_id(sysUser.getDepartment());
        management.setIs_del(G.ISDEL_NO);
        //management.setStatus(G.NOT_APPROVED);
        management.setStatus(G.PASS_APPROVED);
        management.setManagement_finish(G.FINISH_NO);
        insert(management);

        operationRecordService.addOtherOperationRecord(management.getId(), null, "处置申请-新增处置单", "3");


        if (null != management.getAssetsManageList()) {
            List<AssetsManage> assetsManageList = management.getAssetsManageList();
            for (AssetsManage temp : assetsManageList) {
                int id = temp.getId();
                ManagementAssets managementAssets = new ManagementAssets();
                managementAssets.setAssets_id(id);
                managementAssets.setStatus("2");
                managementAssets.setManagement_id(management.getId());
                managementAssets.setCreate_user(sysUserService.getUser().getId());
                management.setCreate_time(new Date());
                managementAssets.setIs_del(G.ISDEL_NO);
                managementAssetsMapper.insert(managementAssets);
            }
        }

        if (null != management.getDetail()) {
            List<ManagementAssets> detail = management.getDetail();
            for (ManagementAssets temp : detail) {
                int id = temp.getAssets_id();
                ManagementAssets managementAssets = new ManagementAssets();
                managementAssets.setStatus("2");
                managementAssets.setProposal(temp.getProposal());
                managementAssets.setAssets_id(id);
                managementAssets.setManagement_id(management.getId());
                managementAssets.setCreate_user(sysUserService.getUser().getId());
                management.setCreate_time(new Date());
                managementAssets.setIs_del(G.ISDEL_NO);
                managementAssetsMapper.insert(managementAssets);
                AssetsManage assetsManage = assetsManageMapper.selectById(id);
                operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(), sysUser.getUser_name() + " 进行资产处置", "7");
            }
        }
        // 启动流程
        //processMemberService.startProcess(G.PROCESS_ZCCZ, management.getId(), sysUser.getId());
        return ResultInfo.success();
    }

    /**
     * app新增处置单
     *
     * @param ids
     * @param remark
     * @param userId
     */
    public void addAPPManage(String ids, String remark, Integer userId, String proposals, Integer type) {
        Management management = new Management();
        SysUser sysUser = sysUserService.selectById(userId);
        //处置单号：CZ+处置单来源（处置申请：SQ；盘亏：PK；盘点异常：PY）+日期+三位自增码（每日重置）
        if (null == management.getSource()) {
            management.setSource(G.DISPOSAL_APPLICATION);
        }
        String code = commonDataTools.getNo(DataType.DISPOSE_OF.getType(), management);
        management.setReason_detail(remark);
        management.setManagement_code(code);
        management.setCreate_time(new Date());
        management.setApply_date(new Date());
        management.setCreate_user(sysUser.getId());
        management.setApply_user(sysUser.getId());
        management.setDepartment_id(sysUser.getDepartment());
        management.setIs_del(G.ISDEL_NO);
        if (1 == type) {
            management.setStatus(G.NOT_SUBMITTED);
        } else {
            management.setStatus(G.NOT_APPROVED);
        }
        management.setManagement_finish(G.FINISH_NO);
        insert(management);
        if (1 == type) {
            operationRecordService.addOtherAppOperationRecord(management.getId(), null, "处置申请-新增处置单", "3", userId);
        } else {
            operationRecordService.addOtherAppOperationRecord(management.getId(), null, "新增处置申请单并送审", "3", userId);

            // 启动流程
            processMemberService.startProcess(G.PROCESS_ZCCZ, management.getId(), userId);
        }


        if (!StringUtils.isEmpty(ids)) {
            String[] idList = ids.split("-");
            String[] proposalList = proposals.split("-");
            for (int i = 0; i < idList.length; i++) {
                ManagementAssets managementAssets = new ManagementAssets();
                managementAssets.setStatus("2");
                managementAssets.setProposal(proposalList[i]);
                managementAssets.setAssets_id(Integer.parseInt(idList[i]));
                managementAssets.setManagement_id(management.getId());
                managementAssets.setCreate_user(sysUser.getId());
                management.setCreate_time(new Date());
                managementAssets.setIs_del(G.ISDEL_NO);
                managementAssetsMapper.insert(managementAssets);
                AssetsManage assetsManage = assetsManageMapper.selectById(Integer.parseInt(idList[i]));
                if (1 == type) {
                    operationRecordService.appAddOperationRecord(management.getId(), assetsManage.getAsset_code(), "处置申请-新增处置单", "7", userId);
                } else {
                    operationRecordService.appAddOperationRecord(management.getId(), assetsManage.getAsset_code(), "新增处置申请单并送审", "7", userId);
                }

            }

        }


    }

    /**
     * 修改处置单
     *
     * @param management
     * @return
     */
    public ResultInfo updateManagement(Management management) {
        if (!G.NOT_SUBMITTED.equals(management.getStatus()) && !G.REJECTION_APPROVAL.equals(management.getStatus())) {
            return ResultInfo.error("处置单状态必须是未送审或被驳回");
        }
        management.setUpdate_time(new Date());
        management.setUpdate_user(sysUserService.getUser().getId());
        updateById(management);

        operationRecordService.addOtherOperationRecord(management.getId(), null, "处置申请-编辑处置单", "3");
        managementAssetsMapper.update(null, Wrappers.<ManagementAssets>lambdaUpdate().eq(ManagementAssets::getManagement_id, management.getId()).set(ManagementAssets::getIs_del, G.ISDEL_YES));

        if (null != management.getDetail()) {
            List<ManagementAssets> detail = management.getDetail();
            for (ManagementAssets temp : detail) {
                int id = temp.getAssets_id();
                ManagementAssets managementAssets = new ManagementAssets();
                managementAssets.setStatus("2");
                managementAssets.setProposal(temp.getProposal());
                managementAssets.setAssets_id(id);
                managementAssets.setManagement_id(management.getId());
                managementAssets.setCreate_user(sysUserService.getUser().getId());
                management.setCreate_time(new Date());
                managementAssets.setIs_del(G.ISDEL_NO);
                managementAssetsMapper.insert(managementAssets);
//                AssetsManage assetsManage = assetsManageMapper.selectById(id);
//                operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(), "处置申请-修改处置单", "7");
            }
        }
//        if (null != management.getAssetsManageList()){
//            List<AssetsManage> assetsManageList = management.getAssetsManageList();
//            for (AssetsManage temp : assetsManageList) {
//                int id = temp.getId();
//                ManagementAssets managementAssets = new ManagementAssets();
//                managementAssets.setAssets_id(id);
//                managementAssets.setStatus("2");
//                managementAssets.setManagement_id(management.getId());
//                managementAssets.setCreate_user(sysUserService.getUser().getId());
//                management.setCreate_time(new Date());
//                managementAssets.setIs_del(G.ISDEL_NO);
//                managementAssetsMapper.insert(managementAssets);
//            }
//        }
//
//
//        if (null != management.getAssetsIdList()){
//            List<Integer > assIdList =management.getAssetsIdList();
//            List<String> proposalList = management.getProposalList();
//            for (int i = 0 ;i< assIdList.size() ; i++){
//                if (null != assIdList.get(i) && !assIdList.get(i).equals("")){
//                    int id = assIdList.get(i);
//                    ManagementAssets managementAssets = new ManagementAssets();
//                    managementAssets.setStatus("2");
////
//                    String temp = proposalList.get(i);
//                    if (temp.equals("报废")){
//                        managementAssets.setProposal("2");
//                    }else if (temp.equals("遗失")){
//                        managementAssets.setProposal("3");
//                    }
//                    managementAssets.setAssets_id(id);
//                    managementAssets.setManagement_id(management.getId());
//                    managementAssets.setCreate_user(sysUserService.getUser().getId());
//                    management.setCreate_time(new Date());
//                    managementAssets.setIs_del(G.ISDEL_NO);
//                    managementAssetsMapper.insert(managementAssets);
//                    AssetsManage assetsManage = assetsManageMapper.selectById(id);
//                    operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(),"处置申请-修改处置单","7");
//                }
//            }
//
//        }

        return ResultInfo.success();
    }


    /**
     * 修改处置单
     *
     * @param management
     * @return
     */
    public ResultInfo updateManagementAndSub(Management management) {
        if (!G.NOT_SUBMITTED.equals(management.getStatus()) && !G.REJECTION_APPROVAL.equals(management.getStatus())) {
            return ResultInfo.error("处置单状态必须是未送审或被驳回");
        }
        management.setUpdate_time(new Date());
        management.setUpdate_user(sysUserService.getUser().getId());
        management.setStatus(G.NOT_APPROVED);
        updateById(management);
        operationRecordService.addOtherOperationRecord(management.getId(), null, "处置申请-编辑处置单并送审", "3");

        managementAssetsMapper.update(null, Wrappers.<ManagementAssets>lambdaUpdate().eq(ManagementAssets::getManagement_id, management.getId()).set(ManagementAssets::getIs_del, G.ISDEL_YES));
        if (null != management.getDetail()) {
            List<ManagementAssets> detail = management.getDetail();
            for (ManagementAssets temp : detail) {
                int id = temp.getAssets_id();
                ManagementAssets managementAssets = new ManagementAssets();
                managementAssets.setStatus("2");
                managementAssets.setProposal(temp.getProposal());
                managementAssets.setAssets_id(id);
                managementAssets.setManagement_id(management.getId());
                managementAssets.setCreate_user(sysUserService.getUser().getId());
                management.setCreate_time(new Date());
                managementAssets.setIs_del(G.ISDEL_NO);
                managementAssetsMapper.insert(managementAssets);
                AssetsManage assetsManage = assetsManageMapper.selectById(id);
                operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(), sysUserService.getUser().getUser_name() + " 进行资产处置送审", "7");
            }
        }
        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCCZ, management.getId(), sysUserService.getUser().getId());
//        if (null != management.getAssetsManageList()){
//            List<AssetsManage> assetsManageList = management.getAssetsManageList();
//            for (AssetsManage temp : assetsManageList) {
//                int id = temp.getId();
//                ManagementAssets managementAssets = new ManagementAssets();
//                managementAssets.setAssets_id(id);
//                managementAssets.setStatus("2");
//                managementAssets.setManagement_id(management.getId());
//                managementAssets.setCreate_user(sysUserService.getUser().getId());
//                management.setCreate_time(new Date());
//                managementAssets.setIs_del(G.ISDEL_NO);
//                managementAssetsMapper.insert(managementAssets);
//            }
//        }
//
//        if (null != management.getAssetsIdList()){
//            List<Integer > assIdList =management.getAssetsIdList();
//            List<String> proposalList = management.getProposalList();
//            for (int i = 0 ;i< assIdList.size() ; i++){
//                if (null != assIdList.get(i) && !assIdList.get(i).equals("")){
//                    int id = assIdList.get(i);
//                    ManagementAssets managementAssets = new ManagementAssets();
//                    managementAssets.setStatus("2");
////
//                    String temp = proposalList.get(i);
//                    if (temp.equals("报废")){
//                        managementAssets.setProposal("2");
//                    }else if (temp.equals("遗失")){
//                        managementAssets.setProposal("3");
//                    }
//                    managementAssets.setAssets_id(id);
//                    managementAssets.setManagement_id(management.getId());
//                    managementAssets.setCreate_user(sysUserService.getUser().getId());
//                    management.setCreate_time(new Date());
//                    managementAssets.setIs_del(G.ISDEL_NO);
//                    managementAssetsMapper.insert(managementAssets);
//                    AssetsManage assetsManage = assetsManageMapper.selectById(id);
//                    operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(),"处置申请-修改处置单并送审","7");
//                }
//            }
//
//        }
        return ResultInfo.success();
    }

    /**
     * 删除处置单
     *
     * @param id
     * @return
     */
    public ResultInfo deleteManagement(Integer id) {
        Management management = managementMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(management.getStatus()) && !G.REJECTION_APPROVAL.equals(management.getStatus())) {
            return ResultInfo.error("处置单状态必须是未送审或者被驳回");
        }
        management.setIs_del(G.ISDEL_YES);
        updateById(management);
        managementAssetsMapper.update(null, Wrappers.<ManagementAssets>lambdaUpdate().eq(ManagementAssets::getManagement_id, management.getId()).set(ManagementAssets::getIs_del, G.ISDEL_YES));
        return ResultInfo.success();
    }


    /**
     * 处置单送审
     *
     * @param id
     * @return
     */
    public ResultInfo submitManage(Integer id) {
        Management management = managementMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(management.getStatus()) && !G.REJECTION_APPROVAL.equals(management.getStatus())) {
            return ResultInfo.error("处置单状态必须是未送审或者被驳回");
        }
        management.setStatus(G.NOT_APPROVED);
        updateById(management);
        operationRecordService.addOtherOperationRecord(management.getId(), null, "处置申请-处置单送审", "3");
        QueryWrapper<ManagementAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("management_id", management.getId());
        queryWrapper.eq("is_del", "0");
        List<ManagementAssets> list = managementAssetsMapper.selectList(queryWrapper);
        for (ManagementAssets temp : list) {
            Integer tempId = temp.getAssets_id();
            AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
            operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(), sysUserService.getUser().getUser_name() + " 进行资产处置送审", "7");
        }

        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCCZ, id, sysUserService.getUser().getId());

        return ResultInfo.success();

    }

    /**
     * App处置单送审
     *
     * @param id
     * @return
     */
    public ResultInfo appSubmitManage(Integer id, Integer userId) {
        Management management = managementMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(management.getStatus()) && !G.REJECTION_APPROVAL.equals(management.getStatus())) {
            return ResultInfo.error("处置单状态必须是未送审或者被驳回");
        }
        management.setStatus(G.NOT_APPROVED);
        updateById(management);
        operationRecordService.addOtherAppOperationRecord(management.getId(), null, "处置申请-处置单送审", "3", userId);
        QueryWrapper<ManagementAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("management_id", management.getId());
        queryWrapper.eq("is_del", "0");
        List<ManagementAssets> list = managementAssetsMapper.selectList(queryWrapper);
        for (ManagementAssets temp : list) {
            Integer tempId = temp.getAssets_id();
            AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
            operationRecordService.appAddOperationRecord(management.getId(), assetsManage.getAsset_code(), "处置申请-处置单送审", "7", userId);
        }
        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCCZ, id, userId);
        return ResultInfo.success();

    }


    /**
     * 打印单据
     *
     * @param id
     * @return
     */
    public ResultInfo printManage(Integer id) {
        Management management = managementMapper.getOneManById(id);
        if (G.NOT_SUBMITTED.equals(management.getStatus()) || G.REJECTION_APPROVAL.equals(management.getStatus())) {
            return ResultInfo.error("处置单状态不能是未送审或者退回");
        }
        List<ManagementAssets> detail = managementAssetsMapper.getInfoByManId(id);
        management.setDetail(detail);
        return ResultInfo.success(management);
    }

    /**
     * 撤回
     *
     * @param id
     * @return
     */
    public ResultInfo backManage(Integer id) {
        Management management = managementMapper.getOneManById(id);
        if (G.NOT_APPROVED.equals(management.getStatus())) {
            // 查询当前流程 查询字典表
            SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCCZ);
            ProcessMember processMember = processMemberService.getProcessMemberByBusId(management.getId(), dictionary.getValue());
            if (null == processMember) {
                new RuntimeException("未查询到流程");
            }
            if (processMember.getIs_revoke() != 1){
                return ResultInfo.error("当前流程已经审批，不能撤回!");
            }
            processMember.setIsCH(1);
            processMemberService.removeProcessMember(processMember, JSON.toJSONString(management));

            management.setStatus(G.NOT_SUBMITTED);
            managementMapper.updateById(management);

            operationRecordService.addOtherOperationRecord(management.getId(), null, "处置申请-处置单撤回", "3");

//            QueryWrapper<ManagementAssets> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("management_id", management.getId());
//            queryWrapper.eq("is_del", "0");
//            List<ManagementAssets> list = managementAssetsMapper.selectList(queryWrapper);
//            for (ManagementAssets temp : list) {
//                Integer tempId = temp.getAssets_id();
//                AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
//                operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(), "处置申请-处置单撤回", "7");
//            }

            return ResultInfo.success();
        }
        return ResultInfo.error("只能对未审批或者审批中的处置单执行撤回操作");
    }

    /**
     * 根据处置单ID查询详细信息
     *
     * @param id
     * @return
     */
//   处置人、处置时间、
    public List<ManagementAssets> getDetailInfo(int id) {

        List<ManagementAssets> list = managementAssetsMapper.getInfoByManId(id);
        Map<Integer, SysDict> brandType = commonDataTools.getSysDictMap("brand_type");
        for (ManagementAssets temp : list) {
            AssetsManage assetsManage = assetsManageMapper.selectById(temp.getAssets_id());
            temp.setBrand_name(commonDataTools.getValue(brandType, assetsManage.getBrand_id(), "label"));
            if (null != temp.getStatus()) {
                if ("1".equals(temp.getStatus())) {
                    temp.setStatusName("已处置");
                } else {
                    temp.setStatusName("未处置");
                }
            }
            if (null != temp.getReason()) {
                temp.setReason_name(ManagementStatusEnums.getValue(temp.getReason()));
            }

            if (null != temp.getProposal()) {
                temp.setProposal_name(ManagementStatusEnums.getValue(temp.getProposal()));

            } else {
                temp.setProposal("暂无数据");
            }
            if (null != temp.getManagement_date()) {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                temp.setDeal_date(s.format(temp.getManagement_date()));
            }
            if (null != temp.getManagement_user()) {
                SysUser sysUser = sysUserService.selectById(temp.getManagement_user());
                if (null != sysUser) {
                    temp.setManagement_name(sysUser.getUser_name());
                }
            }
        }
        return list;
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
     * 分页展示处置管理列表页
     *
     * @param management
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultInfo getManageAssetsByPage(Management management, int pageIndex, int pageSize) {

        //判断用户权限
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

        List<Management> list = new ArrayList<>();

        PageHelper.startPage(pageIndex, pageSize);
        //仅查看个人通过审核数据
        if (G.DATAPERMISSION_PERSONAL == data_permission) {
            list = managementMapper.getPageByPermission(management, userId, null);
            //查看本部门数据
        } else if (G.DATAPERMISSION_DEPT == data_permission) {
            int department = sysUser.getDepartment();
            list = managementMapper.getPageByPermission(management, null, department);
            //查看所有部门数据
        } else {
            list = managementMapper.getPageByPermission(management, null, null);
        }
        PageInfo<Management> pageInfo = new PageInfo<>(list);

        return ResultInfo.success(pageInfo);
    }


    /**
     * 处置操作
     *
     * @param managementAssets
     * @return
     */
    public ResultInfo dealWithAssets(ManagementAssets managementAssets) {
//        if ("1".equals(managementAssets.getStatus())){
//            ResultInfo.error("只能对未处置状态的资产进行处置操作");
//        }
        managementAssets.setManagement_user(sysUserService.getUser().getId());
        managementAssets.setManagement_date(new Date());
        managementAssets.setUpdate_user(sysUserService.getUser().getId());
        managementAssets.setUpdate_time(new Date());
        managementAssets.setStatus("1");
        managementAssetsMapper.updateById(managementAssets);

        ManagementAssets managementAssets1 = managementAssetsMapper.selectById(managementAssets.getId());

        //更改资产管理的信息
        int assets_id = managementAssets1.getAssets_id();
        AssetsManage assetsManage = assetsManageMapper.selectById(assets_id);
//        assetsManage.setAsset_state(AssetsStatusEnums.DISPOSE_OF.getType());
//        assetsManage.setScrap_time(new Date());
//        assetsManage.setUpdate_user(sysUserService.getUser().getId());
        Integer dispose_state = null;
        if ("2".equals(managementAssets1.getReason())) {
            dispose_state = 1;
        } else if ("3".equals(managementAssets1.getReason())) {
            dispose_state = 2;
        }
//        assetsManage.setUpdate_time(new Date());
//        assetsManageMapper.updateById(assetsManage);
        assetsManageMapper.update(null, Wrappers.<AssetsManage>lambdaUpdate()
                .set(AssetsManage::getUse_people, null)
                .set(AssetsManage::getAsset_state, AssetsStatusEnums.DISPOSE_OF.getType())
                .set(AssetsManage::getScrap_time, new Date())
                .set(AssetsManage::getUpdate_user, sysUserService.getUser().getId())
                .set(AssetsManage::getUpdate_time, new Date())
                .set(AssetsManage::getDispose_state, dispose_state)
                .eq(AssetsManage::getId, assets_id));
        //处置的附件保存处理
        if (StringUtils.isNotBlank(managementAssets.getAttachment_ids())) {
            managementAssetsFileMemberMapper.delete(Wrappers.<ManagementAssetsFileMember>lambdaQuery().eq(ManagementAssetsFileMember::getManagementasset_id, managementAssets1.getId()));
            Integer[] split = Convert.toIntArray(managementAssets.getAttachment_ids());
            for (Integer s : split) {
                managementAssetsFileMemberMapper.insert(ManagementAssetsFileMember.builder().managementasset_id(managementAssets1.getId()).file_id(s).build());
            }
        }

        //判断当前资产对应处置单所关联的资产是否都已经被处置 如果都被处置 则对处置单的完成标志设置为已完成
        Integer management_id = managementAssets1.getManagement_id();
        QueryWrapper<ManagementAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("management_id", management_id);
        List<ManagementAssets> list = managementAssetsMapper.selectList(queryWrapper);
        boolean flag = true;
        for (ManagementAssets temp : list) {
            if ("2".equals(temp.getStatus())) {
                flag = false;
            }
        }
        if (flag) {
            Management management = managementMapper.selectById(management_id);
            management.setManagement_finish("1");
            managementMapper.updateById(management);
        }

        AssetsManage assetsManage1 = assetsManageMapper.selectById(assets_id);
        //处置结果   2"报废", 3"遗失", 4"无偿调拨", 5"对外捐赠", 6"出售", 7"出让", 8"转让", 9"置换"
        String reason = managementAssets1.getReason();
        String dispose_result = "";
        dispose_result = ManagementStatusEnums.getValue(reason);
        operationRecordService.addOtherOperationRecord(managementAssets1.getManagement_id(), assetsManage1.getAsset_code(), sysUserService.getUser().getUser_name() + " 进行资产处置" + dispose_result, "3");
//        operationRecordService.addOtherOperationRecord(managementAssets1.getManagement_id(), assetsManage1.getAsset_code(), "处置管理-对编号" + assetsManage1.getAsset_code() + "的资产进行处置", "3");
        return ResultInfo.success();
    }


    /**
     * 分页展示处置管理列表页
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultInfo setManageAssetsByPage(Integer id, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<ManagementAssets> list = managementAssetsMapper.getInfoByManId(id);
        PageInfo<ManagementAssets> pageInfo = new PageInfo<>(list);

        for (ManagementAssets temp : list) {

            if (null != temp.getManagement_date()) {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                temp.setDeal_date(s.format(temp.getManagement_date()));
            }
            if (null != temp.getManagement_user()) {
                SysUser sysUser = sysUserService.selectById(temp.getManagement_user());
                if (null != sysUser) {
                    temp.setManagement_name(sysUser.getUser_name());
                }
            }
            temp.setProposal_name(ManagementStatusEnums.getValue(temp.getProposal()));
            temp.setReason_name(ManagementStatusEnums.getValue(temp.getReason()));

        }
        return ResultInfo.success(pageInfo);
    }


    //查询单条记录日志信息
    public List<OperationRecord> getOperationRecordList(Integer id) {
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("kind", "3");
        queryWrapper.orderByAsc("create_time");
        return operationRecordService.selectList(queryWrapper);
    }

    //查询单条记录日志信息
    public OperationRecord getOperationRecordData(Integer id) {
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operate_id", id);
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("kind", "3");
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("limit 1");
        return operationRecordService.selectOne(queryWrapper);
    }

    public PageInfo<Management> wxGetPageByUserId(Management management, String openId, Integer pageIndex, Integer pageSize) {
        // 得到当前用户
        SysUser current = sysUserService.findByOpenId(openId);
        PageHelper.startPage(pageIndex, pageSize);
        List<Management> list = managementMapper.getPageByUserId(management, management.getCreate_user());
        for (Management temp : list) {
            // 得到当前流程节点
            ProcessConfig process = processMemberService.getInfoByBusId(temp.getId(), G.PROCESS_ZCCZ);
            if (null != process && !CollectionUtils.isEmpty(process.getCurrentInfos())) {
                for (ProcessConfigInfo info : process.getCurrentInfos()) {
                    if (info.getNode_type() == 0 && current.getPost().equals(info.getNode_handler())) {
                        temp.setCurrentTaskName(info.getNode_name());
                    } else if (info.getNode_type() == 1 && current.getId().equals(info.getNode_handler())) {
                        temp.setCurrentTaskName(info.getNode_name());
                    } else if (info.getNode_type() == 2 && process.getProcessMember().getApply_dept_leader_id().equals(current.getId())) {
                        temp.setCurrentTaskName(info.getNode_name());
                    }
                }
            }
        }
        return new PageInfo<>(list);
    }

    public ResultInfo wxSubmitManage(Integer id) {
        Management management = managementMapper.selectById(id);
        if (!G.NOT_SUBMITTED.equals(management.getStatus()) && !G.REJECTION_APPROVAL.equals(management.getStatus())) {
            return ResultInfo.error("处置单状态必须是未送审或者被驳回");
        }
        management.setStatus(G.NOT_APPROVED);
        updateById(management);
        operationRecordService.addOtherOperationRecord(management.getId(), null, "处置申请-处置单送审", "3");
        QueryWrapper<ManagementAssets> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("management_id", management.getId());
        queryWrapper.eq("is_del", "0");
        List<ManagementAssets> list = managementAssetsMapper.selectList(queryWrapper);
        for (ManagementAssets temp : list) {
            Integer tempId = temp.getAssets_id();
            AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
            operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(), "处置申请-处置单送审", "7");
        }

        // 启动流程
        processMemberService.startProcess(G.PROCESS_ZCCZ, id, sysUserService.getUser().getId());

        return ResultInfo.success();
    }

    public ResultInfo wxBackManage(Integer id) {
        Management management = managementMapper.getOneManById(id);
        if (G.NOT_APPROVED.equals(management.getStatus())) {
            management.setStatus(G.NOT_SUBMITTED);
            updateById(management);

            operationRecordService.addOtherOperationRecord(management.getId(), null, "处置申请-处置单撤回", "3");

//            QueryWrapper<ManagementAssets> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("management_id", management.getId());
//            queryWrapper.eq("is_del", "0");
//            List<ManagementAssets> list = managementAssetsMapper.selectList(queryWrapper);
//            for (ManagementAssets temp : list) {
//                Integer tempId = temp.getAssets_id();
//                AssetsManage assetsManage = assetsManageMapper.selectById(tempId);
//                operationRecordService.addOperationRecord(management.getId(), assetsManage.getAsset_code(), "处置申请-处置单撤回", "7");
//            }
            // 查询当前流程 查询字典表
            SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCCZ);
            ProcessMember processMember = processMemberService.getProcessMemberByBusId(management.getId(), dictionary.getValue());
            if (null == processMember) {
                throw new RuntimeException("未查询到流程");
            }

            processMemberService.removeProcessMember(processMember, JSON.toJSONString(management));
            return ResultInfo.success();
        }
        return ResultInfo.error("只能对未审批或者审批中的处置单执行撤回操作");
    }

    public void addWxManage(String[] idList, String remark, Integer userId, String[] proposalList, Integer type) {
        Management management = new Management();
        SysUser sysUser = sysUserService.selectById(userId);
        //处置单号：CZ+处置单来源（处置申请：SQ；盘亏：PK；盘点异常：PY）+日期+三位自增码（每日重置）
        if (null == management.getSource()) {
            management.setSource(G.DISPOSAL_APPLICATION);
        }
        String code = commonDataTools.getNo(DataType.DISPOSE_OF.getType(), management);
        management.setReason_detail(remark);
        management.setManagement_code(code);
        management.setCreate_time(new Date());
        management.setApply_date(new Date());
        management.setCreate_user(sysUser.getId());
        management.setApply_user(sysUser.getId());
        management.setDepartment_id(sysUser.getDepartment());
        management.setIs_del(G.ISDEL_NO);
        if (1 == type) {
            management.setStatus(G.NOT_SUBMITTED);
        } else {
            management.setStatus(G.NOT_APPROVED);
        }
        management.setManagement_finish(G.FINISH_NO);
        insert(management);
        if (1 == type) {
            operationRecordService.addOtherAppOperationRecord(management.getId(), null, "处置申请-新增处置单", "3", userId);
        } else {
            operationRecordService.addOtherAppOperationRecord(management.getId(), null, "处置申请-新增处置单并送审", "3", userId);

            // 启动流程
            processMemberService.startProcess(G.PROCESS_ZCCZ, management.getId(), userId);
        }


        if (idList.length > 0) {
            for (int i = 0; i < idList.length; i++) {
                ManagementAssets managementAssets = new ManagementAssets();
                managementAssets.setStatus("2");
                managementAssets.setProposal(proposalList[i]);
                managementAssets.setAssets_id(Integer.parseInt(idList[i]));
                managementAssets.setManagement_id(management.getId());
                managementAssets.setCreate_user(sysUser.getId());
                management.setCreate_time(new Date());
                managementAssets.setIs_del(G.ISDEL_NO);
                managementAssetsMapper.insert(managementAssets);
                AssetsManage assetsManage = assetsManageMapper.selectById(Integer.parseInt(idList[i]));
                if (1 == type) {
                    operationRecordService.appAddOperationRecord(management.getId(), assetsManage.getAsset_code(), "处置申请-新增处置单", "7", userId);
                } else {
                    operationRecordService.appAddOperationRecord(management.getId(), assetsManage.getAsset_code(), "新增处置申请单并送审", "7", userId);
                }

            }

        }


    }
}
