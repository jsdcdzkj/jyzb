package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.Management;
import com.jsdc.rfid.model.ManagementAssets;
import org.springframework.stereotype.Repository;

@Repository
public class ManagementAssetsDao  extends BaseDao<ManagementAssets> {

    public String getInfoByManId(Integer id){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ma.assets_id,ma.id,a.asset_code,a.asset_name,a.specification,a.brand_id,t.assets_type_name,u.user_name,m.management_code,ma.proposal,ma.status,ma.reason,ma.money,ma.management_user,ma.management_date ");
        sql.append(" FROM management_assets ma ");
        sql.append(" INNER JOIN assets_manage a ON ma.assets_id = a.id ");
        sql.append(" INNER JOIN assets_type t ON a.asset_type_id = t.id ");
        sql.append(" INNER JOIN sys_user u ON a.admin_user = u.id ");
        sql.append(" INNER JOIN management m ON ma.management_id = m.id ");
        sql.append(" WHERE ma.is_del = '0' ");
        sql.append(" AND ma.management_id = "+id);
        return sql.toString();
    }

    public String managementByAssetsType(){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT *, at.assets_type_name as name from (");
        sql.append(" SELECT");
        sql.append(" count( 1 ) value,");
        sql.append(" AT.id deptId");
//        sql.append(" AT.assets_type_name name");
        sql.append(" FROM");
        sql.append(" management_assets ma");
        sql.append(" LEFT JOIN assets_manage am ON ma.assets_id = am.id");
        sql.append(" LEFT JOIN assets_type AT ON am.asset_type_id = AT.id ");
        sql.append(" WHERE");
        sql.append(" ma.assets_id IS NOT NULL ");
        sql.append(" AND AT.id IS NOT NULL ");
        sql.append(" GROUP BY");
        sql.append(" AT.id ");
        sql.append(" ORDER BY");
        sql.append(" count( 1 ) DESC ");
        sql.append(" LIMIT 10");
        sql.append(" ) t LEFT JOIN assets_type AT ON t.deptId = AT.ID ");
        return sql.toString();
    }
}
