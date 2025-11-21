package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.dao.ConsWarehousingManagementDao;
import com.jsdc.rfid.mapper.ConsWarehousingManagementMapper;
import com.jsdc.rfid.model.ConsWarehousingManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.ResultInfo;

import java.util.Date;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-06-07 11:53:30
 */
@Service
@Transactional
public class ConsWarehousingManagementService extends BaseService<ConsWarehousingManagementDao, ConsWarehousingManagement> {

    @Autowired
    private ConsWarehousingManagementMapper consWarehousingManagementMapper;
    @Autowired
    private SysUserService sysUserService;

    public PageInfo<ConsWarehousingManagement> toList(Integer pageIndex, Integer pageSize, ConsWarehousingManagement beanParam) {
        PageHelper.startPage(pageIndex, pageSize);

        List<ConsWarehousingManagement> consWarehousingManagementVos = consWarehousingManagementMapper.toList(beanParam);

        PageInfo<ConsWarehousingManagement> page = new PageInfo<>(consWarehousingManagementVos);

        return page;
    }

    public List<ConsWarehousingManagement> getList(ConsWarehousingManagement beanParam) {

        return consWarehousingManagementMapper.toList(beanParam);
    }
    public ResultInfo getById(Integer id) {
        QueryWrapper<ConsWarehousingManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del",0);
        queryWrapper.eq("id",id);
        ConsWarehousingManagement consWarehousingManagement = selectOne(queryWrapper);
        return ResultInfo.success(consWarehousingManagement);
    }

    /**
     *  添加
     */
    public ResultInfo addConsWarehousingManagement(ConsWarehousingManagement bean) {
        // 删除状态
        bean.setIs_del(String.valueOf(0));
        // 创建时间
        bean.setCreate_time(new Date());
        // 创建者
        bean.setCreate_user(sysUserService.getUser().getId());
        insert(bean);
        return ResultInfo.success();
    }

    /**
     *  编辑
     */
    public ResultInfo editConsWarehousingManagement(ConsWarehousingManagement bean) {
        // 修改者
        bean.setUpdate_user(sysUserService.getUser().getId());
        // 修改时间
        bean.setUpdate_time(new Date());
        updateById(bean);
        return ResultInfo.success();
    }}
