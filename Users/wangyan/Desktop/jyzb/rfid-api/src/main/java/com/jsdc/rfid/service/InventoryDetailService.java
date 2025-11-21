package com.jsdc.rfid.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.InventoryDetailDao;
import com.jsdc.rfid.mapper.InventoryDetailMapper;
import com.jsdc.rfid.model.InventoryDetail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vo.InventoryJobVo;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * ClassName: InventoryDetailService
 * Description:
 * date: 2022/4/24 17:23
 *
 * @author bn
 */
@Transactional
@Service
@AllArgsConstructor
public class InventoryDetailService extends BaseService<InventoryDetailDao,InventoryDetail> {


    private InventoryDetailMapper inventoryDetailMapper;

    private SysUserService sysUserService;

    public void edit(InventoryDetail inventoryDetail) {
        inventoryDetail.setUpdate_time(new Date());
        inventoryDetail.setUpdate_user(sysUserService.getUser().getId());
        inventoryDetail.updateById();
    }

    public void toBatchEdit(List<InventoryDetail> inventoryDetailList) {
        for(InventoryDetail inventoryDetail:inventoryDetailList){
            inventoryDetail.setUpdate_time(new Date());
            inventoryDetail.setUpdate_user(sysUserService.getUser().getId());
            inventoryDetail.updateById();
        }
    }

    public void add(InventoryDetail inventoryDetail){
        inventoryDetail.setCreate_time(new Date());
        inventoryDetail.setCreate_user(sysUserService.getUser().getId());
        inventoryDetail.insert();
    }



}
