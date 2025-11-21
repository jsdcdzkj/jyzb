package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.AssetsManageMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.utils.PostUtils;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("external")
public class ExternalMaintenanceController extends BaseController {

    @Autowired
    private ExternalMaintenanceService externalMaintenanceService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ApplySingleService applySingleService;
    @Autowired
    private ProcessMemberService processMemberService;
    @Autowired
    private AssetsManageMapper assetsManageMapper;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private PictureImgService pictureService;
    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping("toIndex.do")
    public String toIndex(){
        return "system/external/index";
    }

    /**
     * 分页查询
     * @param externalMaintenance
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(ExternalMaintenance externalMaintenance,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(externalMaintenanceService.getPage(externalMaintenance, page, limit));
    }

    @RequestMapping("toAdd.do")
    public String toAdd(Model model){
        return "/system/external/add";
    }

    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model){
        ExternalMaintenance externalMaintenance = externalMaintenanceService.selectById(id);
        model.addAttribute("em", externalMaintenance);
        return "system/external/edit";
    }

    /**
     * 添加
     * @param externalMaintenance
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(ExternalMaintenance externalMaintenance){
        try {
            externalMaintenanceService.add(externalMaintenance);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(ExternalMaintenance externalMaintenance)  {
        try {
            externalMaintenanceService.edit(externalMaintenance);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(Integer id){
        return ResultInfo.success(externalMaintenanceService.deleteEm(id));
    }

    @RequestMapping("toEMApproveIndex.do")
    public String toEMApproveIndex(){
        return "shenpimana/weixiusp/index";
    }

    /**
     * 待审批数据
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "adopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ApplySingleVo bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_WBWX);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        bean.setIsexternal(1);
        PageInfo<ApplySingleVo> pageInfo = applySingleService.getApprovedListByPage(bean, page, limit, sysUserService.getUser());
        return ResultInfo.success(pageInfo);
    }

    /**
     * 维修审批
     * 执行审批操作
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "pendingApproval.json")
    @ResponseBody
    public ResultInfo pendingApproval(ApplySingleVo bean, String logs) {
        if (Base.empty(logs)) {
            logs = bean.getApproval_state().equals(PostUtils.carry_manage_approved) ? "审批通过" : "审批驳回";
        } else {
            logs = bean.getApproval_state().equals(PostUtils.carry_manage_approved) ? "审批通过，" + logs : "审批驳回，" + logs;
        }
        // 判断是通过还是驳回
        if (bean.getApproval_state().equals(PostUtils.carry_manage_reject)) {
            // 得到当前流程节点信息
            ProcessConfig processConfig = processMemberService.getInfoByBusId(bean.getId(), G.PROCESS_WBWX);
            if (null == processConfig) {
                new RuntimeException("流程节点信息不存在");
            }
            ProcessConfigInfo currentNodeInfo = processMemberService.getCurrentNodeInfo(processConfig.getProcessMember(), sysUserService.getUser());
            if (null != currentNodeInfo) {
                processConfig.getProcessMember().setNode_id(currentNodeInfo.getId());
            }
            processMemberService.removeProcessMember(processConfig.getProcessMember(), JSON.toJSONString(bean));
//            historyService.addProcessMemberHistory(processConfig.getProcessMember(), logs, JSON.toJSONString(bean));
        } else {
            // 得到流程的判断条件 processNum purchase_amount
            Integer processNum = 0;
            // 得到资产信息
            ApplySingle applySingle = applySingleService.selectById(bean.getId());
            AssetsManage assetsManage = assetsManageMapper.selectById(applySingle.getAssetmanage_id());
            List<Integer> ids = new ArrayList<>();
            if (null != assetsManage) {
                ids.add(assetsManage.getAsset_type_id());
            }
            if (processMemberService.processing(bean.getId(), G.PROCESS_WBWX, sysUserService.getUser(), logs, processNum, ids, JSON.toJSONString(bean))) {
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getGood_number())
                        .type(PostUtils.operation_type_wbwx).operate_id(bean.getId()).record(logs).build());
                return ResultInfo.success("审批成功");
            }
        }
        Integer count = applySingleService.updateState(bean);
        if (count > 0) {
            if (bean.getApproval_state().equals(PostUtils.carry_manage_approved)) {
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getGood_number())
                        .type(PostUtils.operation_type_wbwx).operate_id(bean.getId()).record(logs).build());
            } else if (bean.getApproval_state().equals(PostUtils.carry_manage_reject)) {
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getGood_number())
                        .type(PostUtils.operation_type_wbwx).operate_id(bean.getId()).record(logs).build());
            }
            return ResultInfo.success("操作成功");
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
    @RequestMapping(value = "pendingApprovalAll.json")
    @ResponseBody
    public ResultInfo pendingApprovalAll(ApplySingleVo bean,String logs) {
        if (Base.empty(logs)) {
            logs = bean.getApproval_state().equals("4")?"审批通过":"审批驳回";
        } else {
            logs = bean.getApproval_state().equals("4")?"审批通过，" + logs : "审批驳回，" + logs;
        }
        // 得到当前流程节点信息
        ProcessConfig processConfig = processMemberService.getInfoByBusId(bean.getId(), G.PROCESS_WBWX);
        if (null == processConfig) {
            throw new RuntimeException("流程节点信息不存在");
        }
        ProcessConfigInfo currentNodeInfo = null;
        currentNodeInfo = processMemberService.getCurrentNodeInfo(processConfig.getProcessMember(), sysUserService.getUser());
        if (null != currentNodeInfo) {
            processConfig.getProcessMember().setNode_id(currentNodeInfo.getId());
        }
        processMemberService.removeProcessMember(processConfig.getProcessMember(), JSON.toJSONString(bean));
        //        historyService.addProcessMemberHistory(processConfig.getProcessMember(), logs, JSON.toJSONString(bean));

        if (bean.getApproval_state().equals(PostUtils.carry_manage_approved)) {
            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(bean.getGood_number())
                    .type(PostUtils.operation_type_wbwx).operate_id(bean.getId()).record(logs).build());
        } else if (bean.getApproval_state().equals(PostUtils.carry_manage_reject)) {
            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(bean.getGood_number())
                    .type(PostUtils.operation_type_wbwx).operate_id(bean.getId()).record(logs).build());
        }
        Integer count = applySingleService.updateState(bean);
        if (count > 0) {
            return ResultInfo.success("操作成功");
        } else {
            return ResultInfo.error("操作失败");
        }
    }

    /**
     * 已审批数据
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "finishAdopt.json")
    @ResponseBody
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ApplySingleVo bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUser, G.PROCESS_WBWX);
        if (CollectionUtils.isEmpty(busIds)) {
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo pageInfo = applySingleService.finishAdopt(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 详情页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "selectByDetails.do")
    public String selectByDetails(Integer id, Integer type, Model model) {
        ApplySingleVo applySingle = applySingleService.findApplySingleDetils(id);
        List<PictureImg> lists = pictureService.getLists(id, 1);
        //维修人员反馈
        Feedback feed = feedbackService.selectOne(new QueryWrapper<Feedback>().eq("applysingle_Id", id));
        if(notEmpty(feed)){
            List<PictureImg> feedList = pictureService.getLists(feed.getId(), 2);
            model.addAttribute("feedList", feedList);
            model.addAttribute("feedback", feed);
        }
        List<ExternalMaintenance> emList = externalMaintenanceService.selectAll();
        List<SysUser> sysUsers = sysUserService.selectList(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getIs_del, G.ISDEL_NO));
        if (notEmpty(applySingle.getIsexternal())) {
            ExternalMaintenance externalMaintenance = externalMaintenanceService.selectById(applySingle.getUnitid());
            applySingle.setUnit(externalMaintenance.getUnitname());
            SysUser sysUser = sysUserService.selectById(applySingle.getManagers());
            applySingle.setManager(sysUser.getUser_name());
        }
        model.addAttribute("emList", emList);
        model.addAttribute("userList", sysUsers);
        model.addAttribute("lists", lists);
        model.addAttribute("apply", applySingle);
        model.addAttribute("menu_type", 1);
        model.addAttribute("type", type);
        return "jdfeddback/details";
    }

    //操作日志
    @RequestMapping(value = "toLogView.do")
    public String toLogView(Integer id, Model model) {
        List<OperationRecord> operationRecords = operationRecordService.
                selectList(Wrappers.<OperationRecord>query().
                        eq("type", PostUtils.operation_type_wbwx).
                        eq("operate_id", id).
                        eq("is_del", "0").orderByDesc("create_time"));
        model.addAttribute("operationRecords", operationRecords);
        return "/purchase/capital/view-log";
    }
}
