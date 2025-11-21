package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.CarryAbnormalDao;
import com.jsdc.rfid.model.CarryAbnormal;
import com.jsdc.rfid.vo.CarryManaveVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @author zln
 * @descript 外携异常管理
 * @date 2022-04-24
 */
@Mapper
public interface CarryAbnormalMapper extends BaseMapper<CarryAbnormal> {

    //分页查询
    @SelectProvider(method = "selectPageList", type = CarryAbnormalDao.class)
    List<CarryAbnormal> selectPageList(CarryAbnormal bean);

    //分页查询
    @SelectProvider(method = "getPageList", type = CarryAbnormalDao.class)
    List<CarryAbnormal> getPageList(CarryAbnormal bean);

    //异常统计
    @SelectProvider(method = "selectAbnormalCount", type = CarryAbnormalDao.class)
    List<CarryManaveVo> selectAbnormalCount();

    @SelectProvider(method = "selectAbnormalDataCountPage", type = CarryAbnormalDao.class)
    List<CarryManaveVo> selectAbnormalDataCountPage(Integer asset_type_id);
}
