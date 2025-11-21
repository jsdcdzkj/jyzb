package com.jsdc.rfid.service.warehouse;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.model.warehouse.WarehousingBorrow;
import com.jsdc.rfid.model.warehouse.WarehousingDelivery;
import com.jsdc.rfid.model.warehouse.WarehousingEnter;
import com.jsdc.rfid.model.warehouse.WarehousingStock;
import com.jsdc.rfid.vo.CarryOverVo;
import com.jsdc.rfid.vo.WarehouseStockDetailVo;

public interface WarehouseStockService {

    //库存列表
    PageInfo<WarehousingStock> pageQuery(WarehousingStockDto warehousingStockDto);

    //库存列表
    PageInfo<WarehousingStock> pageQueryAll(Integer pageIndex, Integer pageSize, WarehousingStockDto warehousingStockDto);

    //入库
    void enterStock(WarehousingEnter warehousingEnter, String isExport);

    //出库
    void deliveryStock(WarehousingDelivery warehousingDelivery);

    //装备库存查询
    JSONArray equipList(WarehousingStockDto warehousingStockDto);

    WarehouseStockDetailVo detail(WarehousingStockDto warehousingStockDto);

    PageInfo<WarehousingStock> stockCount(Integer pageIndex, Integer pageSize,WarehousingStockDto warehousingStockDto);

    CarryOverVo carryOverCount(WarehousingStockDto warehousingStockDto);

    void carryOver(WarehousingStockDto warehousingStockDto);

    void apply(WarehousingBorrow warehousingBorrow);

    PageInfo<WarehousingBorrow> applyPageQuery(Integer pageIndex, Integer pageSize, WarehousingBorrow warehousingDelivery);

    WarehousingBorrow applyLendDetail(Integer id);

    WarehousingBorrow applyDel(Integer id);
}
