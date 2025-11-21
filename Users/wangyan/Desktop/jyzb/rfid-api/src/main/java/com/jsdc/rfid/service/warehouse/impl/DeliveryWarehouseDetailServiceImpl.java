package com.jsdc.rfid.service.warehouse.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.rfid.mapper.warehouse.DeliveryWarehouseDetailMapper;
import com.jsdc.rfid.model.warehouse.WarehousingDeliveryDetail;
import com.jsdc.rfid.service.warehouse.DeliveryWarehouseDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class DeliveryWarehouseDetailServiceImpl extends ServiceImpl<DeliveryWarehouseDetailMapper, WarehousingDeliveryDetail> implements DeliveryWarehouseDetailService {
    @Override
    public void batchSave(List<WarehousingDeliveryDetail> details) {
        saveBatch(details);
    }
}
