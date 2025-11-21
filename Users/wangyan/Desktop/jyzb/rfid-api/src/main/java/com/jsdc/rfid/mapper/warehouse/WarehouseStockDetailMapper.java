package com.jsdc.rfid.mapper.warehouse;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.warehouse.WarehouseStockDao;
import com.jsdc.rfid.dao.warehouse.WarehouseStockDetailDao;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.model.warehouse.WarehousingStock;
import com.jsdc.rfid.model.warehouse.WarehousingStockDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface WarehouseStockDetailMapper extends BaseMapper<WarehousingStockDetail> {

    @SelectProvider(method = "stockList", type = WarehouseStockDetailDao.class)
    List<WarehousingStockDetail> stockList(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "queryLatest", type = WarehouseStockDetailDao.class)
    List<WarehousingStockDetail> queryLatest(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "equipList", type = WarehouseStockDetailDao.class)
    List<WarehousingStockDetail> equipList(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "detailList", type = WarehouseStockDetailDao.class)
    List<WarehousingStockDetail> detailList(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "detailCount", type = WarehouseStockDetailDao.class)
    List<WarehousingStockDetail> detailCount(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "stockCount", type = WarehouseStockDetailDao.class)
    List<WarehousingStockDetail> stockCount(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "disposalCount", type = WarehouseStockDetailDao.class)
    List<WarehousingStockDetail> disposalCount(WarehousingStockDto warehousingStockDto);

    @SelectProvider(method = "stockTypeNum", type = WarehouseStockDetailDao.class)
    List<WarehousingStockDetail> stockTypeNum(WarehousingStockDto warehousingStockDto);
}
