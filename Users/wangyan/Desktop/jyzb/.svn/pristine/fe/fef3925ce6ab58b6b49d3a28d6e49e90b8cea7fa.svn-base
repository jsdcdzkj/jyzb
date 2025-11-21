package com.jsdc.rfid.service.warehouse.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.rfid.mapper.warehouse.WarehouseStockDetailMapper;
import com.jsdc.rfid.model.warehouse.WarehousingStockDetail;
import com.jsdc.rfid.service.warehouse.WarehouseStockDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@AllArgsConstructor
public class WarehouseStockDetailServiceImpl extends ServiceImpl<WarehouseStockDetailMapper, WarehousingStockDetail> implements WarehouseStockDetailService {
    @Override
    public void batchSave(List<WarehousingStockDetail> stockDetails) {
        saveBatch(stockDetails);
    }
}
