package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.ConsInventoryManagement;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.ConsInventoryManagementDao;
import org.apache.ibatis.annotations.SelectProvider;
import vo.ConsDataVo;

import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-06-07 11:53:30
 */
@Mapper
public interface ConsInventoryManagementMapper extends BaseMapper<ConsInventoryManagement> {

    @SelectProvider(method = "toList", type = ConsInventoryManagementDao.class)
    List<ConsInventoryManagement> toList(ConsInventoryManagement beanParam);

    /**
     * 耗材库存总数
     *
     * @Author thr
     */
    @SelectProvider(method = "getTotalCount", type = ConsInventoryManagementDao.class)
    ConsInventoryManagement getTotalCount(ConsInventoryManagement beanParam);

    /**
     * 以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     * 当前库存
     *
     * @Author thr
     */
    @SelectProvider(method = "getInventoryNum", type = ConsInventoryManagementDao.class)
    List<ConsDataVo> getInventoryNum(ConsInventoryManagement beanParam);

}