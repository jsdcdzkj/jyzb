package com.jsdc.rfid.service.warehouse.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.rfid.mapper.warehouse.WarehouseStockCarryOverMapper;
import com.jsdc.rfid.model.warehouse.WarehousingStockCarryOver;
import com.jsdc.rfid.service.warehouse.WarehouseStockCarryOverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class WarehouseStockCarryOverServiceImpl extends ServiceImpl<WarehouseStockCarryOverMapper, WarehousingStockCarryOver> implements WarehouseStockCarryOverService {

    @Override
    public void batchSave(List<WarehousingStockCarryOver> carryOvers) {
        saveBatch(carryOvers);
    }

}
