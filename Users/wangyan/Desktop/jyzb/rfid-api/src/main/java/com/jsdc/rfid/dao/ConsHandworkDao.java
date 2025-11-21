package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ConsHandwork;
import org.springframework.stereotype.Repository;

@Repository
public class ConsHandworkDao extends BaseDao<ConsHandwork> {

    /**
     * 手动出库总数
     *
     * @Author thr
     */
    public String getTotalCount(ConsHandwork beanParam) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT IFNULL(sum(num),0) num FROM cons_handwork");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }

}
