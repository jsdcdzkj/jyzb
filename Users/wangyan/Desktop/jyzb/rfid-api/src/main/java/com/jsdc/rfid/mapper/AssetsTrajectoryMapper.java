package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.AssetsTrajectoryDao;
import com.jsdc.rfid.model.AssetsTrajectory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author thr
 * @descript 资产轨迹
 */
@Mapper
public interface AssetsTrajectoryMapper extends BaseMapper<AssetsTrajectory> {

    //分页查询
    @SelectProvider(method = "selectPageList", type = AssetsTrajectoryDao.class)
    List<AssetsTrajectory> selectPageList(AssetsTrajectory bean);
}
