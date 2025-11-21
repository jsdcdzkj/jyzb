package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ChangeDetailDao;
import com.jsdc.rfid.dao.ChangeInfoDao;
import com.jsdc.rfid.model.ChangeDetail;
import com.jsdc.rfid.model.ChangeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface ChangeDetailMapper extends BaseMapper<ChangeDetail> {

    @SelectProvider(method = "getDetailById", type = ChangeDetailDao.class)
    List<ChangeDetail> getDetailById( Integer id);
}
