package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.AssetsManageMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/repairApply")
public class RepairApplyController {

    @Autowired
    private RepairApplyService repairApplyService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AssetsTypeService assetsTypeService;

    @Autowired
    private ProcessMemberService processMemberService;
    
    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private AssetsManageMapper assetsManageMapper;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysDepartmentService departmentService;

    /**
     * 去到维修申请页面
     */
    @RequestMapping("/toRepairApplyPage")
    public String toRepairApplyPage(Model model) {
        // 所有部门
        List<SysDepartment> departments = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del,G.ISDEL_NO));
        model.addAttribute("departments", departments);
        return "repairApply/index";
    }

    /**
     * 去到维修新增或者编辑页面
     */
    @RequestMapping("/toEditPage.do")
    public String toEditPage(@RequestParam(value = "id", required = false) String id, Model model) {
        if (StringUtils.isNotBlank(id)) {
            model.addAttribute("repairApply", repairApplyService.getRepairApplyById(id));
        }
        model.addAttribute("sysUser", sysUserService.getUser());
        return "repairApply/edit";
    }

    /**
     * 跳转资产展示页面
     *
     * @return
     */
    @RequestMapping("toSelect.do")
    public String toSelect(Model model){
        //获取所有资产类型
        List<AssetsType> assetsTypes =  assetsTypeService.selectList(Wrappers.<AssetsType>lambdaQuery()
                .eq(AssetsType::getIs_del, G.ISDEL_NO));
        model.addAttribute("assetsTypes", assetsTypes);
        return "repairApply/select";
    }

    /**
     * 跳转待审批页面
     *
     * @return
     */
    @RequestMapping(value = "adopt.do")
    public String adoptList(Model model, Integer status) {
        model.addAttribute("status", status);
        // 得到部门
        List<SysDepartment> departments = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del,G.ISDEL_NO));
        model.addAttribute("departments", departments);
        return "shenpimana/weixiusp/index";
    }

    /**
     * 跳转日志详情
     *
     * @return
     */
    @RequestMapping("view-log")
    public String toViewLog() {
        return "repairApply/view-log";
    }

    /**
     * 跳转展示详情页
     *
     * @return
     */
    @RequestMapping("toViewIndex.do")
    public String toViewIndex(Integer id, Integer type, Model model) {
        RepairApply receive = repairApplyService.getRepairApplyById(String.valueOf(id));
        processMemberService.getProcessDataByBusId(receive.getId(), G.PROCESS_WXZC,sysUserService.getUser(),receive);
        model.addAttribute("bean", receive);
        model.addAttribute("beanMember", receive.getApplyMembers());
        model.addAttribute("id", id);
        model.addAttribute("type", type);

        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_WXZC);
        model.addAttribute("key", dictionary.getValue());
        return "repairApply/view";
    }

    /**
     * 获取所有符合条件的资产
     * @return
     */
    @RequestMapping(value = "getLeaveUnused.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getLeaveUnused(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer limit,
                                     AssetsManage assetsManage,
                                     @RequestParam(required = false) String arr ){
        List<ReceiveAssets> list = new ArrayList<>();
        if (!StringUtils.isEmpty(arr)){
            JSONArray array = JSON.parseArray(arr);
            list = array.toJavaList(ReceiveAssets.class);
        }
        return repairApplyService.getLeaveUnused(page, limit, assetsManage, list, sysUserService.getUser());
    }

    /**
     * 根据id获取维修申请信息
     */
    @RequestMapping(value = "/getRepairApplyById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getRepairApplyById(@RequestParam(value = "id") String id) {
        RepairApply repairApply = repairApplyService.getRepairApplyById(id);
        return ResultInfo.success(repairApply);
    }

    /**
     * 新增或者编辑维修申请
     */
    @RequestMapping(value = "/saveOrUpdateRepairApply.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveOrUpdateRepairApply(@NonNull String management_data) {
        RepairApply repairApply = JSON.parseObject(management_data, RepairApply.class);
        repairApplyService.saveOrUpdateRepairApply(repairApply);
        return ResultInfo.success();
    }

    /**
     * 新增时,保存并送审
     */
    @RequestMapping(value = "/saveOrUpdateSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveAndSubmit(@NonNull String management_data) {
        RepairApply repairApply = JSON.parseObject(management_data, RepairApply.class);
        repairApplyService.saveAndSubmit(repairApply, sysUserService.getUser());
        return ResultInfo.success();
    }

    /**
     * 删除维修申请
     */
    @GetMapping("/deleteRepairApply.do")
    @ResponseBody
    public ResultInfo deleteRepairApply(@RequestParam(value = "id") String id) {
        repairApplyService.deleteRepairApply(id);
        return ResultInfo.success();
    }

    /**
     * 维修单送审
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "submitRepairApply.do")
    @ResponseBody
    public ResultInfo submitRepairApply(Integer id) {
        return repairApplyService.submitRepairApply(id);
    }

    /**
     * 查询申请列表
     */
    @RequestMapping(value = "/getRepairApplyList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getRepairApplyList(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer limit,
                                         RepairApply repairApply) {
        return repairApplyService.getRepairApplyList(page, limit, repairApply, sysUserService.getUser());
    }


    /**
     * 查询单个表单日志
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getOperationList.do")
    @ResponseBody
    public ResultInfo getOperationList(Integer id){
        List<OperationRecord> list = repairApplyService.getOperationList(id);
        return ResultInfo.success(list);
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
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, RepairApply bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_WXZC);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        bean.setIs_adopt(1);
        PageInfo<RepairApply> pageInfo = repairApplyService.getListByPage(bean, page, limit, sysUser);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 已审批数据
     */
    @RequestMapping(value = "/finishAdopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, RepairApply bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUser, G.PROCESS_WXZC);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo<RepairApply> pageInfo = repairApplyService.finishAdopt(bean, page, limit, sysUser);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 流程撤回
     */
    @GetMapping(value = "revoke.do")
    @ResponseBody
    public ResultInfo revoke(Integer id) {
        return repairApplyService.revoke(id, sysUserService.getUser());
    }

}
