package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.CarryWhite;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.CarryWhiteDao;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2024-02-28 10:42:26
 */
@Mapper
public interface CarryWhiteMapper extends BaseMapper<CarryWhite> {

    @SelectProvider(method = "toList",type = CarryWhiteDao.class)
    List<CarryWhite> toList(CarryWhite beanParam);

    @SelectProvider(method = "insertBatch",type = CarryWhiteDao.class)
    void insertBatch(List<CarryWhite> carryWhiteList);
}