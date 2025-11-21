package com.jsdc.rfid.mapper.statisticalreport;

import com.jsdc.rfid.dao.AssetsManageDao;
import com.jsdc.rfid.dao.statisticalreport.StatisticalReportDao;
import com.jsdc.rfid.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import vo.ConsDataVo;
import vo.ConsPurchaseApplyVo;
import vo.PurchaseApplyVo;
import vo.StatisticsAssetStateVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Mapper
public interface StatisticalReportMapper {

    /**
     * 资产领用排行TOP10详情 @cz
     */
    @SelectProvider(type = StatisticalReportDao.class, method = "assetCollectionTopDetail")
    List<Map> assetCollectionTopDetail(String typeName,String osIds);

    /**
     * 采购全额排行详情 @cz
     */
    @SelectProvider(type = StatisticalReportDao.class, method = "purchaseTopDetail")
    List<Map> purchaseTopDetail(String typeName,String startTime,String endTime);

    /**
     * 采购总值 @cz
     */
    @SelectProvider(type = StatisticalReportDao.class, method = "censusPriceSum")
    Object censusPriceSum();

    /**
     * 耗材库存总数 @cz
     */
    @SelectProvider(method = "getTotalSum", type = StatisticalReportDao.class)
    Object getTotalSum();

    /**
     * 获取出库总值 @cz
     */
    @SelectProvider(method = "getOutSum", type = StatisticalReportDao.class)
    Object getOutSum();

    /**
     * 采购全额排行(TOP5) @cz
     */
    @SelectProvider(type = StatisticalReportDao.class, method = "purchaseTop")
    List<Map> purchaseTop(String startTime,String endTime);

    /**
     * 获取子部门外携排行 @cz
     */
    @SelectProvider(type = StatisticalReportDao.class, method = "censusAllCarryCountByDept")
    List<Map> censusAllCarryCountByDept();

    /**
     * 异常外携统计 @cz
     */
    @SelectProvider(method = "censusTopOfAbnormalCarryByAssetsType", type = StatisticalReportDao.class)
    List<Map> censusTopOfAbnormalCarryByAssetsType();

    /**
     * 正常外携统计 @cz
     */
    @SelectProvider(method = "censusTopOfNormalCarryByAssetsType", type = StatisticalReportDao.class)
    List<Map> censusTopOfNormalCarryByAssetsType();

    /**dd
     * @Description: 统计资产总值
     * @param: []
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:35
     */
    @SelectProvider(method = "totalAssets",type = StatisticalReportDao.class)
     List<Map<String,Object>> totalAssets();

    /**
     * @Description: 统计未打印标签资产
     * @param: []
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:42
     */
    @SelectProvider(method = "unprintedAsset",type = StatisticalReportDao.class)
    List<Map<String,Object>> unprintedAsset(String deptId);


    /**
     * @Description: 领用
     * @param: []
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:48
     */
    @SelectProvider(method = "receive",type = StatisticalReportDao.class)
    List<Map<String,Object>> receive(String deptId,List<String> times);

    /**
     * @Description: 各部门资产数量
     * @param: [deptId, times]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author: csx
     * @date: 2023/3/10
     * @time: 16:02
     */
    @SelectProvider(method = "assetsManage",type = StatisticalReportDao.class)
    List<Map<String,Object>> assetsManage(String deptId,List<String> times);

    /**
     * @Description: 采购
     * @param: []
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author: csx
     * @date: 2023/3/8
     * @time: 17:11
     */
    @SelectProvider(method = "purchase",type = StatisticalReportDao.class)
    List<Map<String,Object>> purchase(String deptId, List<String> times);

    /**
     *供应商采购排行(TOP10)
     * @return
     */
    @SelectProvider(method = "supplierProcurementRankingMoney",type = StatisticalReportDao.class)
    List<Map<String,Object>> supplierProcurementRankingMoney();

    /**
     * 供应商采购排行(TOP10)
     */
    @SelectProvider(method = "supplierProcurementRankingApply",type = StatisticalReportDao.class)
    List<Map<String,Object>> supplierProcurementRankingApply(String startDay, String endDay);


    /**
    * 采购金额排行<TOP5>
    */
    @SelectProvider(method = "purchaseAmountRanking",type = StatisticalReportDao.class)
    List<Map<String,Object>> purchaseAmountRanking();


    /**
     * 最新十二个月 部门采购单趋势
     */
    @SelectProvider(method = "getProcurementTrendNow",type = StatisticalReportDao.class)
    List<Map<String,Object>> getProcurementTrendNow(String join );

    /**
     * 同比十二个月 部门采购单趋势
     */
    @SelectProvider(method = "getProcurementTrendOnce",type = StatisticalReportDao.class)
    List<Map<String,Object>> getProcurementTrendOnce(String join );


    /**
     *  最近12个月（包含当月）审批通过的资产处置金额趋势
     *  时间为处置时间
     *  主表状态为审批通过
     *  子表状态为已处置
     * @return
     */
    @SelectProvider(method = "managementMonth",type = StatisticalReportDao.class)
    List<Map<String, Object>> managementMonth(String year);

    /**
     *   最近12个月（包含当月）审批通过,已验收的资产采购金额趋势
     *   时间为验收时间
     *   主表状态：审批通过，已验收
     * @return
     */
    @SelectProvider(method = "purchaseMonth",type = StatisticalReportDao.class)
    List<Map<String, Object>> purchaseMonth(String year);

    /**
     *  资产采购，处置有数据年份
     * @return
     */
    @SelectProvider(method = "getManageOrPurchaseYears",type = StatisticalReportDao.class)
    List<String> getManageOrPurchaseYears();

    /**
     *  最近12个月报修数量
     *  时间 创建时间
     * @return
     */
    @SelectProvider(method = "applySingleCount",type = StatisticalReportDao.class)
    List<Map<String, Object>> applySingleCount(String year);

    /**
     *  最近12个月报修处理数量
     *  时间 报修处理结束时间
     * @return
     */
    @SelectProvider(method = "applySingleFeebackCount",type = StatisticalReportDao.class)
    List<Map<String, Object>> applySingleFeebackCount(String year);

    /**
     *  报修，处理 有数据的年份
     * @return
     */
    @SelectProvider(method = "getApplyOrFeebackYears",type = StatisticalReportDao.class)
    List<String> getApplyOrFeebackYears();

    /**
     *  按月 统计异常资产外携数量
     * @return
     */
    @SelectProvider(method = "carryAbnormalCount",type = StatisticalReportDao.class)
    List<Map<String, Object>> carryAbnormalCount(String year);

    /**
     *  按月 统计正常资产外携数量
     * @param year
     * @return
     */
    @SelectProvider(method = "carryManageCount",type = StatisticalReportDao.class)
    List<Map<String, Object>> carryManageCount(String year);

    /**
     *  正常，异常外携数据年份
     * @param
     * @return
     */
    @SelectProvider(method = "getCarryManageYears",type = StatisticalReportDao.class)
    List<String> getCarryManageYears();
    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：以资产类型分类,统计库存数量 TOP10
     * @Date ：2023/3/8 下午 4:33
     * @Modified By：
     */
    @SelectProvider(method = "statTop10InventoryNumByTypeName",type = StatisticalReportDao.class)
    List<Map<String, Object>> statTop10InventoryNumByTypeName(String startDay , String endDay);

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description： 1.库存总价值
     * @Date ：2023/3/9 上午 9:03
     * @Modified By：
     */
    @SelectProvider(method = "statInventoryTotalAssert",type = StatisticalReportDao.class)
    Map<String, Object> statInventoryTotalAssert();


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：库存按品类计算总价值
     * @Date ：2023/3/9 上午 9:39
     * @Modified By：
     */
    @SelectProvider(method = "statInventoryAssertByType",type = StatisticalReportDao.class)
    List<Map<String, Object>> statInventoryAssertByType();


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：资产状态数量
     * @Date ：2023/3/9 上午 11:12
     * @Modified By：
     */
    @SelectProvider(method = "statAssertNumByType",type = StatisticalReportDao.class)
    List<StatisticsAssetStateVo> statAssertNumByType(Integer assertTypeId);


    /**
     * 部门领用总值排行
     * @param startDay
     * @param endDay
     * @return
     */
    @SelectProvider(method = "receiveTotalValue",type = StatisticalReportDao.class)
    List<Map<String, Object>> receiveTotalValue(String startDay, String endDay);

    /**
     * 耗材领用排行TOP10
     * @param deptIds
     * @return
     */
    @SelectProvider(method = "consumableReceive",type = StatisticalReportDao.class)
    List<Map<String, Object>> consumableReceive(String deptIds);


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：库存总数量
     * @Date ：2023/3/9 上午 11:12
     * @Modified By：
     */
    @SelectProvider(method = "statInventoryTotalAmount",type = StatisticalReportDao.class)
    Map<String, Object> statInventoryTotalAmount();

    /**
     * 资产领用排行TOP10 @cz
     *
     * @param osIds 部门ids
     * @return 结果集
     */
    @SelectProvider(method = "assetCollectionTop",type = StatisticalReportDao.class)
    List<Map> assetCollectionTop(String osIds);



    /**
     * @Description: 获取自定义几个月时间
     * @param: [times]
     * @return:
     * @author: csx
     * @date: 2023/3/9
     * @time: 19:41
     */
    @SelectProvider(method = "getMonth",type = StatisticalReportDao.class)
    List<String>getMonth(Integer times,Integer year);


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：今天十二个月部门采购次数
     * @Date ：2023/3/10 下午 5:39
     * @Modified By：
     */
    @SelectProvider(method = "statCountPurchaseOrderNowYear",type = StatisticalReportDao.class)
    List<Map<String, Object>> statCountPurchaseOrderNowYear(String deptIdList);

    @SelectProvider(method = "statCountPurchaseOrderPreviousYear",type = StatisticalReportDao.class)
    List<Map<String, Object>> statCountPurchaseOrderPreviousYear(String join);



    @SelectProvider(method = "getInventoryNum", type = StatisticalReportDao.class)
    List<ConsDataVo> getInventoryNum(StatisticalReportDao beanParam);
    @SelectProvider(method = "getApplyNum", type = StatisticalReportDao.class)
    List<ConsDataVo> getApplyNum(String startTime,String endTime);

    @SelectProvider(method = "getTop5ByCategory",type = StatisticalReportDao.class)
    List<ConsDataVo> getTop5ByCategory(String startDay,String endDay);

    /**
     * 部门耗材领用排行
     * 时间：申请时间
     * 申请状态：已完成，出库状态：已完成
     * @param year
     * @return
     */
    @SelectProvider(method = "deptReceiveTop",type = StatisticalReportDao.class)
    List<Map<String, Object>> deptReceiveTop(String year);


    @SelectProvider(method = "deptReceiveView",type = StatisticalReportDao.class)
    List<Map<String, Object>> deptReceiveView(String deptIdStr);

    /**
     * @Description: 部门资产总值详情
     * @param: [name]
     * @return: java.util.List<com.jsdc.rfid.model.AssetsManage>
     * @author: csx
     * @date: 2023/3/15
     * @time: 10:11
     */
    @SelectProvider(method = "totalAssetsDetails",type = StatisticalReportDao.class)
    List<AssetsManage> totalAssetsDetails(String name);

    /**
     * @Description: 统计未打印标签资产详情
     * @param: [name, deptid]
     * @return: java.util.List<com.jsdc.rfid.model.AssetsManage>
     * @author: csx
     * @date: 2023/3/15
     * @time: 10:30
     */
    @SelectProvider(method = "unprintedAssetDetails",type = StatisticalReportDao.class)
    List<AssetsManage> unprintedAssetDetails(String name,String deptId);

    @SelectProvider(method = "purchaseRanking",type = StatisticalReportDao.class)
    List<ConsPurchaseApplyVo>purchaseRanking(Integer categoryId , String startDay , String endDay);

    @SelectProvider(method = "deptConsumingTotalDetails",type = StatisticalReportDao.class)
    List<ConsReceive>deptConsumingTotalDetails(String join , String startDay , String endDay);

    @SelectProvider(method = "getListByAssetsTypeName",type = StatisticalReportDao.class)
    List<InventoryManagement> getListByAssetsTypeName(String assetTypeName, String startDay, String endDay);
}
