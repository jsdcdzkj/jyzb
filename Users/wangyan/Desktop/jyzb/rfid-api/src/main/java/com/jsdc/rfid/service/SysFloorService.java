package com.jsdc.rfid.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.SysFloorDao;
import com.jsdc.rfid.model.SysFloor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SysFloorService extends BaseService<SysFloorDao, SysFloor> {
}
