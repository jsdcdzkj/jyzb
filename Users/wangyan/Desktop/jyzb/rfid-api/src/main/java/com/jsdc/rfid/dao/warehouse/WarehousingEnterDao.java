package com.jsdc.rfid.dao.warehouse;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.warehouse.WarehousingEnter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.StringJoiner;

@Repository
public class WarehousingEnterDao extends BaseDao<WarehousingEnter> {
    public String sumEquipNum(List<Integer> ids) {
        // 使用 StringBuilder 代替 String 拼接
        StringBuilder sql = new StringBuilder("SELECT enter_id, SUM(equip_num) AS equip_num \n");
        sql.append("FROM warehousing_enter_detail \n");

        // 使用 StringJoiner 来安全地拼接 id 列表
        if (ids != null && !ids.isEmpty()) {
            sql.append("WHERE enter_id IN (");
            StringJoiner joiner = new StringJoiner(",");
            for (Integer id : ids) {
                joiner.add(id.toString());  // 将每个 id 转为字符串并加入 joiner
            }
            sql.append(joiner.toString()).append(") \n");
        }

        sql.append("GROUP BY enter_id");

        return sql.toString();
    }

}
