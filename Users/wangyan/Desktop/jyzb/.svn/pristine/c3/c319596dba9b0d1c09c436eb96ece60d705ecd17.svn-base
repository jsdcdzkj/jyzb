package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.CarryAbnormal;
import org.springframework.stereotype.Repository;

@Repository
public class CarryAbnormalDao extends BaseDao<CarryAbnormal> {

    //分页查询
    public String selectPageList(CarryAbnormal bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.*,t.RFID as asset_epc,p.position_name as position_name,p2.position_name as change_position_name FROM carry_abnormal t ");
        sql.append(" left join assets_manage m on m.id = t.assets_id");
        sql.append(" left join sys_position p on p.id = t.position_id");
        sql.append(" left join sys_position p2 on p2.id = t.change_position_id");
        sql.append(" where t.is_del = 0 ");
        if (notEmpty(bean)) {
            //外携单编号
            if (notEmpty(bean.getError_status())) {
                sql.append(" and t.error_status = '" + bean.getError_status() + "'");
            }
            if (notEmpty(bean.getNumbering())) {
                sql.append(" and t.numbering like '%" + bean.getNumbering() + "%'");
            }
            //资产id
            if (notEmpty(bean.getAssets_id())) {
                sql.append(" and t.assets_id = " + bean.getAssets_id());
            }
            //资产RFID
            if (notEmpty(bean.getRfid())) {
                sql.append(" and t.RFID like '%" + bean.getRfid() + "%'");
            }
            //资产编号
            if (notEmpty(bean.getAssetnumber())) {
                sql.append(" and t.assetnumber = '" + bean.getAssetnumber() + "'");
            }
            //资产名称
            if (notEmpty(bean.getAssetname())) {
                sql.append(" and t.assetname like '%" + bean.getAssetname() + "%'");
            }
            //使用人
            if (notEmpty(bean.getUser_id())) {
                sql.append(" and t.user_Id = " + bean.getUser_id());
            }
            //异常状态：1 未授权外携告警 2 位置变换告警 3 标签损毁告警
            if (notEmpty(bean.getError_status())) {
                sql.append(" and t.error_status = " + bean.getError_status());
            }
            //是否处理报警异常 0否（默认） 1是
            if (notEmpty(bean.getIs_handle())) {
                sql.append(" and t.is_handle = " + bean.getIs_handle());
            }
            //按资产状态统计
            if (notEmpty(bean.getSpecification())) {
                sql.append(" and m.asset_state = " + bean.getUser_id());
            }
            //存放位置
            if (notEmpty(bean.getPosition_id())) {
                sql.append(" and t.position_id = " + bean.getPosition_id());
            }
            //变动位置
            if (notEmpty(bean.getChange_position_id())) {
                sql.append(" and t.change_position_id = " + bean.getChange_position_id());
            }
            //时间范围 如2023-03-04 查询当月数据
            if (notEmpty(bean.getCreation_time_query())) {
                sql.append(" and t.creation_time >= '" + bean.getCreation_time_query() + "-01 00:00:00'");
                sql.append(" and t.creation_time <= '" + bean.getCreation_time_query() + "-31 23:59:59'");
            }
        }
        sql.append(" order by to_char(t.carry_time,'yyyy-MM-dd HH24:mi:ss') desc");
        return sql.toString();
    }


    //分页查询
    public String getPageList(CarryAbnormal bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.*,t.RFID as asset_epc," +
                "u.user_name as user_name," +
                "u2.user_name as handle_user_name, " +
                "p.position_name as change_position_name, " +
                "d.dept_name as dept_name " +
                "FROM carry_abnormal t ");
        sql.append(" LEFT join assets_manage m on m.id = t.assets_id");
        sql.append(" LEFT join sys_user u on u.id = t.user_Id");
        sql.append(" LEFT join sys_user u2 on u2.id = t.handle_user");
        sql.append(" LEFT join sys_department d on d.id = t.dept_Id");
        sql.append(" left join sys_position p on p.id = t.change_position_id");
        sql.append(" where t.is_del = 0 ");
        if (notEmpty(bean)) {
            //外携单编号
            if (notEmpty(bean.getNumbering())) {
                sql.append(" and t.numbering like '%" + bean.getNumbering() + "%'");
            }
            //资产id
            if (notEmpty(bean.getAssets_id())) {
                sql.append(" and t.assets_id = " + bean.getAssets_id());
            }
            //资产RFID
            if (notEmpty(bean.getRfid())) {
                sql.append(" and t.RFID like '%" + bean.getRfid() + "%'");
            }
            //资产编号
            if (notEmpty(bean.getAssetnumber())) {
                sql.append(" and t.assetnumber = '" + bean.getAssetnumber() + "'");
            }
            //资产名称
            if (notEmpty(bean.getAssetname())) {
                sql.append(" and t.assetname like '%" + bean.getAssetname() + "%'");
            }
            //使用人
            if (notEmpty(bean.getUser_id())) {
                sql.append(" and t.user_Id = " + bean.getUser_id());
            }
            //异常状态：1 未授权外携告警 2 位置变换告警 3 标签损毁告警
            if (notEmpty(bean.getError_status())) {
                sql.append(" and t.error_status = " + bean.getError_status());
            }
            //是否处理报警异常 0否（默认） 1是
            if (notEmpty(bean.getIs_handle())) {
                sql.append(" and t.is_handle = " + bean.getIs_handle());
            }
            //按资产状态统计
            if (notEmpty(bean.getSpecification())) {
                sql.append(" and m.asset_state = " + bean.getUser_id());
            }
            //存放位置
            if (notEmpty(bean.getPosition_id())) {
                sql.append(" and t.position_id = " + bean.getPosition_id());
            }
            //变动位置
            if (notEmpty(bean.getChange_position_id())) {
                sql.append(" and t.change_position_id = " + bean.getChange_position_id());
            }
        }
        sql.append(" order by t.handle_time desc,t.creation_time desc");
        return sql.toString();
    }


    public String selectAbnormalCount() {
        String sql = "select type.id as typeId,type.assets_type_name as name,count(bean.id) as value from carry_abnormal bean  " +
                " LEFT JOIN assets_manage ma on bean.assets_id = ma.id  " +
                " LEFT JOIN assets_type type on type.id=ma.asset_type_id " +
                " group by type.assets_type_name ORDER BY value desc LIMIT 10 ";
        return sql;
    }

    public String selectAbnormalDataCountPage(Integer asset_type_id) {
        String sql = "select ma.asset_name as name,count(1) as value from carry_abnormal na LEFT JOIN assets_manage ma on na.assets_id=ma.id\n" +
                "where ma.asset_type_id='" + asset_type_id + "'  GROUP BY na.assets_id ";
        return sql;
    }

}
