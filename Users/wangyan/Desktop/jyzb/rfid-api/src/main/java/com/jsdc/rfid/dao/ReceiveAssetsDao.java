package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ReceiveAssets;
import org.springframework.stereotype.Repository;

@Repository
public class ReceiveAssetsDao extends BaseDao<ReceiveAssets> {


    //根据领用单Id 获取 资产信息
    public String selectReceiveAssets(Integer receive_id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.assets_id,m.asset_code,m.asset_name,t.assets_type_name,u.user_name,a.receive_status,a.real_date,a.return_date,a.return_deal_user,r.receive_code,a.id,m.specification,m.brand_id, m.asset_state as assets_status ");
        sql.append(" FROM receive_assets a  ");
        sql.append(" LEFT JOIN assets_manage m ON a.assets_id = m.id ");
        sql.append(" LEFT JOIN assets_type t ON t.id = m.asset_type_id ");
        sql.append(" LEFT JOIN sys_user u ON m.admin_user = u.id ");
        sql.append(" LEFT JOIN receive r ON a.receive_id = r.id ");
        sql.append(" WHERE a.is_del = '0' ");
        sql.append(" AND a.receive_id = " + receive_id);
        return sql.toString();
    }
}
