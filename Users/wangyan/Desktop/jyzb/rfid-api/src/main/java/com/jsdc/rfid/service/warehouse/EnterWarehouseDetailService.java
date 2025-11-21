package com.jsdc.rfid.service.warehouse;

import com.jsdc.rfid.model.warehouse.WarehousingEnterDetail;

import java.util.List;

public interface EnterWarehouseDetailService {

    void batchSave(List<WarehousingEnterDetail> details);

}
