package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.SupplierDao;
import com.jsdc.rfid.model.Supplier;
import com.jsdc.rfid.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SupplierService extends BaseService<SupplierDao, Supplier> {

    @Autowired
    private SysUserService userService;

    /**
     * 分页查询
     * @param supplier
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<Supplier> getPage(Supplier supplier, Integer pageIndex, Integer pageSize){
        PageHelper.startPage(pageIndex, pageSize);
        return new PageInfo<>(selectList(getWrapper(supplier)));
    }

    /**
     * 列表查询
     * @param supplier
     * @return
     */
    public List<Supplier> getList(Supplier supplier){
        return selectList(getWrapper(supplier));
    }

    /**
     * 新增
     * @param supplier
     * @return
     */
    public Supplier add(Supplier supplier){
        if(validate(supplier)){
            SysUser user = userService.getUser();
            supplier.setCreate_time(new Date());
            supplier.setCreate_user(user.getId());
            supplier.setUpdate_time(new Date());
            supplier.setUpdate_user(user.getId());
            supplier.setIs_del(G.ISDEL_NO);
            supplier.setIs_enable(G.ISENABLE_YES);
            if(insert(supplier) > 0){
                return supplier;
            }
        }
        return null;
    }

    /**
     * 编辑
     * @param supplier
     * @return
     */
    public Supplier edit(Supplier supplier){
        if(validate(supplier)){
            SysUser user = userService.getUser();
            Supplier original = selectById(supplier.getId());
            BeanUtils.copyProperties(supplier, original);
            original.setUpdate_user(user.getId());
            original.setUpdate_time(new Date());
            if(updateById(original) > 0){
                return original;
            }
        }
        return null;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public Boolean delete(Integer id){
        SysUser user = userService.getUser();
        Supplier supplier = selectById(id);
        supplier.setUpdate_time(new Date());
        supplier.setUpdate_user(user.getId());
        supplier.setIs_del(G.ISDEL_YES);
        return updateById(supplier) > 0;
    }

    /**
     * 验证必填项
     * @param supplier
     * @return
     */
    private Boolean validate(Supplier supplier){
        return !StringUtils.isEmpty(supplier.getSupplier_name()) && !StringUtils.isEmpty(supplier.getSupplier_code());
    }
    /**
     * 统一获取查询条件
     * @param supplier
     * @return
     */
    private LambdaUpdateWrapper<Supplier> getWrapper(Supplier supplier){
        LambdaUpdateWrapper<Supplier> wrapper = new LambdaUpdateWrapper<>();
        if(null != supplier){
            if(StringUtils.isNotEmpty(supplier.getSupplier_name())){
                wrapper.like(Supplier::getSupplier_name, supplier.getSupplier_name().trim());
            }
            if(StringUtils.isNotEmpty(supplier.getSupplier_code())){
                wrapper.like(Supplier::getSupplier_code, supplier.getSupplier_code().trim());
            }
            if(StringUtils.isNotEmpty(supplier.getPinyin())){
                wrapper.like(Supplier::getPinyin, supplier.getPinyin().trim());
            }
            if(StringUtils.isNotEmpty(supplier.getContact())){
                wrapper.like(Supplier::getContact, supplier.getContact().trim());
            }
            if(StringUtils.isNotEmpty(supplier.getTelephone())){
                wrapper.like(Supplier::getTelephone, supplier.getTelephone().trim());
            }
            if(StringUtils.isNotEmpty(supplier.getEmail())){
                wrapper.like(Supplier::getEmail, supplier.getEmail().trim());
            }
        }
        wrapper.eq(Supplier::getIs_del, G.ISDEL_NO);
        wrapper.eq(Supplier::getIs_enable, G.ISENABLE_YES);
        return wrapper;
    }
}
