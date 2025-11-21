package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.EquipmentDao;
import com.jsdc.rfid.model.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {

    //获取ip和端口列表
    @SelectProvider(type = EquipmentDao.class, method = "getIpList")
    List<String> getIpList(Equipment bean);

    //列表
    @SelectProvider(type = EquipmentDao.class, method = "getList")
    List<Equipment> getList(Equipment bean);

}
