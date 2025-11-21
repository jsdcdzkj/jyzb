package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.enums.AssetsStatusEnums;
import com.jsdc.rfid.mapper.ReceiveAssetsMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import lombok.NonNull;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/receive")
public class ReceiveController {

    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private ReceiveAssetsMapper receiveAssetsMapper;
    @Autowired
    private SysUserService userService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private SysDepartmentService sysDepartmentService;

    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPostService sysPostService;
    @Autowired
    private AssetsTypeService assetsTypeService;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private SysDictService sysDictService;

    /**
     * 跳转资产展示页面
     *
     * @return
     */
    @RequestMapping("toSelect.do")
    public String toSelect(Model model){
        //获取所有资产类型
        QueryWrapper<AssetsType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        List<AssetsType> assetsTypes =  assetsTypeService.selectList(queryWrapper);
        model.addAttribute("assetsTypes", assetsTypes);
        return "change/receive/select";
    }

    /**
     * 跳转领用列表页
     *
     * @return
     */
    @RequestMapping("toReceiveIndex.do")
    public String toIndex() {
        return "change/receive/index";
    }

    /**
     * 跳转日志详情
     *
     * @return
     */
    @RequestMapping("view-log")
    public String toViewLog() {
        return "change/receive/view-log";
    }


    /**
     * 跳转领用新增页面
     *
     * @return
     */
    @RequestMapping("toAddReceive.do")
    public String toAddReceive(Model model) {
        SysUser sysUser = userService.getUser();
        String dept_name = sysDepartmentService.selectById(sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);

        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del", "0");
        List<SysUser> sysUsers = userService.selectList(userqueryWrapper);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("asset_state", AssetsStatusEnums.IDLE.getType());
        List<AssetsManage> assetsManage = assetsManageService.selectList(queryWrapper);
        model.addAttribute("assetsManage", assetsManage);
        model.addAttribute("sysUser", sysUser);
        model.addAttribute("sysUsers", sysUsers);
        return "change/receive/realAdd";
    }


    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model) {
        Receive receive = receiveService.getOneByReceiveId(id);
        List<ReceiveAssets> receiveAssets = receiveService.getOneById(id);
        QueryWrapper<AssetsManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("asset_state", "0");
        List<AssetsManage> assetsManage = assetsManageService.selectList(queryWrapper);
        QueryWrapper<SysUser> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("is_del", "0");
        List<SysUser> sysUser = userService.selectList(userqueryWrapper);
        model.addAttribute("receive", receive);
        model.addAttribute("receiveAssets", receiveAssets);
        model.addAttribute("assetsManage", assetsManage);
        model.addAttribute("sysUser", sysUser);
        return "change/receive/add";
    }


    /**
     * 跳转领用管理列表页
     *
     * @return
     */
    @RequestMapping("toReceiveManageIndex.do")
    public String toReceiveManageIndex(Integer id, Model model) {
        // 得到部门
        List<SysDepartment> departments = sysDepartmentService.selectList(Wrappers.<SysDepartment>lambdaQuery()
                .eq(SysDepartment::getIs_del, "0"));
        model.addAttribute("departments", departments);
        return "change/receiveManage/index";
    }


    /**
     * 跳转领用页
     *
     * @return
     */
    @RequestMapping("toConfirm.do")
    public String toConfirm(Integer id, Model model) {
//        PageInfo<ReceiveAssets> pageInfo =  receiveService.getOneById(id,1,10);
//        model.addAttribute("receiveAssets",pageInfo);
        model.addAttribute("id", id);
        return "change/receiveManage/apply";
    }

    /**
     * 跳转展示详情页
     *
     * @return
     */
    @RequestMapping("toViewIndex.do")
    public String toViewIndex(Integer id, Integer type, Model model) {
        Receive receive = receiveService.getOneByReceiveId(id);
        //processMemberService.getProcessDataByBusId(receive.getId(), G.PROCESS_ZCSL, sysUserService.getUser(), receive);
        List<ReceiveAssets> receiveAssets = receiveService.getOneById(id);
        model.addAttribute("receive", receive);
        model.addAttribute("receiveAssets", receiveAssets);
        model.addAttribute("id", id);
        model.addAttribute("type", type);

        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_ZCSL);
        model.addAttribute("key", dictionary.getValue());
        return "change/receive/view";
    }


    /**
     * 跳转打印详情页
     *
     * @return
     */
    @RequestMapping("toPrint.do")
    public String toPrint(Integer id, Model model) {
        Receive receive = receiveService.getOneByReceiveId(id);
        List<ReceiveAssets> receiveAssets = receiveService.getOneById(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        model.addAttribute("receive", receive);
        model.addAttribute("receiveAssets", receiveAssets);
        model.addAttribute("time", time);
        return "change/receive/print";
    }

    /**
     * 获取所有闲置状态的资产
     * @return
     */
    @RequestMapping(value = "getLeaveUnused.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getLeaveUnused( @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer limit,AssetsManage assetsManage,
                                      @RequestParam(required = false) String arr ){
        List<ReceiveAssets> list = new ArrayList<>();
        if (!StringUtils.isEmpty(arr)){
            JSONArray array = JSON.parseArray(arr);
            list = array.toJavaList(ReceiveAssets.class);
        }

        System.out.println(list);
        // 查询所有的申领单 过滤状态为1未送审、2未审批、3审批中、4审批通过 的状态
        List<Receive> a = receiveService.selectList(Wrappers.<Receive>lambdaQuery()
                .in(Receive::getStatus, Arrays.asList("1", "2", "3", "4"))
//                .ne(Receive::getCancel_sign, "1")
                .eq(Receive::getIs_del, G.ISDEL_NO)
        );
        if (!CollectionUtils.isEmpty(a)){
            List<ReceiveAssets> b = receiveAssetsMapper.selectList(Wrappers.<ReceiveAssets>lambdaQuery()
                    .in(ReceiveAssets::getReceive_id, a.stream().map(Receive::getId).collect(Collectors.toList()))
                    .and(
                            wq -> wq.eq(ReceiveAssets::getReceive_status, "1")
                                    .or()
                                    .isNull(ReceiveAssets::getReceive_status)
                    )
                    .eq(ReceiveAssets::getIs_del, G.ISDEL_NO)
            );
            if (!CollectionUtils.isEmpty(b)){
                list.addAll(b);
            }
        }
        return receiveService.getLeaveUnused(page, limit,assetsManage,list);
    }


    /**
     * 描述：领用申请 分页展示当前登陆用户的资产领用单
     * 作者：xuaolong
     *
     * @param receive
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getListByPage(Receive receive, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        PageInfo<Receive> pageInfo = receiveService.getListByPage(receive, page, limit);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 新增领用单
     * 作者：xuaolong
     *
     * @param receive_data
     * @param receive
     * @return
     */
    @RequestMapping(value = "addReceive.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addReceive(@NonNull String receive_data, String receive) {
        JSONArray array = JSON.parseArray(receive_data);
        List<ReceiveAssets> list = array.toJavaList(ReceiveAssets.class);
        Receive receive1 = JSON.parseObject(receive, Receive.class);
        return receiveService.addReceive(list, receive1);
    }

    /**
     * 新增领用单并送审
     * 作者：xuaolong
     *
     * @param receive_data
     * @param receive
     * @return
     */
    @RequestMapping(value = "addReceiveAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addReceiveAndSub(@NonNull String receive_data, String receive) {
        JSONArray array = JSON.parseArray(receive_data);
        List<ReceiveAssets> list = array.toJavaList(ReceiveAssets.class);
        Receive receive1 = JSON.parseObject(receive, Receive.class);
        return receiveService.addReceiveAndSub(list, receive1);
    }


    /**
     * 修改领用单
     * 作者：xuaolong
     *
     * @param receive
     * @return
     */
    @RequestMapping(value = "updateReceive.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateReceive(@NonNull String receive_data, String receive) {
        JSONArray array = JSON.parseArray(receive_data);
        List<Integer> list = array.toJavaList(Integer.class);
        Receive receive1 = JSON.parseObject(receive, Receive.class);

        return receiveService.updateReceive(list, receive1);
    }

    /**
     * 修改领用单并送审
     * 作者：xuaolong
     *
     * @param receive
     * @return
     */
    @RequestMapping(value = "updateReceiveAndSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateReceiveAndSub(@NonNull String receive_data, String receive) {
        JSONArray array = JSON.parseArray(receive_data);
        List<Integer> list = array.toJavaList(Integer.class);
        Receive receive1 = JSON.parseObject(receive, Receive.class);

        return receiveService.updateReceiveAndSub(list, receive1);
    }

    /**
     * 删除领用单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteReceive.do")
    @ResponseBody
    public ResultInfo deleteReceive(@NonNull Integer id) {
        return ResultInfo.success(receiveService.deleteReceive(id));
    }

    /**
     * 领用单送审
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "submitReceive.do")
    @ResponseBody
    public ResultInfo submitReceive(Integer id) {
        return receiveService.submitReceive(id);
    }



    /**
     * 领用单撤回
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "backReceive.do")
    @ResponseBody
    public ResultInfo backReceive(Integer id) {
        return receiveService.backReceive(id);
    }

    /**
     * 打印单据信息
     *
     * @param receive
     * @return
     */
    @RequestMapping(value = "printBill.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo printBill(Receive receive) {
        return receiveService.printBill(receive);
    }


    /**
     * 分页展示领用管理
     *
     * @param receive
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "collectionManageByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo collectionManageByPage(Receive receive, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        return receiveService.collectionManageByPage(receive, page, limit);
    }


    /**
     * 根据领用单ID 查询详细信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getOneById.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getOneById(Integer id, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
        PageInfo<ReceiveAssets> pageInfo = receiveService.getOneById(id, page, limit);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 领用确认
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "confirm.do")
    @ResponseBody
    public ResultInfo confirm(Integer id) {
        return receiveService.confirm(id);
    }

    /**
     * 领用归还
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "returnAssets.do")
    @ResponseBody
    public ResultInfo returnAssets(Integer id) {
        return receiveService.returnAssets(id);
    }

    /**
     * 作废
     */
    @RequestMapping(value = "cancel.do")
    @ResponseBody
    public ResultInfo cancel(Integer id, @RequestParam(required = false) String reason) {
        try {
            receiveService.update(null, Wrappers.<Receive>lambdaUpdate().eq(Receive::getId, id).set(Receive::getCancel_sign, "1"));
        } catch (Exception e) {
            return ResultInfo.error("作废失败");
        }
        Receive receive = receiveService.selectById(id);
        operationRecordService.addOtherOperationRecord(receive.getId(), null, "作废: " + (StringUtils.isBlank(reason)? "无备注": reason), "1");
        return ResultInfo.success();
    }

    /**
     * 跳转待审批页面
     *
     * @return
     */
    @RequestMapping(value = "adopt.do")
    public String adoptList(Model model, Integer status) {
        model.addAttribute("status", status);
        // 部门
        List<SysDepartment> departments = sysDepartmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("departments", departments);
        return "shenpimana/lingyongsp/index";
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
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, Receive bean) {
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
//            bean.setUse_id(userId);
//        } else if (G.DATAPERMISSION_DEPT == data_permission) {//查看所有部门数据
//            int department = sysUser.getDepartment();
//            bean.setDepartment_id(department);
//        }
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_ZCSL);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<Receive>(new ArrayList<Receive>()));
        }
        bean.setIds(busIds);
        bean.setIs_adopt(1);
        PageInfo<Receive> pageInfo = receiveService.getListByPage(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 已审批数据
     */
    @RequestMapping(value = "/finishAdopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, Receive bean) {
        //判断用户权限
        SysUser sysUser = sysUserService.getUser();
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUser, G.PROCESS_ZCSL);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo<Receive> pageInfo = receiveService.finishAdopt(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 查询单个表单日志
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getOperationRecordList.do")
    @ResponseBody
    public ResultInfo getOperationRecordList(Integer id){
        List<OperationRecord> list = receiveService.getOperationRecordList(id);
        return ResultInfo.success(list);
    }

    /**
     * 工作台只看当前user申请的订单
     */
    @RequestMapping(value = "getUserPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getUserPage(Receive receive, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        PageInfo<Receive> pageInfo = receiveService.getUserListByPage(receive, page, limit);
        return ResultInfo.success(pageInfo);
    }

}
