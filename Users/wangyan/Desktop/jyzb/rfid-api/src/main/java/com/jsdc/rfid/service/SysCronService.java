package com.jsdc.rfid.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.SysCronDao;
import com.jsdc.rfid.mapper.SysCronMapper;
import com.jsdc.rfid.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author thr
 * @descirpet 定时任务
 */
@Service
@Transactional
public class SysCronService extends BaseService<SysCronDao, SysCron> {

    @Autowired
    private SysCronMapper sysCronMapper;

    public PageInfo<SysCron> selectPageList(Integer pageIndex, Integer pageSize, SysCron bean) {
        PageHelper.startPage(pageIndex, pageSize);
        List<SysCron> list = sysCronMapper.selectPageList(bean);
        return new PageInfo<>(list);
    }

    public List<SysCron> getList(SysCron bean) {
        return sysCronMapper.selectPageList(bean);
    }

}
