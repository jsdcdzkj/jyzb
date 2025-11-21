package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.AssetsAccessRecord;
import org.springframework.stereotype.Repository;

@Repository
public class AssetsAccessRecordDao extends BaseDao<AssetsAccessRecord> {

    //分页查询
    public String selectPageList(AssetsAccessRecord bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.*,u.user_name as user_name,p.position_name as position_name FROM assets_access_record t ");
        sql.append(" inner join assets_manage m on m.id = t.assetid");
        sql.append(" left join sys_user u on u.id = t.userid");
        sql.append(" left join sys_position p on p.id = t.positionid");
        sql.append(" where t.is_del = 0 ");
        if (notEmpty(bean)) {
            //资产id
            if (notEmpty(bean.getAssetid())) {
                sql.append(" and t.assetid = " + bean.getAssetid());
            }
            //资产编码
            if (notEmpty(bean.getAssetcode())) {
                sql.append(" and t.assetcode like '%" + bean.getAssetcode() + "%'");
            }
            //资产epc
            if (notEmpty(bean.getAssetepc())) {
                sql.append(" and t.assetepc like '%" + bean.getAssetepc() + "%'");
            }
            //资产名称
            if (notEmpty(bean.getAssetname())) {
                sql.append(" and t.assetname like '%" + bean.getAssetname() + "%'");
            }
            //进出状态 1进 2出
            if (notEmpty(bean.getAccessstatus())) {
                sql.append(" and t.accessstatus = '" + bean.getAccessstatus() + "'");
            }
        }
        sql.append(" order by t.createtime desc");
        return sql.toString();
    }

    public String accessRecordTop10(){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" count( 1 ) total,");
        sql.append(" a.asset_name name");
        sql.append(" FROM");
        sql.append(" assets_access_record a ");
        sql.append(" WHERE");
        sql.append(" a.is_del = 0 ");
        sql.append(" AND a.asset_id IS NOT NULL ");
        sql.append(" GROUP BY");
        sql.append(" a.asset_id ");
        sql.append(" ORDER BY");
        sql.append(" count( 1 ) DESC ");
        sql.append(" LIMIT 10");
        return sql.toString();
    }

}
