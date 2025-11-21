package com.jsdc.rfid.service.warehouse.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsdc.rfid.mapper.warehouse.BorrowDetailWarehousingMapper;
import com.jsdc.rfid.model.warehouse.WarehousingBorrowDetail;
import com.jsdc.rfid.service.warehouse.BorrowDetailWarehousingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BorrowDetailWarehousingServiceImpl extends ServiceImpl<BorrowDetailWarehousingMapper, WarehousingBorrowDetail> implements BorrowDetailWarehousingService {
    @Override
    public void batchSave(List<WarehousingBorrowDetail> details) {
        saveBatch(details);
    }
}
