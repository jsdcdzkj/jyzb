package com.jsdc.rfid.service.warehouse.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.rfid.mapper.warehouse.EnterWarehouseDetailMapper;
import com.jsdc.rfid.model.warehouse.WarehousingEnterDetail;
import com.jsdc.rfid.service.warehouse.EnterWarehouseDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class EnterWarehouseDetailServiceImpl extends ServiceImpl<EnterWarehouseDetailMapper, WarehousingEnterDetail> implements EnterWarehouseDetailService {


    @Override
    public void batchSave(List<WarehousingEnterDetail> details) {
        saveBatch(details);
    }
}
