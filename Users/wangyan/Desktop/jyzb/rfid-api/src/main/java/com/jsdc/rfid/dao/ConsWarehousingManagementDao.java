package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ConsWarehousingManagement;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-06-07 11:53:30
 */
@Repository
public class ConsWarehousingManagementDao extends BaseDao<ConsWarehousingManagement> {

    public String toList(ConsWarehousingManagement beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM cons_warehousing_management");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
