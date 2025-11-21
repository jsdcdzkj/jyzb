package com.jsdc.rfid.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.ChangeDetailDao;
import com.jsdc.rfid.model.ChangeDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeDetailService extends BaseService<ChangeDetailDao, ChangeDetail> {

}
