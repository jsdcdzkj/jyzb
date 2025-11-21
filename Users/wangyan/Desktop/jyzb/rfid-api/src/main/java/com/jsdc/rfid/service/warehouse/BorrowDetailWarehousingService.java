package com.jsdc.rfid.service.warehouse;

import com.jsdc.rfid.model.warehouse.WarehousingBorrowDetail;

import java.util.List;

public interface BorrowDetailWarehousingService {
    void batchSave(List<WarehousingBorrowDetail> details);
}
