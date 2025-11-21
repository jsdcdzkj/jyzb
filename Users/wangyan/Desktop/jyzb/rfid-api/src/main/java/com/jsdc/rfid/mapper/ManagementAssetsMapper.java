package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ManagementAssetsDao;
import com.jsdc.rfid.dao.ManagementDao;
import com.jsdc.rfid.model.Management;
import com.jsdc.rfid.model.ManagementAssets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import vo.AccessRecordVo;
import vo.StatisticsRepairVo;

import java.util.List;

@Mapper
public interface ManagementAssetsMapper extends BaseMapper<ManagementAssets> {

    @SelectProvider(method = "getInfoByManId", type = ManagementAssetsDao.class)
    List<ManagementAssets> getInfoByManId(Integer id);

    @SelectProvider(method = "managementByAssetsType", type = ManagementAssetsDao.class)
    List<StatisticsRepairVo> managementByAssetsType();
}
