package com.jsdc.rfid.mapper.warehouse;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.warehouse.WarehouseStockCarryOverDao;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.model.warehouse.WarehousingStockCarryOver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface WarehouseStockCarryOverMapper extends BaseMapper<WarehousingStockCarryOver> {


    @SelectProvider(method = "carryOverList", type = WarehouseStockCarryOverDao.class)
    List<WarehousingStockCarryOver> carryOverList(WarehousingStockDto warehousingStockDto);

}
