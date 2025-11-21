package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ConsReceiveAssets;
import org.springframework.stereotype.Repository;

@Repository
public class ConsReceiveAssetsDao extends BaseDao<ConsReceiveAssets> {

    /**
     * 领用排行：柱状图展示领用量TOP5的耗材（按品类）；
     *
     * @Author thr
     */
    public String getTop5ByCategory(ConsReceiveAssets bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tc.id,\n" +
                "\tmax( c.`name` ) name,\n" +
                "\tIFNULL( sum( t.apply_num ), 0 ) amount \n" +
                "FROM\n" +
                "\tcons_category c\n" +
                "\tLEFT JOIN cons_receive_assets t ON c.id = t.asset_type_id AND t.is_del = '0' \n" +
                "\tLEFT JOIN cons_receive a ON a.id = t.receive_id AND a.is_del = '0' AND a.status = '4' \n" +
                "WHERE\n" +
                "\tc.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tc.id \n" +
                "ORDER BY\n" +
                "\tamount desc \n" +
                "\tLIMIT 5");
        return sql.toString();
    }

    /**
     * 部门领用占比：饼状图展示各部门耗材领用占比；
     *
     * @Author thr
     */
    public String getCountByDept(ConsReceiveAssets bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tc.id,\n" +
                "\tc.parent_dept as parent_id ,\n" +
                "\tmax( c.`dept_name` ) NAME,\n" +
                "\tIFNULL( sum( t.apply_num ), 0 ) amount \n" +
                "FROM\n" +
                "\tsys_department c\n" +
                "\tLEFT JOIN cons_receive a ON a.department_id = c.id \n" +
                "\tAND a.is_del = '0' \n" +
                "\tAND a.STATUS = '4'\n" +
                "\tLEFT JOIN cons_receive_assets t ON t.receive_id = a.id \n" +
                "\tAND t.is_del = '0' \n" +
                "WHERE\n" +
                "\tc.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tc.id");
        return sql.toString();
    }

    /**
     * 以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     * 领用量
     *
     * @Author thr
     */
    public String getApplyNum(ConsReceiveAssets bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tm.asset_type_id type_id,\n" +
                "\tm.asset_name name_id,\n" +
                "\t( c.`name` ) 耗材类别,\n" +
                "\t( a.`name` ) 耗材名称,\n" +
                "\tCONCAT( a.id, '-', c.id ) nameTypeId,\n" +
                "\tCONCAT( a.`name`, '-', c.`name` ) nameType,\n" +
                "\tIFNULL(sum(apply_num),0) amount \n" +
                "FROM\n" +
                "\tcons_receive_assets m\n" +
                "\tRIGHT JOIN cons_category c ON c.id = m.asset_type_id and c.is_del = '0' \n" +
                "\tLEFT JOIN cons_assettype a ON a.id = m.asset_name and a.is_del = '0'  \n" +
                "WHERE\n" +
                "\tm.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tc.id,\n" +
                "\ta.id");
        return sql.toString();
    }
}
