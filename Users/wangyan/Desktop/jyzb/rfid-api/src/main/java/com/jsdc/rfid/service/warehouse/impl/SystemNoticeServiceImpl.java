package com.jsdc.rfid.service.warehouse.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.mapper.warehouse.SystemNoticeMapper;
import com.jsdc.rfid.model.warehouse.SystemNotice;
import com.jsdc.rfid.model.warehouse.WarehousingEnter;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.service.warehouse.SystemNoticeService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SystemNoticeServiceImpl extends ServiceImpl<SystemNoticeMapper, SystemNotice> implements SystemNoticeService {

    private final SysUserService sysUserService;

    @Async
    @Override
    public void addNotice(String message,String module) {
        SystemNotice notice = new SystemNotice();
        notice.setMessage(message);
        notice.setModule(module);
        notice.setIs_del(String.valueOf(0));
        notice.setCreate_time(new Date());
        notice.setCreate_user(sysUserService.getUser().getId());
        notice.setDept_id(sysUserService.getUser().getDepartment());
        save(notice);//保存主表
    }

    @Override
    public PageInfo<SystemNotice> pageQuery(Integer pageIndex, Integer pageSize) {
        QueryWrapper<SystemNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del","0");
        queryWrapper.in("dept_id",sysUserService.getUser().getDepartment());
        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
        List<SystemNotice> pageList = list(queryWrapper);
        return new PageInfo<>(pageList);
    }

}
