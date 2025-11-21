package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.CarryManageDao;
import com.jsdc.rfid.model.CarryManage;
import com.jsdc.rfid.vo.CarryManaveVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;


/**
 * @author zln
 * @descript 外携申请管理
 * @date 2022-04-24
 */
@Mapper
public interface CarryManageMapper extends BaseMapper<CarryManage> {

    //分页查询
    @SelectProvider(method = "selectPageList", type = CarryManageDao.class)
    List<CarryManage> selectPageList(CarryManage bean);

    //详情
    @SelectProvider(method = "selectByDetails", type = CarryManageDao.class)
    List<CarryManage> selectByDetails(Integer id,String approval_state);

    @SelectProvider(method = "selectManageCount", type = CarryManageDao.class)
    List<CarryManaveVo> selectManageCount();
}
