package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.SysCron;
import org.springframework.stereotype.Repository;

@Repository
public class SysCronDao extends BaseDao<SysCron> {

    public String selectPageList(SysCron bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from sys_cron where is_del = 0");
        if (notEmpty(bean)) {
            if (notEmpty(bean.getType())) {
                buffer.append(" and type = ").append(bean.getType());
            }
        }
        buffer.append(" order by id");
        return buffer.toString();
    }

}
