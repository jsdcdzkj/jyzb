package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ConsPurchaseDetail;
import org.springframework.stereotype.Repository;

/**
 * ClassName: PurchaseDetailDao
 * Description:
 * date: 2022/4/24 17:02
 *
 * @author bn
 */
@Repository
public class ConsPurchaseDetailDao extends BaseDao<ConsPurchaseDetail> {

    /**
     * 采购趋势：折线图展示近六个月的采购趋势（采购单数、耗材总数）
     * 耗材总数
     *
     * @Author thr
     */
    public String getSixMonthCount(ConsPurchaseDetail bean) {
        StringBuilder sql = new StringBuilder();
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
//                "\t\tDATE_FORMAT(a.apply_time, '%Y-%m' ) > DATE_FORMAT(DATE_SUB( CURDATE( ), INTERVAL 4 MONTH ), '%Y-%m' )\n" +
//                "\tGROUP BY\n" +
//                "\t\tdate \n" +
//                "\t) b ON c.click_date = b.date \n" +
//                "ORDER BY\n" +
//                "\tc.click_date \n");
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
        return sql.toString();
    }

    /**
     * 采购排行：柱状图展示采购量TOP5的耗材（按品类）；
     *
     * @Author thr
     */
    public String getTop5ByCategory(ConsPurchaseDetail bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tc.id,\n" +
                "\tmax( c.`name` ) name,\n" +
                "\tIFNULL( sum( t.purchase_num ), 0 ) amount \n" +
                "FROM\n" +
                "\tcons_category c\n" +
                "\tLEFT JOIN cons_purchase_detail t ON c.id = t.assets_type_id AND t.is_del = '0'\n" +
                "\tLEFT JOIN cons_purchase_apply a ON a.id = t.purchase_apply_id AND a.is_del = '0' AND a.approve_status = '4' \n" +
                "WHERE\n" +
                "\tc.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tc.id \n" +
                "ORDER BY\n" +
                "\tamount desc \n" +
                "\tLIMIT 5");
        return sql.toString();
    }

}
