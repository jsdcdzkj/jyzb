package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ConsPurchaseApply;
import org.springframework.stereotype.Repository;
import vo.ConsPurchaseApplyVo;

/**
 * ClassName: ConsPurchaseApplyDao
 * Description:
 * date: 2022/4/24 16:59
 *
 * @author bn
 */
@Repository
public class ConsPurchaseApplyDao extends BaseDao<ConsPurchaseApply> {

    public String toList(ConsPurchaseApplyVo purchaseApplyVo) {
        StringBuilder sql = new StringBuilder();

        sql.append(" ");

        return sql.toString();
    }

    public String purchaseFromSupplierTo10() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" count( 1 ) total,");
        sql.append(" s.supplier_name name ");
        sql.append(" FROM");
        sql.append(" purchase_apply a ");
        sql.append(" LEFT JOIN supplier s on a.supplier_id = s.id");
        sql.append(" WHERE");
        sql.append(" a.is_del = 0 ");
        sql.append(" AND a.supplier_id IS NOT NULL ");
        sql.append(" GROUP BY");
        sql.append(" a.supplier_id ");
        sql.append(" ORDER BY");
        sql.append(" count( 1 ) DESC ");
        sql.append(" LIMIT 10");
        return sql.toString();
    }

    /**
     * 采购趋势：折线图展示近六个月的采购趋势（采购单数、耗材总数）
     *
     * @Author thr
     */
    public String getSixMonthCount(ConsPurchaseApply bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tc.click_date as time,\n" +
                "\t(case WHEN b.amount IS NULL THEN 0 ELSE b.amount END) as amount\n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tTO_CHAR(CURRENT_DATE - INTERVAL 0 month, 'YYYY-MM')  AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tTO_CHAR(CURRENT_DATE - INTERVAL 1 month, 'YYYY-MM') AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tTO_CHAR(CURRENT_DATE - INTERVAL 2 month, 'YYYY-MM') AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tTO_CHAR(CURRENT_DATE - INTERVAL 3 month, 'YYYY-MM') AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tTO_CHAR(CURRENT_DATE - INTERVAL 4 month, 'YYYY-MM') AS click_date \n" +
                "\t) c\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT\n" +
                "\t\tCOUNT( a.id ) amount,\n" +
                "\t\tTO_CHAR( a.apply_time, 'YYYY-MM' ) as date \n" +
                "\tFROM\n" +
                "\t\tcons_purchase_apply a \n" +
                "\tWHERE\n" +
                "\t\ta.is_del = 0 \n" +
                "\t\tAND a.approve_status = '4' \n" +
                "\t\tAND TO_CHAR(a.apply_time, 'YYYY-MM' ) > TO_CHAR(CURRENT_DATE - INTERVAL 4 month, 'YYYY-MM')\n" +
                "\tGROUP BY\n" +
                "\t\tdate \n" +
                "\t) b ON c.click_date = b.date \n" +
                "ORDER BY\n" +
                "\tc.click_date ");
//        sql.append(" SELECT\n" +
//                "\tc.click_date time,\n" +
//                "\tIFNULL( b.amount, 0 ) AS amount\n" +
//                "FROM\n" +
//                "\t(\n" +
//                "\tSELECT\n" +
//                "\t\tDATE_FORMAT(DATE_SUB( CURDATE( ), INTERVAL 0 MONTH ), '%Y-%m' )  AS click_date UNION ALL\n" +
//                "\tSELECT\n" +
//                "\t\tDATE_FORMAT(DATE_SUB( CURDATE( ), INTERVAL 1 MONTH ), '%Y-%m' ) AS click_date UNION ALL\n" +
//                "\tSELECT\n" +
//                "\t\tDATE_FORMAT(DATE_SUB( CURDATE( ), INTERVAL 2 MONTH ), '%Y-%m' ) AS click_date UNION ALL\n" +
//                "\tSELECT\n" +
//                "\t\tDATE_FORMAT(DATE_SUB( CURDATE( ), INTERVAL 3 MONTH ), '%Y-%m' ) AS click_date UNION ALL\n" +
//                "\tSELECT\n" +
//                "\t\tDATE_FORMAT(DATE_SUB( CURDATE( ), INTERVAL 4 MONTH ), '%Y-%m' ) AS click_date \n" +
//                "\t) c\n" +
//                "\tLEFT JOIN (\n" +
//                "\tSELECT\n" +
//                "\t\tCOUNT( a.id ) amount,\n" +
//                "\t\tDATE_FORMAT( a.apply_time, '%Y-%m' ) date \n" +
//                "\tFROM\n" +
//                "\t\tcons_purchase_apply a \n" +
//                "\tWHERE\n" +
//                "\t\ta.is_del = 0 \n" +
//                "\t\tAND a.approve_status = '4' \n" +
//                "\t\tAND DATE_FORMAT(a.apply_time, '%Y-%m' ) > DATE_FORMAT(DATE_SUB( CURDATE( ), INTERVAL 4 MONTH ), '%Y-%m' )\n" +
//                "\tGROUP BY\n" +
//                "\t\tdate \n" +
//                "\t) b ON c.click_date = b.date \n" +
//                "ORDER BY\n" +
//                "\tc.click_date \n");
        return sql.toString();
    }

}
