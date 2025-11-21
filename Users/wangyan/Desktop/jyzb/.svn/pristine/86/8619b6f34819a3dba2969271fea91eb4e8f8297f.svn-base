package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.SysPlaceDao;
import com.jsdc.rfid.model.SysPlace;
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
public class SysPlaceService extends BaseService<SysPlaceDao, SysPlace> {
    @Autowired
    private SysUserService userService;

    public PageInfo<SysPlace> getPage(Integer pageIndex, Integer pageSize, SysPlace place){
        LambdaQueryWrapper<SysPlace> wrapper = new LambdaQueryWrapper<>();
        if(null != place){
            if(StringUtils.isNotEmpty(place.getPlace_name())){
                wrapper.like(SysPlace::getPlace_name, place.getPlace_name().trim());
            }
        }
        wrapper.eq(SysPlace::getIs_del, G.ISDEL_NO);
//        wrapper.orderByAsc(SysPlace::getCreate_time);
        PageHelper.startPage(pageIndex, pageSize, "create_time asc");
        List<SysPlace> list = selectList(wrapper);
        return new PageInfo<>(list);
    }

    public List<SysPlace> getList(SysPlace place){
        QueryWrapper<SysPlace> wrapper = new QueryWrapper<>();
        if(null != place){
            if(StringUtils.isNotEmpty(place.getPlace_name())){
                wrapper.like("place_name", place.getPlace_name().trim());
            }
        }
        wrapper.eq("is_del", G.ISDEL_NO);
        wrapper.orderByAsc("create_time");
        return selectList(wrapper);
    }

    public SysPlace add(SysPlace place){
        if(validate(place)){
            SysUser user = userService.getUser();
            place.setCreate_time(new Date());
            place.setCreate_user(user.getId());
            place.setUpdate_time(new Date());
            place.setUpdate_user(user.getId());
            place.setIs_del(G.ISDEL_NO);
            if(insert(place) > 0){
                return place;
            }
        }
        return null;
    }

    public SysPlace edit(SysPlace place){
        if(validate(place)){
            SysUser user = userService.getUser();
            SysPlace original = selectById(place.getId());
            BeanUtils.copyProperties(place, original);
            original.setCreate_user(user.getId());
            original.setCreate_time(new Date());
            original.setUpdate_user(user.getId());
            original.setUpdate_time(new Date());
            if(updateById(original) > 0){
                return original;
            }
        }
        return null;
    }

    public Boolean del(Integer id){
        SysPlace place = selectById(id);
        SysUser user = userService.getUser();
        place.setUpdate_time(new Date());
        place.setUpdate_user(user.getId());
        place.setIs_del(G.ISDEL_YES);
        return updateById(place) > 0;
    }

    public Boolean validate(SysPlace place){
        return !StringUtils.isEmpty(place.getPlace_name()) && !StringUtils.isEmpty(place.getPlace_desc());
    }
}
