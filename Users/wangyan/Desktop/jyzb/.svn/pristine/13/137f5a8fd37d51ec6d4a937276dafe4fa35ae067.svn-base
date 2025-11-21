package com.jsdc.rfid.controller;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.service.statisticalreport.StatisticalReportService;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ConsDataVo;
import vo.DataChartVo;
import vo.ResultInfo;
import vo.StatisticsRepairVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据分析
 */
@Controller
@RequestMapping("/sjfx")
public class SjfxController extends BaseController {

    @Autowired
    private SjfxService sjfxService;
    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private SysPositionService sysPositionService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private ApplySingleService applySingleService;
    @Autowired
    private AssetsAccessRecordService assetsAccessRecordService;
    @Autowired
    private PurchaseApplyService purchaseApplyService;
    @Autowired
    private ManagementAssetsService managementAssetsService;
    @Autowired
    private CarryManageService carryManageService;
    @Autowired
    private CarryAbnormalService abnormalService;

    @Autowired
    private ConsReceiveMapper consReceiveMapper;

    @Autowired
    private ConsReceiveAssetsMapper consReceiveAssetsMapper;

    @Autowired
    private ConsPurchaseApplyMapper consPurchaseApplyMapper;

    @Autowired
    private ConsPurchaseDetailMapper consPurchaseDetailMapper;

    @Autowired
    private ConsCategoryMapper consCategoryMapper;

    @Autowired
    private AssetsTypeMapper assetsTypeMapper;

    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    @Autowired
    private StatisticalReportService statisticalReportService;

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private FastEntranceService fastEntranceService;

    @Autowired
    private SysDepartmentService sysDepartmentService;
//    @RequestMapping(value = "index.do")
//    public String index(Model model, DataChartVo vo) {
//        //按分类统计
//        model.addAttribute("classificationData", assetsManageService.classificationData(vo));
//        //按部门统计统计
//        model.addAttribute("departmentData", assetsManageService.departmentData(vo));
//        //按品牌统计统计
//        model.addAttribute("brandData", assetsManageService.brandData(vo));
//        return "index";
//    }

    /**
     * 图标数据接口
     */
    @RequestMapping(value = "getDataList.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getDataList(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
////        //按分类统计
////        jsonObject.put("classificationData", sjfxService.classificationData(vo));
//        //按部门统计统计
//        jsonObject.put("departmentData", sjfxService.departmentData(vo));
////        //按品牌统计统计
////        jsonObject.put("brandData", sjfxService.brandData(vo));
////        //按购置时间统计
////        jsonObject.put("purchaseTimeData", sjfxService.purchaseTimeData(vo));
////        //按外携状态统计: 未授权外携统计 授权外携统计
////        jsonObject.put("carryStatusData", sjfxService.carryStatusData(vo));
//        //异常预警趋势 最近6个月的报警数量
//        jsonObject.put("alarmsNumsData", sjfxService.alarmsNumsData(vo));
        return ResultInfo.success(jsonObject);
    }

    /**
     * 资产信息
     */
    @RequestMapping(value = "assetsInfo.do")
    public String assetsInfo(Model model, Equipment bean) {
        bean = equipmentService.selectById(bean.getId());
        SysPosition position = sysPositionService.selectById(bean.getEquipment_position());
        bean.setPositionName(position.getPosition_name());
        model.addAttribute("bean", bean);
        return "index/assetsView";
    }

    /**
     * 预警信息
     */
    @RequestMapping(value = "warningInfo.do")
    public String warningInfo(Model model, Equipment bean) {
        bean = equipmentService.selectById(bean.getId());
        SysPosition position = sysPositionService.selectById(bean.getEquipment_position());
        bean.setPositionName(position.getPosition_name());
        model.addAttribute("bean", bean);
        return "index/warningView";
    }

    /**
     * 外携趋势（正常外携、异常外携双折线）图（近6个月）
     * thr
     */
    @RequestMapping(value = "getCarryData.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getCarryData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
//        //正常外携 近6个月
//        jsonObject.put("carryNumsData", sjfxService.carryNumsData(vo));
//        //异常外携 近6个月
//        vo.setType("1");
//        jsonObject.put("errorNumsData", sjfxService.alarmsNumsData(vo));

        return ResultInfo.success(jsonObject);
    }


    /**
     * 基础信息：资产总数（饼状图，库内、库外 可按品类统计，默认统计全部品类）
     * thr
     */
    @RequestMapping(value = "assetsRegisterTypeData.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo assetsRegisterTypeData(DataChartVo vo) {
        //基础信息：资产总数（饼状图，库内、库外 可按品类统计，默认统计全部品类）
        return ResultInfo.success(sjfxService.assetsRegisterTypeData(vo));
    }

    /**
     * 已登记总数、闲置总数、使用总数、故障总数、异常总数、处置总数
     * thr
     */
    @RequestMapping(value = "assetsTotalData.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo assetsTotalData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        //已登记总数、闲置总数、使用总数、故障总数、异常总数、处置总数
        jsonObject.put("assetsTotalData", sjfxService.assetsTotalData(vo));
        return ResultInfo.success(jsonObject);
    }

    /**
     * 按部门统计报修数据
     *
     * @return
     */
    @RequestMapping(value = "applyRepair.do")
    @ResponseBody
    public ResultInfo applyRepair() {
        List<StatisticsRepairVo> repairVos = applySingleService.statisticsByDept();
        if (CollectionUtils.isEmpty(repairVos)){
            List<SysDepartment> depts = sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery()
                    // 等于null 或者等于0
                    .and(i -> i.isNull(SysDepartment::getParent_dept).or().eq(SysDepartment::getParent_dept, 0)
                    .eq(SysDepartment::getIs_del,G.ISDEL_NO)
            ));
            List<StatisticsRepairVo> list = new ArrayList<>();
            for (SysDepartment dept : depts) {
//                if (StringUtils.equals(dept.getDept_name(), "江苏鼎驰电子技术有限公司") || StringUtils.equals(dept.getDept_name(), "系统维护")) {
//                    continue;
//                }
                StatisticsRepairVo repairVo = new StatisticsRepairVo();
                repairVo.setName(dept.getDept_name());
                repairVo.setValue(Long.valueOf(0));
                repairVo.setDeptId(0);
                list.add(repairVo);
            }
            return ResultInfo.success(list);
        }


        List<SysDepartment> depts = sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery()
                // 等于null 或者等于0
                .and(i -> i.isNull(SysDepartment::getParent_dept).or().eq(SysDepartment::getParent_dept, 0)
                        .eq(SysDepartment::getIs_del,G.ISDEL_NO)
                ));
        Map<String, Long> levelMap = new HashMap<>();//一级部门map
        Map<String, List<String>> levelSubMapList = new HashMap<>();//一级部门map所有子部门
        for (SysDepartment dept:depts) {
            levelMap.put(String.valueOf(dept.getId()),0L);
            List<Integer> ids =statisticalReportService.getDeptSon(dept.getId());
            List<String> temp = new ArrayList<>();
            for (int id:ids) {
                temp.add(String.valueOf(id));
            }
            levelSubMapList.put(String.valueOf(dept.getId()),temp);
        }

        //按部门组装每个部门领用总值(所有部门没把子部门数据归到一级部门)
        Map<String, Long> newMap = new HashMap<>();
        for (StatisticsRepairVo map : repairVos) {
            String key = String.valueOf(map.getDeptId());
            if(newMap.containsKey(key)){
                Long curVal= newMap.get(key);
                //totalNum
                Long val =  map.getValue();
                newMap.put(key, NumberUtil.add(curVal,val).longValue());
            }else{
                String val =  String.valueOf(map.getValue());
                newMap.put(key, NumberUtil.add("0",val).longValue());
            }
        }

        //====把小部门归到大部门里
        for (String key :levelSubMapList.keySet()) {
            List<String> dList = levelSubMapList.get(key);
            for (String subDept : newMap.keySet()) {
                if(dList.contains(subDept)){
                    Long currentVal = levelMap.get(key) ;
                    levelMap.put(key,NumberUtil.add(newMap.get(subDept),currentVal).longValue());
                }
            }
        }

        Map<String,String> deptMap = sysDepartmentService.getAllDeptMap();
        List<StatisticsRepairVo> list = new ArrayList<>();
        for (String key:  levelMap.keySet()) {
            if (levelMap.get(key)==0L){
                continue;
            }
            StatisticsRepairVo repairVo = new StatisticsRepairVo();
            repairVo.setName(deptMap.get(key));
            repairVo.setValue(levelMap.get(key));
            repairVo.setDeptId(Integer.valueOf(key));
            list.add(repairVo);
        }


        return ResultInfo.success(list);
    }

    /**
     * 按资产状态统计资产数量
     *
     * @param asstestTypeId
     * @return
     */
    @RequestMapping(value = "statisticsByAssetsState.do")
    @ResponseBody
    public ResultInfo statisticsByAssetsState(Integer asstestTypeId) {
        return ResultInfo.success(assetsManageService.statisticsByAssetsState(asstestTypeId));
    }

    /**
     * 出入排行TOP10
     *
     * @return
     */
    @RequestMapping(value = "accessRecordTop10.do")
    @ResponseBody
    public ResultInfo accessRecordTop10() {
        return ResultInfo.success(assetsAccessRecordService.accessRecordTop10());
    }

    /**
     * 采购供应商排行TOP10
     *
     * @return
     */
    @RequestMapping(value = "purchaseFromSupplierTo10.do")
    @ResponseBody
    public ResultInfo purchaseFromSupplierTo10() {
        return ResultInfo.success(purchaseApplyService.purchaseFromSupplierTo10());
    }
    /**
     * 供应商列表
     */
    @RequestMapping(value = "supplierList.do")
    @ResponseBody
    public ResultInfo purchaseFromSupplier(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                                           @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                                           PurchaseApply beanParam) {
        return ResultInfo.success(purchaseApplyService.purchaseFromSupplierList(pageIndex, pageSize, beanParam));
    }

    /**
     * 资产处置占比
     *
     * @return
     */
    @RequestMapping(value = "managementByAssetsType.do")
    @ResponseBody
    public ResultInfo managementByAssetsType() {
        List<StatisticsRepairVo> statisticsRepairVos = managementAssetsService.managementByAssetsType();
        if (CollectionUtils.isEmpty(statisticsRepairVos)){
            // 查询资产类型 取10条
            List<AssetsType> assetsTypes = assetsTypeMapper.selectList(Wrappers.<AssetsType>lambdaQuery()
                    .eq(AssetsType::getIs_del, G.ISDEL_NO)
                    .last("limit 9")
            );

            List<StatisticsRepairVo> list = new ArrayList<>();
            for (AssetsType assetsType : assetsTypes) {
                StatisticsRepairVo repairVo = new StatisticsRepairVo();
                repairVo.setName(assetsType.getAssets_type_name());
                repairVo.setValue(Long.valueOf(0));
                repairVo.setDeptId(0);
                list.add(repairVo);
            }
            return ResultInfo.success(list);
        }
        return ResultInfo.success(statisticsRepairVos);
    }

    /**
     * 统计报表
     * 资产位置变动排名TOP10 （柱状图，按变动次数）
     * thr
     */
    @RequestMapping(value = "positionChangeTop10Data.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo positionChangeTop10Data(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("positionChangeTop10Data", sjfxService.positionChangeTop10Data(vo));
        return ResultInfo.success(jsonObject);
    }

    /**
     * 统计报表
     * 资产报修TOP10
     * thr
     */
    @RequestMapping(value = "assetRepairTop10Data.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo assetRepairTop10Data(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("assetRepairTop10Data", sjfxService.assetRepairTop10Data(vo));
        return ResultInfo.success(jsonObject);
    }

    /**
     * 统计报表
     * 资产报修TOP10（柱状图，按品类；点击按资产展示报修次数信息）
     * thr
     */
    @RequestMapping(value = "assetRepairCountData.json", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo assetRepairCountData(DataChartVo vo,
                                           @RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer limit) {
        return ResultInfo.success(sjfxService.assetRepairCountData(vo, page, limit));
    }


    @RequestMapping(value = "CarrryCountTop.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo CarrryCountTop() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("manageData", carryManageService.selectManageCountTop10());//正常外携
        jsonObject.put("abnormalData", abnormalService.selectAbnormalCountTop10());//异常外携
        return ResultInfo.success(jsonObject);
    }


    @RequestMapping(value = "selectAbnormalDataCountPage.json")
    @ResponseBody
    public ResultInfo carrryCountData(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, Integer asset_type_id) {
        PageInfo pageInfo = abnormalService.selectAbnormalDataCountPage(page, limit, asset_type_id);
        return ResultInfo.success(pageInfo);
    }

    /**
     * 统计资产报修列表
     *
     * @param deptId
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getApplySingles.do")
    @ResponseBody
    public ResultInfo getApplySingles(Integer deptId,
                                      @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer limit) {
        return ResultInfo.success(applySingleService.getPage(page, limit, deptId));
    }

    /**
     * 外携资产列表
     */
    @RequestMapping(value = "getOutAssetsList")
    @ResponseBody
    public ResultInfo getOutAssetsList(CarryManage bean,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        if(null == bean.getAsset_type_id()){
            return ResultInfo.success(new PageInfo<>());
        }
        List<AssetsManage> assetsManages = assetsManageService.selectList(Wrappers.<AssetsManage>lambdaQuery()
                .eq(AssetsManage::getAsset_type_id, bean.getAsset_type_id()).eq(AssetsManage::getIs_del, "0"));
        if(CollectionUtils.isEmpty(assetsManages)){
            return ResultInfo.success(new PageInfo<>());
        }
        bean.setAssetnumberList(assetsManages.stream().map(AssetsManage::getAsset_code).collect(Collectors.toList()));
        PageInfo list = carryManageService.selectPageList(page, limit, bean);

        return ResultInfo.success(list);
    }

    //---------------------------------------------耗材统计start-------------------------------------------------

    /**
     * 1.基础信息：耗材库存总数、耗材出库总数、申领出库总数、手动出库总数；
     */
    @RequestMapping(value = "getConsInfo.json")
    @ResponseBody
    public ResultInfo getConsInfo() {
        return ResultInfo.success(sjfxService.getConsInfo());
    }

    /**
     * 2.采购趋势：折线图展示近六个月的采购趋势（采购单数、耗材总数）；
     */
    @RequestMapping(value = "getPurchaseSixMonthCount.json")
    @ResponseBody
    public ResultInfo getPurchaseSixMonthCount() {
        return ResultInfo.success(sjfxService.getPurchaseSixMonthCount());
    }

    /**
     * 3.领用趋势：折线图展示近六个月的领用趋势（领用单数、耗材领用总数）；
     */
    @RequestMapping(value = "getReceiveSixMonthCount.json")
    @ResponseBody
    public ResultInfo getReceiveSixMonthCount() {
        return ResultInfo.success(sjfxService.getReceiveSixMonthCount());
    }

    /**
     * 4.采购排行：柱状图展示采购量TOP5的耗材（按品类）；
     */
    @RequestMapping(value = "getPurchaseTop5ByCategory.json")
    @ResponseBody
    public ResultInfo getPurchaseTop5ByCategory() {
        return ResultInfo.success(sjfxService.getPurchaseTop5ByCategory());
    }

    /**
     * 5.领用排行：柱状图展示领用量TOP5的耗材（按品类）；
     */
    @RequestMapping(value = "getReceiveTop5ByCategory.json")
    @ResponseBody
    public ResultInfo getReceiveTop5ByCategory() {
        return ResultInfo.success(sjfxService.getReceiveTop5ByCategory());
    }

    /**
     * 6.部门领用占比：饼状图展示各部门耗材领用占比；
     */
    @RequestMapping(value = "getCountByDept.json")
    @ResponseBody
    public ResultInfo getCountByDept() {
        return ResultInfo.success(sjfxService.getCountByDept());
    }

    /**
     * 7.消耗速率列表：以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     */
    @RequestMapping(value = "getConsList.json")
    @ResponseBody
    public ResultInfo getConsList(ConsDataVo vo,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer limit) {
        return ResultInfo.success(sjfxService.getConsList(vo, page, limit));
    }

    /**
     * 耗材部门占比列表
     */
    @RequestMapping(value = "getConsDeptList")
    @ResponseBody
    public ResultInfo getConsDeptList(ConsDataVo vo,
                                      @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer limit) {
        PageHelper.startPage(page, limit);
        List<ConsReceive> consReceives = consReceiveMapper.selectList(Wrappers.<ConsReceive>lambdaQuery()
                .eq(ConsReceive::getDepartment_id, vo.getId()).eq(ConsReceive::getIs_del,"0"));
        if(CollectionUtils.isEmpty(consReceives)){
            return ResultInfo.success(new PageInfo());
        }

        return ResultInfo.success(new PageInfo<>(consReceives));
    }

    /**
     * 采购排行列表
     */
    @RequestMapping(value = "purchaseRanking")
    @ResponseBody
    public ResultInfo purchaseRanking(ConsPurchaseDetail vo,
                                      @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer limit) {
        // 根据名称得到id
        List<ConsCategory> categories = consCategoryMapper.selectList(Wrappers.<ConsCategory>lambdaQuery()
                .eq(ConsCategory::getName,vo.getCategory_name()).eq(ConsCategory::getIs_del, "0"));
        if(CollectionUtils.isEmpty(categories)){
            return ResultInfo.success(new PageInfo());
        }

        Integer categoryId = categories.get(0).getId();
        // 根据品类id得到耗材详情
        List<ConsPurchaseDetail> details = consPurchaseDetailMapper.selectList(Wrappers.<ConsPurchaseDetail>lambdaQuery()
                .eq(ConsPurchaseDetail::getAssets_type_id, categoryId));
        if(CollectionUtils.isEmpty(details)){
            return ResultInfo.success(new PageInfo());
        }

        PageHelper.startPage(page, limit);
        List<ConsPurchaseApply> purchaseApplies = consPurchaseApplyMapper.selectList(Wrappers.<ConsPurchaseApply>lambdaQuery()
                .in(ConsPurchaseApply::getId, details.stream().map(ConsPurchaseDetail::getPurchase_apply_id).collect(Collectors.toList())));

        return ResultInfo.success(new PageInfo<>(purchaseApplies));
    }

    /**
     * 领用排行TOP5的耗材列表
     */
    @RequestMapping(value = "receiveRanking")
    @ResponseBody
    public ResultInfo receiveRanking(ConsPurchaseDetail vo,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer limit) {
        // 根据名称得到id
        List<ConsCategory> categories = consCategoryMapper.selectList(Wrappers.<ConsCategory>lambdaQuery()
                .eq(ConsCategory::getName,vo.getCategory_name()).eq(ConsCategory::getIs_del, "0"));
        if(CollectionUtils.isEmpty(categories)){
            return ResultInfo.success(new PageInfo());
        }

        Integer categoryId = categories.get(0).getId();
        // 根据品类id得到耗材详情
        List<ConsReceiveAssets> details = consReceiveAssetsMapper.selectList(Wrappers.<ConsReceiveAssets>lambdaQuery()
                .eq(ConsReceiveAssets::getAsset_type_id, categoryId));
        if(CollectionUtils.isEmpty(details)){
            return ResultInfo.success(new PageInfo());
        }

        PageHelper.startPage(page, limit);
        List<ConsReceive> consReceives = consReceiveMapper.selectList(Wrappers.<ConsReceive>lambdaQuery()
                .in(ConsReceive::getId, details.stream().map(ConsReceiveAssets::getReceive_id).collect(Collectors.toList())));

        return ResultInfo.success(new PageInfo<>(consReceives));
    }
    //---------------------------------------------耗材统计end-------------------------------------------------

    /**
     * 工作台 编辑入口展示个人所有图标
     */
    @RequestMapping(value = "showAllIcon")
    @ResponseBody
    public ResultInfo showAllIcon(){
        SysUser user = userService.getUser();
        JSONArray permission = permissionService.getPermissionByPost(user.getPost());
        ArrayList list = new ArrayList<>();
        for (int i = 0; i < permission.size(); i++) {
            for (int j = 0; j < ((List)((JSONObject) permission.get(i)).get("children")).size(); j++) {
                list.add( ((List)((JSONObject) permission.get(i)).get("children")).get(j));
            }
        }
        return ResultInfo.success(list);
    }
    /**
     * 保存快捷入口
     */
    @RequestMapping(value = "saveFast")
    @ResponseBody
    public ResultInfo saveFast(String ids){
        return fastEntranceService.saveFast(ids);
    }
    /**
     * 用户快捷入口图标展示
     */
    @RequestMapping(value = "showUserIcon")
    @ResponseBody
    public ResultInfo showUserIcon(){
        return ResultInfo.success(fastEntranceService.showUserIcon());
    }

    /**
     * getAllAssetsType
     */
    @RequestMapping(value = "getAllAssetsType")
    @ResponseBody
    public ResultInfo getAllAssetsType(){
        return ResultInfo.success(assetsTypeMapper.selectList(Wrappers.<AssetsType>lambdaQuery()
                .eq(AssetsType::getIs_del, G.ISDEL_NO)));
    }
    /**
     * 得到所有部门
     */
    @RequestMapping(value = "getAllDept")
    @ResponseBody
    public ResultInfo getAllDept(){
        return ResultInfo.success(sysDepartmentMapper.selectList(Wrappers.<SysDepartment>lambdaQuery()
                .eq(SysDepartment::getIs_del, G.ISDEL_NO)));
    }



}
