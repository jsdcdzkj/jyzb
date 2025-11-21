package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.SysCronDao;
import com.jsdc.rfid.model.SysCron;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author thr
 * @descirpet 定时任务
 */
@Mapper
public interface SysCronMapper extends BaseMapper<SysCron> {

    //pc未派单分页、手机端分页
    @SelectProvider(type = SysCronDao.class, method = "selectPageList")
    List<SysCron> selectPageList(SysCron bean);

}
