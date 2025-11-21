package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.core.base.Base;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.AssetsManageMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.utils.PostUtils;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ConsPurchaseApplyVo;
import vo.PurchaseApplyVo;
import vo.ResultInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程业务扭转控制器
 */
@Controller
@RequestMapping("/")
public class ProcessBusController {

    @Autowired
    private PurchaseApplyService purchaseApplyService;

    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private ProcessMemberHistoryService historyService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ReceiveAssetsService assetsService;

    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private AssetsManageService assetsManageService;

    @Autowired
    private ChangeInfoService changeInfoService;

    @Autowired
    private AssetsManageMapper assetsManageMapper;

    // ------- 公共方法 -------
    /**
     * 驳回
     */
    private void reject(Integer busId, String processType, String logs, String json) {
        // 得到当前流程节点信息
        ProcessConfig processConfig = processMemberService.getInfoByBusId(busId, processType);
        if (null == processConfig) {
            throw new RuntimeException("流程节点信息不存在");
        }
        ProcessConfigInfo currentNodeInfo = processMemberService.getCurrentNodeInfo(processConfig.getProcessMember(), sysUserService.getUser());
        if (null != currentNodeInfo) {
            processConfig.getProcessMember().setNode_id(currentNodeInfo.getId());
        }
        processConfig.getProcessMember().setRejectReason(logs);
        processConfig.getProcessMember().setMsgStatus(2);
        historyService.addProcessMemberHistory(processConfig.getProcessMember(), "流程驳回", json);
        processMemberService.removeProcessMember(processConfig.getProcessMember(), json);
    }
    /**
     * 一键清除流程
     */
    private void clearProcess(Integer busId, String processType, String json, String logs){
        // 得到当前流程节点信息
        ProcessConfig processConfig = processMemberService.getInfoByBusId(busId, processType);
        if (null == processConfig) {
            throw new RuntimeException("流程节点信息不存在");
        }
        ProcessConfigInfo currentNodeInfo = processMemberService.getCurrentNodeInfo(processConfig.getProcessMember(), sysUserService.getUser());
        if (null != currentNodeInfo) {
            processConfig.getProcessMember().setNode_id(currentNodeInfo.getId());
        }
        processConfig.getProcessMember().setApprovalReason(logs);
        historyService.addProcessMemberHistory(processConfig.getProcessMember(), "一键审批", json);
        processMemberService.removeProcessMember(processConfig.getProcessMember(), json);
    }

    /**
     * 通过驳回logs
     */
    private String getLogs(boolean flag, String logs){
        if (Base.empty(logs)) {
            return flag ? "审批通过" : "审批驳回";
        } else {
            return flag ? "审批通过: <div>" + logs + "</div>" : "审批驳回: <div>" + logs + "</div>";
        }
    }

    // ---------------------------- 资产采购 ----------------------------------------------
    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "purchaseApply/pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(PurchaseApply bean, String logs) {
        String oldLog = logs;
        logs = getLogs(bean.getApprove_status().equals("4"), logs);
        // 判断是通过还是驳回
        if (bean.getApprove_status().equals("5")) {
            reject(bean.getId(), G.PROCESS_ZCCG, oldLog, JSON.toJSONString(bean));
        } else {
            // 得到流程的判断条件 processNum purchase_amount
            PurchaseApplyVo purchaseApplyVo = purchaseApplyService.getById(bean.getId());
            PurchaseApply purchaseApply = purchaseApplyVo.getPurchaseApply();
            String purchase_amount = purchaseApply.getPurchase_amount();
            int processNum = 0;
            if (StringUtils.isNotBlank(purchase_amount)) {
                BigDecimal bigDecimal = new BigDecimal(purchase_amount);
                processNum = null != bigDecimal ? bigDecimal.intValue() : 0;
            }
            List<Integer> ids = new ArrayList<>();
            List<PurchaseDetail> a = purchaseApplyVo.getPurchaseDetails();
            if (!CollectionUtils.isEmpty(a)) {
                ids = a.stream().map(PurchaseDetail::getAssets_type_id).collect(Collectors.toList());
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_ZCCG, sysUserService.getUser(), logs, processNum, ids, JSON.toJSONString(bean))) {
                operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getPurchase_no()).operate_id(bean.getId()).type("2").is_del("0").record(logs).build());
                return ResultInfo.successMsg("审批成功", "通过成功");
            }
        }
        operationRecordService.addOperationRecord(
            OperationRecord.builder().field_fk(bean.getPurchase_no()).operate_id(bean.getId()).type("2").is_del("0").record(logs).build()
        );
        int count = purchaseApplyService.updateById(bean);
        if (count > 0) {
            return ResultInfo.successMsg("操作成功", (bean.getApprove_status().equals("5")?"驳回":"通过") + "成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "purchaseApply/pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(PurchaseApply bean, String logs) {
        // 清理流程
        clearProcess(bean.getId(), G.PROCESS_ZCCG, JSON.toJSONString(bean), logs);
        logs = getLogs(bean.getApprove_status().equals("4"), logs);
        operationRecordService.addOperationRecord(
                OperationRecord.builder().field_fk(bean.getPurchase_no()).operate_id(bean.getId()).type("2").is_del("0").record(logs).build()
        );
        int count = purchaseApplyService.updateById(bean);
        if (count > 0) {
            return ResultInfo.successMsg("审批成功", "通过成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    // ---------------------------- 资产申领 ----------------------------------------------
    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "receive/pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(Receive bean,String logs) {
        String oldLog = logs;
        logs = getLogs(bean.getStatus().equals(PostUtils.approved), logs);
        // 判断是通过还是驳回
        if (bean.getStatus().equals(PostUtils.reject)) {
            reject(bean.getId(), G.PROCESS_ZCSL, oldLog, JSON.toJSONString(bean));
        }else{
            // 得到流程的判断条件 processNum
            Integer processNum = 0;
            List<Integer> ids = new ArrayList<>();
            List<ReceiveAssets> receiveAssets = receiveService.getOneById(bean.getId());
            if (!CollectionUtils.isEmpty(receiveAssets)) {
                for (ReceiveAssets receiveAsset : receiveAssets) {
                    AssetsManage a = assetsManageService.selectById(receiveAsset.getAssets_id());
                    ids.add(a.getAsset_type_id());
                }
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_ZCSL, sysUserService.getUser(), logs, processNum,ids, JSON.toJSONString(bean))){
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getReceive_code()).operate_id(bean.getId())
                        .type(PostUtils.operation_type_ly).record(logs).build());
                operationRecordService.addOtherOperationRecord(bean.getId(), null, logs, "1");
                return ResultInfo.successMsg("审批成功", "通过成功");
            }
        }

        // 没有下一个节点
        int count = receiveService.updateById(bean);
        if (count > 0) {
            operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getReceive_code()).operate_id(bean.getId()).type(PostUtils.operation_type_ly).record(logs).build());
            operationRecordService.addOtherOperationRecord(bean.getId(), null, logs, "1");
            //审批通过
            if (PostUtils.approved.equals(bean.getStatus())) {
                List<ReceiveAssets> list = assetsService.selectList(new QueryWrapper<ReceiveAssets>().eq("receive_id", bean.getId()));
                list.forEach(a -> {
                    a.setReceive_status("1");
                    assetsService.updateById(a);
                });
            }
            return ResultInfo.successMsg("操作成功", (bean.getStatus().equals(PostUtils.reject)?"驳回":"通过") + "成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "receive/pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(Receive bean,String logs) {
        clearProcess(bean.getId(), G.PROCESS_ZCSL, JSON.toJSONString(bean), logs);
        logs = getLogs(bean.getStatus().equals(PostUtils.approved), logs);
        // 没有下一个节点
        int count = receiveService.updateById(bean);
        if (count > 0) {
            operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getReceive_code()).operate_id(bean.getId()).type(PostUtils.operation_type_ly).record(logs).build());
            operationRecordService.addOtherOperationRecord(bean.getId(), null, logs, "1");
            //审批通过
            if (PostUtils.approved.equals(bean.getStatus())) {
                List<ReceiveAssets> list = assetsService.selectList(new QueryWrapper<ReceiveAssets>().eq("receive_id", bean.getId()));
                list.forEach(a -> {
                    a.setReceive_status("1");
                    assetsService.updateById(a);
                });
            }
            return ResultInfo.successMsg("审批成功", "通过成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    // ---------------------------- 资产变更 ----------------------------------------------
    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "changeInfo/pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(ChangeInfo bean,String logs) {
        String oldLog = logs;
        logs = getLogs(bean.getApprove_status().equals(PostUtils.approved), logs);
        // 判断是通过还是驳回
        if (bean.getApprove_status().equals(PostUtils.reject)) {
            reject(bean.getId(), G.PROCESS_ZCBG, oldLog, JSON.toJSONString(bean));
        }else{
            // 得到流程的判断条件 processNum purchase_amount
            Integer processNum = 0;
            List<ChangeDetail> details = changeInfoService.getInfoByChangeId(bean.getId());
            List<Integer> ids = new ArrayList<>();
            if (!CollectionUtils.isEmpty(details)) {
                List<Integer> assetIds = details.stream().distinct().map(ChangeDetail::getAssets_id).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(assetIds)) {
                    assetsManageMapper.selectList(Wrappers.<AssetsManage>lambdaQuery().in(AssetsManage::getId, assetIds)).forEach(assetsManage -> {
                        ids.add(assetsManage.getAsset_type_id());
                    });
                }
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_ZCBG, sysUserService.getUser(), logs, processNum, ids,JSON.toJSONString(bean))){
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getChange_code())
                        .type(PostUtils.operation_type_bg).operate_id(bean.getId()).record(logs).build());
                operationRecordService.addOtherOperationRecord(bean.getId(),null,logs,"2");
                return ResultInfo.successMsg("审批成功", "通过成功");
            }
        }
        int count = changeInfoService.updateById(bean);
        if (count > 0) {
            operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getChange_code()).type(PostUtils.operation_type_bg).operate_id(bean.getId()).record(logs).build());
            operationRecordService.addOtherOperationRecord(bean.getId(),null,logs,"2");
            if (bean.getApprove_status().equals(PostUtils.approved)) {
                changeInfoService.changeAssManageInfo(bean.getId());
            }
            return ResultInfo.successMsg("操作成功", (bean.getApprove_status().equals(PostUtils.reject)?"驳回":"通过") + "成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 一键审批
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "changeInfo/pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(ChangeInfo bean,String logs) {
        clearProcess(bean.getId(), G.PROCESS_ZCBG, JSON.toJSONString(bean), logs);
        logs = getLogs(bean.getApprove_status().equals(PostUtils.approved), logs);
        int count = changeInfoService.updateById(bean);
        if (count > 0) {
            operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getChange_code()).type(PostUtils.operation_type_bg).operate_id(bean.getId()).record(logs).build());
            operationRecordService.addOtherOperationRecord(bean.getId(),null,logs,"2");
            if (bean.getApprove_status().equals(PostUtils.approved)) {
                changeInfoService.changeAssManageInfo(bean.getId());
            }
            return ResultInfo.successMsg("审批成功", "通过成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    // ---------------------------- 资产处置 ----------------------------------------------
    @Autowired
    private ManagementService managementService;
    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "management/pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(Management bean,String logs) {
        String oldLog = logs;
        logs = getLogs(bean.getStatus().equals(PostUtils.approved), logs);
        if (StringUtils.isBlank(bean.getManagement_code()) && null != bean.getId()){
            String management_code = managementService.selectById(bean.getId()).getManagement_code();
            bean.setManagement_code(management_code);
        }
        // 判断是通过还是驳回
        if (bean.getStatus().equals(PostUtils.reject)) {
            reject(bean.getId(), G.PROCESS_ZCCZ, oldLog, JSON.toJSONString(bean));
        }else{
            // 得到流程的判断条件 processNum
            Integer processNum = 0;
            List<Integer> ids = new ArrayList<>();
            List<ManagementAssets> assetsList = managementService.getDetailInfo(bean.getId());
            if (!CollectionUtils.isEmpty(assetsList)){
                for (ManagementAssets assets : assetsList){
                    AssetsManage a = assetsManageService.selectById(assets.getAssets_id());
                    ids.add(a.getAsset_type_id());
                }
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_ZCCZ, sysUserService.getUser(), logs, processNum,ids, JSON.toJSONString(bean))){
//                operationRecordService.addOperationRecord(OperationRecord.builder()
//                        .field_fk(bean.getManagement_code())
//                        .type(PostUtils.operation_type_cz).operate_id(bean.getId()).record(logs).build());
                operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getManagement_code()).type(PostUtils.operation_type_cz).operate_id(bean.getId()).record(logs).build());
                operationRecordService.addOtherOperationRecord(bean.getId(),null,logs,"3");
                return ResultInfo.successMsg("审批成功", "通过成功");
            }
        }
        int count = managementService.updateById(bean);
        if (count > 0) {
            operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getManagement_code()).type(PostUtils.operation_type_cz).operate_id(bean.getId()).record(logs).build());
            operationRecordService.addOtherOperationRecord(bean.getId(),null,logs,"3");
            return ResultInfo.successMsg("操作成功", (bean.getStatus().equals(PostUtils.reject)?"驳回":"通过") + "成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "management/pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(Management bean,String logs) {
        clearProcess(bean.getId(), G.PROCESS_ZCCZ, JSON.toJSONString(bean), logs);
        logs = getLogs(bean.getStatus().equals(PostUtils.approved), logs);
        int count = managementService.updateById(bean);
        if (count > 0) {
            operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getManagement_code()).type(PostUtils.operation_type_cz).operate_id(bean.getId()).record(logs).build());
            operationRecordService.addOtherOperationRecord(bean.getId(),null,logs,"3");
            return ResultInfo.successMsg("审批成功", "通过成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    // ---------------------------- 资产维修 ----------------------------------------------
    @Autowired
    private RepairApplyService repairApplyService;
    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "repairApply/pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(RepairApply bean,String logs) {
        String oldLog = logs;
        logs = getLogs(bean.getStatus().equals(PostUtils.approved), logs);
        // 判断是通过还是驳回
        if (bean.getStatus().equals(PostUtils.reject)) {
            reject(bean.getId(), G.PROCESS_WXZC, oldLog, JSON.toJSONString(bean));
        }else{
            RepairApply repairApply = repairApplyService.getRepairApplyById(String.valueOf(bean.getId()));
            // 得到流程的判断条件 processNum
            Integer processNum = null;
            try {
                processNum = Integer.valueOf(repairApply.getTotal_cost());
            } catch (NumberFormatException e) {
                processNum = 0;
            }
            List<Integer> ids = new ArrayList<>();

            if (!CollectionUtils.isEmpty(repairApply.getApplyMembers())) {
                for (RepairApplyMember temp : repairApply.getApplyMembers()) {
                    AssetsManage a = assetsManageMapper.selectById(temp.getAsset_id());
                    ids.add(a.getAsset_type_id());
                }
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_WXZC, sysUserService.getUser(), logs, processNum,ids, JSON.toJSONString(bean))){
                operationRecordService.addOtherOperationRecord(bean.getId(), null, logs, "5");
                return ResultInfo.successMsg("审批成功", "通过成功");
            }
        }

        // 没有下一个节点
        int count = repairApplyService.updateById(bean);
        if (count > 0) {
            operationRecordService.addOtherOperationRecord(bean.getId(), null, logs, "5");
            return ResultInfo.successMsg("操作成功", (bean.getStatus().equals(PostUtils.reject)?"驳回":"通过") + "成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "repairApply/pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(RepairApply bean,String logs) {
        clearProcess(bean.getId(), G.PROCESS_WXZC, JSON.toJSONString(bean), logs);
        logs = getLogs(bean.getStatus().equals(PostUtils.approved), logs);
        // 没有下一个节点
        int count = repairApplyService.updateById(bean);
        if (count > 0) {
            operationRecordService.addOtherOperationRecord(bean.getId(), null, logs, "5");
            return ResultInfo.successMsg("审批成功", "通过成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    // ---------------------------- 资产外携 ----------------------------------------------
    @Autowired
    private CarryManageService carryManageService;
    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "carrymanage/pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(CarryManage bean, String logs) {
        String oldLog = logs;
        logs = getLogs(bean.getApproval_state().equals(PostUtils.carry_manage_approved), logs);
        // 判断是通过还是驳回
        if (bean.getApproval_state().equals(PostUtils.carry_manage_reject)) {
            reject(bean.getId(), G.PROCESS_ZCWX, oldLog, JSON.toJSONString(bean));
        } else {
            // 得到流程的判断条件 processNum purchase_amount
            Integer processNum = 0;
            // 得到资产信息
            CarryManage carry = carryManageService.selectById(bean.getId());
            AssetsManage assetsManage = assetsManageMapper.selectById(carry.getAsset_manage_id());
            List<Integer> ids = new ArrayList<>();
            if (null != assetsManage) {
                ids.add(assetsManage.getAsset_type_id());
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_ZCWX, sysUserService.getUser(), logs, processNum, ids, JSON.toJSONString(bean))) {
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getAssetnumber())
                        .type(PostUtils.operation_type_wx).operate_id(bean.getId()).record(logs).build());
                return ResultInfo.successMsg("审批成功", "通过成功");
            }
        }
        Integer count = carryManageService.updateState(bean);
        if (count > 0) {
            operationRecordService.addOperationRecord(
                    OperationRecord.builder().field_fk(bean.getAssetnumber()).type(PostUtils.operation_type_wx).operate_id(bean.getId()).record(logs).build()
            );
            return ResultInfo.successMsg("操作成功", (bean.getApproval_state().equals(PostUtils.carry_manage_reject)?"驳回":"通过") + "成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 执行审批操作all
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "carrymanage/pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(CarryManage bean, String logs) {
        clearProcess(bean.getId(), G.PROCESS_ZCWX, JSON.toJSONString(bean), logs);
        logs = getLogs(bean.getApproval_state().equals(PostUtils.carry_manage_approved), logs);
        Integer count = carryManageService.updateState(bean);
        if (count > 0) {
            operationRecordService.addOperationRecord(
                    OperationRecord.builder().field_fk(bean.getAssetnumber()).type(PostUtils.operation_type_wx).operate_id(bean.getId()).record(logs).build()
            );
            return ResultInfo.successMsg("审批成功", "通过成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    // ---------------------------- 耗材采购 ----------------------------------------------
    @Autowired
    private ConsPurchaseApplyService consPurchaseApplyService;
    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "consPurchaseApply/pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(ConsPurchaseApply bean, String logs) {
        String oldLog = logs;
        logs = getLogs(bean.getApprove_status().equals("4"), logs);
        // 判断是通过还是驳回
        if (bean.getApprove_status().equals("5")) {
            reject(bean.getId(), G.PROCESS_HCCG, oldLog, JSON.toJSONString(bean));
        } else {
            ConsPurchaseApplyVo now = consPurchaseApplyService.getById(bean.getId());
            ConsPurchaseApply consPurchaseApply = now.getPurchaseApply();
            // 得到流程的判断条件 processNum purchase_amount
            String purchase_amount = consPurchaseApply.getPurchase_amount();
            int processNum = 0;
            if (StringUtils.isNotBlank(purchase_amount)) {
                BigDecimal bigDecimal = new BigDecimal(purchase_amount);
                processNum = null != bigDecimal ? bigDecimal.intValue() : 0;
            }
            List<Integer> ids = new ArrayList<>();
            List<ConsPurchaseDetail> details = now.getPurchaseDetails();
            if (!CollectionUtils.isEmpty(details)) {
                ids = details.stream().map(ConsPurchaseDetail::getConsumable_id2).collect(Collectors.toList());
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_HCCG, sysUserService.getUser(), logs, processNum, ids, JSON.toJSONString(bean))) {
                operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getPurchase_no()).operate_id(bean.getId()).type("2").is_del("0").record(logs).build());
                return ResultInfo.successMsg("审批成功", "通过成功");
            }
        }
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getPurchase_no()).operate_id(bean.getId()).type("2").is_del("0").record(logs).build());
        int count = consPurchaseApplyService.updateById(bean);
        if (count > 0) {
            return ResultInfo.successMsg("操作成功", (bean.getApprove_status().equals("5")?"驳回":"通过") + "成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 执行 一键审批
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "consPurchaseApply/pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(ConsPurchaseApply bean, String logs) {
        clearProcess(bean.getId(), G.PROCESS_HCCG, JSON.toJSONString(bean), logs);
        logs = getLogs(bean.getApprove_status().equals("4"), logs);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getPurchase_no()).operate_id(bean.getId()).type("2").is_del("0").record(logs).build());
        int count = consPurchaseApplyService.updateById(bean);
        if (count > 0) {
            return ResultInfo.successMsg("审批成功", "通过成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    // ---------------------------- 耗材申领 ----------------------------------------------
    @Autowired
    private ConsReceiveService consReceiveService;

    @Autowired
    private ConsumableService consumableService;
    /**
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "consReceive/pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(ConsReceive bean, String logs) {
        String oldLog = logs;
        logs = getLogs(bean.getStatus().equals(PostUtils.approved), logs);
        // 判断是通过还是驳回
        if (bean.getStatus().equals("5")) {
            reject(bean.getId(), G.PROCESS_HCSL, oldLog, JSON.toJSONString(bean));
        }else{
            // 得到流程的判断条件 processNum
            Integer processNum = 0;
            List<Integer> ids = new ArrayList<>();
            List<ConsReceiveAssets> assets = consReceiveService.getOneInfoById(bean.getId());
            if (!CollectionUtils.isEmpty(assets)){
                for (ConsReceiveAssets asset : assets) {
                    List<Consumable> consumables = consumableService.selectList(Wrappers.<Consumable>lambdaQuery().eq(Consumable::getConsumable_name, asset.getConsumable_name()));
                    ids.add(consumables.get(0).getId());
                }
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_HCSL, sysUserService.getUser(), logs, processNum, ids, JSON.toJSONString(bean))){
                operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getReceive_code()).operate_id(bean.getId()).type("11").is_del("0").record(logs).build());
                return ResultInfo.successMsg("审批成功", "通过成功");
            }
        }
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(bean.getReceive_code()).operate_id(bean.getId()).type("11").is_del("0").record(logs).build());
        int count = consReceiveService.updateById(bean);
        if (count > 0 && bean.getStatus().equals("4")) {
            consReceiveService.checkOut(bean.getId());
            return ResultInfo.success("审批成功");
        }else if (count > 0){
            return ResultInfo.success("驳回成功");
        }else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 执行 一键审批
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "consReceive/pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(ConsReceive bean, String logs) {
        clearProcess(bean.getId(), G.PROCESS_HCSL, JSON.toJSONString(bean), logs);
        logs = getLogs(bean.getStatus().equals(PostUtils.approved), logs);
        operationRecordService.addOperationRecord(
                OperationRecord.builder().field_fk(bean.getReceive_code()).
                operate_id(bean.getId()).type("11").is_del("0").record(logs).build()
        );
        int count = consReceiveService.updateById(bean);
        if (count > 0 && bean.getStatus().equals("4")) {
            consReceiveService.checkOut(bean.getId());
            return ResultInfo.success("审批成功");
        }else if (count > 0){
            return ResultInfo.success("驳回成功");
        }else {
            return ResultInfo.error("操作失败");
        }
    }
}
