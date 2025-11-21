package com.jsdc.rfid.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ConsumableDao;
import com.jsdc.rfid.model.Consumable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import com.jsdc.rfid.vo.ConsumableVo;

import java.util.List;

@Mapper
public interface ConsumableMapper extends BaseMapper<Consumable> {
    /**
    *首页库存预警详情列表
    * Author wzn
    * Date 2023/5/17 10:02
    */
    @SelectProvider(method = "earlyWarningList", type = ConsumableDao.class)
    List<ConsumableVo> earlyWarningList();



}
