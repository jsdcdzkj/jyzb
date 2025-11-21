package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.enums.DataType;
import com.jsdc.rfid.mapper.AssetsManageMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.utils.CommonDataTools;
import com.jsdc.rfid.utils.PostUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zln
 * @descript 外携申请管理
 * @date 2022-04-24
 */
@Controller
@RequestMapping("/carrymanage")
public class CarryManageController extends BaseController {

    @Autowired
    private CarryManageService carryManageService;
    @Autowired
    private AssetsManageService manageService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CommonDataTools commonDataTools;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysPostService sysPostService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private ProcessMemberHistoryService historyService;

    @Autowired
    private AssetsManageMapper assetsManageMapper;

    /**
     * 跳转我提交的数据页面
     *
     * @return
     */
    @RequestMapping(value = "pageList.do")
    public String pageList() {
        return "carrymanage/page";
    }

    /**
     * 我提交的数据
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "pageList.json")
    @ResponseBody
    public ResultInfo pageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, CarryManage bean) {
        bean.setCarry_id(sysUserService.getUser().getId());
        PageInfo pageInfo = carryManageService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 跳转审批通过列表分页页面
     *
     * @return
     */
    @RequestMapping(value = "adopt.do")
    public String adoptList() {
        return "carrymanage/adopt";
    }


    /**
     * 跳转待审批列表
     *
     * @return
     */
    @RequestMapping(value = "pendingApproval.do")
    public String pendingApproval(Model model, Integer status) {
        model.addAttribute("status", status);
        return "shenpimana/waixiesp/index";
    }


    /**
     * 待审批分页列表
     * 审批通过分页列表
     * (未做权限设置)
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "adopt.json")
    @ResponseBody
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, CarryManage bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
//        int userId = sysUser.getId();
//        Integer postId = sysUserService.selectById(userId).getPost();
//        if (null == postId) {
//            return ResultInfo.error("没有权限查看");
//        }
//        SysPost sysPost = sysPostService.selectById(postId);
//        if (null == sysPost) {
//            return ResultInfo.error("没有权限查看");
//        }
//        Integer data_permission = sysPost.getData_permission();
//        if (G.DATAPERMISSION_PERSONAL == data_permission) {//仅查看个人通过审核数据
//            bean.setUser_Id(userId);
//        } else if (G.DATAPERMISSION_DEPT == data_permission) {//查看所有部门数据
//            int department = sysUser.getDepartment();
//            bean.setDept_Id(department);
//        }
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCWX);
        if (CollectionUtils.isEmpty(busIds)) {
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        bean.setIs_adopt(1);
        PageInfo pageInfo = carryManageService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 待审批分页列表
     * 审批通过分页列表
     * (未做权限设置)
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "adopt1.json")
    @ResponseBody
    public ResultInfo adoptList1(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, CarryManage bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        int userId = sysUser.getId();
        Integer postId = sysUserService.selectById(userId).getPost();
        if (null == postId) {
            return ResultInfo.error("没有权限查看");
        }
        SysPost sysPost = sysPostService.selectById(postId);
        if (null == sysPost) {
            return ResultInfo.error("没有权限查看");
        }
        Integer data_permission = sysPost.getData_permission();
        if (G.DATAPERMISSION_PERSONAL == data_permission) {//仅查看个人通过审核数据
            bean.setUser_id(userId);
        } else if (G.DATAPERMISSION_DEPT == data_permission) {//查看所有部门数据
            int department = sysUser.getDepartment();
            bean.setDept_id(department);
        }
        // 根据流程进行业务id筛选
//        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCWX);
//        if (CollectionUtils.isEmpty(busIds)) {
//            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
//        }
//        bean.setIds(busIds);
        bean.setIs_adopt(1);
        PageInfo pageInfo = carryManageService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
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
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, CarryManage bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        // 根据流程进行业务id筛选
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUser, G.PROCESS_ZCWX);
        if (CollectionUtils.isEmpty(busIds)) {
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo pageInfo = carryManageService.finishAdopt(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 跳转详情、修改、新增页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "selectByCarryManageId.do")
    public String selectByCarryManageId(Integer id, Model model) {
        List<SysDepartment> dwList = departmentService.selectList(new QueryWrapper<SysDepartment>().eq("is_del", "0"));
        model.addAttribute("dwList", dwList);
        List<SysUser> users = sysUserService.selectList(new QueryWrapper<SysUser>().eq("is_del", "0"));
        model.addAttribute("users", users);
        CarryManage carryManage = new CarryManage();
        if (notEmpty(id)) {
            carryManage = carryManageService.selectById(id);
            model.addAttribute("bean", carryManage);
        } else {
            String code = commonDataTools.getNo(DataType.CARRYMANAGE_WX.getType(), null);
            carryManage.setNumbering(code);
            carryManage.setCarry_id(sysUserService.getUser().getId());
            carryManage.setCarry_name(sysUserService.getUser().getUser_name());
        }
        model.addAttribute("bean", carryManage);
        return "carrymanage/edit";
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
        if (notEmpty(id)) {
            CarryManage temp = carryManageService.selectById(id);
            SysUser current = sysUserService.getUser();
            // 得到当前流程节点
            processMemberService.getProcessDataByBusId(temp.getId(), G.PROCESS_ZCWX, current, temp);
            model.addAttribute("bean", temp);
        }
        model.addAttribute("type", type);

        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCWX);
        model.addAttribute("key", dictionary.getValue());
        return "carrymanage/details";
    }

    /**
     * 执行删除功能
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteCarryManage.json")
    @ResponseBody
    public ResultInfo deleteCarryManage(Integer id) {
        Integer count = carryManageService.deleteCarryManage(id);
        if (count > 0) {
            CarryManage carryManage = carryManageService.selectById(id);
            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(carryManage.getAssetnumber())
                    .type(PostUtils.operation_type_wx).operate_id(id).record("删除一条数据").build());
            return ResultInfo.success("操作成功！");
        } else {
            return ResultInfo.error("操作失败！");
        }
    }

    /**
     * 执行保存功能
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "saveCarryManage.json")
    @ResponseBody
    public ResultInfo saveCarryManage(CarryManage bean) {
        Integer id = null;
        if (notEmpty(bean.getId())) {
            id = bean.getId();
        }
        try {
            Integer count = carryManageService.saveCarryManage(bean);
            // 是否需要送审
            boolean isNeedApproval = StringUtils.equals(bean.getApproval_state(), "0");
            if (notEmpty(id)) {
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getAssetnumber())
                        .type(PostUtils.operation_type_wx)
                        .operate_id(bean.getId())
                        .record(isNeedApproval?"外携申请-编辑外携申请单并送审":"外携申请-编辑外携申请单")
                        .build());
            } else {
                operationRecordService.addOperationRecord(OperationRecord.builder()
                        .field_fk(bean.getAssetnumber())
                        .type(PostUtils.operation_type_wx)
                        .operate_id(bean.getId())
                        .record(isNeedApproval?"外携申请-新增外携申请单并送审":"外携申请-新增外携申请单")
                        .build());
            }
//            if (isNeedApproval) {
//                operationRecordService.addOperationRecord(OperationRecord.builder()
//                        .field_fk(bean.getAssetnumber())
//                        .type(PostUtils.operation_type_wx).operate_id(id).record("送审了一条数据").build());
//            }
            if (count > 0) {
                String msg = isNeedApproval?"送审成功":(notEmpty(id)?"编辑成功":"添加成功");
                return ResultInfo.builder().code(0).msg(msg).build();
            } else {
                return ResultInfo.error("操作失败！");
            }
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
    }


    /**
     * 获取资产数据
     * 获取部门数据
     * 获取用户数据
     * 根据单位ID，用户名ID，查询当前资产列表
     *
     * @param type 1、获取用户列表  2、获取资产数据
     * @return
     */
    @RequestMapping(value = "selectListData.json")
    @ResponseBody
    public String selectAssetManageList(String type, Integer deptId, Integer userId, Integer asset_type_id) {
        JSONObject object = new JSONObject();
        if (notEmpty(type)) {
            if ("1".equals(type)) {
                List<SysUser> userList = sysUserService.selectList(new QueryWrapper<SysUser>().eq("is_del", "0").eq("department", deptId));
                object.put("users", userList);
            } else if ("2".equals(type)) {
                //获取资产列表数据
                QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<AssetsManage>().eq("use_people", userId).eq("is_del", "0");
                if (null != asset_type_id) {
                    queryWrapper.eq("asset_type_id", asset_type_id);
                }
                List<AssetsManage> manageList = manageService.selectList(queryWrapper);
                object.put("manages", manageList);
            }
        }
        return object.toJSONString();
    }

    //操作日志
    @RequestMapping(value = "toLogView.do")
    public String toLogView(Integer id, Model model) {
        List<OperationRecord> operationRecords = operationRecordService.
                selectList(Wrappers.<OperationRecord>query().
                        eq("type", PostUtils.operation_type_wx).
                        eq("operate_id", id).
                        eq("is_del", "0").orderByDesc("create_time"));
        model.addAttribute("operationRecords", operationRecords);
        return "/purchase/capital/view-log";
    }

    /**
     * 撤单
     * 送审
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "backCarryManage.json")
    @ResponseBody
    public ResultInfo backCarryManage(Integer id, Integer type, String assetnumber) {
        CarryManage carryManage = carryManageService.selectById(id);
        String title = "外携申请-外携申请带撤回";
        if (notEmpty(type) && type == 1) {
            carryManage.setApproval_state("0");
            title = "外携申请-外携申请单送审";
            // 启动流程
            processMemberService.startProcess(G.PROCESS_ZCWX, id, sysUserService.getUser().getId());
        } else {
            carryManage.setApproval_state("3");
            // 查询当前流程 查询字典表
            SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCWX);
            ProcessMember processMember = processMemberService.getProcessMemberByBusId(id, dictionary.getValue());
            if (null == processMember) {
                throw new RuntimeException("未查询到流程");
            }
//            String remark = "";
//            String json = JSON.toJSONString(carryManage);
//            historyService.addProcessMemberHistory(processMember, remark, json);
            if (processMember.getIs_revoke() != 1){
                return ResultInfo.error("当前流程已经审批，不能撤回!");
            }
            processMember.setIsCH(1);
            processMemberService.removeProcessMember(processMember, JSON.toJSONString(carryManage));
        }
        Integer count = carryManageService.updateById(carryManage);
        if (notEmpty(assetnumber)) {
            operationRecordService.addOperationRecord(OperationRecord.builder()
                    .field_fk(assetnumber)
                    .type(PostUtils.operation_type_wx).operate_id(id).record(title).build());
        }
        return ResultInfo.success(count);
    }
}
