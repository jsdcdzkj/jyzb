package com.jsdc.rfid.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseService;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.dao.EquipmentDao;
import com.jsdc.rfid.mapper.EquipmentMapper;
import com.jsdc.rfid.model.Equipment;
import com.jsdc.rfid.model.SysDict;
import com.jsdc.rfid.model.SysPosition;
import com.jsdc.rfid.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipmentService extends BaseService<EquipmentDao, Equipment> {

    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysPositionService positionService;
    @Autowired
    private SysDictService dictService;

    /**
     * 分页查询
     *
     * @param equipment
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageInfo getPage(Equipment equipment, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Equipment> pageInfo = new PageInfo<>(selectList(getWrapper(equipment)));
        //设备用途
        SysDict dict = new SysDict();
        dict.setType("equipment_usage");
        List<SysDict> dicts = dictService.getList(dict);
        //设备类型
        SysDict dict2 = new SysDict();
        dict2.setType("equipment_type");
        List<SysDict> dicts2 = dictService.getList(dict2);

        pageInfo.getList().forEach(x -> {
            SysPosition position = positionService.selectById(x.getEquipment_position());
            x.setPositionName(position.getPosition_name());
            if(null != x.getEquipment_usage()){
                Optional<SysDict> o = dicts.stream().filter(y -> y.getValue().equals(String.valueOf(x.getEquipment_usage()))).findFirst();
                o.ifPresent(sysDict -> x.setUsage_name(sysDict.getLabel()));

            }
            if(null != x.getEquipment_type()){
                Optional<SysDict> o = dicts2.stream().filter(y -> y.getValue().equals(String.valueOf(x.getEquipment_type()))).findFirst();
                o.ifPresent(sysDict -> x.setType_name(sysDict.getLabel()));

            }
        });
        return pageInfo;
    }

    /**
     * 列表查询
     */
    public List<Equipment> getList(Equipment equipment) {
        List<Equipment> list = selectList(getWrapper(equipment));
        list.forEach(x -> {
            SysPosition position = positionService.selectById(x.getEquipment_position());
            //纬度
            x.setLat(position.getLat());
            //经度
            x.setLng(position.getLng());
        });
        return list;
    }

    /**
     * 列表查询
     */
    public List<Equipment> getLists(Equipment equipment) {
        //        list.forEach(x -> {
//            SysPosition position = positionService.selectById(x.getEquipment_position());
//            //纬度
//            x.setLat(position.getLat());
//            //经度
//            x.setLng(position.getLng());
//        });
        return equipmentMapper.getList(equipment);
    }

    /**
     * 新增
     *
     * @param equipment
     * @return
     */
    public Equipment add(Equipment equipment) {
        if (validate(equipment)) {
            SysUser user = userService.getUser();
            equipment.setCreate_time(new Date());
            equipment.setCreate_user(user.getId());
            equipment.setUpdate_time(new Date());
            equipment.setUpdate_user(user.getId());
            equipment.setIs_enable(G.ISENABLE_YES);
            equipment.setIs_del(G.ISDEL_NO);
            //报警状态 1正常 2报警
            equipment.setWarning_status(1);
            if (insert(equipment) > 0) {
                return equipment;
            }
        }
        return null;
    }

    /**
     * 修改
     *
     * @param equipment
     * @return
     */
    public Equipment edit(Equipment equipment) {
        if (validate(equipment)) {
            SysUser user = userService.getUser();
            Equipment original = selectById(equipment.getId());
            BeanUtils.copyProperties(equipment, original);
            original.setUpdate_user(user.getId());
            original.setUpdate_time(new Date());
            if (updateById(original) > 0) {
                return original;
            }
        }
        return null;
    }

    public Boolean del(Integer id) {
        SysUser user = userService.getUser();
        Equipment equipment = selectById(id);
        equipment.setUpdate_time(new Date());
        equipment.setUpdate_user(user.getId());
        equipment.setIs_del(G.ISDEL_YES);
        if (updateById(equipment) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证必填项
     *
     * @param equipment
     * @return
     */
    private Boolean validate(Equipment equipment) {
        if (StringUtils.isEmpty(equipment.getEquipment_name()) || null == equipment.getEquipment_type()
                || null == equipment.getEquipment_position()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取查询条件
     *
     * @param equipment
     * @return
     */
    private LambdaQueryWrapper<Equipment> getWrapper(Equipment equipment) {
        LambdaQueryWrapper<Equipment> wrapper = new LambdaQueryWrapper<>();
        if (Base.notEmpty(equipment)) {
            if (StringUtils.isNotEmpty(equipment.getEquipment_name())) {
                wrapper.like(Equipment::getEquipment_name, equipment.getEquipment_name().trim());
            }
            if (null != equipment.getEquipment_type()) {
                wrapper.eq(Equipment::getEquipment_type, equipment.getEquipment_type());
            }
            if (StringUtils.isNotEmpty(equipment.getIp())) {
                wrapper.like(Equipment::getIp, equipment.getIp().trim());
            }
            if (StringUtils.isNotEmpty(equipment.getMac())) {
                wrapper.like(Equipment::getMac, equipment.getMac().trim());
            }
            // 存放位置
            if (Base.notEmpty(equipment.getEquipment_position())) {
                wrapper.eq(Equipment::getEquipment_position, equipment.getEquipment_position());
            }
            //报警状态 1正常 2报警
            if (Base.notEmpty(equipment.getWarning_status())) {
                wrapper.eq(Equipment::getWarning_status, equipment.getWarning_status());
            }
            //设备状态 0:离线 1:在线
            if (Base.notEmpty(equipment.getStatus())) {
                wrapper.eq(Equipment::getStatus, equipment.getStatus());
            }
            //用处 【字典 1扫描 2报警】
            if (Base.notEmpty(equipment.getEquipment_usage())) {
                wrapper.eq(Equipment::getEquipment_usage, equipment.getEquipment_usage());
            }
        }
        wrapper.eq(Equipment::getIs_del, G.ISDEL_NO);
        wrapper.eq(Equipment::getIs_enable, G.ISENABLE_YES);
        return wrapper;
    }

    //获取ip和端口列表
    public List<String> getIpList(Equipment bean) {
//        List<String> list = equipmentMapper.getIpList(bean);
        List<String> list = new ArrayList<>();
        List<Equipment> a = equipmentMapper.selectList(Wrappers.<Equipment>lambdaQuery()
                .eq(Equipment::getIs_del, G.ISDEL_NO)
                .eq(Equipment::getEquipment_usage, 2)
        );
        if (CollectionUtils.isEmpty(a)){
            return list;
        }
        for (Equipment temp : a){
            list.add(temp.getIp() + ":" + temp.getPort());
        }
        return list;
    }
}
