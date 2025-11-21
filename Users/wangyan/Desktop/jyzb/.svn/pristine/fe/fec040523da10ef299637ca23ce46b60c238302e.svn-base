package com.jsdc.rfid.mapper.warehouse;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.warehouse.WarehouseStockDao;
import com.jsdc.rfid.dao.warehouse.WarehouseStockDetailDao;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.model.warehouse.WarehousingBorrow;
import com.jsdc.rfid.model.warehouse.WarehousingStock;
import com.jsdc.rfid.model.warehouse.WarehousingStockDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface WarehouseStockMapper extends BaseMapper<WarehousingStock> {

    @SelectProvider(method = "equipStock", type = WarehouseStockDao.class)
    List<WarehousingStock> equipStock(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "equipStockAll", type = WarehouseStockDao.class)
    List<WarehousingStock> equipStockAll(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "equipStockWarehouse", type = WarehouseStockDao.class)
    List<WarehousingBorrow> equipStockWarehouse(WarehousingBorrow warehousingBorrow);

    @SelectProvider(method = "stockCount", type = WarehouseStockDao.class)
    List<WarehousingStock> stockCount(WarehousingStockDto warehousingStockDto);
}
