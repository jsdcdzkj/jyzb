package com.jsdc.rfid.dao;

import com.jsdc.core.base.Base;
import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.AssetsManage;
import org.springframework.stereotype.Repository;
import vo.DataChartVo;

/**
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Repository
public class AssetsManageDao extends BaseDao<AssetsManage> {

    public String toList(AssetsManage beanParam) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM assets_manage");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }

    /**
     * 统计报表
     * 按分类统计
     * thr
     */
    public String classificationData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( m.id ) AS amount,\n" +
                "\tmax( t.assets_type_name ) AS name \n" +
                "FROM\n" +
                "\tassets_type t\n" +
                "\tLEFT JOIN assets_manage m ON t.id = m.asset_type_id \n" +
                "\tAND m.is_del = '0' \n" +
                "WHERE\n" +
                "\tt.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tt.id \n" +
                "ORDER BY\n" +
                "amount desc");
        return sql.toString();
    }

    /**
     * 统计报表
     * 按部门统计统计
     * thr
     */
    public String departmentData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( m.id ) AS amount,\n" +
                "\tmax( t.dept_name ) AS name \n" +
                "FROM\n" +
                "\tsys_department t\n" +
                "\tLEFT JOIN assets_manage m ON t.id = m.dept_id \n" +
                "\tAND m.is_del = '0' \n" +
                "WHERE\n" +
                "\tt.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tt.id \n" +
                "ORDER BY\n" +
                "amount desc");
        return sql.toString();
    }

    /**
     * 统计报表
     * 按品牌统计统计
     * thr
     */
    public String brandData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( m.id ) AS amount,\n" +
                "\tmax( t.label ) AS name \n" +
                "FROM\n" +
                "\tsys_dict t\n" +
                "\tLEFT JOIN assets_manage m ON t.value = m.brand_id \n" +
                "\tAND m.is_del = '0' \n" +
                "WHERE\n" +
                "\tt.is_del = '0' \n" +
                "\tAND t.type = 'brand_type' \n" +
                "GROUP BY\n" +
                "\tt.id \n" +
                "ORDER BY\n" +
                "amount desc");
        return sql.toString();
    }

    /**
     * 统计报表
     * 按购置时间统计
     * thr
     */
    public String purchaseTimeData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM assets_manage");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }

    /**
     * 统计报表
     * "按外携状态统计:
     * 未授权外携统计
     * 授权外携统计"
     * thr
     */
    public String carryStatusData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( t.id ) AS amount,\n" +
                "\tt.approval_state AS NAME \n" +
                "FROM\n" +
                "\tcarry_manage t \n" +
                "WHERE\n" +
                "\tt.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tt.approval_state \n" +
                "ORDER BY\n" +
                "amount desc");
        return sql.toString();
    }

    /**
     * 统计报表
     * 异常外携
     * 异常预警趋势
     * 最近6个月的报警数量
     * thr
     */
    public String alarmsNumsData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tc.click_date AS name,\n" +
                "\tIFNULL( b.amount, 0 ) AS amount \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 0 MONTH ), 'yyyy-MM' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 1 MONTH ), 'yyyy-MM' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 2 MONTH ), 'yyyy-MM' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 3 MONTH ), 'yyyy-MM' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 4 MONTH ), 'yyyy-MM' ) AS click_date \n" +
                "\t) c\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT\n" +
                "\t\tCOUNT( DISTINCT a.id ) amount,\n" +
                "\t\tto_char( a.creation_time, 'yyyy-MM' ) date \n" +
                "\tFROM\n" +
                "\t\tcarry_abnormal a \n" +
                "\tWHERE\n" +
                "\t\ta.creation_time BETWEEN date_sub( now( ), INTERVAL 6 MONTH ) \n" +
                "\t\tAND now( ) \n");
        if (Base.notEmpty(vo)) {
            //异常状态：1 未授权外携告警 2 位置变换告警 3 标签损毁告警
            if (Base.notEmpty(vo.getType())) {
                sql.append(" and a.error_status = ").append(vo.getType());
            }
        }
        sql.append("\tGROUP BY\n" +
                "\t\tdate \n" +
                "\t) b ON c.click_date = b.date \n" +
                "ORDER BY\n" +
                "\tc.click_date \n" +
                "\t");
        return sql.toString();
    }

    /**
     * 统计报表
     * 正常外携 近6个月
     * thr
     */
    public String carryNumsData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tc.click_date AS NAME,\n" +
               // "\tIFNULL( b.amount, 0 ) AS amount \n" +
                "case when b.amount is null or b.amount = '' then 0 else  b.amount end  amount "+
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 0 MONTH ), 'yyyy-MM' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 1 MONTH ), 'yyyy-MM' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 2 MONTH ), 'yyyy-MM' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 3 MONTH ), 'yyyy-MM' ) AS click_date UNION ALL\n" +
                "\tSELECT\n" +
                "\t\tto_char( DATE_SUB( CURDATE( ), INTERVAL 4 MONTH ), 'yyyy-MM' ) AS click_date \n" +
                "\t) c\n" +
                "\tLEFT JOIN (\n" +
                "\tSELECT\n" +
                "\t\tCOUNT( DISTINCT a.id ) amount,\n" +
                "\t\tto_char( a.creation_time, 'yyyy-MM' ) date \n" +
                "\tFROM\n" +
                "\t\tcarry_manage a \n" +
                "\tWHERE\n" +
                "\t\ta.approval_state = '2' \n" +
                "\t\tAND a.is_del = '0' \n" +
                "\t\tAND a.creation_time BETWEEN date_sub( now( ), INTERVAL 6 MONTH ) \n" +
                "\t\tAND now( ) \n" +
                "\tGROUP BY\n" +
                "\t\tdate \n" +
                "\t) b ON c.click_date = b.date \n" +
                "ORDER BY\n" +
                "\tc.click_date");
        return sql.toString();
    }

    /**
     * 统计报表
     * 基础信息：资产总数（饼状图，库内、库外 可按品类统计，默认统计全部品类）
     * thr
     */
    public String assetsRegisterTypeData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( m.id ) as amount,\n" +
                "\tm.register_type as name \n" +
                "FROM\n" +
                "\tassets_manage m \n" +
                "WHERE\n" +
                "\tm.is_del = '0' and m.register_type is not null \n");
        if (Base.notEmpty(vo)) {
            //资产品类
            if (Base.notEmpty(vo.getAsset_type_id())) {
                sql.append(" and m.asset_type_id = ").append(vo.getAsset_type_id());
            }
        }
        sql.append(" GROUP BY\n" +
                "\tm.register_type");
        return sql.toString();
    }

    /**
     * 统计报表
     * 已登记总数、闲置总数、使用总数、故障总数、异常总数、处置总数
     * thr
     */
    public String assetsTotalData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( m.id ) as amount,\n" +
                "\tm.asset_state as name \n" +
                "FROM\n" +
                "\tassets_manage m \n" +
                "WHERE\n" +
                "\tm.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tm.asset_state");
        return sql.toString();
    }

    public String statisticsByAssetsState(Integer asstestTypeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" count( 1 ) value,");
        sql.append(" asset_state,");
        sql.append(" CASE");
        sql.append("    asset_state ");
        sql.append("    WHEN 0 THEN");
        sql.append("    '闲置' ");
        sql.append("    WHEN 1 THEN");
        sql.append("    '使用' ");
        sql.append("    WHEN 2 THEN");
        sql.append("    '领用' ");
        sql.append("    WHEN 3 THEN");
        sql.append("    '变更' ");
        sql.append("    WHEN 4 THEN");
        sql.append("    '调拨' ");
        sql.append("    WHEN 5 THEN");
        sql.append("    '故障' ");
        sql.append("    WHEN 6 THEN");
        sql.append("    '处置' ");
        sql.append("    WHEN 7 THEN");
        sql.append("    '异常' ");
        sql.append(" END AS name ");
        sql.append(" FROM");
        sql.append(" assets_manage ");
        sql.append(" where 1=1");
        sql.append(" and is_del = '0'");
        if (null != asstestTypeId) {
            sql.append(" and asset_type_id = " + asstestTypeId);
        }
        sql.append(" GROUP BY");
        sql.append(" asset_state");
        return sql.toString();
    }

    /**
     * 统计报表
     * 资产位置变动排名TOP10 （柱状图，按变动次数）
     * thr
     */
    public String positionChangeTop10Data(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( t.id ) AS amount,\n" +
                "\tt.asset_id,\n" +
                "\tm.asset_name AS NAME \n" +
                "FROM\n" +
                "\tassets_trajectory t\n" +
                "\tLEFT JOIN assets_manage m ON m.id = t.asset_id \n" +
                "WHERE\n" +
                "\tt.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tt.asset_id \n" +
                "ORDER BY\n" +
                "\tamount DESC \n" +
                "\tLIMIT 10");
        return sql.toString();
    }

    /**
     * 统计报表
     * 资产报修TOP10（柱状图，按品类；点击按资产展示报修次数信息）
     * thr
     */
    public String assetRepairTop10Data(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( t.id ) AS amount,\n" +
                "\tt.assetType_Id as asset_type_id,\n" +
                "\ttp.assets_type_name AS NAME \n" +
                "FROM\n" +
                "\tapply_single t\n" +
                "\tLEFT JOIN assets_manage m ON m.id = t.assetManage_id\n" +
                "\tLEFT JOIN assets_type tp ON tp.id = t.assetType_Id \n" +
                "WHERE\n" +
                "\tt.is_del = '0' \n" +
                "GROUP BY\n" +
                "\tt.assetType_Id \n" +
                "ORDER BY\n" +
                "\tamount DESC \n" +
                "\tLIMIT 10");
        return sql.toString();
    }

    /**
     * 统计报表
     * 资产报修TOP10（柱状图，按品类；点击按资产展示报修次数信息）
     * thr
     */
    public String assetRepairCountData(DataChartVo vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n" +
                "\tcount( t.id ) AS amount,\n" +
                "\tt.assetManage_id,\n" +
                "\tm.asset_name AS NAME \n" +
                "FROM\n" +
                "\tapply_single t\n" +
                "\tLEFT JOIN assets_manage m ON m.id = t.assetManage_id\n" +
                "WHERE\n" +
                "\tt.is_del = '0' \n");
        if (Base.notEmpty(vo)) {
            //资产品类
            if (Base.notEmpty(vo.getAsset_type_id())) {
                sql.append("\tAND t.assetType_Id=").append(vo.getAsset_type_id());
            }
        }
        sql.append(" GROUP BY\n" +
                "\tt.assetManage_id\n" +
                "\tORDER BY m.asset_name");
        return sql.toString();
    }

}
