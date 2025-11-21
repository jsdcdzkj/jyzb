package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.InventoryManagement;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Repository
public class InventoryManagementDao extends BaseDao<InventoryManagement> {

    public String toList(InventoryManagement beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM inventory_management");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }

    public String getListByAssetsTypeName(String assetTypeName , String startDay,String endDay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" ast.assets_type_name as assetsTypeName, ");
        sql.append(" im.asset_name, ");
        sql.append(" im.actual_amount, ");
        sql.append(" IFNULL( im.inventory_num, 0 ) AS inventory_num, ");
        sql.append(" im.warehousing_time ");
        sql.append(" FROM ");
        sql.append(" inventory_management AS im ");
        sql.append(" LEFT JOIN assets_type AS ast ON im.asset_type_id = ast.id ");
        sql.append(" WHERE ");
        sql.append(" assets_type_name = '" + assetTypeName +"' ");
        if (null != startDay && startDay != "") {
            sql.append(" and  im.warehousing_time >= '").append(startDay).append("'");

        }
        if (null != endDay && endDay != "") {
            sql.append(" AND im.warehousing_time < '").append(endDay).append("' ");
        }
        sql.append(" ORDER BY im.warehousing_time DESC");
        return sql.toString();
    }
}
