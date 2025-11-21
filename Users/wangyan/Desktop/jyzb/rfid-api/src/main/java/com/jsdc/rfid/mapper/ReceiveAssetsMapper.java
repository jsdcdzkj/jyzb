package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ReceiveAssetsDao;
import com.jsdc.rfid.model.ReceiveAssets;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ReceiveAssetsMapper extends BaseMapper<ReceiveAssets> {

    @SelectProvider(method = "selectReceiveAssets", type = ReceiveAssetsDao.class)
    List<ReceiveAssets> selectReceiveAssets(Integer receive_id);
}
