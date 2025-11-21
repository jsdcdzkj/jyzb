package com.jsdc.rfid.dao;


import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.Management;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class ManagementDao extends BaseDao<Management> {

    public String getPageByUserId(Management management, Integer userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT m.id,m.management_code,m.department_id,d.dept_name department_name,m.apply_user,u.user_name apply_name,m.apply_date, ");
        sql.append(" m.status,m.reason_detail,m.source,m.management_finish ");
        sql.append(" FROM management m ");
        sql.append(" LEFT JOIN sys_department d ON m.department_id = d.id ");
        sql.append(" LEFT JOIN sys_user u ON m.apply_user = u.id ");
        sql.append(" WHERE m.is_del = '0' ");
        if (null != management) {
            if (notEmpty(userId)) {
                sql.append(" AND m.apply_user = " + userId);
            }
            if (notEmpty(management.getDepartment_id())) {
                sql.append(" AND m.department_id = " + management.getDepartment_id());
            }
            if (StringUtils.isNotEmpty(management.getManagement_code())) {
                sql.append(" AND m.management_code like'%" + management.getManagement_code() + "%' ");
            }
            if (notEmpty(management.getStatus())) {
                sql.append(" AND m.status = " + management.getStatus());
            }
            // ids
            if (!CollectionUtils.isEmpty(management.getIds())) {
                sql.append(" AND m.id in (");
                for (Integer id : management.getIds()) {
                    sql.append(id + ",");
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(")");
            }
            // 申请人
            if (StringUtils.isNotBlank(management.getApply_name())) {
                sql.append(" and m.apply_user in ( select id from sys_user where user_name like '%" + management.getApply_name() + "%' ) ");
            }
            // 日期区间
            if (StringUtils.isNotBlank(management.getTimeStr())) {
                String[] split = management.getTimeStr().split(" - ");
                String startTime = split[0] + " 00:00:00";
                String endTime = split[1] + " 23:59:59";
                sql.append(" and m.apply_date between '" + startTime + "' and '" + endTime + "'");
            }
        }
        sql.append(" order by m.create_time desc ");
        return sql.toString();
    }


    public String getOneManById(Integer id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT m.id,m.management_code,m.department_id,d.dept_name department_name,m.apply_user,u.user_name apply_name,m.apply_date, ");
        sql.append(" m.status,m.reason_detail,m.source,m.management_finish ");
        sql.append(" FROM management m ");
        sql.append(" LEFT JOIN sys_department d ON m.department_id = d.id ");
        sql.append(" LEFT JOIN sys_user u ON m.apply_user = u.id ");
        sql.append(" WHERE m.is_del = '0' ");
        sql.append(" AND m.id = " + id);
        return sql.toString();
    }


    public String getPageByPermission(Management management, Integer userId, Integer department_id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT m.id,m.management_code,m.department_id,d.dept_name department_name,m.apply_user,u.user_name apply_name,m.apply_date, ");
        sql.append(" m.status,m.reason_detail,m.source,m.management_finish ");
        sql.append(" FROM management m ");
        sql.append(" LEFT JOIN sys_department d ON m.department_id = d.id ");
        sql.append(" LEFT JOIN sys_user u ON m.apply_user = u.id ");
        sql.append(" WHERE m.is_del = '0' ");
        sql.append(" AND m.status = '4' ");
        if (null != userId) {
            sql.append(" AND m.create_user = " + userId);
        }
        if (null != department_id) {
            sql.append(" AND m.department_id = " + department_id);
        }

        if (null != management) {
            if (StringUtils.isNotEmpty(management.getManagement_code())) {
                sql.append(" AND m.management_code like'%" + management.getManagement_code() + "%' ");
            }
            // 申请人
            if (StringUtils.isNotBlank(management.getApply_name())) {
                sql.append(" and m.apply_user in ( select id from sys_user where user_name like '%" + management.getApply_name() + "%' ) ");
            }
            // 日期区间
            if (StringUtils.isNotBlank(management.getTimeStr())) {
                String[] split = management.getTimeStr().split(" - ");
                String startTime = split[0] + " 00:00:00";
                String endTime = split[1] + " 23:59:59";
                sql.append(" and m.apply_date between '" + startTime + "' and '" + endTime + "'");
            }
            // 部门
            if (notEmpty(management.getDepartment_id())) {
                sql.append(" AND m.department_id = " + management.getDepartment_id());
            }
            // 是否完成
            if (notEmpty(management.getManagement_finish())) {
                sql.append(" AND m.management_finish = " + management.getManagement_finish());
            }
        }
        sql.append(" order by m.create_time desc ");
        return sql.toString();
    }
}
