package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ConsPurchaseDetailDao;
import com.jsdc.rfid.model.ConsPurchaseDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.ConsDataVo;

import java.util.List;

@Mapper
public interface ConsPurchaseDetailMapper extends BaseMapper<ConsPurchaseDetail> {

    /**
     * 采购趋势：折线图展示近六个月的采购趋势（采购单数、耗材总数）
     * 耗材总数
     *
     * @Author thr
     */
    @SelectProvider(type = ConsPurchaseDetailDao.class, method = "getSixMonthCount")
    List<ConsDataVo> getSixMonthCount(ConsPurchaseDetail bean);

    /**
     * 采购排行：柱状图展示采购量TOP5的耗材（按品类）；
     *
     * @Author thr
     */
    @SelectProvider(type = ConsPurchaseDetailDao.class, method = "getTop5ByCategory")
    List<ConsDataVo> getTop5ByCategory(ConsPurchaseDetail bean);

}
