package com.jsdc.rfid.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.ManagementAssetsDao;
import com.jsdc.rfid.mapper.ManagementAssetsMapper;
import com.jsdc.rfid.model.ManagementAssets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.StatisticsRepairVo;

import java.util.List;

@Service
@Transactional
public class ManagementAssetsService extends BaseService<ManagementAssetsDao, ManagementAssets> {

    @Autowired
    private ManagementAssetsMapper managementAssetsMapper;

    /**
     * 资产处置占比
     * @return
     */
    public List<StatisticsRepairVo> managementByAssetsType(){
        return managementAssetsMapper.managementByAssetsType();
    }
}
