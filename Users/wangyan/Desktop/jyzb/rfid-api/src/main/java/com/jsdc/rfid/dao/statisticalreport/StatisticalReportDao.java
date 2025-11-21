package com.jsdc.rfid.dao.statisticalreport;

import com.jsdc.rfid.model.ConsInventoryManagement;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * ClassName: StatisticalReportDao
 * Description:
 * date: 2023/3/8 14:49
 *
 * @author bn
 */
@Repository
public class StatisticalReportDao {

    public String assetCollectionTopDetail(String typeName,String osIds) {
        return "SELECT\n" +
                "\tcr.receive_code as outbound_code ,\n" +
                "\t1 as use_num,\n" +
                //"-- \tca.asset_type_id,\n" +
                "\tcim.*,\n" +
                "\tat.assets_type_name,\n" +
                "\tsu.user_name as apply_user_name,\n" +
                "\tcim.asset_name as catName,\n" +
                "\tca.receive_status as STATUS,\n" +
                "\tto_char(cr.create_time, 'yyyy-MM-dd HH24:mi:ss') as datetime\n" +
                "FROM\n" +
                "\treceive cr\n" +
                "\tLEFT JOIN receive_assets ca ON ca.receive_id = cr.id\n" +
                "\tLEFT JOIN assets_manage cim ON ca.assets_id = cim.id\n" +
                "\tLEFT JOIN assets_type at ON at.id = cim.asset_type_id \n" +
                "\tLEFT JOIN sys_user su on su.id = cr.use_id\n" +
                //"-- \tLEFT JOIN cons_assettype cat on cim.asset_name_id = cat.id\n" +
                "WHERE\n" +
                "\tcr.is_del = 0 \n" +
                //"-- \tAND ( cr.STATUS = 4 or cr.status = 6) \n" +
                "\tAND ca.is_del = 0 \n" +
                "\tAND ca.receive_status = 2 \n" +
                //"-- \tAND ca.use_num != 0 \n" +
                //"-- \tAND ca.id IS NOT NULL \t\n" +
                //"\tAND at.assets_type_name = '测试类别'\t\n" +
                (StringUtils.isEmpty(osIds) ? "" : "and cr.department_id in ( " + osIds + ")\n") +
                (StringUtils.isEmpty(typeName) ? "" : "\tAND at.assets_type_name = '" + typeName + "'") +
                "\tORDER BY cr.create_time desc";
//        return "SELECT\n" +
//                "\tcr.outbound_code ,\n" +
//                "\tca.use_num,\n" +
//                "\tca.asset_type_id,\n" +
//                "\tcim.*,\n" +
//                "\tat.assets_type_name,\n" +
//                "\tsu.user_name as apply_user_name,\n" +
//                "\tcat.name as catName,\n" +
//                "\tcr.STATUS,\n" +
//                "\tto_char(cr.create_time, 'yyyy-MM-dd HH24:mi:ss') as datetime\n" +
//                "FROM\n" +
//                "\tcons_receive cr\n" +
//                "\tLEFT JOIN cons_receive_assets ca ON ca.receive_id = cr.id\n" +
//                "\tLEFT JOIN cons_inventory_management cim ON ca.cons_id = cim.id\n" +
//                "\tLEFT JOIN assets_type at ON at.id = ca.asset_type_id \n" +
//                "\tLEFT JOIN sys_user su on su.id = cr.use_id\n" +
//                "\tLEFT JOIN cons_assettype cat on cim.asset_name_id = cat.id\n" +
//                "WHERE\n" +
//                "\tcr.is_del = 0 \n" +
//                "\tAND ( cr.STATUS = 4 or cr.status = 6) \n" +
//                "\tAND ca.is_del = 0 \n" +
//                "\tAND ca.use_num != 0 \n" +
//                "\tAND ca.cons_id IS NOT NULL " +
//                (StringUtils.isEmpty(osIds) ? "" : "and cr.department_id in ( " + osIds + ")\n") +
//                (StringUtils.isEmpty(typeName) ? "" : "\tAND at.assets_type_name = '" + typeName + "'") +
//                "\tORDER BY cr.create_time desc";
    }

    public String purchaseTopDetail(String typeName,String startTime,String endTime) {

//        return "SELECT\n" +
//                "\tpa.*,\n" +
//                "\tsu.user_name as apply_user_name,\n" +
//                "\t\tcase when pd.accept_num * pd.actual_price = 0 then 0 else pd.accept_num * pd.actual_price end  AS count,\n" +
//                "\t\tcase when pd.purchase_price * pd.purchase_num = 0 then 0 else pd.purchase_price * pd.purchase_num end  AS pCount,\n" +
//                "\tpd.assets_name as name,\n" +
//                "\tat.assets_type_name,\n" +
//                "\tto_char(pa.apply_time, 'yyyy-MM-dd HH24:mi:ss') as datetime\n" +
//                "FROM\n" +
//                "\tpurchase_apply pa\n" +
//                "\tLEFT JOIN purchase_detail pd ON pa.id = pd.purchase_apply_id\n" +
//                "\tLEFT JOIN assets_type at ON pd.assets_type_id = at.id \n" +
//                "\tLEFT JOIN sys_user su on pa.apply_user = su.id\n" +
//                "WHERE\n" +
//                "\tpa.approve_status = 4 \n" +
//                "\tAND pa.is_del = 0 " +
//                (StringUtils.isEmpty(typeName) ? "" : "\tAND at.assets_type_name = '" + typeName + "'") +
//                (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) ? "" : "and pa.create_time > '" + startTime + "'and pa.create_time <='" + endTime + "'") +
//                "\tORDER BY pa.create_time desc";
        return "SELECT\n" +
                "\tpa.*,\n" +
                "\tsu.user_name as apply_user_name,\n" +
                "\t\tcase when pd.accept_num * pd.actual_price = 0 then 0 else pd.accept_num * pd.actual_price end  AS count,\n" +
                "\t\tcase when pd.purchase_price * pd.purchase_num = 0 then 0 else pd.purchase_price * pd.purchase_num end  AS pCount,\n" +
                "\tpd.name as name,\n" +
                "\tat.consumable_name as assets_type_name,\n" +
                "\tto_char(pa.apply_time, 'yyyy-MM-dd HH24:mi:ss') as datetime\n" +
                "FROM\n" +
                "\tcons_purchase_apply pa\n" +
                "\tLEFT JOIN cons_purchase_detail pd ON pa.id = pd.purchase_apply_id\n" +
                "\tLEFT JOIN consumable at ON pd.consumable_id2 = at.id \n" +
                "\tLEFT JOIN sys_user su on pa.apply_user = su.id\n" +
                "WHERE\n" +
                "\tpa.approve_status = 4 \n" +
                "\tAND pa.is_del = 0 \tORDER BY pa.create_time desc";
    }

    public String statCountPurchaseOrderNowYear(String deptIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tsm.m1,\n" +
                "\t case when c is null or c = '' then 0 else c end  purchaseTime  \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\t1 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t2 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t3 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t4 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t5 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t6 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t7 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t8 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t9 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t10 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t11 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t12 AS m1 \n" +
                "\t) sm\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT to_char ( apply_time,'MM' )  AS m2,\n" +
                "\t\tCOUNT( id ) c \n" +
                "\tFROM\n" +
                "\t\tpurchase_apply \n" +
                "\tWHERE\n" +
                "\t\tis_del = 0 \n" +
                "\t\tAND inspected_status = '2' \n" +
                "\t\t AND  to_char( apply_time ,'yyyy' )= to_char (NOW(),'yyyy')  \n");
        if (!StringUtils.isEmpty(deptIdList)) {
            sql.append("\t\tAND dept_id IN ( " + deptIdList + ") \n");
        }

        sql.append("\tGROUP BY\n" +
                "\t\tto_char( apply_time,'MM' )  \n" +
                "\tORDER BY\n" +
                "\t\tto_char( apply_time,'MM' ) ASC \n" +
                "\t) t ON sm.m1 = t.m2\n" +
                "ORDER BY\n" +
                "\tsm.m1");
        return sql.toString();
    }

    public String statCountPurchaseOrderPreviousYear(String deptIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tsm.m1,\n" +
                "\t case when c is null or c = '' then 0 else c end  purchaseTime  \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\t1 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t2 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t3 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t4 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t5 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t6 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t7 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t8 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t9 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t10 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t11 AS m1 UNION\n" +
                "\tSELECT\n" +
                "\t\t12 AS m1 \n" +
                "\t) sm\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT to_char (apply_time,'MM') AS m2,\n" +
                "\t\tCOUNT( id ) c \n" +
                "\tFROM\n" +
                "\t\tpurchase_apply \n" +
                "\tWHERE\n" +
                "\t\tis_del = 0 \n" +
                "\t\tAND inspected_status = '2' \n" +
                "\t\tAND  to_char( apply_time ,'yyyy' )= to_char (NOW(),'yyyy' )  \n");
        if (!StringUtils.isEmpty(deptIdList)) {
            sql.append("\t\tAND dept_id IN ( " + deptIdList + ") \n");
        }

        sql.append("\tGROUP BY to_char ( apply_time,'MM' ) " +
                "\tORDER BY to_char ( apply_time,'MM' ) ASC \n" +
                "\t) t ON sm.m1 = t.m2\n" +
                "ORDER BY\n" +
                "\tsm.m1");
        return sql.toString();
    }

    /**
     * 资产领用排行TOP10 @cz
     *
     * @param osIds 部门列表
     * @return 结果集
     */
    public String assetCollectionTop(String osIds) {
//        return "select sum(ca.use_num) as count,ca.asset_type_id,at.assets_type_name from cons_receive cr\n" +
//                "left join cons_receive_assets ca on ca.receive_id = cr.id\n" +
//                "left join cons_inventory_management cim on ca.cons_id = cim.id\n" +
//                "left join assets_type at on at.id = ca.asset_type_id\n" +
//                "where cr.is_del = 0 and cr.status = 4 and ca.is_del = 0 and ca.cons_id is not null and at.id is not null\n" +
//                (StringUtils.isEmpty(osIds) ? "" : "and cr.department_id in ( " + osIds + ")\n") +
//                "Group by ca.asset_type_id\n" +
//                "ORDER BY count desc\n" +
//                "limit 10";
        return "  select  count( * ) as count, am.asset_type_id,  min(at.assets_type_name) as assets_type_name " +
                " from " +
                " receive cr inner join receive_assets ca on ca.receive_id = cr.id inner join assets_manage am on am.id = ca.assets_id left join assets_type at on at.id = am.asset_type_id  " +
                " where " +
                " cr.is_del = 0  and cr.status = 4   and ca.receive_status = 2   and ca.is_del = 0  and at.id is not null " +
                (StringUtils.isEmpty(osIds) ? "" : "\t and cr.department_id IN ( " + osIds + " ) \n") +
                " group by am.asset_type_id  " +
                " order by  count desc  " +
                " limit 10 ";
    }

    /**
     * 采购总值 @cz
     */
    public String censusPriceSum() {
        return "SELECT\n" +
                "\tsum( actual_amount )\n" +
                "FROM\n" +
                "\tcons_purchase_apply \n" +
                "WHERE\n" +
                "\tis_del = 0 \n" +
                "\tAND approve_status = 4";
    }

    /**
     * 耗材库存总数 @cz
     */
    public String getTotalSum() {
        return "select sum(inventory_num * actual_amount)from cons_inventory_management\n" +
                "where is_del = 0";
    }

    /**
     * 获取出库总值 @cz
     */
    public String getOutSum() {
        return "select sum(ca.use_num * cim.actual_amount) as count from cons_receive cr\n" +
                "left join cons_receive_assets ca on ca.receive_id = cr.id\n" +
                "left join cons_inventory_management cim on ca.cons_id = cim.id\n" +
                "where cr.is_del = 0 and cr.status = 4 and ca.is_del = 0 and ca.cons_id is not null";
    }

    /**
     * 采购全额排行(TOP5) @cz
     */
    public String purchaseTop(String startTime,String endTime) {
        return "select * from ( SELECT\n" +
                "                min(pa.id) paid,min(pd.id) pdid,\n" +
                "\t\t\t\t\t\t\t\tsum(pd.accept_num * pd.actual_price) as count\n" +
//                "\t\t\t\t\t\t\t\tat.assets_type_name\n" +
                "                FROM\n" +
                "                purchase_apply pa\n" +
                "\t\t\t\t\t\t\t\tleft join purchase_detail pd on pa.id = pd.purchase_apply_id\n" +
                "\t\t\t\t\t\t\t\tleft join assets_type at on pd.assets_type_id = at.id\n" +
                "                WHERE\n" +
                "                pa.approve_status = 4 \n" +
                "                AND pa.is_del = 0 \n" +
                (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) ? "" : "and pa.create_time > '" + startTime + "'and pa.create_time <='" + endTime + "'") +
                "\t\t\t\t\t\t\t\tGROUP BY at.id\n" +
                "\t\t\t\t\t\t\t\tORDER BY count desc\n" +
                "                LIMIT 5 ) t left join assets_type at on t.pdid = at.id";
    }

    /**
     * 获取子部门外携排行 @cz
     */
    public String censusAllCarryCountByDept() {
        return "SELECT\n" +
                "\tam.dept_id as child_id,\n" +
                "\tCOUNT(am.dept_id) as count\n" +
                "FROM\n" +
                "\tassets_manage am\n" +
                "\tRIGHT JOIN ( SELECT asset_manage_id FROM carry_manage WHERE is_del = 0 AND approval_state = 2 UNION ALL SELECT assets_id AS asset_manage_id FROM carry_abnormal WHERE is_del = 0 and error_status = 1 ) cam ON am.id = cam.asset_manage_id\n" +
                "GROUP BY am.dept_id";
    }

    /**
     * 异常外携统计 @cz
     */
    public String censusTopOfAbnormalCarryByAssetsType() {
        return "SELECT\n" +
                "\tat.id,\n" +
                "\tat.assets_type_name,\n" +
                "\tcount(\n" +
                "\tat.id) as count\n" +
                "FROM\n" +
                "\tcarry_abnormal cb\n" +
                "\tLEFT JOIN assets_manage am ON cb.assets_id = am.id\n" +
                "\tLEFT JOIN assets_type at ON am.asset_type_id = at.id \n" +
                "WHERE\n" +
                "\tcb.is_del = 0 \n" +
                "\tand cb.error_status = 1 \n" +
                "GROUP BY\n" +
                "\tat.id\n" +
                "ORDER BY count desc\n" +
                "limit 5";
    }

    /**
     * 正常外携统计 @cz
     */
    public String censusTopOfNormalCarryByAssetsType() {
        return "SELECT\n" +
                "\tat.id,\n" +
                "\tat.assets_type_name,\n" +
                "\tcount(\n" +
                "\tat.id) as count\n" +
                "FROM\n" +
                "\tcarry_manage cm\n" +
                "\tLEFT JOIN assets_manage am ON cm.asset_manage_id = am.id\n" +
                "\tLEFT JOIN assets_type at ON am.asset_type_id = at.id \n" +
                "WHERE\n" +
                "\tcm.is_del = 0 \n" +
                "-- \tAND cm.approval_state = 2 \n" +
                "GROUP BY\n" +
                "\tat.id\n" +
                "ORDER BY count desc\n" +
                "limit 5";
    }


    /**
     * 最近十二个月，包含当月
     *
     * @return
     */
    public String month() {
        StringBuilder sql = new StringBuilder();
        sql.append("(SELECT to_char((CURDATE()), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 1 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 2 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 3 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 4 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 5 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 6 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 7 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 8 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 9 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 10 MONTH), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 11 MONTH), 'yyyy-MM') AS `month`) d ");


        return sql.toString();
    }

    public String getManageOrPurchaseYears() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT to_char(ma.management_date, 'yyyy') years FROM management_assets ma ");
        sql.append("LEFT JOIN management m ON ma.management_id = m.id ");
        sql.append("LEFT JOIN assets_manage am ON ma.assets_id = am.id ");
        sql.append("WHERE ma.`status` = 1 AND m.`status` = 4 AND m.is_del = 0 ");
        sql.append("AND ma.management_date IS NOT NULL AND am.quantity IS NOT NULL AND am.unit_price IS NOT NULL ");
        sql.append("UNION ");
        sql.append("SELECT to_char(pa.inspected_time, 'yyyy') years FROM purchase_detail pd ");
        sql.append("LEFT JOIN purchase_apply pa ON pd.purchase_apply_id = pa.id ");
        sql.append("WHERE pd.is_del = 0 AND pa.is_del = 0 AND pa.inspected_status = 2 ");
        sql.append("AND pa.approve_status = 4 AND pa.inspected_time IS NOT NULL ");

        return sql.toString();
    }

    /**
     * 最近12个月（包含当月）审批通过的资产处置金额趋势
     * 时间为处置时间
     * 主表状态为审批通过
     * 子表状态为已处置
     *
     * @return
     */
    public String managementMonth(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT months,COALESCE (sum(tmp.prices), 0) AS prices from (");
        sql.append("SELECT to_char(ma.management_date,'yyyy-MM') months,am.quantity * am.unit_price AS prices ");
        sql.append("FROM management_assets ma LEFT JOIN management m ON ma.management_id = m.id ");
        sql.append("LEFT JOIN assets_manage am ON ma.assets_id = am.id ");
        sql.append("WHERE ma.status = 1 AND m.status = 4 AND m.is_del = 0 AND ma.management_date IS NOT NULL ");
        sql.append("AND am.quantity IS NOT NULL AND am.unit_price IS NOT NULL ");
        if (!StringUtils.isEmpty(year)) {
            sql.append(" AND ma.management_date>= '" + String.format("%s-01-01 00:00:00'", year));
            sql.append(" AND ma.management_date<='" + String.format("%s-12-31 23:59:59'", year));
        }
        sql.append(") tmp GROUP BY months ");
        return sql.toString();
    }

    /**
     * 最近12个月（包含当月）审批通过,已验收的资产采购金额趋势
     * 时间为验收时间
     * 主表状态：审批通过，已验收
     *
     * @return
     */
    public String purchaseMonth(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT months,COALESCE (sum(tmp.prices), 0) AS prices from ( ");
        sql.append("SELECT to_char(pa.inspected_time,'yyyy-MM') months, ");
        sql.append("(pd.accept_num * pd.actual_price) prices FROM purchase_detail pd ");
        sql.append("LEFT JOIN purchase_apply pa ON pd.purchase_apply_id = pa.id ");
        sql.append("WHERE pd.is_del = 0 AND pa.is_del = 0 AND pa.inspected_status = 2 ");
        sql.append("AND pa.approve_status = 4 AND pa.inspected_time IS NOT NULL ");
        if (!StringUtils.isEmpty(year)) {
            sql.append(" AND pa.inspected_time>= '" + String.format("%s-01-01 00:00:00'", year));
            sql.append(" AND pa.inspected_time<='" + String.format("%s-12-31 23:59:59'", year));
        }
        sql.append(") tmp GROUP BY months ");

        return sql.toString();
    }


    /**
     * 最近12个月报修数量
     * 时间 创建时间
     *
     * @return
     */
    public String applySingleCount(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT months,COUNT(id) AS prices from ( ");
        sql.append("SELECT to_char(aps.creation_time, 'yyyy-MM') months,aps.id FROM apply_single aps ");
        sql.append("WHERE aps.is_del = 0 ");
        if (!StringUtils.isEmpty(year)) {
            sql.append(" AND aps.creation_time>= '" + String.format("%s-01-01 00:00:00'", year));
            sql.append(" AND aps.creation_time<='" + String.format("%s-12-31 23:59:59'", year));
        }
        sql.append(") tmp GROUP BY months ");

        return sql.toString();
    }

    /**
     * 最近12个月报修处理数量
     * 时间 报修处理结束时间
     *
     * @return
     */
    public String applySingleFeebackCount(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT months,COUNT(id) AS prices FROM( ");
        sql.append("SELECT DISTINCT aps.id,to_char(f.endTime, 'yyyy-MM') months FROM apply_single aps ");
        sql.append("LEFT JOIN feedback f ON aps.id = f.applysingle_Id ");
        sql.append("WHERE aps.is_del = 0 AND aps.state = 7 AND f.endTime is NOT NULL ");
        if (!StringUtils.isEmpty(year)) {
            sql.append(" AND f.endTime>= '" + String.format("%s-01-01 00:00:00'", year));
            sql.append(" AND f.endTime<='" + String.format("%s-12-31 23:59:59'", year));
        }
        sql.append(") tmp GROUP BY tmp.months ");


        return sql.toString();
    }


    public String getApplyOrFeebackYears() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT to_char(aps.creation_time, '%Y') years FROM apply_single aps WHERE aps.is_del = 0 ");
        sql.append("UNION ");
        sql.append("SELECT DISTINCT to_char(f.endTime, '%Y') years FROM apply_single aps ");
        sql.append("LEFT JOIN feedback f ON aps.id = f.applysingle_Id ");
        sql.append("WHERE aps.is_del = 0 AND aps.state = 7 AND f.endTime IS NOT NULL ");
        return sql.toString();
    }

    /**
     * 按月统计异常外携数量
     * 时间：外携时间
     *
     * @return
     */
    public String carryAbnormalCount(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT months,COUNT(id) AS prices FROM ( ");
        sql.append("SELECT cb.id,to_char(cb.creation_time, 'yyyy-MM') months FROM carry_abnormal cb ");
        sql.append("WHERE cb.is_del = 0 ");
        if (!StringUtils.isEmpty(year)) {
            sql.append(" AND cb.creation_time>= '" + String.format("%s-01-01 00:00:00'", year));
            sql.append(" AND cb.creation_time<='" + String.format("%s-12-31 23:59:59'", year));
        }
        sql.append(") tmp GROUP BY months ");


        return sql.toString();
    }

    /**
     * 按月统计正常外携数量
     *
     * @param year
     * @return
     */
    public String carryManageCount(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT months,COUNT(id) AS prices from( ");
        sql.append("SELECT cm.id,to_char(cm.creation_time, 'yyyy-MM') months from carry_manage cm ");
        sql.append("WHERE cm.is_del=0 AND approval_state=2 ");
        if (!StringUtils.isEmpty(year)) {
            sql.append(" AND cm.creation_time>= '" + String.format("%s-01-01 00:00:00'", year));
            sql.append(" AND cm.creation_time<='" + String.format("%s-12-31 23:59:59'", year));
        }
        sql.append(") tmp GROUP BY months ");


        return sql.toString();
    }

    public String getCarryManageYears() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT to_char(cb.creation_time, 'yyyy') years FROM carry_abnormal cb WHERE cb.is_del = 0 ");
        sql.append("UNION ");
        sql.append("SELECT to_char(cm.creation_time, 'yyyy') years from carry_manage cm WHERE cm.is_del=0 AND approval_state=2 ");
        return sql.toString();
    }

    /**
     * 供应商采购排行(TOP10)
     * 按申请单金额TOP10
     *
     * @return
     */
    public String supplierProcurementRankingMoney() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT s.id, s.supplier_name as companyName, sum(pa.actual_amount)  as money ");
        sql.append(" FROM supplier s ");
        sql.append(" LEFT JOIN purchase_apply pa ON pa.supplier_id = s.id ");
        sql.append(" WHERE pa.is_del = 0 AND pa.approve_status = 4");
        sql.append(" GROUP BY s.id ORDER BY money desc");
        sql.append(" LIMIT 10");
        return sql.toString();
    }


    /**
     * 供应商采购排行(TOP10)
     * 按申请单数量TOP10
     *
     * @return
     */
    public String supplierProcurementRankingApply(String startDay, String endDay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT s.id , s.supplier_name AS companyName, count(*) AS count FROM supplier s ");
        sql.append(" LEFT JOIN purchase_apply pa ON pa.supplier_id = s.id ");
        sql.append(" WHERE pa.is_del = 0 AND pa.approve_status = 4 ");
        if(null !=startDay && startDay != ""){
            sql.append(" and  pa.create_time >= '").append(startDay).append("'");
        }
        if(null !=endDay && endDay != "") {
            sql.append(" AND pa.create_time < '").append(endDay).append("' ");
        }
        sql.append(" GROUP BY s.id ORDER BY count desc ");
        sql.append(" LIMIT 10 ");

        return sql.toString();
    }

    /**
     * 采购金额排行<TOP5>
     */
    public String purchaseAmountRanking() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT  actual_amount AS amount, purchase_name AS purName FROM purchase_apply");
        sql.append(" WHERE approve_status = 4 ");
        sql.append(" ORDER BY actual_amount  + 0 desc");
        sql.append(" LIMIT 5 ");
        return sql.toString();
    }

    /**
     * 最新十二个月 部门采购单趋势
     */
    public String getProcurementTrendNow(String join) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.month,COUNT(pa.id) count FROM ").append(month());
        sql.append(" LEFT JOIN purchase_apply pa ON pa.is_del = 0 and pa.dept_Id in (" + join + ") and to_char(pa.apply_time, 'yyyy-MM') = d.month ");
        sql.append(" GROUP BY d.month ORDER BY d.month ");
        return sql.toString();
    }

    /**
     * 同比十二个月 部门采购单趋势
     */
    public String getProcurementTrendOnce(String join) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.month,COUNT(pa.id) count FROM ");
        sql.append("(SELECT to_char((CURDATE() - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 1 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 2 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 3 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 4 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 5 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 6 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 7 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 8 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 9 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 10 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month` ");
        sql.append("UNION SELECT to_char((CURDATE() - INTERVAL 11 MONTH - INTERVAL 1 YEAR), 'yyyy-MM') AS `month`) " +
                "d ");
        sql.append(" LEFT JOIN purchase_apply pa ON pa.is_del = 0 and pa.dept_Id in (" + join + ") and to_char(" +
                "(pa.apply_time), 'yyyy-MM') = d.month ");
        sql.append(" GROUP BY d.month ORDER BY d.month ");
        return sql.toString();
    }


    /***
     * @Description: 统计资产总值
     * @param:
     * @return:
     * @author: csx
     * @date: 2023/3/8
     * @time: 11:49
     */

    public String totalAssets() {
        StringBuilder str = new StringBuilder();
        str.append("SELECT\n" +
                "\tSUM( a.total ) total,\n" +
                "\ta.id id,\n" +
                "\t min(b.dept_name) as dept_name  \n" +
                "FROM\n" +
                "\t(\n" +
                "\t\t(\n" +
                "\t\tSELECT\n" +
//                "\t\t\tSUM( a.quantity * a.unit_price ) total,\n" +
                "\t\t\tSUM( a.unit_price ) total,\n" +
                "\t\t\ta.dept_id id \n" +
                "\t\tFROM\n" +
                "\t\t\tassets_manage a \n" +
                "\t\tWHERE\n" +
                "\t\t\ta.dept_id IN ( SELECT a.id FROM sys_department a WHERE parent_dept = 0 ) \n" +
                "\t\t\tAND a.is_del = 0 \n" +
                "\t\t\tAND a.dispose_state IS NULL \n" +
                "\t\tGROUP BY\n" +
                "\t\t\ta.dept_id \n" +
                "\t\tORDER BY\n" +
                "\t\t\ttotal DESC \n" +
                "\t\t) UNION ALL\n" +
                "\t\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tSUM( a.quantity * a.unit_price ) total,\n" +
                "\t\t\t     min(b.parent_dept) id \n" +
                "\t\tFROM\n" +
                "\t\t\tassets_manage a\n" +
                "\t\t\tLEFT JOIN sys_department b ON a.dept_id = b.id \n" +
                "\t\tWHERE\n" +
                "\t\t\ta.dept_id IN (\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\ta.id \n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tsys_department a \n" +
                "\t\t\tWHERE\n" +
                "\t\t\ta.parent_dept IN ( SELECT a.id FROM sys_department a WHERE parent_dept = 0 )) \n" +
                "\t\t\tAND a.is_del = 0 \n" +
                "\t\t\tAND a.dispose_state IS NULL \n" +
                "\t\tGROUP BY\n" +
                "\t\t\ta.dept_id \n" +
                "\t\tORDER BY\n" +
                "\t\t\ttotal DESC \n" +
                "\t\t) UNION ALL\n" +
                "\t\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\ta.total,\n" +
                "\t\t\tb.parent_dept id \n" +
                "\t\tFROM\n" +
                "\t\t\t(\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\tSUM( a.quantity * a.unit_price ) total,\n" +
                "\t\t\t\t min(b.parent_dept) id \n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tassets_manage a\n" +
                "\t\t\t\tLEFT JOIN sys_department b ON a.dept_id = b.id \n" +
                "\t\t\tWHERE\n" +
                "\t\t\t\ta.dept_id IN (\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\ta.id \n" +
                "\t\t\t\tFROM\n" +
                "\t\t\t\t\tsys_department a \n" +
                "\t\t\t\tWHERE\n" +
                "\t\t\t\t\ta.parent_dept IN (\n" +
                "\t\t\t\t\tSELECT\n" +
                "\t\t\t\t\t\ta.id \n" +
                "\t\t\t\t\tFROM\n" +
                "\t\t\t\t\t\tsys_department a \n" +
                "\t\t\t\t\tWHERE\n" +
                "\t\t\t\t\ta.parent_dept IN ( SELECT a.id FROM sys_department a WHERE parent_dept = 0 ))) \n" +
                "\t\t\t\tAND a.is_del = 0 \n" +
                "\t\t\t\tAND a.dispose_state IS NULL \n" +
                "\t\t\tGROUP BY\n" +
                "\t\t\t\ta.dept_id \n" +
                "\t\t\tORDER BY\n" +
                "\t\t\t\ttotal DESC \n" +
                "\t\t\t) a\n" +
                "\t\t\tLEFT JOIN sys_department b ON a.id = b.id \n" +
                "\t\t) \n" +
                "\t) a\n" +
                "\tLEFT JOIN sys_department b ON a.id = b.id \n" +
                " where b.is_del = 0 "+
                "GROUP BY\n" +
                "\ta.id \n" +
                //"ORDER BY a.total DESC \n" +
                "\tLIMIT 0,\n" +
                "\t10;");
        return str.toString();
    }

    /**
     * @Description: 统计未打印标签资产
     * @param: []
     * @return: java.lang.String
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:41
     */
    public String unprintedAsset(String deptId) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT\n" +
                "\tSUM(a.quantity) total,\n" +
                "\tmin(b.assets_type_name) typeName \n" +
                "FROM\n" +
                "\tassets_manage a\n" +
                "\tLEFT JOIN assets_type b ON a.asset_type_id = b.id \n" +
                "WHERE\n" +
                "\ta.is_print = 0 \n" +
                "\tAND a.is_del = 0 \n" +
                "\tAND a.dispose_state IS NULL ");
        if (!StringUtils.isEmpty(deptId)) {
            str.append("\tAND a.dept_id in (  " + deptId + ")\n");
        }
        str.append("GROUP BY\n" +
                "\ta.asset_type_id\n" +
                "\tORDER BY\n" +
                "\ttotal DESC \n" +
                "\tLIMIT 0,\n" +
                "\t10 ;");
        return str.toString();
    }


    /**
     * @Description: 各部门 #领用
     * @param: []
     * @return: java.lang.String
     * @author: csx
     * @date: 2023/3/8
     * @time: 14:47
     */
    public String receive(String deptId, List<String> times) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT\n" +
                "\tSUM(a.apply_num) total,to_char(a.apply_date,'yyyy-MM') times\n" +
                "FROM\n" +
                "\t\tcons_receive a\n" +
                "\tLEFT JOIN cons_receive_assets b ON b.receive_id = a.id\n" +
                "\tLEFT JOIN cons_assettype c ON c.id = b.asset_type_id \n" +
                "WHERE\n" +
                "\t\ta.is_del = 0 \n" +
                "\tAND b.is_del = 0 \n" +
                "\tAND c.is_del = 0 \n" +
                "\tAND a.status=4\n" );
        if (!StringUtils.isEmpty(deptId)) {
            str.append("\tAND a.department_id in (  " + deptId + ")\n");
        }
        if (!StringUtils.isEmpty(times)) {
            str.append("\tAND to_char(a.apply_date,'yyyy-MM') IN (");
            for (int i = 0; i < times.size(); i++) {
                if (i == 0) {
                    str.append("'" + times.get(i) + "'");
                } else {
                    str.append(",'" + times.get(i) + "'");
                }
            }
            str.append(")");
        }
        str.append("GROUP BY\n" +
                "\t to_char(a.apply_date,'yyyy-MM')");
        return str.toString();
    }

    /**
     * @Description: 各部门资产数量
     * @param: [deptId, times]
     * @return: java.lang.String
     * @author: csx
     * @date: 2023/3/10
     * @time: 16:01
     */
    public String assetsManage(String deptId, List<String> times) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT\n" +
                "\tSUM( a.quantity ) total,\n" +
                "\t\tto_char(a.purchase_time,'yyyy-MM') times\n" +
                "FROM\n" +
                "\tassets_manage a \n" +
                "WHERE\n" +
                "\ta.is_del = 0 ");
        if (!StringUtils.isEmpty(deptId)) {
            str.append("\tAND a.dept_id in (  " + deptId + ")\n");
        }
        if (!StringUtils.isEmpty(times)) {
            str.append("\tAND to_char(a.purchase_time,'yyyy-MM') IN (");
            for (int i = 0; i < times.size(); i++) {
                if (i == 0) {
                    str.append("'" + times.get(i) + "'");
                } else {
                    str.append(",'" + times.get(i) + "'");
                }
            }
            str.append(")");
        }
        str.append("GROUP BY\n" +
                "\tto_char(a.purchase_time,'yyyy-MM');");
        return str.toString();
    }

    /**
     * @Description: 各部门采购领用对比  #采购
     * @param: []
     * @return: java.lang.String
     * @author: csx
     * @date: 2023/3/8
     * @time: 16:52
     */
    public String purchase(String deptId, List<String> times) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT\n" +
                "\tCOUNT(1) total,min(to_char(a.apply_time,'yyyy-MM')) times\n" +
                //"\tCOUNT(1) total,min(STR_TO_DATE(a.apply_time,'yyyy-MM')) times\n" +
                "FROM\n" +
                "\tcons_purchase_apply a\n" +
                "\tLEFT JOIN cons_purchase_detail b ON a.id = b.purchase_apply_id\n" +
                "\tLEFT JOIN cons_assettype c ON c.id = b.assets_type_id \n" +
                "WHERE\n" +
                "\ta.approve_status = 4 \n" +
                "\tAND a.is_del = 0 \n" +
                "\tAND b.is_del = 0 \n");
        if (!StringUtils.isEmpty(deptId)) {
            str.append("\tAND a.dept_id in (  " + deptId + ")\n");
        }
        if (!StringUtils.isEmpty(times)) {
            str.append("\tAND to_char(a.apply_time,'yyyy-MM') IN (");
            //str.append("\tAND STR_TO_DATE(a.apply_time,'yyyy-MM') IN (");
            for (int i = 0; i < times.size(); i++) {
                if (i == 0) {
                    str.append("'" + times.get(i) + "'");
                } else {
                    str.append(",'" + times.get(i) + "'");
                }
            }
            str.append(")");
        }
        str.append(" GROUP BY\n" +
                "\tto_char(a.inspected_time,'yyyy-MM');");
                //"\tSTR_TO_DATE(a.inspected_time,'yyyy-MM');");
        return str.toString();
    }

    /**
     * @Author: yc
     * @Params: 以资产类型分类, 统计库存数量 TOP10
     * @Return:
     * @Description：
     * @Date ：2023/3/8 下午 4:33
     * @Modified By：
     */
    public String statTop10InventoryNumByTypeName(String startDay , String endDay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" ast.assets_type_name, ");
        sql.append(" case when SUM( im.inventory_num ) is null or SUM( im.inventory_num ) = '' then 0 else SUM( im.inventory_num ) end  sum  ");
        sql.append(" FROM ");
        sql.append(" assets_type AS ast ");
        sql.append(" LEFT JOIN inventory_management AS im ON im.is_del = 0  AND ast.id = im.asset_type_id   ");
        sql.append(" WHERE ast.is_del = 0 ");
        if (null != startDay && startDay != "") {
            sql.append(" and  im.warehousing_time >= '").append(startDay).append("'");

        }
        if (null != endDay && endDay != "") {
            sql.append(" AND im.warehousing_time < '").append(endDay).append("' ");
        }
        sql.append("GROUP BY ast.assets_type_name ");
        sql.append("ORDER BY sum DESC limit 10");
        return sql.toString();
    }

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description： 1.库存总价值
     * @Date ：2023/3/9 上午 9:03
     * @Modified By：
     */
    public String statInventoryTotalAssert() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tsum( im.actual_amount * im.inventory_num ) AS totalAsset \n" +
                "FROM\n" +
                "\tinventory_management AS im");
        return sql.toString();
    }

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：库存总数
     * @Date ：2023/3/10 下午 2:00
     * @Modified By：
     */
    public String statInventoryTotalAmount() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tsum( im.inventory_num ) AS totalNum\n" +
                "FROM\n" +
                "\tinventory_management AS im ");
        return sql.toString();
    }

    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：库存按品类计算总价值
     * @Date ：2023/3/9 上午 9:39
     * @Modified By：
     */
    public String statInventoryAssertByType() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                        "\tATP.assets_type_name,\n" +
                        "\tsum( t1.num ) AS totalSum,\n" +
                        "\tsum( t1.asset ) AS totalAsset \n" +
                        "FROM\n" +
                        "\t(\n" +
                        "\tSELECT\n" +
                        "\t\tasset_name,\n" +
                        "\t\tasset_type_id,\n" +
                        "\t\tim.actual_amount,\n" +
                        "\t\tim.inventory_num AS num,\n" +
                        "\t\t( im.inventory_num * im.actual_amount ) AS asset \n" +
                        "\tFROM\n" +
                        "\t\tinventory_management AS im \n" +
                        "\tWHERE\n" +
                        "\t\tim.inventory_num IS NOT NULL \n" +
                        "\t) t1\n" +
                        "\tLEFT JOIN assets_type AS ATP ON t1.asset_type_id = ATP.id \n" +
                        "GROUP BY\n" +
                        "\tATP.assets_type_name \n"
//                "ORDER BY\n" +
//                "\ttotalAsset DESC"
        );
        return sql.toString();
    }


    /**
     * @Author: yc
     * @Params:
     * @Return:
     * @Description：资产状态数量
     * @Date ：2023/3/9 上午 11:12
     * @Modified By：
     */
    public String statAssertNumByType(Integer assertTypeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( 1 ) AS \n" +
                "VALUE\n" +
                "\t,\n" +
                "\tasset_state,\n" +
                "CASE\n" +
                "\t\tasset_state \n" +
                "\t\tWHEN 0 THEN\n" +
                "\t\t'闲置' \n" +
                "\t\tWHEN 1 THEN\n" +
                "\t\t'使用' \n" +
                "\t\tWHEN 2 THEN\n" +
                "\t\t'领用' \n" +
                "\t\tWHEN 3 THEN\n" +
                "\t\t'变更' \n" +
                "\t\tWHEN 4 THEN\n" +
                "\t\t'调拨' \n" +
                "\t\tWHEN 5 THEN\n" +
                "\t\t'故障' \n" +
                "\t\tWHEN 6 THEN\n" +
                "\t\t'处置' \n" +
                "\t\tWHEN 7 THEN\n" +
                "\t\t'异常' \n" +
                "END AS NAME \n" +
                "FROM\n" +
                "\tassets_manage \n" +
                "WHERE\n" +
                "\tis_del = '0'\n");
        if (null != assertTypeId) {
            sql.append("and asset_type_id = " + assertTypeId);
        }
        sql.append(" GROUP BY\n" +
                "\tasset_state");
        return sql.toString();
    }

    /**
     * 耗材领用排行TOP10
     *
     * @return
     */
    public String consumableReceive(String deptIds) {
        StringBuilder sql = new StringBuilder();

        sql.append(" select * from (");
        sql.append("  SELECT  sum( receive.out_num * management.actual_amount) as totalNum,management.consumable_id as asset_type_id ,MIN( cate.consumable_name ) AS categoryName");
        sql.append("  FROM");
        sql.append("  	cons_receive receive");
        sql.append("  	LEFT JOIN cons_receive_assets con ON receive.id = con.receive_id ");
        sql.append("  	LEFT JOIN cons_inventory_management management ON con.cons_id = management.id ");
        sql.append("  	LEFT JOIN consumable cate ON management.consumable_id = cate.ID ");
        sql.append("  WHERE");
        sql.append("  management.is_del = 0 ");
        sql.append(" AND receive.status = 6 ");
        if (null != deptIds && deptIds != "") {
            sql.append(" and receive.department_id in ( ").append(deptIds).append(")");
        }

        sql.append("  group by management.consumable_id  ");
        sql.append("  ) t ORDER BY totalNum desc LIMIT 10");

        return sql.toString();
    }

    /**
     * 部门领用总值排行
     *
     * @return
     */
    public String receiveTotalValue(String startDay, String endDay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT receive.department_id, receive.out_num, management.actual_amount, receive.out_num * management.actual_amount as totalNum ");
        sql.append(" FROM");
        sql.append(" cons_receive receive");
        sql.append(" LEFT JOIN cons_receive_assets con ON receive.id = con.receive_id 	AND receive.is_finish = 1 ");
        sql.append(" LEFT JOIN cons_inventory_management management ON con.cons_id = management.id        ");
        sql.append(" WHERE ");
        sql.append(" management.is_del = 0 ");
        if (null != startDay && startDay != "") {
            sql.append(" and  receive.apply_date >= '").append(startDay).append("'");

        }
        if (null != endDay && endDay != "") {
            sql.append(" AND receive.apply_date < '").append(endDay).append("' ");
        }
        return sql.toString();
    }


    /**
     * 最近几个月
     *
     * @return
     */
    public String getMonth(Integer times, Integer year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT to_char((CURDATE()- INTERVAL " + year + " YEAR), 'yyyy-MM') AS `month` ");
        for (int i = 1; i <= times - 1; i++) {
            sql.append("UNION SELECT to_char((CURDATE()- INTERVAL " + year + " YEAR - INTERVAL " + i + " MONTH), 'yyyy-MM') AS " +
                    "`month` ");
        }
        sql.append(" ORDER BY `month` asc");
        return sql.toString();
    }

    /**
     * 以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     * 当前库存
     *
     * @Author thr  copy by lb
     */
    public String getInventoryNum(ConsInventoryManagement bean) {
        StringBuilder sql = new StringBuilder();
        sql.append("  SELECT *, c.consumable_name nametype from ( SELECT\n" +
                        "\tmin(m.asset_type_id) type_id,\n" +
                        "\tmin(m.asset_name_id) name_id,\n" +
//                        "\tmin( c.consumable_name ) 耗材类别,\n" +
                        //"\t( a.name ) 耗材名称,\n" +
                        "\tCONCAT( c.id ) nameTypeId,\n" +
//                        "\tCONCAT( c.consumable_name ) nameType,\n" +
                        //"\tIFNULL(sum(inventory_num),0) amount \n" +
                        "case when sum( inventory_num ) is null or sum( inventory_num ) = '' then 0 else  sum( inventory_num) end  amount "+
                        "FROM\n" +
                        "\tcons_inventory_management m\n" +
                        "\tRIGHT JOIN consumable c ON c.id = m.consumable_id and c.is_del = '0' \n" +
                        "\tLEFT JOIN cons_assettype a ON a.id = m.asset_name_id and a.is_del = '0' \n" +
                        "WHERE\n" +
                        "\tm.is_del = '0' \n" +
                        "GROUP BY\n" +
                        "\tc.id\n" +
                " ) t LEFT JOIN consumable c on c.id = t.nametypeid\n"
               /* "\tc.id,\n" +
                "\ta.id"*/
        );
        return sql.toString();
    }


    /**
     * 以列表方式展现耗材消耗速率（耗材品类、耗材名称、日均消耗/领用量、当前库存、预计可用天数）
     * 领用量
     *
     * @Author thr
     */
    public String getApplyNum(String startTime, String endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("  select * , c.consumable_name 耗材类别, c.consumable_name nametype  from ( SELECT\n" +
                "\tmin(m.asset_type_id) type_id,\n" +
                "\tmin(m.asset_name) name_id,\n" +
//                "\tmin( c.name ) 耗材类别,\n" +
                //"\t( a.`name` ) 耗材名称,\n" +
                "\tCONCAT( c.id ) nameTypeId,\n" +
//                "\tCONCAT( c.name) nameType,\n" +
                //"\tIFNULL(sum(apply_num),0) amount \n" +
                "\tcase when sum(apply_num) is null or sum(apply_num) = '' then 0 else  sum(apply_num) end  amount  " +
                "FROM\n" +
                "\tcons_receive_assets m\n" +
                "\tRIGHT JOIN consumable c ON c.id = m.asset_type_id and c.is_del = '0' \n" +
//                "\tRIGHT JOIN cons_category c ON c.id = m.asset_type_id and c.is_del = '0' \n" +
//                "\tLEFT JOIN cons_assettype a ON a.id = m.asset_name and a.is_del = '0'  \n" +
                "WHERE\n" +
                "\tm.is_del = '0' \n").append(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) ? "" : "and m.create_time > '" + startTime + "'and m.create_time <='" + endTime + "'").append("GROUP BY\n" +
                "\tc.id\n" +
                ") t left join consumable c on c.id = t.nametypeid"
        );
                /*"\tc.id,\n" +
                "\ta.id");*/
        return sql.toString();
    }

    /**
     * 采购排行：柱状图展示采购量TOP5的耗材（按品类）；
     */
    public String getTop5ByCategory(String startDay, String endDay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT\n" +
                "\tc.id,\n" +
                "\tmax( c.consumable_name ) name,\n" +
                "\tcase when sum( t.purchase_num ) is null or sum( t.purchase_num ) = '' then 0 else  sum( t.purchase_num ) end  amount  \n" +
                "FROM\n" +
                "\tconsumable c\n" +
                "\tLEFT JOIN cons_purchase_detail t ON c.id = t.consumable_id2 AND t.is_del = '0'\n" +
                "\tLEFT JOIN cons_purchase_apply a ON a.id = t.purchase_apply_id AND a.is_del = '0' AND a.approve_status = '4' \n" +
                "WHERE\n" +
                "\tc.is_del = '0' and a.apply_time is NOT null \n");

        if (null != startDay && startDay != "") {
            sql.append(" and  a.apply_time >= '").append(startDay).append("'");

        }
        if (null != endDay && endDay != "") {
            sql.append(" AND a.apply_time < '").append(endDay).append("' ");
        }

        sql.append(
                "GROUP BY\n" +
                        "\tc.id \n" +
                        "ORDER BY\n" +
                        "\tamount desc \n" +
                        "\tLIMIT 5");
        return sql.toString();
    }


    /**
     * 部门耗材领用排行
     * 时间：申请时间
     * 申请状态：已完成，出库状态：已完成
     *
     * @param year
     * @return
     */
    public String deptReceiveTop(String year) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cr.department_id,SUM(apply_num) applyNums from cons_receive cr ");
        sql.append("WHERE cr.is_del=0 AND cr.status =6 AND cr.is_finish=1 ");
        if (!StringUtils.isEmpty(year)) {
            sql.append(" AND cr.apply_date>= '" + String.format("%s-01-01 00:00:00'", year));
            sql.append(" AND cr.apply_date<='" + String.format("%s-12-31 23:59:59'", year));
        }
        sql.append("GROUP BY department_id ORDER BY applyNums  DESC ");


        return sql.toString();
    }


    public String deptReceiveView(String deptIdStr) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cr.receive_code,cat.`name`,cr.apply_date,cra.apply_num,cra.use_num from cons_receive_assets cra  ");
        sql.append("LEFT JOIN cons_receive cr ON cra.receive_id=cr.id ");
        sql.append("LEFT JOIN cons_assettype cat ON cra.asset_name=cat.id ");
        sql.append("WHERE cr.is_del=0 AND cr.`status`=6 AND cr.is_finish=1 ");
        sql.append("AND cr.department_id in (" + deptIdStr + ")");
        return sql.toString();
    }

    public String purchaseRanking(Integer categoryId , String startDay , String endDay){
        String sql= "SELECT\n" +
               // "\tIFNULL( t.purchase_num , 0 ) amount ,\n" +
                " case when t.purchase_num = 0 then 0 else t.purchase_num end  amount ," +
                "\ta.*,t.category_id as category_id\n" +
                "FROM\n" +
                "\tconsumable c\n" +
                "\tLEFT JOIN cons_purchase_detail t ON c.id = t.consumable_id2 \n" +
                "\tAND t.is_del = '0'\n" +
                "\tLEFT JOIN cons_purchase_apply a ON a.id = t.purchase_apply_id \n" +
                "\tAND a.is_del = '0' \n" +
                "\tAND a.approve_status = '4' \n" +
                "WHERE\n" +
                "\tc.is_del = '0'  and a.apply_time is NOT null \n" +
                "\tand c.id = \n" + categoryId +"";

        if(null !=startDay && startDay != ""){
            sql += " and  a.apply_time >= '" + startDay + "'";

        }
        if(null !=endDay && endDay != "") {
            sql += " AND a.apply_time < '" + endDay +"' ";
        }
        sql += " ORDER BY  a.apply_time desc";

        return sql.toString();
    }

    /**
     * 部门领用总值排行
     *
     * @return
     */
    public String deptConsumingTotalDetails(String join ,String startDay, String endDay) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT receive.out_num, management.actual_amount, receive.out_num * management.actual_amount as totalNum ,receive.* ");
        sql.append(" FROM");
        sql.append(" cons_receive receive");
        sql.append(" LEFT JOIN cons_receive_assets con ON receive.id = con.receive_id 	AND receive.is_finish = 1 ");
        sql.append(" LEFT JOIN cons_inventory_management management ON con.cons_id = management.id        ");
        sql.append(" WHERE ");
        sql.append(" management.is_del = 0 ");
        if(null !=startDay && startDay != ""){
            sql.append(" and  receive.apply_date >= '").append(startDay).append("'");

        }
        if(null !=endDay && endDay != "") {
            sql.append(" AND receive.apply_date < '").append(endDay).append("' ");
        }
        if (null != join){
            sql.append("and receive.department_id in (").append(join).append(")");
        }
        sql.append(" ORDER BY  receive.apply_date desc");
        return sql.toString();
    }


    /**
     * @Description: 资产总值排行详情
     * @param: [name]
     * @return: java.lang.String
     * @author: csx
     * @date: 2023/3/15
     * @time: 10:29
     */
    public String totalAssetsDetails(String name) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT\n" +
                "\ta.*\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\ta.*\n" +
                "\t\tFROM\n" +
                "\t\t\tassets_manage a \n" +
                "\t\tWHERE\n" +
                "\t\t\ta.dept_id IN ( SELECT a.id FROM sys_department a WHERE a.dept_name='" + name + "' AND a" +
                ".is_del=0 ) \n" +
                "\t\t\tAND a.is_del = 0 \n" +
                "\t\t\tAND a.dispose_state IS NULL \n" +
                "\t\t) UNION ALL\n" +
                "\t\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\ta.*\n" +
                "\t\tFROM\n" +
                "\t\t\tassets_manage a\n" +
                "\t\t\tLEFT JOIN sys_department b ON a.dept_id = b.id \n" +
                "\t\tWHERE\n" +
                "\t\t\ta.dept_id IN (\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\ta.id \n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tsys_department a \n" +
                "\t\t\tWHERE\n" +
                "\t\t\ta.parent_dept IN ( SELECT a.id FROM sys_department a WHERE a.dept_name='" + name + "' AND a" +
                ".is_del=0 ))" +
                " \n" +
                "\t\t\tAND a.is_del = 0 \n" +
                "\t\t\tAND a.dispose_state IS NULL \n" +
                "\t\t) UNION ALL\n" +
                "\t\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\ta.*\n" +
                "\t\tFROM\n" +
                "\t\t\t(\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\ta.*\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tassets_manage a\n" +
                "\t\t\t\tLEFT JOIN sys_department b ON a.dept_id = b.id \n" +
                "\t\t\tWHERE\n" +
                "\t\t\t\ta.dept_id IN (\n" +
                "\t\t\t\tSELECT\n" +
                "\t\t\t\t\ta.id \n" +
                "\t\t\t\tFROM\n" +
                "\t\t\t\t\tsys_department a \n" +
                "\t\t\t\tWHERE\n" +
                "\t\t\t\t\ta.parent_dept IN (\n" +
                "\t\t\t\t\tSELECT\n" +
                "\t\t\t\t\t\ta.id \n" +
                "\t\t\t\t\tFROM\n" +
                "\t\t\t\t\t\tsys_department a \n" +
                "\t\t\t\t\tWHERE\n" +
                "\t\t\t\t\ta.parent_dept IN ( SELECT a.id FROM sys_department a WHERE a.dept_name='" + name + "' AND " +
                "a" +
                ".is_del=0 ))) \n" +
                "\t\t\t\tAND a.is_del = 0 \n" +
                "\t\t\t\tAND a.dispose_state IS NULL \n" +
                "\t\t\t) a\n" +
                "\t\t\tLEFT JOIN sys_department b ON a.id = b.id \n" +
                "\t\t) \n" +
                "\t) a\n" +
                "\tLEFT JOIN sys_department b ON a.id = b.id  \n" +
                "\t");
        return str.toString();
    }

/**
 * @Description: 统计未打印标签资产详情
 * @param: [name, deptId]
 * @return: java.lang.String
 * @author: csx
 * @date: 2023/3/15
 * @time: 10:29
 */
    public String unprintedAssetDetails(String name, String deptId) {
        StringBuilder str = new StringBuilder();
        str.append("\tSELECT\n" +
                "\ta.*\n" +
                "FROM\n" +
                "\tassets_manage a\n" +
                "\tLEFT JOIN assets_type b ON a.asset_type_id = b.id \n" +
                "WHERE\n" +
                "\ta.is_print = 0 \n" +
                "\tAND a.is_del = 0 \n" +
                "\tAND a.dispose_state IS NULL \n");
        if (!StringUtils.isEmpty(deptId)) {
            str.append("\tAND a.dept_id in (  " + deptId + ")\n");
        }
        str.append("\tAND b.assets_type_name='" + name + "'");
        return  str.toString();
    }

    public String getListByAssetsTypeName(String assetTypeName, String startDay, String endDay ){
        StringBuilder str = new StringBuilder();
        // IFNULL(im.inventory_num,0)  as inventory_num,
        str.append(" SELECT im.*,ast.assets_type_name as assetTypeName FROM  inventory_management im LEFT JOIN assets_type AS ast ON im.asset_type_id = ast.id  ");
        str.append(" WHERE im.is_del = 0 ");
        str.append(" AND ast.assets_type_name = '"+assetTypeName+"' ");
        if (null != startDay && startDay != "") {
            str.append(" and  im.warehousing_time >= '").append(startDay).append("'");

        }
        if (null != endDay && endDay != "") {
            str.append(" AND im.warehousing_time < '").append(endDay).append("' ");
        }
        str.append("ORDER BY im.warehousing_time desc");
        return str.toString();
    }
}
