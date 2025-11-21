package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.Borrow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BorrowDao extends BaseDao<Borrow> {

    public String getPageInfo(Borrow borrow) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.borrow_code,d.dept_name department_name,b.use_id,b.use_date,b.handle_id,b.remark,b.status,b.id,b.estimate_return_date ");
        sql.append(" FROM borrow b ");
        sql.append(" LEFT JOIN sys_department d ON b.department_id = d.id ");
        sql.append(" WHERE b.is_del = '0' ");
        if (null != borrow) {
            if (notEmpty(borrow.getUse_id())) {
                sql.append(" AND b.use_id = " + borrow.getUse_id());
            }
            if (null != borrow.getDepartment_id()) {
                sql.append(" AND b.department_id = " + borrow.getDepartment_id());
            }
            if (StringUtils.isNotEmpty(borrow.getBorrow_code())) {
                sql.append(" AND b.borrow_code like'%" + borrow.getBorrow_code() + "%' ");
            }
            if (notEmpty(borrow.getStatus())) {
                sql.append(" AND b.status = '" + borrow.getStatus() + "'");
            }
        }
        sql.append(" order by b.create_time DESC ");
        return sql.toString();
    }


    public String collectionBorrowByPage(Borrow borrow, Integer userId, Integer department_id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT b.borrow_code,d.dept_name department_name,b.use_id,b.use_date,b.handle_id,b.remark,b.status,b.id,b.estimate_return_date ");
        sql.append(" FROM borrow b ");
        sql.append(" LEFT JOIN sys_department d ON b.department_id = d.id ");
        sql.append(" WHERE b.is_del = '0' ");
        sql.append(" AND b.status = 4 ");
        if (null != userId) {
            sql.append(" AND b.create_user = " + userId);
        }
        if (null != department_id) {
            sql.append(" AND b.department_id = " + department_id);
        }
        if (null != borrow) {
            if (StringUtils.isNotEmpty(borrow.getBorrow_code())) {
                sql.append(" AND b.borrow_code like'%" + borrow.getBorrow_code() + "%' ");
            }
        }
        sql.append(" order by b.create_time DESC ");
        return sql.toString();
    }
}
