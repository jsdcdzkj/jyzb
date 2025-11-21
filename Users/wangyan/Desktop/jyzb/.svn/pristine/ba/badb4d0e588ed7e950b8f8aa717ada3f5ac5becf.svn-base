package com.jsdc.rfid.service;

import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.ConsInAndOutStatisticsDao;
import com.jsdc.rfid.mapper.ConsInAndOutStatisticsMapper;
import com.jsdc.rfid.model.ConsInAndOutStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 统计进出表数据信息Service
 */
@Service
@Transactional
public class ConsInAndOutStatisticsService extends BaseService<ConsInAndOutStatisticsDao, ConsInAndOutStatistics> {

    @Autowired
    private ConsInAndOutStatisticsMapper consInAndOutStatisticsMapper;

    @Autowired
    private SysUserService sysUserService;


    /**
     * 新增统计信息
     */
    public ConsInAndOutStatistics insertCons(ConsInAndOutStatistics consInAndOutStatistics) {
        consInAndOutStatistics.setIs_del(G.ISDEL_NO);
        consInAndOutStatistics.setCreate_user(consInAndOutStatistics.getLogin_user() != null ? consInAndOutStatistics.getLogin_user() : sysUserService.getUser().getId());
        consInAndOutStatistics.setCreate_time(new Date());
        consInAndOutStatisticsMapper.insert(consInAndOutStatistics);
        return consInAndOutStatistics;
    }
}
