package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ExternalMaintenance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ExternalMaintenanceDao extends BaseDao<ExternalMaintenance> {
    public String getPage(ExternalMaintenance externalMaintenance){
        StringBuilder sql = new StringBuilder();
        sql.append(" select");
        sql.append(" em.*");
        sql.append(" from externalmaintenance em");
        sql.append(" where 1=1");
        if(null != externalMaintenance){
            if(StringUtils.isNotEmpty(externalMaintenance.getUnitname())){
                sql.append(" and em.unitName like '%").append(externalMaintenance.getUnitname().trim()).append("%'");
            }
            if(StringUtils.isNotEmpty(externalMaintenance.getUnitcode())){
                sql.append(" and em.unitCode like '%").append(externalMaintenance.getUnitcode().trim()).append("%'");
            }
        }
        sql.append(" and em.is_del = 0");
        sql.append(" ORDER BY em.id ");
        return sql.toString();
    }
}
