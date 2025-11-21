package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ConsPurchaseApplyDao;
import com.jsdc.rfid.model.ConsPurchaseApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.AccessRecordVo;
import vo.ConsDataVo;
import vo.ConsPurchaseApplyVo;

import java.util.List;

@Mapper
public interface ConsPurchaseApplyMapper extends BaseMapper<ConsPurchaseApply> {

    @SelectProvider(type = ConsPurchaseApplyDao.class, method = "toList")
    List<ConsPurchaseApplyVo> toList(ConsPurchaseApplyVo purchaseApplyVo);

    @SelectProvider(type = ConsPurchaseApplyDao.class, method = "purchaseFromSupplierTo10")
    List<AccessRecordVo> purchaseFromSupplierTo10();

    /**
     * 采购趋势：折线图展示近六个月的采购趋势（采购单数、耗材总数）
     *
     * @Author thr
     */
    @SelectProvider(type = ConsPurchaseApplyDao.class, method = "getSixMonthCount")
    List<ConsDataVo> getSixMonthCount(ConsPurchaseApply bean);
}
