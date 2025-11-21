package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ReceiveDao;
import com.jsdc.rfid.model.Receive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ReceiveMapper extends BaseMapper<Receive> {

    @SelectProvider(method = "selectReceiveByPage", type = ReceiveDao.class)
    List<Receive> selectReceiveByPage(Receive receive, Integer userId);

    @SelectProvider(method = "collectionManageByPage", type = ReceiveDao.class)
    List<Receive> collectionManageByPage(Receive receive, Integer userId, Integer department_id);

    @SelectProvider(method = "getAllReceive", type = ReceiveDao.class)
    List<Receive> getAllReceive(Integer userId);

    @SelectProvider(method = "getWxReceiveList", type = ReceiveDao.class)
    List<Receive> getWxReceiveList(Receive bean);

    @SelectProvider(method = "selectUserReceiveByPage", type = ReceiveDao.class)
    List<Receive> selectUserReceiveByPage(Receive receive, Integer userId);

    @SelectProvider(method = "getWxReceivingList", type = ReceiveDao.class)
    List<Receive> getWxReceivingList(Receive bean);
}
