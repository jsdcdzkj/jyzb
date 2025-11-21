package com.jsdc.rfid.mapper.warehouse;

import com.jsdc.rfid.dao.warehouse.StatisticsWarehouseDao;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.model.warehouse.WarehousingStockCarryOver;
import com.jsdc.rfid.model.warehouse.WarehousingStockDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface StatisticsWarehouseMapper {

    @SelectProvider(method = "equipNumStatistics", type = StatisticsWarehouseDao.class)
    List<WarehousingStockDetail> equipNumStatistics(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "expiredEquipNumStatistics", type = StatisticsWarehouseDao.class)
    List<WarehousingStockDetail> expiredEquipNumStatistics(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "criticalEquipNumStatistics", type = StatisticsWarehouseDao.class)
    List<WarehousingStockDetail> criticalEquipNumStatistics(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "equipTypeStatistics", type = StatisticsWarehouseDao.class)
    List<WarehousingStockDetail> equipTypeStatistics(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "equipDeptStatistics", type = StatisticsWarehouseDao.class)
    List<WarehousingStockDetail> equipDeptStatistics(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "carryOverStatistics", type = StatisticsWarehouseDao.class)
    List<WarehousingStockCarryOver> carryOverStatistics(WarehousingStockDto warehousingStockDto);
}
