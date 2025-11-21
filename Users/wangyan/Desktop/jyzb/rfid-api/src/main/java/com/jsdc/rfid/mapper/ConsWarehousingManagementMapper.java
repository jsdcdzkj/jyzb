package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.ConsWarehousingManagement;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.ConsWarehousingManagementDao;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-06-07 11:53:30
 */
@Mapper
public interface ConsWarehousingManagementMapper extends BaseMapper<ConsWarehousingManagement> {

    @SelectProvider(method = "toList",type = ConsWarehousingManagementDao.class)
    List<ConsWarehousingManagement> toList(ConsWarehousingManagement beanParam);
}