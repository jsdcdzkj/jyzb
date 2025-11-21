package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.WarehousingPurchaseMember;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Repository
public class WarehousingPurchaseMemberDao extends BaseDao<WarehousingPurchaseMember> {

    public String toList(WarehousingPurchaseMember beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM warehousing_purchase_member");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
