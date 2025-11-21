package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.SysDepartment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class SysDepartmentDao extends BaseDao<SysDepartment> {

    public String getPage(SysDepartment department){
        StringBuilder sql = new StringBuilder();
        sql.append(" select");
        sql.append(" d.*,");
        sql.append(" dd.dept_name parent_name,");
        sql.append(" p.position_name");
        sql.append(" from sys_department d");
        sql.append(" left join sys_department dd on d.parent_dept = dd.id");
        sql.append(" left join sys_position p on d.dept_position = p.id");
        sql.append(" where 1=1");
        if(null != department){
            if(StringUtils.isNotEmpty(department.getDept_name())){
                sql.append(" and d.dept_name like '%").append(department.getDept_name().trim()).append("%'");
            }
            if(StringUtils.isNotEmpty(department.getDept_code())){
                sql.append(" and d.dept_code like '%").append(department.getDept_code().trim()).append("%'");
            }
            if(null != department.getDept_position()){
                sql.append(" and d.dept_position = ").append(department.getDept_position());
            }
        }
        sql.append(" and d.is_del = 0");
        sql.append(" and d.is_enable = 1");
        return sql.toString();
    }
}
