package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.SysDictDao;
import com.jsdc.rfid.model.SysDict;
import com.jsdc.rfid.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SysDictService extends BaseService<SysDictDao, SysDict> {

    @Autowired
    private SysUserService userService;
    /**
     * 列表查询
     * @param dict
     * @return
     */
    public List<SysDict> getList(SysDict dict){
        return selectList(getWrapper(dict));
    }

    /**
     * 分页查询
     * @param dict
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo<SysDict> getPage(SysDict dict, Integer pageIndex, Integer pageSize){
        PageHelper.startPage(pageIndex, pageSize);
        return new PageInfo<>(selectList(getWrapper(dict)));
    }

    public SysDict add(SysDict dict){
        if(validate(dict)){
            SysUser user = userService.getUser();
            dict.setCreate_time(new Date());
            dict.setCreate_user(user.getId());
            dict.setUpdate_time(new Date());
            dict.setUpdate_user(user.getId());
            dict.setIs_del(G.ISDEL_NO);
            if(insert(dict) > 0){
                return dict;
            }
        }
        return null;
    }

    public SysDict edit(SysDict dict){
        if(validate(dict)){
            SysUser user = userService.getUser();
            SysDict original = selectById(dict.getId());
            BeanUtils.copyProperties(dict, original);
            original.setUpdate_user(user.getId());
            original.setUpdate_time(new Date());
            if(updateById(original) > 0){
                return original;
            }
        }
        return null;
    }

    public Boolean del(Integer id){
        SysUser user = userService.getUser();
        SysDict sysDict = selectById(id);
        sysDict.setUpdate_time(new Date());
        sysDict.setUpdate_user(user.getId());
        sysDict.setIs_del(G.ISDEL_YES);
        return updateById(sysDict) > 0;
    }

    private LambdaQueryWrapper<SysDict> getWrapper(SysDict dict){
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        if(null != dict){
            if(StringUtils.isNotEmpty(dict.getType())){
                wrapper.eq(SysDict::getType, dict.getType().trim());
            }
            if(StringUtils.isNotEmpty(dict.getDescript())){
                wrapper.like(SysDict::getDescript, dict.getDescript().trim());
            }
            if(StringUtils.isNotEmpty(dict.getLabel())){
                wrapper.like(SysDict::getLabel, dict.getLabel().trim());
            }
        }
        wrapper.eq(SysDict::getIs_del, G.ISDEL_NO);
        return wrapper;
    }

    private Boolean validate(SysDict dict){
        return !StringUtils.isEmpty(dict.getType()) && !StringUtils.isEmpty(dict.getLabel()) && null != dict.getValue();
    }

    /**
     * 根据流程代码获取流程
     * @param code
     * @return
     */
    public SysDict getProcessByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        List<SysDict> sysDicts = selectList(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getIs_del, G.ISDEL_NO)
                .eq(SysDict::getType, "process_type")
                .like(SysDict::getLabel, code)
        );
        if(CollectionUtils.isEmpty(sysDicts)) {
            return null;
        }
        return sysDicts.get(0);
    }
}
