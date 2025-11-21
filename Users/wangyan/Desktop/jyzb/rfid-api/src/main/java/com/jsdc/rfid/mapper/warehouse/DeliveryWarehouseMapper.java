package com.jsdc.rfid.mapper.warehouse;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.warehouse.WarehousingDelivery;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeliveryWarehouseMapper extends BaseMapper<WarehousingDelivery> {
}
