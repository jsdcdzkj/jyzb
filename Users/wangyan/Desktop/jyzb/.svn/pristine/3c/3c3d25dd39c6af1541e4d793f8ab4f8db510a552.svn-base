package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.dao.ConsHandworkDao;
import com.jsdc.rfid.model.ConsHandwork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface ConsHandworkMapper extends BaseMapper<ConsHandwork> {

    /**
     * 手动出库总数
     *
     * @Author thr
     */
    @SelectProvider(method = "getTotalCount", type = ConsHandworkDao.class)
    ConsHandwork getTotalCount(ConsHandwork beanParam);
}
