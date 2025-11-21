package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.AssetsManage;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.AssetsManageDao;
import org.apache.ibatis.annotations.SelectProvider;
import vo.DataChartVo;
import vo.StatisticsAssetStateVo;

import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Mapper
public interface AssetsManageMapper extends BaseMapper<AssetsManage> {

    @SelectProvider(method = "toList", type = AssetsManageDao.class)
    List<AssetsManage> toList(AssetsManage beanParam);

    /**
     * 统计报表
     * 按分类统计
     * thr
     */
    @SelectProvider(method = "classificationData", type = AssetsManageDao.class)
    List<DataChartVo> classificationData(DataChartVo vo);

    /**
     * 统计报表
     * 按部门统计统计
     * thr
     */
    @SelectProvider(method = "departmentData", type = AssetsManageDao.class)
    List<DataChartVo> departmentData(DataChartVo vo);

    /**
     * 统计报表
     * 按品牌统计统计
     * thr
     */
    @SelectProvider(method = "brandData", type = AssetsManageDao.class)
    List<DataChartVo> brandData(DataChartVo vo);

    /**
     * 统计报表
     * 按购置时间统计
     * thr
     */
    @SelectProvider(method = "purchaseTimeData", type = AssetsManageDao.class)
    List<DataChartVo> purchaseTimeData(DataChartVo vo);

    /**
     * 统计报表
     * "按外携状态统计:
     * 未授权外携统计
     * 授权外携统计"
     * thr
     */
    @SelectProvider(method = "carryStatusData", type = AssetsManageDao.class)
    List<DataChartVo> carryStatusData(DataChartVo vo);

    /**
     * 统计报表
     * 异常预警趋势
     * 最近6个月的报警数量
     * thr
     */
    @SelectProvider(method = "alarmsNumsData", type = AssetsManageDao.class)
    List<DataChartVo> alarmsNumsData(DataChartVo vo);

    /**
     * 统计报表
     * 正常外携 近6个月
     * thr
     */
    @SelectProvider(method = "carryNumsData", type = AssetsManageDao.class)
    List<DataChartVo> carryNumsData(DataChartVo vo);

    /**
     * 统计报表
     * 基础信息：资产总数（饼状图，库内、库外 可按品类统计，默认统计全部品类）
     * thr
     */
    @SelectProvider(method = "assetsRegisterTypeData", type = AssetsManageDao.class)
    List<DataChartVo> assetsRegisterTypeData(DataChartVo vo);

    /**
     * 统计报表
     * 已登记总数、闲置总数、使用总数、故障总数、异常总数、处置总数
     * thr
     */
    @SelectProvider(method = "assetsTotalData", type = AssetsManageDao.class)
    List<DataChartVo> assetsTotalData(DataChartVo vo);

    /**
     * 按资产状态统计资产数量
     * @return
     */
    @SelectProvider(method = "statisticsByAssetsState", type = AssetsManageDao.class)
    List<StatisticsAssetStateVo> statisticsByAssetsState(Integer asstestTypeId);

    /**
     * 资产位置变动排名TOP10 （柱状图，按变动次数）
     * @return
     */
    @SelectProvider(method = "positionChangeTop10Data", type = AssetsManageDao.class)
    List<DataChartVo> positionChangeTop10Data(DataChartVo vo);

    /**
     * 统计报表
     * 资产报修TOP10（柱状图，按品类；点击按资产展示报修次数信息）
     * thr
     */
    @SelectProvider(method = "assetRepairTop10Data", type = AssetsManageDao.class)
    List<DataChartVo> assetRepairTop10Data(DataChartVo vo);

    /**
     * 统计报表
     * 资产报修TOP10（柱状图，按品类；点击按资产展示报修次数信息）
     * thr
     */
    @SelectProvider(method = "assetRepairCountData", type = AssetsManageDao.class)
    List<DataChartVo> assetRepairCountData(DataChartVo vo);
}