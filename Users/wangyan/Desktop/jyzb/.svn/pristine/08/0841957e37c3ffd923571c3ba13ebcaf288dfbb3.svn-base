package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.WarehousingManagement;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Repository
public class WarehousingManagementDao extends BaseDao<WarehousingManagement> {

    public String toList(WarehousingManagement beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM warehousing_management");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
