package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ApplySingleDao;
import com.jsdc.rfid.model.ApplySingle;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.StatisticsRepairVo;

import java.util.List;

/**
 * @author zln
 * @descript 报修申请功能
 * @date 2022-04-24
 */
@Mapper
public interface ApplySingleMapper extends BaseMapper<ApplySingle> {

    //pc未派单分页、手机端分页
    @SelectProvider(type = ApplySingleDao.class, method = "selectPageList")
    List<ApplySingleVo> selectPageList(ApplySingle bean);

    //已派单列表PC
    @SelectProvider(type = ApplySingleDao.class, method = "selectYPDPage")
    List<ApplySingleVo> selectYPDPage(ApplySingle bean);

    //手机端-维修记录
    @SelectProvider(type = ApplySingleDao.class, method = "selectByHistoryList")
    List<ApplySingleVo> selectByHistoryList(ApplySingle bean);

    //申请单状态
    @SelectProvider(type = ApplySingleDao.class, method = "getJqList")
    List<ApplySingleVo> getJqList(ApplySingle bean);

    //服务评价
    @SelectProvider(type = ApplySingleDao.class, method = "selectEvaluationData")
    List<ApplySingleVo> selectEvaluationData(ApplySingle bean);

    //详情
    @SelectProvider(type = ApplySingleDao.class, method = "selectByDetails")
    ApplySingleVo selectByDetails(Integer id);

    //统计维修人员所属申请单数量
    @SelectProvider(type = ApplySingleDao.class, method = "selectApplyCount")
    List<ApplySingleVo> selectApplyCount(ApplySingle bean);

    //导出功能
    @SelectProvider(type = ApplySingleDao.class, method = "repairExport")
    List<ApplySingleVo> repairExport(ApplySingle bean);

    //导出统计
    @SelectProvider(type = ApplySingleDao.class, method = "exportCount")
    List<ApplySingleVo> exportCount(ApplySingle bean);

    @SelectProvider(type = ApplySingleDao.class, method = "statisticsByDept")
    List<StatisticsRepairVo> statisticsByDept();

    /**
     * 报修管理
     * 20230128做变更
     *
     * @param bean
     * @return
     */
    @SelectProvider(type = ApplySingleDao.class, method = "pageList")
    List<ApplySingleVo> pageList(ApplySingle bean);
    /**
     * 待审批列表
     * @param bean
     */
    @SelectProvider(type = ApplySingleDao.class, method = "getApprovedListByPage")
    List<ApplySingleVo> getApprovedListByPage(ApplySingleVo bean);

    /**
     * 获取维修人员待维修 报修单数量
     * @param bean
     * @return
     */
    @SelectProvider(type = ApplySingleDao.class, method = "getRepairList")
    List<ApplySingleVo> getRepairList(ApplySingle bean);

    /**
     * 报修本月总数
     * @param applySingleVoMonth
     * @return
     */
    @SelectProvider(type = ApplySingleDao.class, method = "getRepairMonthList")
    List<ApplySingleVo> getRepairMonthList(ApplySingleVo applySingleVoMonth);
}
