package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.AssetsTrajectory;
import org.springframework.stereotype.Repository;

@Repository
public class AssetsTrajectoryDao extends BaseDao<AssetsTrajectory> {

    //分页查询
    public String selectPageList(AssetsTrajectory bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.*,p.position_name as place_name,p2.position_name as change_position_name FROM assets_trajectory t ");
        sql.append(" inner join assets_manage m on m.id = t.asset_id");
        sql.append(" inner join sys_position p on p.id = t.position_id");
        sql.append(" inner join sys_position p2 on p2.id = t.change_position_id");
        sql.append(" where t.is_del = 0 ");
        if (notEmpty(bean)) {
            //资产id
            if (notEmpty(bean.getAsset_id())) {
                sql.append(" and t.asset_id = " + bean.getAsset_id());
            }
            //资产编码
            if (notEmpty(bean.getAsset_code())) {
                sql.append(" and t.asset_code like '%" + bean.getAsset_code() + "%'");
            }
            //资产EPC
            if (notEmpty(bean.getAsset_epc())) {
                sql.append(" and t.asset_epc like '%" + bean.getAsset_epc() + "%'");
            }
            //资产名称
            if (notEmpty(bean.getAsset_name())) {
                sql.append(" and t.asset_name like '%" + bean.getAsset_name() + "%'");
            }
            //存放位置
            if (notEmpty(bean.getPosition_id())) {
                sql.append(" and t.position_id = '" + bean.getPosition_id() + "'");
            }
        }
        sql.append(" order by t.update_time desc,t.id desc");
        return sql.toString();
    }

}
