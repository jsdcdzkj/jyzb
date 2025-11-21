package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.InventoryWarehousingMember;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Repository
public class InventoryWarehousingMemberDao extends BaseDao<InventoryWarehousingMember> {

    public String toList(InventoryWarehousingMember beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM inventory_warehousing_member");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
