package com.jsdc.rfid.controller.statisticalreport;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.utils.DateTimeUtils;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.ConsHandworkService;
import com.jsdc.rfid.service.ConsReceiveService;
import com.jsdc.rfid.service.InventoryManagementService;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.service.statisticalreport.StatisticalReportService;
import com.jsdc.rfid.service.statisticalreport.StatisticalService;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;
import vo.StatisticsAssetStateVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : lb
 * @date: 2023/3/7 18:24
 * desc :
 **/
@Controller
@RequestMapping("/statisticalReport")
public class StatisticalReportController {

    @Autowired
    private StatisticalReportService statisticalReportService;
    @Autowired
    private ConsReceiveService consReceiveService;
    @Autowired
    private ConsHandworkService consHandworkService;

    @Autowired
    private InventoryManagementService inventoryManagementService;

    @Autowired
    private StatisticalService statisticalService;
    @Autowired
    private SysUserService sysUserService;

    /***
     * @Description: 统计资产总值
     * @param: []
     * @return: vo.ResultInfo
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:35
     */
    @GetMapping("totalAssets")
    @ResponseBody
    public ResultInfo totalAssets() {
        return ResultInfo.success(statisticalReportService.totalAssets());
    }


    /**
     * @Description: 统计未打印标签资产
     * @param: []
     * @return: vo.ResultInfo
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:43
     */
    @GetMapping("unprintedAsset")
    @ResponseBody
    public ResultInfo unprintedAsset(Integer deptId) {
        return ResultInfo.success(statisticalReportService.unprintedAsset(deptId));
    }

    /**
     * @Description: 各部门领用对比
     * @param: []
     * @return: vo.ResultInfo
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:49
     */
    @GetMapping("receive")
    @ResponseBody
    public ResultInfo receive(Integer deptId) throws ParseException {
        return ResultInfo.success(statisticalReportService.receive(deptId));
    }

    /**
     * @Description: 各部门采购
     * @param: [deptId]
     * @return: vo.ResultInfo
     * @author: csx
     * @date: 2023/3/10
     * @time: 9:37
     */
    @GetMapping("purchase")
    @ResponseBody
    public ResultInfo purchase(Integer deptId) throws ParseException {
        return ResultInfo.success(statisticalReportService.purchase(deptId));
    }

    /**
     * 供应商采购排行(TOP10) 按申请单金额TOP10
     *
     * @return
     */
    @RequestMapping("getSupplierProcurementRankingMoney")
    @ResponseBody
    public ResultInfo getSupplierProcurementRankingMoney() {
        return ResultInfo.success(statisticalReportService.getSupplierProcurementRankingMoney());
    }

    /**
     * 供应商采购排行(TOP10) 按申请单数量TOP10
     *
     * @return
     */
    @RequestMapping("getSupplierProcurementRankingApply")
    @ResponseBody
    public ResultInfo getSupplierProcurementRankingApply(Integer year) {
        return ResultInfo.success(statisticalReportService.getSupplierProcurementRankingApply(year));
    }


    /**
     * 采购金额排行<TOP5>
     *
     * @param
     * @return
     */
    @RequestMapping("getPurchaseAmountRanking")
    @ResponseBody
    public ResultInfo getPurchaseAmountRanking() {
        return ResultInfo.success(statisticalReportService.getPurchaseAmountRanking());
    }


    /**
     * 最新十二个月 部门采购单趋势 同比
     *
     * @param id
     * @return
     */
    @RequestMapping("getPurchaseOrderTrendYearOnYear")
    @ResponseBody
    public ResultInfo getPurchaseOrderTrendYearOnYear(Integer id) {
        return ResultInfo.success(statisticalReportService.getPurchaseOrderTrendYearOnYear(id));
    }


    /**
     * 资产采购、处置趋势(双折线、按月金额)
     *
     * @return
     */
    @RequestMapping(value = "getManageOrPurchaseMonth.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getManageOrPurchaseMonth(String year) {
        return ResultInfo.success(statisticalReportService.getManageOrPurchaseMonth(year));
    }


    /**
     * 报修、处理关系图(堆条形图，各月处理完成数、报修数)
     *
     * @return
     */
    @RequestMapping("getApplyOrFeebackCount.do")
    @ResponseBody
    public ResultInfo getApplyOrFeebackCount(String year) {
        return ResultInfo.success(statisticalReportService.getApplyOrFeebackCount(year));
    }

    /**
     * 按月 正常、异常外携资产趋势图
     *
     * @return
     */
    @RequestMapping("getCarryManageCount.do")
    @ResponseBody
    public ResultInfo getCarryManageCount(String year) {
        return ResultInfo.success(statisticalReportService.carryManageCount(year));
    }

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：以资产类型分类,  库存数量TOP10
     * @Date ：2023/3/8 下午 4:33
     * @Modified By：
     */
    @RequestMapping("statTop10InventoryNumByTypeName")
    @ResponseBody
    public ResultInfo statTop10InventoryNumByTypeName(Integer year) {

        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }

        // 方案一
        List<Map<String, Object>> list = this.statisticalReportService.statTop10InventoryNumByTypeName(startDay,endDay);

        // 方案二
        ArrayList<String> assetsTypeNameList = new ArrayList<>();
        ArrayList<BigDecimal> sumList = new ArrayList<>();

        for (Map<String, Object> map : list) {
            String name = (String) map.get("assets_type_name");
            Object s = map.get("sum");
            BigDecimal bigDecimal = new BigDecimal(s.toString()).setScale(2, RoundingMode.HALF_UP);
            assetsTypeNameList.add(name);
            sumList.add(bigDecimal);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("assetsTypeNameList", assetsTypeNameList);
        jsonObject.put("sumList", sumList);
        return ResultInfo.success(jsonObject);
    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description： 1.库存总价值  2. 库存总数
     * @Date ：2023/3/9 上午 9:03
     * @Modified By：
     */
    @RequestMapping("statInventoryTotalAssertAndTotalAmount")
    @ResponseBody
    public ResultInfo statInventoryTotalAssertAndTotalAmount() {
        // 库存总价值
        Map<String, Object> map1 = this.statisticalReportService.statInventoryTotalAssert();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalAsset", map1.get("totalasset"));

        // 库存总数
        Map<String, Object> map2 = this.statisticalReportService.statInventoryTotalAmount();
        jsonObject.put("totalNum", map2.get("totalnum"));

        return ResultInfo.success(jsonObject);
    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：库存按品类计算总价值
     * @Date ：2023/3/9 上午 9:39
     * @Modified By：
     */
    @RequestMapping("statInventoryAssertByType")
    @ResponseBody
    public ResultInfo statInventoryAssertByType(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                                                @RequestParam(defaultValue = "10", value = "limit") Integer pageSize) {
        PageInfo<Map<String, Object>> pageInfo = this.statisticalReportService.statInventoryAssertByType(pageIndex, pageSize);
        return ResultInfo.success(pageInfo);
    }


    /**
     * @Author: yc
     * @Params:资产类型Id
     * @Return:
     * @Description：资产状态数量
     * @Date ：2023/3/9 上午 11:12
     * @Modified By：
     */
    @RequestMapping("statAssertNumByType")
    @ResponseBody
    public ResultInfo statAssertNumByType(Integer assertTypeId) {
        List<StatisticsAssetStateVo> list = this.statisticalReportService.statAssertNumByType(assertTypeId);
        return ResultInfo.success(list);
    }


    /**
     * 耗材领用排行TOP10
     *
     * @param deptIds
     * @return
     */
    @RequestMapping("consumableReceive.do")
    @ResponseBody
    public ResultInfo consumableReceive(String deptIds) {
        return ResultInfo.success(statisticalReportService.consumableReceive(deptIds));
    }

    /**
     *  部门领用top5
     * @param year
     * @return
     */
    @RequestMapping("deptReceiveTop.do")
    @ResponseBody
    public ResultInfo deptReceiveTop(String year){
        return ResultInfo.success(statisticalReportService.deptReceiveTop(year));
    }

    /**
     *  部门领用详情
     * @param deptName
     * @return
     */
    @RequestMapping(value = "deptReceiveView.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo deptReceiveView(String year,String deptName, @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(statisticalReportService.deptReceiveView(year,deptName,page,limit));
    }

    /**
     * 部门领用总值排行
     *
     * @param year
     * @return
     */
    @RequestMapping("receiveTotalValue.do")
    @ResponseBody
    public ResultInfo receiveTotalValue(Integer year) {
        return ResultInfo.success(statisticalReportService.receiveTotalValue(year));
    }

    /**
     * 异常/正常外携资产排行(按品类、TOP5) @cz
     *
     * @return 结果集
     */
    @GetMapping("carryTop")
    @ResponseBody
    public ResultInfo carryTop() {
        return ResultInfo.success(statisticalReportService.carryTop());
    }

    /**
     * 各部门外携行为占比 @cz
     *
     * @return 结果集
     */
    @GetMapping("censusAllCarryCountByDept")
    @ResponseBody
    public ResultInfo censusAllCarryCountByDept() {
        return ResultInfo.success(statisticalReportService.censusAllCarryCountByDept());
    }

    /**
     * 采购全额排行(TOP5) @cz
     *
     * @return 结果集
     */
    @GetMapping("purchaseTop")
    @ResponseBody
    public ResultInfo purchaseTop(@RequestParam("year") Integer year) {
        return ResultInfo.success(statisticalReportService.purchaseTop(year));
    }

    /**
     * 采购总值/库存总值/出库总值 @cz
     *
     * @return 结果集
     */
    @GetMapping("censusAssetsInAndOut")
    @ResponseBody
    public ResultInfo censusAssetsInAndOut() {
        return ResultInfo.success(statisticalReportService.censusAssetsInAndOut());
    }

    /**
     * 消耗速率TOP15 @cz
     *
     * @return 结果集
     */
    @GetMapping("rateConsumptionTop.do")
    @ResponseBody
    public ResultInfo rateConsumptionTop(@RequestParam Integer year) {
        return ResultInfo.success(statisticalReportService.rateConsumptionTop(year));
    }

    /**
     * 资产领用排行TOP10 @cz
     *
     * @param osId 部门id
     * @return 结果集
     */
    @GetMapping("assetCollectionTop")
    @ResponseBody
    public ResultInfo assetCollectionTop(@RequestParam(required = false) Integer osId) {
        return ResultInfo.success(statisticalReportService.assetCollectionTop(osId));
    }


    /**
     * @Author: yc
     * @Params: 部门ID
     * @Return:
     * @Description：部门采购单趋势 十二个月部门采购次数 上一年十二个月部门采购次数
     * @Date ：2023/3/10 下午 5:21
     * @Modified By：
     */
    @RequestMapping("statCountPurchaseOrderByDeptId")
    @ResponseBody
    public ResultInfo statCountPurchaseOrder(Integer deptId) {
        List<Map<String, Object>> nowYearList = statisticalReportService.statCountPurchaseOrderNowYear(deptId);
        List<Map<String, Object>> previousYearList = statisticalReportService.statCountPurchaseOrderPreviousYear(deptId);

        LocalDate now = LocalDate.now();
        Month month = now.getMonth();
        int value = month.getValue();

        ArrayList<Object> nowYearListPage = new ArrayList<>();
        ArrayList<Object> previousYearListPage = new ArrayList<>();

        for (int i = 0; i < value; i++) {
            Map<String, Object> map = nowYearList.get(i);
            nowYearListPage.add(map.get("purchaseTime"));
        }

        for (int i = 0; i < previousYearList.size(); i++) {
            Map<String, Object> map = previousYearList.get(i);
            previousYearListPage.add(map.get("purchaseTime"));
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nowYearListPage", nowYearListPage);
        jsonObject.put("previousYearListPage", previousYearListPage);
        return ResultInfo.success(jsonObject);
    }
    /**
     * TODO top5排行加年份
     *
     * @param year
     * @return vo.ResultInfo
     * @Date 2023/3/14 15:48
     * @Author wr
     */
    @RequestMapping(value = "getPurchaseTop5ByCategory.json")
    @ResponseBody
    public ResultInfo getPurchaseTop5ByCategory(Integer year) {
        return ResultInfo.success(statisticalReportService.getPurchaseTop5ByCategory(year));
    }

    /**
     * 供应商列表
     */
    @RequestMapping(value = "supplierList.do")
    @ResponseBody
    public ResultInfo purchaseFromSupplier(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                                           @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                                           String supplierName,Integer year ) {
        return ResultInfo.success(statisticalReportService.purchaseFromSupplierList(pageIndex, pageSize, supplierName,year));
    }

    /**
     * 采购排行列表
     */
    @RequestMapping(value = "purchaseRanking")
    @ResponseBody
    public ResultInfo purchaseRanking(String category_name,Integer year,
                                      @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer limit) {
        return ResultInfo.success(statisticalReportService.purchaseRanking(category_name,year,page,limit));
    }
    /**
     * 部门领用总值详情
     */
    @RequestMapping(value = "deptConsumingTotalDetails.do")
    @ResponseBody
    public ResultInfo deptConsumingTotalDetails(String name,Integer year,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer limit){

        return ResultInfo.success(statisticalReportService.deptConsumingTotalDetails(name,year,page,limit));
    }

    /**
     * @Description: 部门资产总值详情
     * @param: [pageIndex, pageSize, name]
     * @return: vo.ResultInfo
     * @author: csx
     * @date: 2023/3/15
     * @time: 10:22
     */
    @RequestMapping(value = "totalAssetsDetails")
    @ResponseBody
    public ResultInfo totalAssetsDetails(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                                         @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,String name) {
        return  ResultInfo.success(statisticalReportService.totalAssetsDetails(pageIndex,pageSize,name));
    }

    /**
     * @Description: 统计未打印标签资产详情
     * @param: [pageIndex, pageSize, name, deptId]
     * @return: vo.ResultInfo
     * @author: csx
     * @date: 2023/3/15
     * @time: 10:35
     */
    @RequestMapping(value = "unprintedAssetDetails")
    @ResponseBody
    public ResultInfo unprintedAssetDetails(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                                          @RequestParam(defaultValue = "10", value = "limit") Integer pageSize, String name,Integer deptId){
        return  ResultInfo.success(statisticalReportService.unprintedAssetDetails(pageIndex,pageSize,name,deptId));
    }

    /**
     * 申领出库总数
     */
    @RequestMapping(value = "checkoutSum.do")
    @ResponseBody
    public ResultInfo checkoutSum() {
        return ResultInfo.success(statisticalReportService.checkoutSum());
    }

    /**
     * 申领出库总数 点击展示内容
     * @param consReceive
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getPassPage.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getPassListByPage(ConsReceive consReceive, @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer limit){
        return consReceiveService.getCheckOutListByPage(consReceive, page, limit,true);
    }

    /**
     * 手动出库总数 点击展示内容
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             ConsHandwork beanParam) {
        return consHandworkService.getListByPage(beanParam ,pageIndex, pageSize ,true);
    }

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：根据品类名称查询库存分页详情
     * @Date ：2023/3/15 上午 9:54
     * @Modified By：
     */
    @RequestMapping(value = "getListFromInventoryManagementByAssetsTypeName", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getListFromInventoryManagementByAssetsTypeName(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             String assetTypeName ,
                             Integer year) {
        PageInfo<InventoryManagement> inventoryManagementPageInfo = this.statisticalReportService.getListByAssetsTypeName(pageIndex, pageSize, assetTypeName,year);
        return ResultInfo.success(inventoryManagementPageInfo);
    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：根据耗材品类名称展示耗材出库详情分页列表
     * @Date ：2023/3/15 下午 2:31
     * @Modified By：
     */
    @RequestMapping(value = "getPageListFromConsReceiveByConsCategoryName", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getListFromInventoryManagementByAssetsTypeName(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                                                                     @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                                                                     ConsReceive beanParam) {
        PageInfo<ConsReceive> inventoryManagementPageInfo = this.consReceiveService.getPageListByConsCategoryName(pageIndex, pageSize, beanParam);
        return ResultInfo.success(inventoryManagementPageInfo);
    }

    /**
     * 采购金额排行TOP5 - 详情 @cz
     *
     * @param pageIndex 页码
     * @param pageSize 页数
     * @param typeName 查询参数
     * @return 分页结果集
     */
    @PostMapping("purchaseTopDetail")
    @ResponseBody
    public ResultInfo purchaseTopDetail(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                                        @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                                        String typeName,Integer year) {
        return ResultInfo.success(statisticalReportService.purchaseTopDetail(pageIndex,pageSize,typeName,year));
    }

    /**
     * 资产领用排行TOP10详情 @cz
     *
     * @param pageIndex 页码
     * @param pageSize 页数
     * @param typeName 查询参数
     * @return 结果集
     */
    @PostMapping("assetCollectionTopDetail")
    @ResponseBody
    public ResultInfo assetCollectionTopDetail(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                                        @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                                        String typeName,String osId) {
        return ResultInfo.success(statisticalReportService.assetCollectionTopDetail(pageIndex,pageSize,typeName,osId));
    }

    @RequestMapping("toViewTop5.do")
    public String toViewTop5(Integer id , @RequestParam(required = false) Integer categoryId, Model model) {
        List<ConsPurchaseDetail> consPurchaseDetails = statisticalReportService.toViewTop5(id, categoryId);
        model.addAttribute("receiveAssets", consPurchaseDetails);
        return "haocai/receive/viewtop5";
    }




    //员工工作台页面 开始
    //员工工作台-我的工作台开始

    /**
     * 我的固定资产数
     *
     * @return
     */
    @PostMapping("getMyAssetsNum.json")
    @ResponseBody
    public ResultInfo getMyAssetsNum() {
        SysUser currentUser = sysUserService.getUser();
        //我的固定资产数
        PageInfo<AssetsManage> assetsManagePageList = statisticalService.getMyAssetsNum(currentUser);
        return ResultInfo.success(assetsManagePageList.getTotal());
    }


    /**
     * 固定资产申请数   弃用
     *
     * @return
     */
    @PostMapping("getMyAssetsApplayNum.json")
    @ResponseBody
    public ResultInfo getMyAssetsApplayNum() {
        SysUser currentUser = sysUserService.getUser();
        //固定资产申请数
        PageInfo<Receive> pageList = statisticalService.getMyAssetsApplayNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 固定资产申请数
     *
     * @return
     */
    @PostMapping("getMyAssetsApplayNumCount.json")
    @ResponseBody
    public ResultInfo getMyAssetsApplayNumCount() {
        SysUser currentUser = sysUserService.getUser();
        //固定资产申请数
        return ResultInfo.success(statisticalService.getMyAssetsApplayNumCount());
    }


    /**
     * 报修申请数 弃用
     *
     * @return
     */
    @PostMapping("getApplySingleNum.json")
    @ResponseBody
    public ResultInfo getApplySingleNum() {
        SysUser currentUser = sysUserService.getUser();
        //报修申请数
        PageInfo<ApplySingleVo> pageList = statisticalService.getApplySingleNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 报修申请数
     *
     * @return
     */
    @PostMapping("getApplySingleNumCount.json")
    @ResponseBody
    public ResultInfo getApplySingleNumCount() {
        SysUser currentUser = sysUserService.getUser();
        //报修申请数
        PageInfo pageList = statisticalService.getApplySingleNumCount(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }


    /**
     * 外携申请数
     *
     * @return
     */
    @PostMapping("getCarryManageNum.json")
    @ResponseBody
    public ResultInfo getCarryManageNum() {
        SysUser currentUser = sysUserService.getUser();
        //外携申请数
        PageInfo pageList = statisticalService.getCarryManageNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }


    /**
     * 耗材申领数
     *
     * @return
     */
    @PostMapping("getConsReceiveNum.json")
    @ResponseBody
    public ResultInfo getConsReceiveNum() {
        SysUser currentUser = sysUserService.getUser();
        //耗材申领数
        PageInfo pageList = statisticalService.getConsReceiveNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }
    //员工工作台-我的工作台结束


    //员工工作台-固定资产待办事项 开始

    /**
     * 采购待审批数
     *
     * @return
     */
    @PostMapping("getCgdspNum.json")
    @ResponseBody
    public ResultInfo getCgdspNum() {
        SysUser currentUser = sysUserService.getUser();
        //采购待审批数
        PageInfo pageList = statisticalService.getCgdspNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 领用待审批数
     *
     * @return
     */
    @PostMapping("getLydspNum.json")
    @ResponseBody
    public ResultInfo getLydspNum() {
        SysUser currentUser = sysUserService.getUser();
        //领用待审批数
        PageInfo pageList = statisticalService.getLydspNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }


    /**
     * 变更待审批数
     *
     * @return
     */
    @PostMapping("getBgdspNum.json")
    @ResponseBody
    public ResultInfo getBgdspNum() {
        SysUser currentUser = sysUserService.getUser();
        //变更待审批数
        PageInfo pageList = statisticalService.getBgdspNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 处置待审批数
     *
     * @return
     */
    @PostMapping("getCzdspNum.json")
    @ResponseBody
    public ResultInfo getCzdspNum() {
        SysUser currentUser = sysUserService.getUser();
        //处置待审批数
        PageInfo pageList = statisticalService.getCzdspNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 外携待审批数
     *
     * @return
     */
    @PostMapping("getWxdspNum.json")
    @ResponseBody
    public ResultInfo getWxdspNum() {
        SysUser currentUser = sysUserService.getUser();
        //外携待审批数
        PageInfo pageList = statisticalService.getWxdspNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 维修待审批数
     *
     * @return
     */
    @PostMapping("getWxzcdspNum.json")
    @ResponseBody
    public ResultInfo getWxzcdspNum() {
        SysUser currentUser = sysUserService.getUser();
        //维修待审批数
        PageInfo pageList = statisticalService.getWxzcdspNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    //员工工作台-固定资产待办事项 结束


    //员工工作台-耗材待办事项 开始

    /**
     * 采购待审批数
     *
     * @return
     */
    @PostMapping("getHaoCaicgdspNum.json")
    @ResponseBody
    public ResultInfo getHaoCaicgdspNum() {
        SysUser currentUser = sysUserService.getUser();
        //采购待审批数
        PageInfo pageList = statisticalService.getHaoCaicgdspNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 领用待审批数
     *
     * @return
     */
    @PostMapping("getHaoCailydspNum.json")
    @ResponseBody
    public ResultInfo getHaoCailydspNum() {
        SysUser currentUser = sysUserService.getUser();
        //采购待审批数
        PageInfo pageList = statisticalService.getHaoCailydspNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }
    // 员工工作台-耗材待办事项 结束



    //运维工作台 开始
    /**
     * 本月报修总数
     */

    @PostMapping("getApplySingleVoMonthNum.json")
    @ResponseBody
    public ResultInfo getApplySingleVoMonthNum() {
        SysUser currentUser = sysUserService.getUser();
        PageInfo pageList = statisticalService.getApplySingleVoMonthNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 我的任务
     */

    @PostMapping("getApplySingleVoNum.json")
    @ResponseBody
    public ResultInfo getApplySingleVoNum() {
        SysUser currentUser = sysUserService.getUser();
        //我的任务
        PageInfo pageList = statisticalService.getApplySingleVoNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }


    /**
     * 外携申请
     */

    @PostMapping("getYwwxsqNum.json")
    @ResponseBody
    public ResultInfo getYwwxsqNum() {
        SysUser currentUser = sysUserService.getUser();
        //外携申请
        PageInfo pageList = statisticalService.getYwwxsqNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }

    /**
     * 耗材申领
     */

    @PostMapping("getYwhcslNum.json")
    @ResponseBody
    public ResultInfo getYwhcslNum() {
        SysUser currentUser = sysUserService.getUser();
        //耗材申领
        PageInfo pageList = statisticalService.getYwhcslNum(currentUser);
        return ResultInfo.success(pageList.getTotal());
    }
    //运维工作台 结束

}
