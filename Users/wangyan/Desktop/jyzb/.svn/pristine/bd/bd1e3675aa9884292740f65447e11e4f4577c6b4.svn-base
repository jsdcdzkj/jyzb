package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.PurchaseApplyDao;
import com.jsdc.rfid.model.PurchaseApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.AccessRecordVo;
import vo.PurchaseApplyVo;

import java.util.List;

@Mapper
public interface PurchaseApplyMapper extends BaseMapper<PurchaseApply> {
    @SelectProvider(type = PurchaseApplyDao.class,method = "toList")
    List<PurchaseApplyVo> toList(PurchaseApplyVo purchaseApplyVo);

    @SelectProvider(type = PurchaseApplyDao.class,method = "purchaseFromSupplierTo10")
    List<AccessRecordVo> purchaseFromSupplierTo10();
}
