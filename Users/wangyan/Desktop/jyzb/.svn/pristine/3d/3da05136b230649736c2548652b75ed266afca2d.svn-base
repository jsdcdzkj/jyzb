package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.ConsAssettypeMapper;
import com.jsdc.rfid.mapper.ConsSpecificationMapper;
import com.jsdc.rfid.mapper.RFIDConfigMapper;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/consReceive")
public class ConsReceiveController {

    @Autowired
    private ConsReceiveService receiveService;

    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPostService sysPostService;
    @Autowired
    private SysDepartmentService sysDepartmentService;
    @Autowired
    private ConsCategoryService consCategoryService;
    @Autowired
    private ConsAssettypeService consAssettypeService;
    @Autowired
    private ConsSpecificationService consSpecificationService;
    @Autowired
    private ConsInventoryManagementService consInventoryManagementService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ConsCategoryService categoryService;

    @Autowired
    private ConsAssettypeMapper assettypeMapper;

    @Autowired
    private ConsSpecificationMapper specificationMapper;

    @Autowired
    private ProcessMemberService processMemberService;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ConsumableService consumableService;

    @Autowired
    private RFIDConfigMapper rfidConfigMapper;

    /**
     * 跳转耗材申领列表页
     */
    @RequestMapping(value = "toConsIndex.do")
    public String toConsIndex(Model model) {
        List<SysDepartment> departments = sysDepartmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("departments", departments);
        return "haocai/receive/index";
    }

    /**
     * 跳转耗材申领新增页面
     */
    @RequestMapping("toAddReceive.do")
    public String toAddReceive(Model model) {
        SysUser sysUser = userService.getUser();
        String dept_name = sysDepartmentService.selectById(sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);

        QueryWrapper<ConsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        List<ConsCategory> consCategories = consCategoryService.selectList(queryWrapper);

        QueryWrapper<ConsAssettype> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("is_del","0");
        List<ConsAssettype> consAssettypes = consAssettypeService.selectList(queryWrapper1);

        QueryWrapper<ConsSpecification> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("is_del","0");
        List<ConsSpecification> consSpecifications = consSpecificationService.selectList(queryWrapper2);


        model.addAttribute("sysUser", sysUser);
        model.addAttribute("consCategories", consCategories);
        model.addAttribute("consAssettypes", consAssettypes);
        model.addAttribute("consSpecifications", consSpecifications);
        return "haocai/receive/add";
    }

    /**
     * 跳转耗材申领修改页面
     */
    @RequestMapping("toUpdateReceive.do")
    public String toUpdateReceive(Integer id,Model model) {
        SysUser sysUser = userService.getUser();
        String dept_name = sysDepartmentService.selectById(sysUser.getDepartment()).getDept_name();
        sysUser.setDept_name(dept_name);

        QueryWrapper<ConsCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        List<ConsCategory> consCategories = consCategoryService.selectList(queryWrapper);

        QueryWrapper<ConsAssettype> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("is_del","0");
        List<ConsAssettype> consAssettypes = consAssettypeService.selectList(queryWrapper1);

        QueryWrapper<ConsSpecification> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("is_del","0");
        List<ConsSpecification> consSpecifications = consSpecificationService.selectList(queryWrapper2);

        ConsReceive consReceive = receiveService.getOneInfo(id);

        List<ConsReceiveAssets> consReceiveAssets = receiveService.getOneInfoById(id);

        model.addAttribute("sysUser", sysUser);
        model.addAttribute("consCategories", consCategories);
        model.addAttribute("consAssettypes", consAssettypes);
        model.addAttribute("consSpecifications", consSpecifications);
        model.addAttribute("consReceive", consReceive);
        model.addAttribute("consReceiveAssets", consReceiveAssets);
        model.addAttribute("id", id);
        return "haocai/receive/edit";
    }

    /**
     * 跳转展示明细页面
     */
    @RequestMapping("toViewIndex.do")
    public String toViewIndex(Integer id, Integer type,
                              @RequestParam(required = false, defaultValue = "1") Integer pageType,
                              Model model) {
        ConsReceive consReceive = receiveService.getOneInfo(id);
        processMemberService.getProcessDataByBusId(consReceive.getId(), G.PROCESS_HCSL, sysUserService.getUser(), consReceive);
        List<ConsReceiveAssets> receiveAssets = receiveService.getOneInfoById(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());

        model.addAttribute("receive", consReceive);
        model.addAttribute("receiveAssets", receiveAssets);
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        model.addAttribute("time", time);
        model.addAttribute("pageType", pageType);
        model.addAttribute("user", sysUserService.getUser());

        // 查询当前流程 查询字典表
        SysDict dictionary = sysDictService.getProcessByCode(G.PROCESS_HCSL);
        model.addAttribute("key", dictionary.getValue());
        return "haocai/receive/view";
    }



    /**
     * 跳转打印页面
     */
    @RequestMapping("toPrint.do")
    public String toPrint(Integer id, Model model) {
        ConsReceive consReceive = receiveService.getOneInfo(id);
        List<ConsReceiveAssets> receiveAssets = receiveService.getOneInfoById(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());

        model.addAttribute("receive", consReceive);
        model.addAttribute("receiveAssets", receiveAssets);
        model.addAttribute("id", id);
        model.addAttribute("time", time);
        return "haocai/receive/print";
    }

    /**
     * 跳转申领出库列表页
     */
    @RequestMapping(value = "retrieval.do")
    public String retrieval() {
        return "haocai/receiveManage/index";
    }

    /**
     * 跳转申领出库详情页
     */
    @RequestMapping(value = "retrievalDetails.do")
    public String retrievalDetails(Integer id, Model model) {
        ConsReceive consReceive = receiveService.getOneInfo(id);
        List<ConsReceiveAssets> receiveAssets = receiveService.getOneInfoById(id);
        model.addAttribute("receive", consReceive);
        model.addAttribute("receiveAssets", receiveAssets);
        model.addAttribute("id", id);
        return "haocai/receiveManage/apply";
    }




    /**
     * 描述：根据权限展示通过的耗材申领单
     * 作者：xuaolong
     *
     * @param consReceive
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getPassPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getPassListByPage(ConsReceive consReceive, @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer limit){

        return receiveService.getCheckOutListByPage(consReceive, page, limit);
    }


    /**
     * 描述：耗材领用申请 分页展示当前登陆用户的资产领用单
     * 作者：xuaolong
     *
     * @param consReceive
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getListByPage(ConsReceive consReceive, @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer limit) {
        PageInfo<ConsReceive> pageInfo = receiveService.getListByPage(consReceive, page, limit);
        return ResultInfo.success(pageInfo);
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
        return receiveService.subConReceive(id);
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
        try {
            return receiveService.backReceive(id);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
    }

    /**
     * 领用单确认
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "sureReceive.do")
    @ResponseBody
    public ResultInfo sureReceive(Integer id) {
        return receiveService.sureReceive(id);
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
        return ResultInfo.success(receiveService.delConsReceive(id));
    }



    //根据耗材名称 耗材品类 规格型号 获取库存数量
    @RequestMapping(value = "getStockNum.do")
    @ResponseBody
    public ResultInfo getStockNum(Integer asset_type_id ,Integer asset_name ,Integer specifications ){
        String code = "-1";
        QueryWrapper<ConsInventoryManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("asset_type_id",asset_type_id);
        queryWrapper.eq("asset_name_id",asset_name);
        queryWrapper.eq("specifications",specifications);
        queryWrapper.eq("is_del","0");
        List<ConsInventoryManagement> consInventoryManagement = consInventoryManagementService.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(consInventoryManagement) ){

            return ResultInfo.success(code);
        }
        Integer count = 0;
        for (ConsInventoryManagement temp : consInventoryManagement){
            count += temp.getInventory_num();
        }
        ConsInventoryManagement consInventoryManagement1 = new ConsInventoryManagement();
        consInventoryManagement1.setInventory_num(count);
        return ResultInfo.success(consInventoryManagement1);

    }




    /**
     * 跳转采购待审批页面
     *
     * @return
     */
    @RequestMapping(value = "adopt.do")
    public String adoptList(Model model, Integer status) {
        model.addAttribute("status", status);
        return "haocai/shengpi/lyIndex";
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
        List<ConsReceiveAssets> list = array.toJavaList(ConsReceiveAssets.class);
        ConsReceive receive1 = JSON.parseObject(receive, ConsReceive.class);
        return receiveService.addConsReceive(list, receive1);

    }


    /**
     * 新增领用单并送审
     * 作者：xuaolong
     *
     * @param receive_data
     * @param receive
     * @return
     */
    @RequestMapping(value = "addReceiveSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addReceiveSub(@NonNull String receive_data, String receive) {

        JSONArray array = JSON.parseArray(receive_data);
        List<ConsReceiveAssets> list = array.toJavaList(ConsReceiveAssets.class);
        ConsReceive receive1 = JSON.parseObject(receive, ConsReceive.class);
        return receiveService.addConsReceiveSub(list, receive1);
    }

    /**
     * 修改领用单
     * 作者：xuaolong
     *
     * @param receive_data
     * @param receive
     * @return
     */
    @RequestMapping(value = "updateReceive.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateReceive(@NonNull String receive_data, String receive) {

        JSONArray array = JSON.parseArray(receive_data);
        List<ConsReceiveAssets> list = array.toJavaList(ConsReceiveAssets.class);
        ConsReceive receive1 = JSON.parseObject(receive, ConsReceive.class);
        return receiveService.updateConsReceive(list, receive1);
    }
    /**
     * 调整领用单申请数量
     */
    @RequestMapping(value = "updateReceiveNum.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateReceiveNum(ConsReceiveAssets consReceiveAssets) {

        if (null == consReceiveAssets.getId()){
            return ResultInfo.error("领用单id不能为空");
        }
        if (null == consReceiveAssets.getApply_num()){
            return ResultInfo.error("申请数量不能为空");
        }
        return receiveService.updateReceiveNum(consReceiveAssets);
    }

    /**
     * 修改领用单并送审
     * 作者：xuaolong
     *
     * @param receive_data
     * @param receive
     * @return
     */
    @RequestMapping(value = "updateReceiveSub.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateReceiveSub(@NonNull String receive_data, String receive) {

        JSONArray array = JSON.parseArray(receive_data);
        List<ConsReceiveAssets> list = array.toJavaList(ConsReceiveAssets.class);
        ConsReceive receive1 = JSON.parseObject(receive, ConsReceive.class);
        return receiveService.updateConsReceiveSub(list, receive1);

    }

    /**
     * 作废
     */
    @RequestMapping(value = "cancel.do")
    @ResponseBody
    public ResultInfo cancel(Integer id, @RequestParam(required = false) String reason) {
        try {
            receiveService.update(null, Wrappers.<ConsReceive>lambdaUpdate().eq(ConsReceive::getId, id).set(ConsReceive::getCancel_sign, "1"));
        } catch (Exception e) {
            return ResultInfo.error("作废失败");
        }
        ConsReceive receive = receiveService.selectById(id);
        operationRecordService.addOperationRecord(OperationRecord.builder().field_fk(receive.getReceive_code()).
                operate_id(receive.getId()).type("11").is_del("0").record("作废: " + (StringUtils.isBlank(reason)? "无备注": reason)).build());
        return ResultInfo.success();
    }

    /**
     * 采购待审批数据
     *
     * @param page
     * @param limit
     * @param bean
     * @return
     */
    @RequestMapping(value = "adopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo adoptList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ConsReceive bean) {
        //判断用户权限
        SysUser sysUser = userService.getUser();
//        int userId = sysUser.getId();
//        Integer postId = userService.selectById(userId).getPost();
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
        List<Integer> busIds = processMemberService.getBusIdsByPostOrUserId(sysUser, G.PROCESS_HCSL);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        bean.setIs_adopt(1);
        PageInfo<ConsReceive> pageInfo = receiveService.getListByPage(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 已审批
     */
    @RequestMapping(value = "finishAdopt.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo finishAdopt(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, ConsReceive bean) {
        //判断用户权限
        SysUser sysUser = userService.getUser();
        List<Integer> busIds = processMemberService.getBusIdsByUserIdHistory(sysUser, G.PROCESS_HCSL);
        if(CollectionUtils.isEmpty(busIds)){
            return ResultInfo.success(new PageInfo<>(new ArrayList<>()));
        }
        bean.setIds(busIds);
        PageInfo<ConsReceive> pageInfo = receiveService.finishAdopt(bean, page, limit);
        return ResultInfo.success(pageInfo);
    }


    /**
     * 跳转日志详情
     *
     * @return
     */
    @RequestMapping("viewLog.do")
    public String viewLog() {
        return "haocai/receive/viewLog";
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
     * 出库
     * @param id
     * @param outNum
     * @return
     */
    @RequestMapping(value = "retrievalOut.do")
    @ResponseBody
    public ResultInfo retrievalOut(Integer id,Integer outNum){
        return receiveService.retrieval(id, outNum);
    }


    /**
     * 页面跳转: 选择耗材
     */
    @RequestMapping("selectConsumables.do")
    public String selectConsumablesPage(Model model) {
        // 查询仓库
        List<Warehouse> warehouseList = warehouseService.selectList(Wrappers.<Warehouse>lambdaQuery()
                .eq(Warehouse::getIs_del, G.ISDEL_NO));
        model.addAttribute("warehouseList", warehouseList);

        // 查询耗材分类
        List<ConsCategory> categoryList = categoryService.selectList(Wrappers.<ConsCategory>lambdaQuery()
                .eq(ConsCategory::getIs_del, G.ISDEL_NO));
        model.addAttribute("categoryList", categoryList);

        // 查询耗材名称
        List<ConsAssettype> assettypeList = assettypeMapper.selectList(Wrappers.<ConsAssettype>lambdaQuery()
                .eq(ConsAssettype::getIs_del, G.ISDEL_NO));
        model.addAttribute("assettypeList", assettypeList);

        // 查询耗材规格
        List<ConsSpecification> specificationList = specificationMapper.selectList(Wrappers.<ConsSpecification>lambdaQuery()
                .eq(ConsSpecification::getIs_del, G.ISDEL_NO));
        model.addAttribute("specificationList", specificationList);

        // 耗材类型
        List<Consumable> consumableList = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getIs_del, G.ISDEL_NO));
        List<Consumable> consumables = new ArrayList<>();
        for (Consumable consumable : consumableList) {
            if (consumable.getParent_id() == 0) {
                consumables.add(consumable);
            }
        }
        for (Consumable consumable : consumables) {
            List<Consumable> children = new ArrayList<>();
            for (Consumable consumable1 : consumableList) {
                if (consumable1.getParent_id().equals(consumable.getId())) {
                    children.add(consumable1);
                }
            }
            consumable.setChildren(children);
        }
        model.addAttribute("consumables", consumables);

        return "haocai/receive/selectConsumables";
    }

    /**
     * 根据采购id得到采购耗材
     */
    @RequestMapping("getConsReceiveAssetsList.do")
    @ResponseBody
    public ResultInfo getConsReceiveAssetsList(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer limit,
                                               ConsInventoryManagement consInventoryManagement) {

        PageInfo<ConsInventoryManagement> pageInfo = receiveService.getConsPurchaseDetailPage(page, limit, ConsInventoryManagement.builder()
                .specifications(consInventoryManagement.getSpecification_id())
                .warehouse_id(consInventoryManagement.getWarehouse_id())
                .name(StringUtils.isNotBlank(consInventoryManagement.getName()) ? consInventoryManagement.getName().trim() : "")
                .model(StringUtils.isNotBlank(consInventoryManagement.getModel()) ? consInventoryManagement.getModel().trim() : "")
                .consumable_id(null != consInventoryManagement.getConsumable_id() ? consInventoryManagement.getConsumable_id() : null)
                .build()
        );
        return ResultInfo.success(pageInfo);
    }

    /**
     * 编辑数据初始化
     */
    @RequestMapping("getLydById.do")
    @ResponseBody
    public ResultInfo getLydById(Integer id) {
        return ResultInfo.success(receiveService.getOneInfoById(id));
    }

    /**
     * 编辑数据初始化
     */
    @RequestMapping("getLydByIdPage.do")
    @ResponseBody
    public ResultInfo getLydByIdPage(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer limit,
                                     Integer id) {
        PageHelper.startPage(page, limit);
        return ResultInfo.success(new PageInfo<>(receiveService.getOneInfoById(id)));
    }

    /**
     * 跳转统计页面
     */
    @RequestMapping("statistics.do")
    public String statistics(Model model) {
        List<Consumable> consumables = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getParent_id, 0)
                .eq(Consumable::getIs_del, G.ISDEL_NO)
        );
        for (Consumable consumable : consumables) {
            List<Consumable> consumableList = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                    .eq(Consumable::getParent_id, consumable.getId())
                    .eq(Consumable::getIs_del, G.ISDEL_NO)
            );
            consumable.setChildren(consumableList);
        }
        model.addAttribute("consumables", consumables);
        return "haocai/statistics/inAndOutbound";
    }

    /**
     * 查询统计数据
     */
    @RequestMapping("getStatisticsList.do")
    @ResponseBody
    public ResultInfo getStatisticsList(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer limit,
                                        ConsInAndOutStatistics consInAndOutStatistics) {
        return ResultInfo.success(receiveService.getStatisticsList(consInAndOutStatistics, page, limit));
    }
}
