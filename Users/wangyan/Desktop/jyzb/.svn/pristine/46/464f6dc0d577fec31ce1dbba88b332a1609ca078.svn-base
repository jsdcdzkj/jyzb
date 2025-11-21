package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.SysLogDao;
import com.jsdc.rfid.model.SysLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysLogService extends BaseService<SysLogDao, SysLog> {

    public PageInfo<SysLog> getPage(SysLog log, Integer pageIndex, Integer pageSize){
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        if(null != log){
            if(StringUtils.isNotEmpty(log.getUser_name())){
                wrapper.like(SysLog::getUser_name, log.getUser_name());
            }
            if(null != log.getUser_id()){
                wrapper.eq(SysLog::getUser_id, log.getUser_id());
            }
            if(StringUtils.isNotEmpty(log.getStart_time())){
                wrapper.ge(SysLog::getOperate_time, log.getStart_time()+" 00:00:00");
            }
            if(StringUtils.isNotEmpty(log.getEnd_time())){
                wrapper.le(SysLog::getOperate_time, log.getEnd_time()+" 23:59:59");
            }
        }
//        wrapper.orderByDesc(SysLog::getOperate_time);
        PageHelper.startPage(pageIndex, pageSize, "operate_time desc");
        return new PageInfo<>(selectList(wrapper));
    }
}
