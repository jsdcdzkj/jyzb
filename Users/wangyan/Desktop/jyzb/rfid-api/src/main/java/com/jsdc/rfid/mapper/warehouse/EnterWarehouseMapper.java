package com.jsdc.rfid.mapper.warehouse;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.warehouse.WarehousingEnterDao;
import com.jsdc.rfid.model.warehouse.WarehousingEnter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

@Mapper
public interface EnterWarehouseMapper extends BaseMapper<WarehousingEnter> {
    @SelectProvider(method = "sumEquipNum", type = WarehousingEnterDao.class)
    List<Map> sumEquipNum(@Param("ids") List<Integer> ids);
}
