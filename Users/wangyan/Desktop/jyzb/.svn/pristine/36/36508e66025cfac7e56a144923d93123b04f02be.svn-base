package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ConsInventoryManagement;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-06-07 11:53:30
 */
@Repository
public class ConsInventoryManagementDao extends BaseDao<ConsInventoryManagement> {

    public String toList(ConsInventoryManagement beanParam) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM cons_inventory_management");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }

    /**
     * 耗材库存总数
     *
     * @Author thr
     */
    public String getTotalCount(ConsInventoryManagement beanParam) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT IFNULL(sum(inventory_num),0) inventory_num FROM cons_inventory_management");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }

    /**
     * 以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     * 当前库存
     *
     * @Author thr
     */
    public String getInventoryNum(ConsInventoryManagement bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tm.asset_type_id type_id,\n" +
                "\tm.asset_name_id name_id,\n" +
                "\t( c.`name` ) 耗材类别,\n" +
                "\t( a.`name` ) 耗材名称,\n" +
                "\tCONCAT( a.id, '-', c.id ) nameTypeId,\n" +
                "\tCONCAT( a.`name`, '-', c.`name` ) nameType,\n" +
                "\tIFNULL(sum(inventory_num),0) amount \n" +
                "FROM\n" +
                "\tcons_inventory_management m\n" +
                "\tRIGHT JOIN cons_category c ON c.id = m.asset_type_id and c.is_del = '0' \n" +
                "\tLEFT JOIN cons_assettype a ON a.id = m.asset_name_id and a.is_del = '0' \n" +
                "WHERE\n" +
                "\tm.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tc.id,\n" +
                "\ta.id");
        return sql.toString();
    }

}
