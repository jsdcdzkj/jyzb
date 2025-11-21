package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.InventoryJobDao;
import com.jsdc.rfid.model.AssetsManage;
import com.jsdc.rfid.model.InventoryDetail;
import com.jsdc.rfid.model.InventoryJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface InventoryJobMapper extends BaseMapper<InventoryJob> {


    @SelectProvider(type = InventoryJobDao.class,method = "toRFID")
    List<InventoryDetail> toRFID(Integer id);

    @SelectProvider(type = InventoryJobDao.class,method = "getAssetsManages")
    List<AssetsManage> getAssetsManages(InventoryJob inventoryJob);

    @SelectProvider(type = InventoryJobDao.class,method = "startRFID")
    List<String> startRFID(InventoryJob inventoryJob);
}
