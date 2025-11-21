package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.SysPositionDao;
import com.jsdc.rfid.model.SysPlace;
import com.jsdc.rfid.model.SysPosition;
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
public class SysPositionService extends BaseService<SysPositionDao, SysPosition> {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPlaceService placeService;

    public PageInfo<SysPosition> getPage(SysPosition position, Integer pageIndex, Integer pageSize){

        PageHelper.startPage(pageIndex, pageSize);
        List<SysPosition> list = selectList(getWrapper(position));
        PageInfo<SysPosition> pageInfo = new PageInfo<>(list);
        pageInfo.getList().forEach(x -> {
            SysPlace place = placeService.selectById(x.getPlace_id());
            x.setPlace_name(place.getPlace_name());
        });
        return pageInfo;
    }

    public List<SysPosition> getList(SysPosition position){
        return selectList(getWrapper(position));
    }

    public SysPosition getMarker(Integer id){
        return selectById(id);
    }
    public SysPosition add(SysPosition position){
        if(validate(position)){
            SysUser user = userService.getUser();
            position.setCreate_time(new Date());
            position.setCreate_user(user.getId());
            position.setUpdate_time(new Date());
            position.setUpdate_user(user.getId());
            position.setIs_del(G.ISDEL_NO);
            if(insert(position) > 0){
                return position;
            }
        }
        return null;
    }

    public SysPosition edit(SysPosition position){
        if(validate(position)){
            SysUser user = userService.getUser();
            SysPosition original = selectById(position.getId());
            BeanUtils.copyProperties(position, original);
            original.setUpdate_time(new Date());
            original.setUpdate_user(user.getId());
            if(updateById(original) > 0){
                return original;
            }
        }
        return null;
    }

    public Boolean del(Integer id){
        SysUser user = userService.getUser();
        SysPosition position = selectById(id);
        position.setUpdate_user(user.getId());
        position.setUpdate_time(new Date());
        position.setIs_del(G.ISDEL_YES);
        return updateById(position) > 0;
    }

    /**
     * 处理查询条件
     * @param position
     * @return
     */
    private LambdaQueryWrapper<SysPosition> getWrapper(SysPosition position){
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        if(null != position){
            if(StringUtils.isNotEmpty(position.getPosition_name())){
                wrapper.like(SysPosition::getPosition_name, position.getPosition_name().trim());
            }
            if(null != position.getPlace_id()){
                wrapper.eq(SysPosition::getPlace_id, position.getPlace_id());
            }
        }
        wrapper.eq(SysPosition::getIs_del, G.ISDEL_NO);
        return wrapper;
    }

    private Boolean validate(SysPosition position){
        if(StringUtils.isEmpty(position.getPosition_name()) || null == position.getPlace_id()){
            return false;
        }
        return true;
    }

    /**
     * 根据场所ID获取位置列表
     * @param placeId
     * @return
     */
    public List<SysPosition> getListByPlaceId(Integer placeId) {
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPosition::getPlace_id, placeId);
        wrapper.eq(SysPosition::getIs_del, G.ISDEL_NO);
        return selectList(wrapper);
    }
}
