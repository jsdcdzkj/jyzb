package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ConsReceive;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ConsReceiveDao extends BaseDao<ConsReceive> {

    /**
     * 耗材出库总数、申领出库总数
     *
     * @Author thr
     */
    public String getTotalCount(ConsReceive beanParam) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT IFNULL(sum(apply_num),0) apply_num, IFNULL(sum(out_num),0) out_num FROM cons_receive");
        sql.append(" where is_del = 0 ");
        //状态 1未送审、2未审批、3审批中、4审批通过 5审批退回
        sql.append(" and status = '4' ");
        return sql.toString();
    }

    /**
     * 领用趋势：折线图展示近六个月的领用趋势（领用单数、耗材领用总数）；
     *
     * @Author thr
     */
    public String getSixMonthCount(ConsReceive beanParam) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tc.click_date time,\n" +
                "\tIFNULL( b.apply_num, 0 ) AS apply_num,\n" +
                "\tIFNULL( b.amount, 0 ) AS amount \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tDATE_FORMAT( DATE_SUB( CURDATE( ), INTERVAL 0 MONTH ), '%Y-%m' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tDATE_FORMAT( DATE_SUB( CURDATE( ), INTERVAL 1 MONTH ), '%Y-%m' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tDATE_FORMAT( DATE_SUB( CURDATE( ), INTERVAL 2 MONTH ), '%Y-%m' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tDATE_FORMAT( DATE_SUB( CURDATE( ), INTERVAL 3 MONTH ), '%Y-%m' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tDATE_FORMAT( DATE_SUB( CURDATE( ), INTERVAL 4 MONTH ), '%Y-%m' ) AS click_date \n" +
                "\t) c\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT\n" +
                "\t\tsum( a.apply_num ) apply_num,\n" +
                "\t\tcount( a.id ) amount,\n" +
                "\t\tDATE_FORMAT( a.apply_date, '%Y-%m' ) date \n" +
                "\tFROM\n" +
                "\t\tcons_receive a \n" +
                "\tWHERE\n" +
                "\t\ta.is_del = 0 \n" +
                "\t\tAND a.STATUS = '4' \n" +
                "\t\tAND DATE_FORMAT( a.apply_date, '%Y-%m' ) > DATE_FORMAT( DATE_SUB( CURDATE( ), INTERVAL 4 MONTH ), '%Y-%m' ) \n" +
                "\tGROUP BY\n" +
                "\t\tdate \n" +
                "\t) b ON c.click_date = b.date \n" +
                "ORDER BY\n" +
                "\tc.click_date");
        return sql.toString();
    }

    public String getPageListByConsCategoryName(ConsReceive beanParam, String deptId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
            sql.append(" cr.id, ");
            sql.append(" cr.outbound_code, ");
//            sql.append(" cate.NAME AS cateName, ");
            sql.append(" cra.consumable_name AS cateName, ");
            sql.append(" cra.asset_name AS assetName , ");
            sql.append(" su.user_name as userName, ");
            sql.append(" cr.apply_date ");
        sql.append(" FROM ");
        sql.append(" cons_receive AS cr ");
        sql.append(" LEFT JOIN cons_receive_assets AS cra ON cr.id = cra.receive_id ");
//        sql.append(" LEFT JOIN cons_category cate ON cra.asset_type_id = cate.id ");
        sql.append(" LEFT JOIN sys_user AS su ON cr.use_id = su.id ");
//        sql.append(" LEFT JOIN cons_assettype cat ON cra.asset_name = cat.id ");
        sql.append(" WHERE ");
        sql.append(" cr.status = 6 ");


        if (StringUtils.isNotBlank(deptId)) {
            sql.append(" AND cr.department_id in ( " + deptId + " ) ");
        }
        if (StringUtils.isNotBlank(beanParam.getAssetName())) {
            sql.append(" AND cra.consumable_name= '" + beanParam.getAssetName() + "'");
        }
//        if (StringUtils.isNotBlank(beanParam.getAssetName())) {
//            sql.append(" AND cate.NAME= '" + beanParam.getAssetName() + "'");
//        }
        sql.append(" ORDER BY ");
        sql.append(" cr.apply_date desc ");
        return sql.toString();
    }

}
