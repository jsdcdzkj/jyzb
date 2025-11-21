package com.jsdc.rfid.service.statisticalreport;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.common.utils.DateTimeUtils;
import com.jsdc.rfid.mapper.*;
import com.jsdc.rfid.mapper.statisticalreport.StatisticalReportMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import vo.ConsDataVo;
import vo.ConsPurchaseApplyVo;
import vo.ReceiveAndPurchaseVo;
import vo.StatisticsAssetStateVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author : lb
 * @date: 2023/3/7 18:29
 * desc :耗材领用排行TOP10
 **/
@Service
@Transactional
public class StatisticalReportService {
    @Autowired
    private SysDepartmentService departmentService;

    @Resource
    private StatisticalReportMapper statisticalReportMapper;

    @Autowired
    private ConsInventoryManagementService consInventoryManagementService;

    @Autowired
    private ConsReceiveAssetsService consReceiveAssetsService;
    @Autowired
    private SysLogService sysLogService;

    @Resource
    private SysDepartmentMapper sysDepartmentMapper;
    @Autowired
    private ConsReceiveService consReceiveService;
    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private PurchaseApplyMapper purchaseApplyMapper;
    @Autowired
    private ConsReceiveMapper consReceiveMapper;

    @Autowired
    private ConsHandworkMapper consHandworkMapper;

    @Autowired
    private ConsCategoryMapper consCategoryMapper;

    @Autowired
    private ConsPurchaseDetailMapper consPurchaseDetailMapper;

    @Autowired
    private ConsPurchaseApplyMapper consPurchaseApplyMapper;

    @Autowired
    private ConsSpecificationMapper consSpecificationMapper;

    @Autowired
    private AssetsTypeMapper assetsTypeMapper;
    /**
     * TODO 传入部门id  获取所有当前部门以及子部门
     *
     * @param deptId
     * @return
     */
    public ArrayList<Integer> getDeptSon(Integer deptId) {
        //拿取所有部门
        List<SysDepartment> allSysDeptList = departmentService.getList(null);
        //收集部门以及子部门
        ArrayList<Integer> childDeptList = new ArrayList<>();

        childDeptList.add(deptId);
        //进行递归
        getDeptIdList(deptId, allSysDeptList, childDeptList);

        return childDeptList;
    }

    private void getDeptIdList(Integer deptId, List<SysDepartment> allSysDeptList, List<Integer> childDeptList) {
        // 遍历所有部门，查deptId的所有子部门
        allSysDeptList.forEach(e -> {
            // deptId=parentId为当前部门的直接子部门
            if (deptId.equals(e.getParent_dept())) {
                // 添加子部门
                childDeptList.add(e.getId());
                // 递归查deptId子部门下的子部门
                this.getDeptIdList(e.getId(), allSysDeptList, childDeptList);
            }
        });
    }


    /**
     * @Description: 统计资产总值
     * @param: []
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:34
     */
    public Map<String, List<String>> totalAssets() {
        List<Map<String, Object>> list = statisticalReportMapper.totalAssets();
        DecimalFormat df = new DecimalFormat("#.00");
        Map<String, List<String>> totalAssets = new HashMap<>();
        List<String> X = new ArrayList<>();
        List<String> Y = new ArrayList<>();
        for (Map<String, Object> map : list) {
            if (map.get("dept_name").toString().contains("江苏鼎驰电子")){
                continue;
            }
            if (map.get("total") == null){
                continue;
            }
            X.add(map.get("dept_name").toString());
            Y.add(df.format(map.get("total")));
        }
        totalAssets.put("X", X);
        totalAssets.put("Y", Y);
        return totalAssets;
    }

    /**
     * @Description: 统计未打印标签资产
     * @param: []
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:42
     */
    public Map<String, List<String>> unprintedAsset(Integer deptId) {
        List<String> str = new ArrayList<>();
        if (!org.springframework.util.StringUtils.isEmpty(deptId)) {
            str = this.getDeptSon(deptId).stream().map(String::valueOf).collect(Collectors.toList());
        }
        List<Map<String, Object>> list = statisticalReportMapper.unprintedAsset(String.join(",", str));
        DecimalFormat df = new DecimalFormat("#.00");
        Map<String, List<String>> unprintedAsset = new HashMap<>();
        List<String> X = new ArrayList<>();
        List<String> Y = new ArrayList<>();
        for (Map<String, Object> map : list) {
            X.add(map.get("typeName").toString());
            Y.add(df.format(map.get("total")));
        }
        unprintedAsset.put("X", X);
        unprintedAsset.put("Y", Y);
        return unprintedAsset;
    }

    /**
     * @Description: 各部门采领用单数同比
     * @param: []
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:48
     */
    public Map<String, List<String>> receive(Integer deptId) throws ParseException {
        Map<String, List<String>> purchaseAll = new HashMap<>();
        List<String> nowYear = new ArrayList<>();
        List<String> lastYear = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            nowYear.add(DateTimeUtils.getLastTime(DateTimeUtils.formatDate(DateTimeUtils.getCurrYearFirst()), "yyyy-MM", 0
                    , i, 0));
            lastYear.add(DateTimeUtils.getLastTime(DateTimeUtils.formatDate(DateTimeUtils.getCurrYearFirst()), "yyyy-MM", -1, i, 0));
        }
        List<String> str = new ArrayList<>();
        if (!org.springframework.util.StringUtils.isEmpty(deptId)) {
            str = this.getDeptSon(deptId).stream().map(String::valueOf).collect(Collectors.toList());
        }
        //今年领用
        List<Map<String, Object>> receive = statisticalReportMapper.receive(String.join(",", str), nowYear);
        //去年领用
        List<Map<String, Object>> receive1 = statisticalReportMapper.receive(String.join(",", str), lastYear);
        List<ReceiveAndPurchaseVo> receiveAndPurchaseVos = new ArrayList<>();
        List<String> receiveTotal = new ArrayList<>();
        List<String> receive1Total = new ArrayList<>();
        for (String s : nowYear) {
            ReceiveAndPurchaseVo receiveAndPurchaseVo = new ReceiveAndPurchaseVo();
            receiveAndPurchaseVo.setTime(s);
            for (Map<String, Object> map : receive) {
                if (s.equals(map.get("times"))) {
                    receiveAndPurchaseVo.setNowTotal(map.get("total") + "");
                }
            }
            for (Map<String, Object> map : receive1) {
                if (DateTimeUtils.getLastTime(s, "yyyy-MM", -1, 0, 0).equals(map.get("times"))) {
                    receiveAndPurchaseVo.setLastTotal(map.get("total") + "");
                }
            }
            receiveAndPurchaseVos.add(receiveAndPurchaseVo);
        }
        for (String s : nowYear) {
            for (ReceiveAndPurchaseVo vo : receiveAndPurchaseVos) {
                if (s.equals(vo.getTime())) {
                    if (StringUtils.isEmpty(vo.getNowTotal())) {
                        receiveTotal.add("0");
                    } else {
                        receiveTotal.add(vo.getNowTotal());
                    }
                }
                if (s.equals(vo.getTime())) {
                    if (StringUtils.isEmpty(vo.getLastTotal())) {
                        receive1Total.add("0");
                    } else {
                        receive1Total.add(vo.getLastTotal());
                    }
                }
            }
        }

        purchaseAll.put("X", nowYear);
        purchaseAll.put("receive", receiveTotal);
        purchaseAll.put("receive1", receive1Total);
        return purchaseAll;
    }

    /**
     * @Description: 各部门采购单数同比
     * @param: [deptId]
     * @return: vo.StatisticsAssetStateVo
     * @author: csx
     * @date: 2023/3/10
     * @time: 9:30
     */
    public Map<String, List<String>> purchase(Integer deptId) throws ParseException {
        Map<String, List<String>> purchaseAll = new HashMap<>();
        List<String> nowYear = new ArrayList<>();
        List<String> lastYear = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            nowYear.add(DateTimeUtils.getLastTime(DateTimeUtils.formatDate(DateTimeUtils.getCurrYearFirst()), "yyyy-MM", 0, i, 0));
            lastYear.add(DateTimeUtils.getLastTime(DateTimeUtils.formatDate(DateTimeUtils.getCurrYearFirst()), "yyyy-MM", -1, i, 0));
        }
        List<String> str = new ArrayList<>();
        if (!org.springframework.util.StringUtils.isEmpty(deptId)) {
            str = this.getDeptSon(deptId).stream().map(String::valueOf).collect(Collectors.toList());
        }
        //今年采购
        List<Map<String, Object>> purchase = statisticalReportMapper.purchase(String.join(",", str), nowYear);
        //上年采购
        List<Map<String, Object>> purchase1 = statisticalReportMapper.purchase(String.join(",", str), lastYear);
        List<ReceiveAndPurchaseVo> receiveAndPurchaseVos = new ArrayList<>();
        List<String> nowTotal = new ArrayList<>();
        List<String> lastTotal = new ArrayList<>();
        for (String s : nowYear) {
            ReceiveAndPurchaseVo receiveAndPurchaseVo = new ReceiveAndPurchaseVo();
            receiveAndPurchaseVo.setTime(s);
            for (Map<String, Object> map : purchase) {
                if (s.equals(map.get("times"))) {
                    receiveAndPurchaseVo.setNowTotal(map.get("total") + "");
                }
            }
            for (Map<String, Object> map : purchase1) {
                if (DateTimeUtils.getLastTime(s, "yyyy-MM", -1, 0, 0).equals(map.get("times"))) {
                    receiveAndPurchaseVo.setLastTotal(map.get("total") + "");
                }
            }
            receiveAndPurchaseVos.add(receiveAndPurchaseVo);
        }
        for (String s : nowYear) {
            for (ReceiveAndPurchaseVo vo : receiveAndPurchaseVos) {
                if (s.equals(vo.getTime())) {
                    if (StringUtils.isEmpty(vo.getNowTotal())) {
                        nowTotal.add("0");
                    } else {
                        nowTotal.add(vo.getNowTotal());
                    }
                }
                if (s.equals(vo.getTime())) {
                    if (StringUtils.isEmpty(vo.getLastTotal())) {
                        lastTotal.add("0");
                    } else {
                        lastTotal.add(vo.getLastTotal());
                    }
                }
            }
        }

        purchaseAll.put("X", nowYear);
        purchaseAll.put("now", nowTotal);
        purchaseAll.put("last", lastTotal);
        return purchaseAll;
    }

    /**
     * 供应商采购排行(TOP10)
     * TODO 按申请单金额TOP10
     *
     * @return
     */
    public Map<String, Object> getSupplierProcurementRankingMoney() {
        ArrayList<Object> money = new ArrayList<>();
        ArrayList<Object> name = new ArrayList<>();
        List<Map<String, Object>> list = statisticalReportMapper.supplierProcurementRankingMoney();
        //循环组装数据
        list.forEach(x -> {
            money.add(x.get("money"));
            name.add(x.get("companyName"));
        });
        //组装返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("money", money);
        map.put("name", name);
        return map;
    }

    /**
     * 供应商采购排行(TOP10)
     * TODO 按申请单数量TOP10
     *
     * @return
     */
    public Map<String, Object> getSupplierProcurementRankingApply(Integer year) {
        HashMap<String, Object> map = new HashMap<>();
        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }
        List<Map<String, Object>> list = statisticalReportMapper.supplierProcurementRankingApply(startDay, endDay);
        ArrayList<Object> count = new ArrayList<>();
        ArrayList<Object> name = new ArrayList<>();

        //循环组装数据
        list.forEach(x -> {
            count.add(x.get("count"));
            name.add(x.get("companyName"));
        });
        //组装返回数据

        map.put("count", count);
        map.put("name", name);
        return map;
    }


    /**
     * 采购金额排行<TOP5>
     */
    public Map<String, Object> getPurchaseAmountRanking() {
        ArrayList<Object> name = new ArrayList<>();
        ArrayList<Object> amount = new ArrayList<>();
        List<Map<String, Object>> list = statisticalReportMapper.purchaseAmountRanking();
        //循环组装数据
        list.forEach(x -> {
            name.add(x.get("purName"));
            amount.add(x.get("amount"));

        });
        //组装返回数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("amount", amount);
        return map;
    }

    /**
     * 最新十二个月 部门采购单趋势 同比
     */
    public Map<String, Object> getPurchaseOrderTrendYearOnYear(Integer id) {
        ArrayList<Integer> deptSon = getDeptSon(id);
        String join = StringUtils.join(deptSon, ",");
        ArrayList<Object> month = new ArrayList<>();
        ArrayList<Object> count = new ArrayList<>();
        ArrayList<Object> monthYearOnYear = new ArrayList<>();
        ArrayList<Object> countYearOnYear = new ArrayList<>();
        List<Map<String, Object>> procurementTrendNow = statisticalReportMapper.getProcurementTrendNow(join);
        procurementTrendNow.forEach(x -> {
            month.add(x.get("month"));
            count.add(x.get("count"));
        });
        List<Map<String, Object>> procurementTrendOnce = statisticalReportMapper.getProcurementTrendOnce(join);
        procurementTrendOnce.forEach(x -> {
            monthYearOnYear.add(x.get("month"));
            countYearOnYear.add(x.get("count"));
        });
        HashMap<String, Object> map = new HashMap<>();
        map.put("month", month);
        map.put("count", count);
        map.put("monthYearOnYear", monthYearOnYear);
        map.put("countYearOnYear", countYearOnYear);
        return map;
    }


    /**
     * 最近12个月
     *
     * @return
     */
    public List<String> getNearMonths() {
        List<String> data = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 11);
        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            String month = calendar.get(Calendar.YEAR) + "-" + String.format("%02d", calendar.get(Calendar.MONTH) == 0 ? 12 : calendar.get(Calendar.MONTH));
            data.add(month);
        }

        return data;
    }

    /**
     * 获取指定年的12个月
     *
     * @param year
     * @return
     */
    public List<String> getYearMonths(String year) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(String.format("%s-%02d", year, i + 1));
        }
        return data;
    }

    /**
     * 正常，异常外携数据年份
     *
     * @param
     * @return
     */
    public List<String> getCarryManageYears() {
        return statisticalReportMapper.getCarryManageYears();
    }

    /**
     * 资产采购，处置有数据年份
     *
     * @return
     */
    public List<String> getManageOrPurchaseYears() {
        return statisticalReportMapper.getManageOrPurchaseYears();
    }

    /**
     * 报修，处理 有数据的年份
     *
     * @return
     */
    public List<String> getApplyOrFeebackYears() {
        return statisticalReportMapper.getApplyOrFeebackYears();
    }

    public JSONObject getManageOrPurchaseMonth(String year) {


        JSONObject jsonObject = new JSONObject();

        List<String> months = null;
        if (StringUtils.isEmpty(year)) {
            months = getNearMonths();
        } else {
            months = getYearMonths(year);
        }

        // 资产处置
        List<Map<String, Object>> managements = statisticalReportMapper.managementMonth(year);
        List<Double> managementPrices = new ArrayList<>();
        // 资产采购
        List<Map<String, Object>> purchases = statisticalReportMapper.purchaseMonth(year);
        List<Double> purchasesPrices = new ArrayList<>();
        months.forEach(x -> {

            Map<String, Object> optional = managements.stream().filter(y -> y.get("months").equals(x)).findAny().orElse(null);

            if (optional == null) {
                managementPrices.add(0d);
            } else {
                Object s = optional.get("prices");
                BigDecimal bigDecimal = new BigDecimal(s.toString()).setScale(2, RoundingMode.HALF_UP);
                managementPrices.add(bigDecimal.doubleValue());
            }

            Map<String, Object> optional1 = purchases.stream().filter(y -> y.get("months").equals(x)).findAny().orElse(null);

            if (optional1 == null) {
                purchasesPrices.add(0d);
            } else {
                Object s = optional1.get("prices");
                BigDecimal bigDecimal = new BigDecimal(s.toString()).setScale(2, RoundingMode.HALF_UP);
                purchasesPrices.add(bigDecimal.doubleValue());
            }
        });


//        List<Double> managementPrices=managements.stream().map(m->(Double)m.get("prices")).collect(Collectors.toList());
//        List<Double> purchasesPrices=purchases.stream().map(m->(Double)m.get("prices")).collect(Collectors.toList());

        jsonObject.put("months", months);

        jsonObject.put("managementPrices", managementPrices);
        jsonObject.put("purchasesPrices", purchasesPrices);

        return jsonObject;
    }

    public JSONObject getApplyOrFeebackCount(String year) {
        JSONObject jsonObject = new JSONObject();

        List<String> months = null;
        if (StringUtils.isEmpty(year)) {
            months = getNearMonths();
        } else {
            months = getYearMonths(year);
        }

        // 报修数量
        List<Map<String, Object>> applys = statisticalReportMapper.applySingleCount(year);
        List<Long> applyCounts = new ArrayList<>();
        List<Map<String, Object>> feebacks = statisticalReportMapper.applySingleFeebackCount(year);
        List<Long> feebackCounts = new ArrayList<>();
        months.forEach(x -> {

            Map<String, Object> optional = applys.stream().filter(y -> y.get("months").equals(x)).findAny().orElse(null);

            if (optional == null) {
                applyCounts.add(0L);
            } else {
                applyCounts.add((Long) optional.get("prices"));
            }

            Map<String, Object> optional1 = feebacks.stream().filter(y -> y.get("months").equals(x)).findAny().orElse(null);

            if (optional1 == null) {
                feebackCounts.add(0L);
            } else {
                feebackCounts.add((Long) optional1.get("prices"));
            }
        });


        jsonObject.put("months", months);
        jsonObject.put("applyCounts", applyCounts);

        jsonObject.put("feebackCounts", feebackCounts);

        return jsonObject;
    }

    public JSONObject carryManageCount(String year) {
        JSONObject jsonObject = new JSONObject();


        List<String> months = null;
        if (StringUtils.isEmpty(year)) {
            months = getNearMonths();
        } else {
            months = getYearMonths(year);
        }

        // 异常外携
        List<Map<String, Object>> carrys = statisticalReportMapper.carryAbnormalCount(year);
        List<Long> carrysCounts = new ArrayList<>();
        // 正常外携
        List<Map<String, Object>> manages = statisticalReportMapper.carryManageCount(year);
        List<Long> managesCounts = new ArrayList<>();
        months.forEach(x -> {

            Map<String, Object> optional = carrys.stream().filter(y -> y.get("months").equals(x)).findAny().orElse(null);

            if (optional == null) {
                carrysCounts.add(0L);
            } else {
                carrysCounts.add((Long) optional.get("prices"));
            }

            Map<String, Object> optional1 = manages.stream().filter(y -> y.get("months").equals(x)).findAny().orElse(null);

            if (optional1 == null) {
                managesCounts.add(0L);
            } else {
                managesCounts.add((Long) optional1.get("prices"));
            }
        });


        jsonObject.put("months", months);
        jsonObject.put("carrysCounts", carrysCounts);

        jsonObject.put("managesCounts", managesCounts);

        return jsonObject;
    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：以资产类型分类,统计库存数量 TOP10
     * @Date ：2023/3/8 下午 4:33
     * @Modified By：
     */
    public List<Map<String, Object>> statTop10InventoryNumByTypeName(String startDay , String endDay ) {
        return statisticalReportMapper.statTop10InventoryNumByTypeName(startDay ,endDay );

    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description： 1.库存总价值
     * @Date ：2023/3/9 上午 9:03
     * @Modified By：
     */
    public Map<String, Object> statInventoryTotalAssert() {
        return statisticalReportMapper.statInventoryTotalAssert();
    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：库存按品类计算总价值
     * @Date ：2023/3/9 上午 9:39
     * @Modified By：
     */
    public PageInfo<Map<String, Object>> statInventoryAssertByType(Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, Object>> maps = statisticalReportMapper.statInventoryAssertByType();
        return new PageInfo<>(maps);
    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：资产状态数量
     * @Date ：2023/3/9 上午 11:12
     * @Modified By：
     */
    public List<StatisticsAssetStateVo> statAssertNumByType(Integer assertTypeId) {
        return statisticalReportMapper.statAssertNumByType(assertTypeId);
    }

    /**
     * 耗材领用排行TOP10
     *
     * @param deptIds
     * @return
     */
    public Map<String, List<String>> consumableReceive(String deptIds) {
        Map<String, List<String>> returnMap = new HashMap<>();

        if (!"".equals(StringUtils.trimToEmpty(deptIds)) && NumberUtils.toInt(deptIds) > 0) {
            List<Integer> ids = getDeptSon(NumberUtils.toInt(deptIds));
            deptIds = StringUtils.join(ids, ",");
        } else {
            deptIds = "";
        }


        List<Map<String, Object>> list = statisticalReportMapper.consumableReceive(deptIds);

        List<String> listX = new ArrayList<>();
        List<String> listY = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String categoryName = String.valueOf(map.get("categoryName"));
            listX.add(categoryName);
            listY.add(String.valueOf(map.get("totalNum")));
        }

        returnMap.put("x", listX);
        returnMap.put("y", listY);
        return returnMap;

    }

    /**
     * 部门领用总值排行
     *
     * @param year
     * @return
     */
    public Map<String, List<String>> deptReceiveTop(String year) {

        Map<String, List<String>> returnMap = new HashMap<>();
        //取所有数据
        List<Map<String, Object>> lists = statisticalReportMapper.deptReceiveTop(year);

        //获取一级部门
        List<SysDepartment> level = departmentService.getAllDept();
        Map<String, Double> levelMap = new HashMap<>();//一级部门map
        Map<String, List<String>> levelSubMapList = new HashMap<>();//一级部门map所有子部门
        for (SysDepartment dept : level) {
            levelMap.put(String.valueOf(dept.getId()), 0D);
            List<Integer> ids = getDeptSon(dept.getId());
            List<String> temp = new ArrayList<>();
            for (int id : ids) {
                temp.add(String.valueOf(id));
            }
            levelSubMapList.put(String.valueOf(dept.getId()), temp);
        }

        //按部门组装每个部门领用总值(所有部门没把子部门数据归到一级部门)
        Map<String, Double> newMap = new HashMap<>();
        for (Map<String, Object> map : lists) {
            String key = String.valueOf(map.get("department_id"));
            if (newMap.containsKey(key)) {
                String curVal = String.valueOf(newMap.get(key));
                //totalNum
                String val = String.valueOf(map.get("applyNums"));
                newMap.put(key, NumberUtil.add(curVal, val).doubleValue());
            } else {
                String val = String.valueOf(map.get("applyNums"));
                newMap.put(key, NumberUtil.add("0", val).doubleValue());
            }
        }
        //====把小部门归到大部门里
        for (String key : levelSubMapList.keySet()) {
            List<String> dList = levelSubMapList.get(key);
            for (String subDept : newMap.keySet()) {
                if (dList.contains(subDept)) {
                    double currentVal = levelMap.get(key);
                    levelMap.put(key, NumberUtil.add(newMap.get(subDept).doubleValue(), currentVal));
                }
            }
        }


        Map<String, Double> resultMap = sortDescend(levelMap, 0);
        Map<String, String> deptMap = departmentService.getAllDeptMap();

        List<String> listX = new ArrayList<>();
        List<String> listY = new ArrayList<>();
        for (String key : resultMap.keySet()) {
            if (resultMap.get(key) == 0d || listX.size() == 5) {
                break;
            }
            String deptName = deptMap.get(key);
            listX.add(deptName);
            listY.add(String.valueOf(resultMap.get(key)));
        }

        returnMap.put("x", listX);
//        if (listY.isEmpty()){
//            listY.add("0");
//        }
        returnMap.put("y", listY);
        return returnMap;
    }


    /**
     * 部门领用总值排行
     *
     * @param year
     * @return
     */
    public Map<String, List<String>> receiveTotalValue(Integer year) {

        Map<String, List<String>> returnMap = new HashMap<>();
        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }

        //取所有数据
        List<Map<String, Object>> lists = statisticalReportMapper.receiveTotalValue(startDay, endDay);

        //获取一级部门
        List<SysDepartment> level = departmentService.getAllDept();
        Map<String, Double> levelMap = new HashMap<>();//一级部门map
        Map<String, List<String>> levelSubMapList = new HashMap<>();//一级部门map所有子部门
        for (SysDepartment dept : level) {
            levelMap.put(String.valueOf(dept.getId()), 0D);
            List<Integer> ids = getDeptSon(dept.getId());
            List<String> temp = new ArrayList<>();
            for (int id : ids) {
                temp.add(String.valueOf(id));
            }
            levelSubMapList.put(String.valueOf(dept.getId()), temp);
        }

        //按部门组装每个部门领用总值(所有部门没把子部门数据归到一级部门)
        Map<String, Double> newMap = new HashMap<>();
        for (Map<String, Object> map : lists) {
            String key = String.valueOf(map.get("department_id"));
            if (newMap.containsKey(key)) {
                String curVal = String.valueOf(newMap.get(key));
                //totalNum
                String val = String.valueOf(map.get("totalNum"));
                newMap.put(key, NumberUtil.add(curVal, val).doubleValue());
            } else {
                String val = String.valueOf(map.get("totalNum"));
                newMap.put(key, NumberUtil.add("0", val).doubleValue());
            }
        }
        //====把小部门归到大部门里
        for (String key : levelSubMapList.keySet()) {
            List<String> dList = levelSubMapList.get(key);
            for (String subDept : newMap.keySet()) {
                if (dList.contains(subDept)) {
                    double currentVal = levelMap.get(key);
                    levelMap.put(key, NumberUtil.add(newMap.get(subDept).doubleValue(), currentVal));
                }
            }
        }


        Map<String, Double> resultMap = sortDescend(levelMap, 0);
        Map<String, String> deptMap = departmentService.getAllDeptMap();

        List<String> listX = new ArrayList<>();
        List<String> listY = new ArrayList<>();
        for (String key : resultMap.keySet()) {
            String deptName = deptMap.get(key);
            listX.add(deptName);
            listY.add(String.valueOf(resultMap.get(key)));
        }

        returnMap.put("x", listX);
        returnMap.put("y", listY);
        return returnMap;
    }

    /**
     * map 按value排序 asc
     *
     * @param map
     * @param <K>
     * @param <V>
     * @param top 如果top <= 0 取全部  否按 top取值
     * @return
     */
    private static <K, V extends Comparable<? super V>> Map<K, V> sortAscend(Map<K, V> map, int top) {
        List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return compare;
            }
        });

        Map<K, V> returnMap = new LinkedHashMap<K, V>();
        int i = 0;
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
            i = i + 1;
            if (top > 0 && top == i) {
                break; //取top N
            }
        }
        return returnMap;
    }

    /**
     * map 按value排序 desc
     *
     * @param map
     * @param <K>
     * @param <V>
     * @param top 如果top <= 0 取全部  否按 top取值
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortDescend(Map<K, V> map, int top) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> returnMap = new LinkedHashMap<K, V>();
        int i = 0;
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
            i = i + 1;
            if (top > 0 && top == i) {
                break; //取top N
            }
        }
        return returnMap;
    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：库存总数量
     * @Date ：2023/3/9 上午 11:12
     * @Modified By：
     */
    public Map<String, Object> statInventoryTotalAmount() {
        return statisticalReportMapper.statInventoryTotalAmount();
    }


    /**
     * 异常/正常外携资产排行(按品类、TOP5)@cz
     *
     * @return 结果集
     */
    public Object carryTop() {
        //结果集
        HashMap<String, Object> res = new HashMap<>();
        //正常外携统计
        List<Map> normal = statisticalReportMapper.censusTopOfNormalCarryByAssetsType();
        //异常外携统计
        List<Map> abnormal = statisticalReportMapper.censusTopOfAbnormalCarryByAssetsType();
        res.put("normal", normal);
        res.put("abnormal", abnormal);
        return res;
    }

    /**
     * 各部门外携行为占比@cz
     *
     * @return 结果集
     */
    public Object censusAllCarryCountByDept() {
        //查询所有顶级部门
        SysDepartment sysDepartment = new SysDepartment();
        sysDepartment.setParent_dept(0);
        sysDepartment.setIs_del("0");
        List<SysDepartment> list = departmentService.getList(sysDepartment);
        //过滤掉非OA部门
//        List<SysDepartment> list = beforeFilter.stream().filter(item -> item.getOa_dept_id() != 0).collect(Collectors.toList());
        //初始化结果集
        LinkedList<Map> res = new LinkedList<>();
        for (SysDepartment department : list) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("id", department.getId());
            item.put("deptName", department.getDept_name());
            item.put("count", 0L);
            res.add(item);
        }
        //获取子部门外携排行
        Long sum = 0L;
        List<Map> topsOfChild = statisticalReportMapper.censusAllCarryCountByDept();
        for (Map map : topsOfChild) {
            sum = sum + (Long) map.get("count");
        }
        //塞入父级id
        for (Map map : topsOfChild) {
            Integer childId = (Integer) map.get("child_id");
            map.put("parentId", getParentId(childId));
        }
        //结果集赋值
        for (Map map : topsOfChild) {
            Integer parentId = (Integer) map.get("parentId");
            for (Map item : res) {
                Integer id = (Integer) item.get("id");
                if (parentId.equals(id)) {
                    Long count = (Long) item.get("count");
                    item.put("count", count + (Long) map.get("count"));
                }
            }
        }
        //结果集百分比计算
        for (Map item : res) {
            Long count = (Long) item.get("count");
            double percentage = count.doubleValue() / sum.doubleValue();
            //保留两位小数
            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(2);
            item.put("percentage", nt.format(percentage));
        }
        //排序
        return res.stream().sorted((item1, item2) -> {
            Long countOf1 = (Long) item1.get("count");
            Long countOf2 = (Long) item2.get("count");
            return countOf2.compareTo(countOf1);
        }).collect(Collectors.toList());
    }

    /**
     * 采购全额排行(TOP5) @cz
     *
     * @return 结果集
     */
    public Object purchaseTop(Integer year) {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(Calendar.YEAR); //当前年份
        String yearFirstStr = ""; //筛选开始时间
        String yearLastStr = "";//筛选结束时间
        if (year == i) {
            //当前年判断
            yearFirstStr = DateTimeUtils.getYearFirstStr(year);
            yearLastStr = DateTimeUtils.formatDateChoseSimple(new Date(), DateTimeUtils.DEFAULT_FORMAT_DATA_TIME);
        } else if (year != -1){
            //往年判断
            yearFirstStr = DateTimeUtils.getYearFirstStr(year);
            yearLastStr = DateTimeUtils.getCurrYearLast(year);
        }
        HashMap<String, Object> res = new HashMap<>();
        List<Map> maps = statisticalReportMapper.purchaseTop(yearFirstStr, yearLastStr);
        LinkedList<String> x = new LinkedList<>();
        LinkedList<String> y = new LinkedList<>();
        for (Map map : maps) {
            x.add(String.valueOf(map.get("count")));
            y.add(String.valueOf(map.get("assets_type_name")));
        }
        res.put("x", x);
        res.put("y", y);
        return res;
    }

    /**
     * 采购总值/库存总值/出库总值 @cz
     *
     * @return 结果集
     */
    public Object censusAssetsInAndOut() {
        //结果集
        HashMap<String, Object> res = new HashMap<>();
        res.put("current", statisticalReportMapper.getTotalSum());
        res.put("out", statisticalReportMapper.getOutSum());
        res.put("in", statisticalReportMapper.censusPriceSum());
        return res;
    }

    /**
     * 消耗速率TOP15 @cz
     *
     * @return 结果集
     */
    public Object rateConsumptionTop(Integer year) {
        Map<String, List<String>> returnMap = new HashMap<>();
        List<ConsDataVo> list = new ArrayList<>();
        //当前库存
        //List<ConsDataVo> consDataVos = consInventoryManagementService.getInventoryNum(null);
        List<ConsDataVo> consDataVos = statisticalReportMapper.getInventoryNum(null);
        //领用量、加入筛选天数,并设置日均消耗统计天数
        long betweenDay; //日期差
        Calendar instance = Calendar.getInstance();
        int i = instance.get(Calendar.YEAR); //当前年份
        String yearFirstStr = ""; //筛选开始时间
        String yearLastStr = "";//筛选结束时间
        if (year == -1) {
            //系统运行天数(全部判断)
            LambdaUpdateWrapper<SysLog> wrapper = new LambdaUpdateWrapper<>();
            wrapper.orderByAsc(SysLog::getId);
            wrapper.last("limit 1");
            SysLog sysLog = sysLogService.selectOne(wrapper);
            //日期差
            betweenDay = DateUtil.between(sysLog.getOperate_time(), new Date(), DateUnit.DAY);
        } else if (year == i) {
            //当前年判断
            yearFirstStr = DateTimeUtils.getYearFirstStr(year);
            yearLastStr = DateTimeUtils.formatDateChoseSimple(new Date(), DateTimeUtils.DEFAULT_FORMAT_DATA_TIME);
            //计算本年时期差
            Date start = DateTimeUtils.parseDateChoseSimple(yearFirstStr, DateTimeUtils.DEFAULT_FORMAT_DATA_TIME);
            Date end = DateTimeUtils.parseDateChoseSimple(yearLastStr, DateTimeUtils.DEFAULT_FORMAT_DATA_TIME);
            betweenDay = DateTimeUtils.differentDaysByMillisecond(start, end);
        } else {
            //往年判断
            yearFirstStr = DateTimeUtils.getYearFirstStr(year);
            yearLastStr = DateTimeUtils.getCurrYearLast(year);
            //判断闰年
            betweenDay = new GregorianCalendar().isLeapYear(year) ? 366L : 365L;
        }
        // 领用量
        List<ConsDataVo> consDataVos2 = statisticalReportMapper.getApplyNum(yearFirstStr, yearLastStr);
        Map<String, Integer> map = new HashMap<>();
        consDataVos2.forEach(x -> {
            map.put(x.getNameTypeId(), x.getAmount());
        });

        consDataVos.forEach(x -> {
            ConsDataVo vo2 = new ConsDataVo();
            vo2.setName(x.getNameType());
            // 当前库存
            vo2.setStockNum(x.getAmount());
            if (map.containsKey(x.getNameTypeId())) {
                //除法保留两位小数

                //日均消耗/领用量
                BigDecimal num = new BigDecimal(map.get(x.getNameTypeId()));
                BigDecimal days = new BigDecimal(betweenDay);
                //2.日均消耗量=领用总数/系统运行天数
                vo2.setDaysApplyNum(num.divide(days, 2, BigDecimal.ROUND_HALF_UP).toString());
                //3.预计可用天数=库存总数/日均消耗量
                if (map.get(x.getNameTypeId()) > 0) {
                    // 预计可用天数
                    vo2.setDaysNum(new BigDecimal(x.getAmount()).divide(days, 1, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    // 预计可用天数
                    vo2.setDaysNum("0");
                }
            } else {
                //日均消耗/领用量
                vo2.setDaysApplyNum("0");
                // 预计可用天数
                vo2.setDaysNum("0");
            }
            list.add(vo2);
        });
        //排序并截取前15返回结果集
        list.sort((p1, p2) -> Double.valueOf(p2.getDaysApplyNum()).compareTo(Double.valueOf(p1.getDaysApplyNum())));
        List<ConsDataVo> top15 = list.stream().skip(0).limit(15).collect(Collectors.toList());

        List<String> xList = new ArrayList<>();
        List<String> yList = new ArrayList<>();
        for (ConsDataVo cVo : top15) {
            xList.add(cVo.getName());
            yList.add(cVo.getDaysApplyNum());
        }
        returnMap.put("x", xList);
        returnMap.put("y", yList);
        return returnMap;
    }

    /**
     * 根据子集获取最父级id @cz
     *
     * @param id 子集id
     * @return 附件id
     */
    private Integer getParentId(Integer id) {
        SysDepartment parent = sysDepartmentMapper.selectById(id);
        if (null != parent && parent.getParent_dept() != null && parent.getParent_dept() != 0) {
            return getParentId(parent.getParent_dept());
        } else {
            return parent.getId();
        }
    }

    /**
     * 资产领用排行TOP10 @cz
     *
     * @param osId 部门id
     * @return 结果集
     */
    public Map<String, Object> assetCollectionTop(Integer osId) {
        //处理请求参数
        List<String> str = new ArrayList<>();
        if (!org.springframework.util.StringUtils.isEmpty(osId)) {
            str = this.getDeptSon(osId).stream().map(String::valueOf).collect(Collectors.toList());
        }
        //定义结果集
        Map<String, Object> res = new HashMap<>();
        List<Map> maps = statisticalReportMapper.assetCollectionTop(String.join(",", str));
        LinkedList<String> assetsTypeNameList = new LinkedList<>();
        LinkedList<String> countList = new LinkedList<>();
        for (Map map : maps) {
            assetsTypeNameList.add((String) map.get("assets_type_name"));
            countList.add(String.valueOf(map.get("count")));
        }
        // 如果没有数据，返回默认数据
        if (CollectionUtils.isEmpty(assetsTypeNameList) && CollectionUtils.isEmpty(countList)) {
            List<AssetsType> assetsTypes = assetsTypeMapper.selectList(Wrappers.<AssetsType>lambdaQuery().eq(AssetsType::getIs_del, G.ISDEL_NO).last("limit 10"));
            for (AssetsType assetsType : assetsTypes) {
                assetsTypeNameList.add(assetsType.getAssets_type_name());
                countList.add("0");
            }
        }
        // 如果数据不足10条，补充数据
        if (assetsTypeNameList.size() < 10) {
            int size = 10 - assetsTypeNameList.size();
            List<AssetsType> assetsTypes = assetsTypeMapper.selectList(Wrappers.<AssetsType>lambdaQuery().eq(AssetsType::getIs_del, G.ISDEL_NO).last("limit " + size));
            for (AssetsType assetsType : assetsTypes) {
                assetsTypeNameList.add(assetsType.getAssets_type_name());
                countList.add("0");
            }
        }
        res.put("x", assetsTypeNameList);
        res.put("y", countList);
        return res;
    }

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：部门采购单趋势
     * @Date ：2023/3/10 下午 5:21
     * @Modified By：
     */
    public List<Map<String, Object>> statCountPurchaseOrderNowYear(Integer deptId) {
        String join = "";
        if (deptId != null) {
            ArrayList<Integer> deptSon = this.getDeptSon(deptId);
            join = StringUtils.join(deptSon, ",");
        }

        List<Map<String, Object>> list = this.statisticalReportMapper.statCountPurchaseOrderNowYear(join);
        return list;
    }

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：上一年十二个月部门采购次数
     * @Date ：2023/3/10 下午 5:21
     * @Modified By：
     */
    public List<Map<String, Object>> statCountPurchaseOrderPreviousYear(Integer deptId) {
        String join = "";
        if (deptId != null) {
            ArrayList<Integer> deptSon = this.getDeptSon(deptId);
            join = StringUtils.join(deptSon, ",");
        }
        List<Map<String, Object>> list = this.statisticalReportMapper.statCountPurchaseOrderPreviousYear(join);
        return list;
    }

    /**
     * 4.采购排行：柱状图展示采购量TOP5的耗材（按品类）；
     */
    public JSONObject getPurchaseTop5ByCategory(Integer year) {
        JSONObject jsonObject = new JSONObject();


        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }

        List<ConsDataVo> consDataVos = statisticalReportMapper.getTop5ByCategory(startDay, endDay);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        consDataVos.forEach(x -> {
            xData.add(x.getName());
            yData.add(x.getAmount().toString());
        });
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    public PageInfo purchaseFromSupplierList(Integer pageIndex, Integer pageSize, String supplierName, Integer year) {
        PurchaseApply beanParam = new PurchaseApply();
        if (net.hasor.utils.StringUtils.isNotBlank(supplierName)) {
            List<Supplier> list = supplierMapper.selectList(Wrappers.<Supplier>lambdaQuery()
                    .eq(Supplier::getSupplier_name, supplierName)
                    .eq(Supplier::getIs_del, "0")
            );
            if (!CollectionUtils.isEmpty(list)) {
                beanParam.setSupplier_id(list.get(0).getId());
            }
        }

        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }

        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<PurchaseApply> purchaseApplies = purchaseApplyMapper.selectList(Wrappers.<PurchaseApply>lambdaQuery()
                .eq(PurchaseApply::getIs_del, "0")
                .eq(null != beanParam.getSupplier_id(), PurchaseApply::getSupplier_id, beanParam.getSupplier_id())
                .eq(PurchaseApply::getApprove_status, "4")
                .ge(!org.springframework.util.StringUtils.isEmpty(startDay), PurchaseApply::getCreate_time, startDay)
                .lt(!org.springframework.util.StringUtils.isEmpty(endDay), PurchaseApply::getCreate_time, endDay)

        );
        for (PurchaseApply purchaseApply : purchaseApplies) {
            SysUser sysUser = sysUserService.selectById(purchaseApply.getApply_user());
            if (null != sysUser) {
                purchaseApply.setApply_user_name(sysUser.getUser_name());
            }
            Supplier supplier = supplierService.selectById(purchaseApply.getSupplier_id());
            if (null != supplier) {
                purchaseApply.setSupplier_name(supplier.getSupplier_name());
            }
        }
        return new PageInfo<>(purchaseApplies);
    }


    public PageInfo<ConsReceive> deptReceiveView(String year, String deptName, Integer page, Integer limit) {
        SysDepartment sysDepartment = sysDepartmentMapper
                .selectOne(Wrappers.<SysDepartment>lambdaQuery()
                        .eq(SysDepartment::getDept_name, deptName)
                        .eq(SysDepartment::getIs_del, 0)
                        .eq(SysDepartment::getParent_dept, 0));

        List<Integer> deptIds = getDeptSon(sysDepartment.getId());


        PageHelper.startPage(page, limit);

        QueryWrapper<ConsReceive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.eq("status", "6");
        queryWrapper.eq("is_finish", "1");
        queryWrapper.in("department_id", deptIds);
        if (StringUtils.isNotEmpty(year)) {
            queryWrapper.between("apply_date", String.format("%s-01-01 00:00:00", year), String.format("%s-12-31 23:59:59", year));
        }
        queryWrapper.orderByDesc("apply_date");


        List<ConsReceive> list = consReceiveService.selectList(queryWrapper);

        for (ConsReceive temp : list) {
            Integer departId = temp.getDepartment_id();
            if (null != departId) {
                SysDepartment department = sysDepartmentMapper.selectById(departId);
                if (null != department) {
                    temp.setDepartment_name(department.getDept_name());
                }
            }
            Integer useId = temp.getUse_id();
            if (null != useId) {
                SysUser sysUser = sysUserService.selectById(useId);
                if (null != sysUser) {
                    temp.setUse_name(sysUser.getUser_name());
                }
            }
            if (null == temp.getOutbound_code()) {
                temp.setOutbound_code("暂无数据");
            }
        }
        PageInfo<ConsReceive> pageInfo = new PageInfo<>(list);


        return pageInfo;

    }

    @Autowired
    private ConsumableMapper consumableMapper;

    public PageInfo purchaseRanking(String category_name, Integer year, Integer page, Integer limit) {
        // 根据名称得到id
        List<Consumable> categories = consumableMapper.selectList(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getConsumable_name, category_name).eq(Consumable::getIs_del, "0"));
        if (com.alibaba.excel.util.CollectionUtils.isEmpty(categories)) {
            return new PageInfo();
        }

        Integer categoryId = categories.get(0).getId();

        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }

        // 根据品类id得到耗材详情
        PageHelper.startPage(page, limit);
        List<ConsPurchaseApplyVo> consPurchaseApplyVos = statisticalReportMapper.purchaseRanking(categoryId, startDay, endDay);
        return new PageInfo<>(consPurchaseApplyVos);
    }

    public PageInfo deptConsumingTotalDetails(String name, Integer year, Integer page, Integer limit) {
        QueryWrapper qw = new QueryWrapper<>();
        qw.eq("dept_name", name);
        SysDepartment sysDepartment = sysDepartmentMapper.selectOne(qw);
        ArrayList<Integer> deptSon = getDeptSon(sysDepartment.getId());
        String join = StringUtils.join(deptSon, ",");

        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }
        // 部门id得到耗材详情
        PageHelper.startPage(page, limit);
        List<ConsReceive> consReceives = statisticalReportMapper.deptConsumingTotalDetails(join, startDay, endDay);

        for (ConsReceive temp : consReceives) {
            Integer departId = temp.getDepartment_id();
            if (null != departId) {
                SysDepartment department = sysDepartmentMapper.selectById(departId);
                if (null != department) {
                    temp.setDepartment_name(department.getDept_name());
                }
            }
        }
        PageInfo<ConsReceive> pageInfo = new PageInfo<>(consReceives);
        return pageInfo;
    }

    public Object checkoutSum() {

        JSONObject jsonObject = new JSONObject();
        //申领出库总数
        QueryWrapper<ConsReceive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        queryWrapper.in("status", Arrays.asList("6"));
        List<ConsReceive> list = consReceiveMapper.selectList(queryWrapper);
        int sum = 0;
        if (null != list) {
            for (ConsReceive c : list) {
                if ("6".equals(c.getStatus())) {
                    sum = sum + c.getOut_num();
                }
            }
        }
        jsonObject.put("outNum", sum);
        //手动出库总数
        QueryWrapper<ConsHandwork> queryWrapperWork = new QueryWrapper<>();
        queryWrapperWork.eq("is_del", "0");
        queryWrapperWork.orderByDesc("create_time");
        List<ConsHandwork> listWork = consHandworkMapper.selectList(queryWrapperWork);
        int sumWork = 0;
        if (null != listWork) {
            for (ConsHandwork c : listWork) {
                if ("2".equals(c.getIs_finish()) && null != c.getNum()) {
                    sumWork = sumWork + c.getNum();
                }
            }
        }
        jsonObject.put("handworkNum", sumWork);
        return jsonObject;
    }

    /**
     * 采购金额排行TOP5 - 详情 @cz
     *
     * @param pageIndex 页码
     * @param pageSize  页数
     * @param typeName  查询参数
     * @return 分页结果集
     */
    public Object purchaseTopDetail(Integer pageIndex, Integer pageSize, String typeName, Integer year) {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(Calendar.YEAR); //当前年份
        String yearFirstStr = ""; //筛选开始时间
        String yearLastStr = "";//筛选结束时间
        if (year == i) {
            //当前年判断
            yearFirstStr = DateTimeUtils.getYearFirstStr(year);
            yearLastStr = DateTimeUtils.formatDateChoseSimple(new Date(), DateTimeUtils.DEFAULT_FORMAT_DATA_TIME);
        } else if (year != -1) {
            //往年判断
            yearFirstStr = DateTimeUtils.getYearFirstStr(year);
            yearLastStr = DateTimeUtils.getCurrYearLast(year);
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<Map> maps = statisticalReportMapper.purchaseTopDetail(typeName, yearFirstStr, yearLastStr);
        return new PageInfo<>(maps);
    }

    /**
     * 资产领用排行TOP10详情 @cz
     *
     * @param pageIndex 页码
     * @param pageSize  页数
     * @param typeName  查询参数
     * @return 结果集
     */
    public Object assetCollectionTopDetail(Integer pageIndex, Integer pageSize, String typeName, String osId) {
        PageHelper.startPage(pageIndex, pageSize);
        //处理请求参数
        List<String> str = new ArrayList<>();
        if (!org.springframework.util.StringUtils.isEmpty(osId)) {
            str = this.getDeptSon(Integer.valueOf(osId)).stream().map(String::valueOf).collect(Collectors.toList());
        }
        List<Map> maps = statisticalReportMapper.assetCollectionTopDetail(typeName, String.join(",", str));
        return new PageInfo<>(maps);
    }

    /**
     * @Description: 部门资产总值详情
     * @param: [name]
     * @return: java.util.List<com.jsdc.rfid.model.AssetsManage>
     * @author: csx
     * @date: 2023/3/15
     * @time: 10:13
     */
    public PageInfo totalAssetsDetails(Integer index,Integer size, String name) {
        PageHelper.startPage(index, size);
        List<AssetsManage> list=statisticalReportMapper.totalAssetsDetails(name);
        return new PageInfo<>(list);
    }

    /**
     * @Description: 统计未打印标签资产详情
     * @param: [index, size, name, deptId]
     * @return: com.github.pagehelper.PageInfo
     * @author: csx
     * @date: 2023/3/15
     * @time: 10:34
     */
    public PageInfo unprintedAssetDetails(Integer index,Integer size, String name,Integer deptId){
        List<String> str = new ArrayList<>();
        if (!org.springframework.util.StringUtils.isEmpty(deptId)) {
            str = this.getDeptSon(deptId).stream().map(String::valueOf).collect(Collectors.toList());
        }
        PageHelper.startPage(index, size);
        List<AssetsManage> list=statisticalReportMapper.unprintedAssetDetails(name,String.join(",", str));
        return new PageInfo(list);
    }

    public PageInfo getListByAssetsTypeName(Integer pageIndex, Integer pageSize, String assetTypeName, Integer year) {
        String startDay = "";
        String endDay = "";
        if (null != year && year > 0) {
            startDay = DateTimeUtils.getYearFirstStr(year);
            endDay = DateTimeUtils.getCurrYearLast(year);
        }
        PageHelper.startPage(pageIndex, pageSize);
        List<InventoryManagement> inventoryManagementList = this.statisticalReportMapper.getListByAssetsTypeName(assetTypeName,startDay,endDay);
        return new PageInfo(inventoryManagementList);
    }

    public List<ConsPurchaseDetail>  toViewTop5(Integer id, Integer categoryId) {
        QueryWrapper<ConsPurchaseDetail> qw = new QueryWrapper<>();
        qw.eq("purchase_apply_id",id);
        qw.eq(null != categoryId, "category_id", categoryId);
        List<ConsPurchaseDetail> list = consPurchaseDetailMapper.selectList(qw);
        list.forEach(x->{
            x.setPurchase_apply_name(consPurchaseApplyMapper.selectById(x.getPurchase_apply_id()).getPurchase_name());
            x.setCategory_name(consCategoryMapper.selectById(x.getCategory_id()).getName());
            x.setSpecification_name(consSpecificationMapper.selectById(x.getSpecification_id()).getTypename());
        });

        return list;
    }
}
