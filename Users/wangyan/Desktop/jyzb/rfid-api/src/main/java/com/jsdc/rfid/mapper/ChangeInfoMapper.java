package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ChangeInfoDao;
import com.jsdc.rfid.dao.ReceiveDao;
import com.jsdc.rfid.model.ChangeInfo;
import com.jsdc.rfid.model.Receive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ChangeInfoMapper extends BaseMapper<ChangeInfo> {

    @SelectProvider(method = "getInfoByPage", type = ChangeInfoDao.class)
    List<ChangeInfo> getInfoByPage(ChangeInfo changeInfo,Integer userId);


    @SelectProvider(method = "collectionChangeByPage", type = ChangeInfoDao.class)
    List<ChangeInfo> collectionChangeByPage(ChangeInfo changeInfo,Integer userId,Integer department_id);


}
