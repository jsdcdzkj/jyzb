package com.jsdc.rfid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsdc.rfid.model.OperationRecord;
import org.apache.ibatis.annotations.Mapper;
import com.jsdc.rfid.dao.OperationRecordDao;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Mapper
public interface OperationRecordMapper extends BaseMapper<OperationRecord> {

    @SelectProvider(method = "toList",type = OperationRecordDao.class)
    List<OperationRecord> toList(OperationRecord beanParam);
}