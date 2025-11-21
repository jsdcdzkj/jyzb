package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ExternalMaintenanceDao;
import com.jsdc.rfid.model.ExternalMaintenance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ExternalMaintenanceMapper extends BaseMapper<ExternalMaintenance> {
    @SelectProvider(type = ExternalMaintenanceDao.class, method = "getPage")
    List<ExternalMaintenance> getPage(ExternalMaintenance externalMaintenance);
}
