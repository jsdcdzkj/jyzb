package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ChangeDetail;
import org.springframework.stereotype.Repository;

@Repository
public class ChangeDetailDao extends BaseDao<ChangeDetail> {

    public String getDetailById(Integer id){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.assets_id,m.asset_code,m.asset_name,t.assets_type_name,u.user_name,m.specification,d.id,m.brand_id ");
        sql.append(" FROM change_detail d ");
        sql.append(" LEFT JOIN assets_manage m ON d.assets_id = m.id ");
        sql.append(" LEFT JOIN assets_type t ON m.asset_type_id = t.id ");
        sql.append(" LEFT JOIN sys_user u ON m.admin_user = u.id ");
        sql.append(" WHERE d.is_del = '0' ");
        sql.append(" AND d.change_id = "+id);
        return sql.toString();
    }
}
