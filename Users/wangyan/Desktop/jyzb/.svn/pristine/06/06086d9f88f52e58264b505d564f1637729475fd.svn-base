package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.WarehousingManagementDao;
import com.jsdc.rfid.model.WarehousingManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Mapper
public interface WarehousingManagementMapper extends BaseMapper<WarehousingManagement> {

    @SelectProvider(method = "toList",type = WarehousingManagementDao.class)
    List<WarehousingManagement> toList(WarehousingManagement beanParam);

    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(List<WarehousingManagement> entityList);
}