package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.CarryWhite;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author zhangdequan
 * @create 2024-02-28 10:42:26
 */
@Repository
public class CarryWhiteDao extends BaseDao<CarryWhite> {

    public String toList(CarryWhite beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM carry_white");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }

    public String insertBatch(List<CarryWhite> carryWhiteList){
        StringBuilder sql = new StringBuilder();
        sql.append(" INSERT INTO carry_white (id,asset_id,create_time,create_user,update_time,update_user,is_del) VALUES ");
        for (CarryWhite carryWhite : carryWhiteList) {
            sql.append(" (");
            sql.append(carryWhite.getId()).append(",");
            sql.append(carryWhite.getAsset_id()).append(",");
            sql.append(carryWhite.getCreate_time()).append(",");
            sql.append(carryWhite.getCreate_user()).append(",");
            sql.append(carryWhite.getUpdate_time()).append(",");
            sql.append(carryWhite.getUpdate_user()).append(",");
            sql.append(carryWhite.getIs_del()).append(",");
            sql.append(" ),");
        }
        sql.deleteCharAt(sql.length() - 1);
        return sql.toString();
    }
}
