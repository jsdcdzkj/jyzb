package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.OperationRecordDao;
import com.jsdc.rfid.mapper.OperationRecordMapper;
import com.jsdc.rfid.model.OperationRecord;
import com.jsdc.rfid.model.SysUser;
import net.hasor.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.ResultInfo;

import java.util.Date;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Service
@Transactional
public class OperationRecordService extends BaseService<OperationRecordDao, OperationRecord> {

    @Autowired
    private OperationRecordMapper operationRecordMapper;
    @Autowired
    private SysUserService sysUserService;

    public static final String MODE_TYPE_ASSETS = "1";//资产管理模块

    public PageInfo<OperationRecord> toList(Integer pageIndex, Integer pageSize, OperationRecord beanParam) {
        PageHelper.startPage(pageIndex, pageSize);

        List<OperationRecord> operationRecordVos = operationRecordMapper.toList(beanParam);

        return new PageInfo<>(operationRecordVos);
    }

    public List<OperationRecord> getList(OperationRecord beanParam) {

        return operationRecordMapper.toList(beanParam);
    }

    public ResultInfo getById(Integer id) {
        QueryWrapper<OperationRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        queryWrapper.eq("id", id);
        OperationRecord operationRecord = selectOne(queryWrapper);
        return ResultInfo.success(operationRecord);
    }

    private void saveBean(OperationRecord bean, SysUser currentUser) {
        // 操作记录 XXX 审批通过
        if (StringUtils.isNotBlank(bean.getRecord()) && bean.getRecord().contains("审批")) {
            bean.setRecord(currentUser.getUser_name() + " " + bean.getRecord());
        }
        // 删除状态
        bean.setIs_del(G.ISDEL_NO);
        // 创建时间
        bean.setCreate_time(new Date());
        // 创建者
        bean.setCreate_user(currentUser.getId());
        // 更新者
        bean.setUpdate_user(currentUser.getId());
        insert(bean);

    }

    /**
     * 添加
     */
    public ResultInfo addOperationRecord(OperationRecord bean) {
        // 创建者
        SysUser sysUser;
        if (Base.empty(bean.getUserId())) {
            sysUser = sysUserService.getUser();
        } else {
            sysUser = sysUserService.selectById(bean.getUserId());
        }
        saveBean(bean, sysUser);
        return ResultInfo.success();
    }

    /**
     * 添加
     */
    public ResultInfo addOperationRecord1(OperationRecord bean, Integer userId) {
        saveBean(bean, sysUserService.selectById(userId));
        return ResultInfo.success();
    }

    /**
     * 查看操作记录
     */
    public List<OperationRecord> getOperationRecord(String field_fk, String type) {

        return selectList(Wrappers.<OperationRecord>lambdaQuery()
                .eq(OperationRecord::getField_fk, field_fk)
                .eq(StringUtils.isNotBlank(type), OperationRecord::getType, type)
                .eq(OperationRecord::getIs_del, String.valueOf(0))
        );
    }

    /**
     * 添加
     */
    public ResultInfo addOperationRecord(Integer operate_id, String field_fk, String record, String type) {
        return addOperationRecord(operate_id, field_fk, record, type, sysUserService.getUser());
    }

    public ResultInfo addOperationRecord(Integer operate_id, String field_fk, String record, String type, SysUser currentUser) {
        OperationRecord bean = OperationRecord.builder().operate_id(operate_id).field_fk(field_fk).type(type).record(record).build();
        saveBean(bean, currentUser);
        return ResultInfo.success();
    }

    /**
     * app添加
     */
    public ResultInfo appAddOperationRecord(Integer operate_id, String field_fk, String record, String type, Integer user_id) {
        SysUser sysUser = sysUserService.selectById(user_id);
        OperationRecord bean = OperationRecord.builder().operate_id(operate_id).field_fk(field_fk).type(type).record(record).build();
        saveBean(bean, sysUser);
        return ResultInfo.success();
    }

    /**
     * 添加(领用 变更 处置)
     */
    public ResultInfo addOtherOperationRecord(Integer operate_id, String field_fk, String record, String kind) {
        return addOtherOperationRecord(operate_id, field_fk, record, kind, sysUserService.getUser());
    }
    /**
     * 添加(领用 变更 处置)微信小程序专用
     */
    public ResultInfo addWxOtherOperationRecord(Integer operate_id, String field_fk, String record, String kind, SysUser sysUser) {
        return addOtherOperationRecord(operate_id, field_fk, record, kind, sysUserService.selectById(sysUser));
    }


    public ResultInfo addOtherOperationRecord(Integer operate_id, String field_fk, String record, String kind, SysUser currentUser) {
        OperationRecord bean = new OperationRecord();
        bean.setOperate_id(operate_id);
        bean.setField_fk(field_fk);
        bean.setRecord(record);
        bean.setKind(kind);
        saveBean(bean, currentUser);
        return ResultInfo.success();
    }


    /**
     * App添加(领用 变更 处置)
     */
    public ResultInfo addOtherAppOperationRecord(Integer operate_id, String field_fk, String record, String kind, Integer userId) {
        OperationRecord bean = new OperationRecord();
        bean.setOperate_id(operate_id);
        bean.setField_fk(field_fk);
        bean.setRecord(record);
        bean.setKind(kind);
        saveBean(bean, sysUserService.selectById(userId));
        return ResultInfo.success();
    }

    /**
     * 编辑
     */
    public ResultInfo editOperationRecord(OperationRecord bean) {
        // 修改者
//        bean.setUpdate_user(sysUserService.getUser().getId());
        // 修改时间
        bean.setUpdate_time(new Date());
        updateById(bean);
        return ResultInfo.success();
    }
}
