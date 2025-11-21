package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.OperationRecord;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Repository
public class OperationRecordDao extends BaseDao<OperationRecord> {

    public String toList(OperationRecord beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM operation_record");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
