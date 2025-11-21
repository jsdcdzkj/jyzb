package com.jsdc.rfid.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.InventorySurplusDao;
import com.jsdc.rfid.mapper.InventorySurplusMapper;
import com.jsdc.rfid.model.InventorySurplus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * ClassName: InventorySurplusService
 * Description:
 * date: 2022/4/24 17:29
 *
 * @author bn
 */
@Transactional
@Service
@AllArgsConstructor
public class InventorySurplusService extends BaseService<InventorySurplusDao, InventorySurplus> {


    private InventorySurplusMapper inventorySurplusMapper;


    public void toAdd(InventorySurplus inventorySurplus){
        inventorySurplus.setCreate_time(new Date());

        inventorySurplus.insert();
    }
}
