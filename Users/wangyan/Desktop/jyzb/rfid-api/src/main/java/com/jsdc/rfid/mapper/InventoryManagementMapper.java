package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.InventoryManagement;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.InventoryManagementDao;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Mapper
public interface InventoryManagementMapper extends BaseMapper<InventoryManagement> {

    @SelectProvider(method = "toList",type = InventoryManagementDao.class)
    List<InventoryManagement> toList(InventoryManagement beanParam);

    @SelectProvider(method = "getListByAssetsTypeName",type = InventoryManagementDao.class)
    List<InventoryManagement> getListByAssetsTypeName(String assetTypeName , String startDay,String endDay);
}
