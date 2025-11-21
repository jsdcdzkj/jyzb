package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ManagementDao;
import com.jsdc.rfid.dao.ReceiveDao;
import com.jsdc.rfid.model.Management;
import com.jsdc.rfid.model.Receive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ManagementMapper  extends BaseMapper<Management> {


    @SelectProvider(method = "getPageByUserId", type = ManagementDao.class)
    List<Management> getPageByUserId(Management management, Integer userId);


    @SelectProvider(method = "getOneManById", type = ManagementDao.class)
    Management getOneManById(Integer id);


    @SelectProvider(method = "getPageByPermission", type = ManagementDao.class)
    List<Management> getPageByPermission(Management management , Integer userId,Integer department_id);
}
