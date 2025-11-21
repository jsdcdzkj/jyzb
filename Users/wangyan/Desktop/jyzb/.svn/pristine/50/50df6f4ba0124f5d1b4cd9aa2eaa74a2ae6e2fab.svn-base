package com.jsdc.rfid.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.mapper.SysDepartmentMapper;
import com.jsdc.rfid.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.ConsDataVo;
import vo.DataChartVo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@SuppressWarnings("ALL")
public class SjfxService {

    @Autowired
    private AssetsManageService assetsManageService;
    @Autowired
    private ConsHandworkService consHandworkService;
    @Autowired
    private ConsInventoryManagementService consInventoryManagementService;
    @Autowired
    private ConsPurchaseApplyService consPurchaseApplyService;
    @Autowired
    private ConsPurchaseDetailService consPurchaseDetailService;
    @Autowired
    private ConsReceiveService consReceiveService;
    @Autowired
    private ConsReceiveAssetsService consReceiveAssetsService;
    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;

    /**
     * 统计报表
     * 按分类统计
     * thr
     */
    public JSONObject classificationData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.classificationData(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    /**
     * 统计报表
     * 按部门统计统计
     * thr
     */
    public JSONObject departmentData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.departmentData(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    /**
     * 统计报表
     * 按品牌统计统计
     * thr
     */
    public JSONObject brandData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.brandData(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    /**
     * 统计报表
     * 按购置时间统计
     * thr
     */
    public JSONObject purchaseTimeData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.purchaseTimeData(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    /**
     * 统计报表
     * "按外携状态统计:
     * 未授权外携统计
     * 授权外携统计"
     * thr
     */
    public JSONObject carryStatusData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.carryStatusData(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    /**
     * 统计报表
     * 异常预警趋势
     * 最近6个月的报警数量
     * thr
     */
    public JSONObject alarmsNumsData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.alarmsNumsData(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    /**
     * 统计报表
     * 正常外携 近6个月
     * thr
     */
    public JSONObject carryNumsData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.carryNumsData(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    /**
     * 统计报表
     * 基础信息：资产总数（饼状图，库内、库外 可按品类统计，默认统计全部品类）
     * thr
     */
    public JSONArray assetsRegisterTypeData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.assetsRegisterTypeData(vo);
        JSONArray result = new JSONArray();
        List<String> types = new ArrayList<>();
        list.forEach(x -> {
            JSONObject jo = new JSONObject();
            jo.put("name", "0".equals(x.getName()) ? "库外登记" : "库内登记");
            jo.put("value", Integer.parseInt(x.getAmount()));
            result.add(jo);
            types.add(x.getName());
        });
        if(!types.contains("0")){
            JSONObject jo = new JSONObject();
            jo.put("name", "库外登记");
            jo.put("value", 0);
            result.add(jo);
        }
        if(!types.contains("1")){
            JSONObject jo = new JSONObject();
            jo.put("name", "库内登记");
            jo.put("value", 0);
            result.add(jo);
        }
        return result;
    }

    /**
     * 统计报表
     * 已登记总数、闲置总数、使用总数、故障总数、异常总数、处置总数
     * thr
     */
    public JSONObject assetsTotalData(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.assetsTotalData(vo);
        //已登记总数、闲置总数、使用总数、故障总数、异常总数、处置总数
        int totalCount = 0;
        for (DataChartVo vo1 : list) {
            totalCount += Integer.parseInt(vo1.getAmount());
            //资产状态 0"闲置",1"使用",2"领用",3"变更",4"调拨",5"故障",6"处置",7"异常"
            if (vo1.getName().equals("0")) {
                jsonObject.put("idleData", vo1.getAmount());
            } else if (vo1.getName().equals("1")) {
                jsonObject.put("useData", vo1.getAmount());
            } else if (vo1.getName().equals("2")) {
//                jsonObject.put("totalData", vo1.getAmount());
            } else if (vo1.getName().equals("3")) {
//                jsonObject.put("totalData", vo1.getAmount());
            } else if (vo1.getName().equals("4")) {
//                jsonObject.put("totalData", vo1.getAmount());
            } else if (vo1.getName().equals("5")) {
                jsonObject.put("faultData", vo1.getAmount());
            } else if (vo1.getName().equals("6")) {
                jsonObject.put("handleData", vo1.getAmount());
            } else if (vo1.getName().equals("7")) {
                jsonObject.put("abnormalData", vo1.getAmount());
            }
        }
        jsonObject.put("totalData", totalCount);
        return jsonObject;
    }


    /**
     * 统计报表
     * 资产位置变动排名TOP10 （柱状图，按变动次数）
     * thr
     */
    public JSONObject positionChangeTop10Data(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.positionChangeTop10Data(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        return jsonObject;
    }

    /**
     * 统计报表
     * 资产报修TOP10
     * thr
     */
    public JSONObject assetRepairTop10Data(DataChartVo vo) {
        JSONObject jsonObject = new JSONObject();
        List<DataChartVo> list = assetsManageService.assetRepairTop10Data(vo);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        //资产品类id集合
        List<Integer> assetTypeIdData = new ArrayList<>();
        for (DataChartVo vo1 : list) {
            xData.add(vo1.getName());
            yData.add(vo1.getAmount());
            assetTypeIdData.add(vo1.getAsset_type_id());
        }
        jsonObject.put("xData", xData);
        jsonObject.put("yData", yData);
        jsonObject.put("assetTypeIdData", assetTypeIdData);
        return jsonObject;
    }

    /**
     * 统计报表
     * 资产报修TOP10（柱状图，按品类；点击按资产展示报修次数信息）
     * thr
     */
    public PageInfo assetRepairCountData(DataChartVo vo, Integer pageIndex, Integer pageSize) {
        JSONObject jsonObject = new JSONObject();
        PageHelper.startPage(pageIndex, pageSize);
        List<DataChartVo> list = assetsManageService.assetRepairCountData(vo);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    //---------------------------------------------耗材统计start-------------------------------------------------

    /**
     * 1.基础信息：耗材库存总数、耗材出库总数、申领出库总数、手动出库总数；
     */
    public JSONObject getConsInfo() {
        JSONObject jsonObject = new JSONObject();
        //耗材库存总数
        ConsInventoryManagement consInventoryManagement = consInventoryManagementService.getTotalCount(null);
        jsonObject.put("inventoryNum", consInventoryManagement.getInventory_num());
        //耗材出库总数、申领出库总数
        ConsReceive consReceive = consReceiveService.getTotalCount(null);
        jsonObject.put("applyNum", consReceive.getApply_num());
        jsonObject.put("outNum", consReceive.getOut_num());
        //手动出库总数
        ConsHandwork consHandwork = consHandworkService.getTotalCount(null);
        jsonObject.put("handworkNum", consHandwork.getNum());
        return jsonObject;
    }

    /**
     * 2.采购趋势：折线图展示近六个月的采购趋势（采购单数、耗材总数）；
     */
    public JSONObject getPurchaseSixMonthCount() {
        JSONObject jsonObject = new JSONObject();
        List<ConsDataVo> consDataVos = consPurchaseApplyService.getSixMonthCount(null);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        List<String> yData2 = new ArrayList<>();
        consDataVos.forEach(x -> {
            xData.add(x.getTime());
            yData.add(x.getAmount().toString());
        });
        List<ConsDataVo> consDataVos2 = consPurchaseDetailService.getSixMonthCount(null);
        JSONArray result2 = new JSONArray();
        consDataVos2.forEach(x -> {
            yData2.add(x.getAmount().toString());
        });
        //日期
        jsonObject.put("xData", xData);
        //采购单数
        jsonObject.put("countData", yData);
        //耗材总数
        jsonObject.put("numData", yData2);
        return jsonObject;
    }

    /**
     * 3.领用趋势：折线图展示近六个月的领用趋势（领用单数、耗材领用总数）；
     */
    public JSONObject getReceiveSixMonthCount() {
        JSONObject jsonObject = new JSONObject();
        List<ConsDataVo> consDataVos = consReceiveService.getSixMonthCount(null);
        List<String> xData = new ArrayList<>();
        List<String> yData = new ArrayList<>();
        List<String> yData2 = new ArrayList<>();
        consDataVos.forEach(x -> {
            xData.add(x.getTime());
            yData.add(x.getAmount().toString());
            yData2.add(x.getApply_num().toString());
        });
        jsonObject.put("xData", xData);
        //领用单数
        jsonObject.put("countData", yData);
        //耗材领用总数
        jsonObject.put("numData", yData2);
        return jsonObject;
    }

    /**
     * 4.采购排行：柱状图展示采购量TOP5的耗材（按品类）；
     */
    public JSONObject getPurchaseTop5ByCategory() {
        JSONObject jsonObject = new JSONObject();
        List<ConsDataVo> consDataVos = consPurchaseDetailService.getTop5ByCategory(null);
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

    /**
     * 5.领用排行：柱状图展示领用量TOP5的耗材（按品类）；
     */
    public JSONObject getReceiveTop5ByCategory() {
        JSONObject jsonObject = new JSONObject();
        List<ConsDataVo> consDataVos = consReceiveAssetsService.getTop5ByCategory(null);
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

    /**
     * 6.部门领用占比：饼状图展示各部门耗材领用占比；
     */
    public JSONArray getCountByDept() {
        JSONObject jsonObject = new JSONObject();
        List<ConsDataVo> consDataVos = consReceiveAssetsService.getCountByDept(null);



        // consDataVos. 如果parent_id存在，就是子节点，否则是父节点,子节点的amount 累加到父节点上
        List<ConsDataVo> newConsDataVos = new ArrayList<>();
        // 剩余
        List<ConsDataVo> surplusConsDataVos = new ArrayList<>();
        for (ConsDataVo consDataVo : consDataVos) {
//            if (consDataVo.getName().equals("江苏鼎驰电子技术有限公司")) {
//                continue;
//            }
            if (consDataVo.getParent_id() == null || consDataVo.getParent_id() == 0) {
                newConsDataVos.add(consDataVo);
            }else {
                surplusConsDataVos.add(consDataVo);
            }
        }
        for(ConsDataVo consDataVo : newConsDataVos){
            int amount = consDataVo.getAmount();
            List<ConsDataVo> children = new ArrayList<>();
            for(ConsDataVo surplusConsDataVo : surplusConsDataVos){
                //找到 parent 的最上级父节点
                surplusConsDataVo.setParent_id(getParentId(surplusConsDataVo.getId()));
//                SysDepartment parent = sysDepartmentMapper.selectById(surplusConsDataVo.getParent_id());
//                if(null != parent && parent.getParent_dept() != null && parent.getParent_dept() != 0){
//                    surplusConsDataVo.setParent_id(parent.getParent_dept());
//                }
                if(consDataVo.getId().equals(surplusConsDataVo.getParent_id())){
                    amount += surplusConsDataVo.getAmount();
                }else{
                    children.add(surplusConsDataVo);
                }
            }
            consDataVo.setAmount(amount);
            surplusConsDataVos = children;
        }
        if (surplusConsDataVos.size() > 0) {
            for (ConsDataVo dataVo : newConsDataVos) {
                if (dataVo.getName().contains("其他")){
                    for (ConsDataVo surplusConsDataVo : surplusConsDataVos) {
                        dataVo.setAmount(dataVo.getAmount() + surplusConsDataVo.getAmount());
                    }
                }
            }
        }
        JSONArray result = new JSONArray();

        newConsDataVos.forEach(x -> {
            JSONObject jo = new JSONObject();
            jo.put("id", x.getId());
            jo.put("name", x.getName());
            jo.put("value", x.getAmount().toString());
            result.add(jo);
        });
        return result;
    }

    private Integer getParentId(Integer id){
        SysDepartment parent = sysDepartmentMapper.selectById(id);
        if(null != parent && parent.getParent_dept() != null && parent.getParent_dept() != 0){
            return getParentId(parent.getParent_dept());
        }else{
            return parent.getId();
        }
    }

    /**
     * 7.消耗速率列表：以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     */
    public PageInfo getConsList(ConsDataVo vo, Integer pageIndex, Integer pageSize) {
        JSONObject jsonObject = new JSONObject();
        List<ConsDataVo> list = new ArrayList<>();

        //当前库存
        List<ConsDataVo> consDataVos = consInventoryManagementService.getInventoryNum(null);
        //领用量
        List<ConsDataVo> consDataVos2 = consReceiveAssetsService.getApplyNum(null);
        //系统运行天数
        LambdaUpdateWrapper<SysLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.orderByAsc(SysLog::getId);
        wrapper.last("limit 1");
        SysLog sysLog = sysLogService.selectOne(wrapper);
        //日期差
        long betweenDay = DateUtil.between(sysLog.getOperate_time(), new Date(), DateUnit.DAY);

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

        List<ConsDataVo> lists = list.stream().skip((pageIndex - 1)*pageSize).limit(pageSize).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(lists);
        pageInfo.setTotal(list.size());
        return pageInfo;
    }

    //---------------------------------------------耗材统计end-------------------------------------------------

}
