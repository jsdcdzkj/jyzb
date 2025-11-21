package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.Receive;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
public class ReceiveDao extends BaseDao<Receive> {

    //当前登陆用户创建的资产领用单
    public String selectReceiveByPage(Receive receive, Integer userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT r.*, d.dept_name department_name ");
        sql.append(" FROM receive r ");
        sql.append(" LEFT JOIN sys_department d ON r.department_id = d.id ");
        sql.append(" WHERE r.is_del = '0' ");
        if (notEmpty(receive.getStatus())) {
            sql.append(" AND r.status = " + receive.getStatus());
        }
        if (null != receive) {
            if (null != userId) {
                sql.append(" AND r.use_id = " + userId);
            }else if (null != receive.getUserId()) {
                sql.append(" AND r.use_id = " + receive.getUserId());
            }

            if (notEmpty(receive.getDepartment_id())) {
                sql.append(" AND r.department_id = " + receive.getDepartment_id());
            }
            if (StringUtils.isNotEmpty(receive.getReceive_code())) {
                sql.append(" and r.receive_code like'%" + receive.getReceive_code() + "%' ");
            }
            if (notEmpty(receive.getCreate_user())) {
                sql.append(" AND r.create_user = " + receive.getCreate_user());
            }
            //ids list集合
            if (!CollectionUtils.isEmpty(receive.getIds())) {
                sql.append(" and r.id in (");
                for (int i = 0; i < receive.getIds().size(); i++) {
                    if (i == receive.getIds().size() - 1) {
                        sql.append(receive.getIds().get(i));
                    } else {
                        sql.append(receive.getIds().get(i) + ",");
                    }
                }
                sql.append(")");
            }
            // 日期区间
            if (StringUtils.isNotBlank(receive.getTimeStr())) {
                String[] split = receive.getTimeStr().split(" - ");
                String startTime = split[0] + " 00:00:00";
                String endTime = split[1] + " 23:59:59";
                sql.append(" and r.use_date between '" + startTime + "' and '" + endTime + "'");
            }
            // 领用人
            if (StringUtils.isNotBlank(receive.getUse_name())) {
                sql.append(" and r.use_id in ( select id from sys_user where user_name like '%" + receive.getUse_name() + "%' ) ");
            }
        }
        sql.append(" order by r.create_time desc ");
        return sql.toString();
    }

    //当前登陆用户权限范围内的通过审批的资产领用单
    public String collectionManageByPage(Receive receive, Integer userId, Integer department_id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT r.*, d.dept_name department_name ");
        sql.append(" FROM receive r ");
        sql.append(" LEFT JOIN sys_department d ON r.department_id = d.id ");
        sql.append(" LEFT JOIN receive_assets ra ON ra.receive_id = r.id ");
        sql.append(" LEFT JOIN assets_manage am ON am.id = ra.assets_id ");
        sql.append(" WHERE r.is_del = '0' and ra.is_del = '0' ");
        sql.append(" AND r.status = '4' ");
        if (null != userId) {
            sql.append(" AND r.create_user = " + userId);
        }
        if (null != department_id) {
            sql.append(" AND r.department_id = " + department_id);
        }
        if (null != receive) {
            if (StringUtils.isNotEmpty(receive.getReceive_code())) {
                sql.append(" and r.receive_code like'%" + receive.getReceive_code() + "%' ");
            }
            // 资产名称
            if (StringUtils.isNotBlank(receive.getAssets_names())) {
                sql.append(" and am.asset_name like '%" + receive.getAssets_names() + "%' ");
            }

            // 日期区间
            if (StringUtils.isNotBlank(receive.getTimeStr())) {
                String[] split = receive.getTimeStr().split(" - ");
                String startTime = split[0] + " 00:00:00";
                String endTime = split[1] + " 23:59:59";
                sql.append(" and r.use_date between '" + startTime + "' and '" + endTime + "'");
            }
            // 领用人
            if (StringUtils.isNotBlank(receive.getUse_name())) {
                sql.append(" and r.use_id in ( select id from sys_user where user_name like '%" + receive.getUse_name() + "%' ) ");
            }

            // 部门
            if (notEmpty(receive.getDepartment_id())) {
                sql.append(" AND r.department_id = " + receive.getDepartment_id());
            }
        }
        sql.append(" order by r.create_time desc ");
//        sql.append(" GROUP BY r.id order by r.create_time desc ");
        return sql.toString();
    }

    public String getAllReceive(Integer userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT r.id,r.receive_code,d.dept_name department_name,r.use_id,r.use_date,r.handle_id,r.remark,r.status ");
        sql.append(" FROM receive r ");
        sql.append(" LEFT JOIN sys_department d ON r.department_id = d.id ");
        sql.append(" WHERE r.is_del = '0' ");
        sql.append(" AND r.create_user = " + userId);
        sql.append(" order by r.create_time DESC ");
        return sql.toString();

    }

    /**
     * 微信小程序
     * 资产领用申请列表
     */
    public String getWxReceiveList(Receive receive) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT r.id,r.receive_code,d.dept_name department_name,r.use_id,r.use_date,r.handle_id,r.remark,r.status ");
        sql.append(" FROM receive r ");
        sql.append(" LEFT JOIN sys_department d ON r.department_id = d.id ");
        sql.append(" WHERE r.is_del = '0' ");
        sql.append(" AND r.create_user = " + receive.getUserId());
        if (notEmpty(receive)) {
            if (notEmpty(receive.getReceive_code())) {
                sql.append(" and r.receive_code like '%" + receive.getReceive_code() + "%' ");
            }
        }
        sql.append(" order by r.create_time DESC ");
        return sql.toString();

    }


    //当前登陆用户创建的资产领用单
    public String selectUserReceiveByPage(Receive receive, Integer userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT r.*, d.dept_name department_name ");
        sql.append(" FROM receive r ");
        sql.append(" LEFT JOIN sys_department d ON r.department_id = d.id ");
        sql.append(" WHERE r.is_del = '0' ");
        if (notEmpty(receive.getStatus())) {
            sql.append(" AND r.status = " + receive.getStatus());
        }
        if (null != receive) {
            if (null != userId) {
                sql.append(" AND r.use_id = " + userId);
            }
            if (notEmpty(receive.getDepartment_id())) {
                sql.append(" AND r.department_id = " + receive.getDepartment_id());
            }
            if (StringUtils.isNotEmpty(receive.getReceive_code())) {
                sql.append(" and r.receive_code like'%" + receive.getReceive_code() + "%' ");
            }
            if (notEmpty(receive.getCreate_user())) {
                sql.append(" AND r.create_user = " + receive.getCreate_user());
            }
            //ids list集合
            if (!CollectionUtils.isEmpty(receive.getIds())) {
                sql.append(" and r.id in (");
                for (int i = 0; i < receive.getIds().size(); i++) {
                    if (i == receive.getIds().size() - 1) {
                        sql.append(receive.getIds().get(i));
                    } else {
                        sql.append(receive.getIds().get(i) + ",");
                    }
                }
                sql.append(")");
            }

        }
        sql.append(" order by r.create_time desc ");
        return sql.toString();
    }

    /**
     * 微信小程序
     * 资产申领中数据
     */
    public String getWxReceivingList(Receive receive) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT r.id,r.receive_code,d.dept_name department_name,r.use_id,r.use_date,r.handle_id,r.remark,r.status ");
        sql.append(" FROM receive r ");
        sql.append(" LEFT JOIN sys_department d ON r.department_id = d.id ");
        sql.append(" WHERE r.is_del = '0' ");
        sql.append(" AND r.create_user = " + receive.getUserId());
        sql.append(" AND r.status in ('2','3')"); //2未审批、3审批中
        if (notEmpty(receive)) {
            if (notEmpty(receive.getReceive_code())) {
                sql.append(" and r.receive_code like '%" + receive.getReceive_code() + "%' ");
            }
        }
        sql.append(" order by r.create_time DESC ");
        return sql.toString();

    }
}
