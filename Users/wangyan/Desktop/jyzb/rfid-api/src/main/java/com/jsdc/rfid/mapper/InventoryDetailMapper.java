package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.InventoryDetailDao;
import com.jsdc.rfid.model.InventoryDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.InventoryJobVo;

import java.util.List;
import java.util.Map;

@Mapper
public interface InventoryDetailMapper extends BaseMapper<InventoryDetail> {


    @SelectProvider(type = InventoryDetailDao.class,method = "getInventoryDetail")
    List<InventoryDetail> getInventoryDetail(InventoryJobVo inventoryJobVo);

    @SelectProvider(type = InventoryDetailDao.class,method = "getInventoryDetailStatistics")
    List<Map<String, Object>> getInventoryDetailStatistics(InventoryJobVo inventoryJobVo);

    @SelectProvider(type = InventoryDetailDao.class,method = "getInventoryDept")
    List<Map<String, Object>> getInventoryDept(InventoryJobVo inventoryJobVo);

    @SelectProvider(type = InventoryDetailDao.class,method = "getInventoryDeptDetail")
    List<Map<String, Object>> getInventoryDeptDetail(InventoryJobVo inventoryJobVo);
}
