package com.jsdc.rfid.dao;


import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ChangeInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class ChangeInfoDao extends BaseDao<ChangeInfo> {

    public String getInfoByPage(ChangeInfo changeInfo, Integer userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.id,c.change_code,c.status,c.use_id,c.department_id,c.position,c.apply_user,c.apply_date,c.approve_status,c.remark,d.dept_name department_name,p.place_name ");
        sql.append(" FROM change_info c ");
        sql.append(" LEFT JOIN sys_department d ON c.department_id = d.id ");
        sql.append(" LEFT JOIN sys_place p ON c.position = p.id ");
        sql.append(" WHERE c.is_del = '0' ");
        if (null != changeInfo) {
            if (notEmpty(userId)) {
                sql.append(" AND c.create_user = " + userId);
            }
            if (notEmpty(changeInfo.getDepartment_id())) {
                sql.append(" AND c.department_id = " + changeInfo.getDepartment_id());
            }
            if (notEmpty(changeInfo.getApprove_status())) {
                sql.append(" AND c.approve_status = '" + changeInfo.getApprove_status() + "'");
            }
            if (StringUtils.isNotEmpty(changeInfo.getChange_code())) {
                sql.append(" AND c.change_code like'%" + changeInfo.getChange_code() + "%' ");
            }
            // ids
            if (!CollectionUtils.isEmpty(changeInfo.getIds())) {
                sql.append(" AND c.id in (");
                for (Integer id : changeInfo.getIds()) {
                    sql.append(id + ",");
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append(")");
            }
            // 申请人
            if (StringUtils.isNotBlank(changeInfo.getApply_name())) {
                sql.append(" and c.apply_user in ( select id from sys_user where user_name like '%" + changeInfo.getApply_name() + "%' ) ");
            }
            // 日期区间
            if (StringUtils.isNotBlank(changeInfo.getTimeStr())) {
                String[] split = changeInfo.getTimeStr().split(" - ");
                String startTime = split[0] + " 00:00:00";
                String endTime = split[1] + " 23:59:59";
                sql.append(" and c.apply_date between '" + startTime + "' and '" + endTime + "'");
            }
        }
        sql.append(" order by c.create_time DESC ");
        return sql.toString();
    }


    public String collectionChangeByPage(ChangeInfo changeInfo, Integer userId, Integer department_id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.id,c.source,c.change_code,c.status,c.use_id,c.department_id,c.position,c.apply_user,c.apply_date,c.approve_status,c.remark,d.dept_name department_name,p.place_name ");
        sql.append(" FROM change_info c ");
        sql.append(" LEFT JOIN sys_department d ON c.department_id = d.id ");
        sql.append(" LEFT JOIN sys_place p ON c.position = p.id ");
        sql.append(" WHERE c.is_del = '0' ");
        sql.append(" AND c.approve_status = '4' ");
        if (null != userId) {
            sql.append(" AND c.create_user = " + userId);
        }
        if (null != department_id) {
            sql.append(" AND c.department_id = " + department_id);
        }
        if (null != changeInfo) {
            if (StringUtils.isNotEmpty(changeInfo.getChange_code())) {
                sql.append(" AND c.change_code like'%" + changeInfo.getChange_code() + "%' ");
            }

            // 申请人
            if (StringUtils.isNotBlank(changeInfo.getApply_name())) {
                sql.append(" and c.apply_user in ( select id from sys_user where user_name like '%" + changeInfo.getApply_name() + "%' ) ");
            }
            // 日期区间
            if (StringUtils.isNotBlank(changeInfo.getTimeStr())) {
                String[] split = changeInfo.getTimeStr().split(" - ");
                String startTime = split[0] + " 00:00:00";
                String endTime = split[1] + " 23:59:59";
                sql.append(" and c.apply_date between '" + startTime + "' and '" + endTime + "'");
            }
        }
        sql.append(" order by c.create_time DESC ");

        return sql.toString();
    }
}
