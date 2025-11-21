package com.jsdc.rfid.dao;

import com.alibaba.excel.util.CollectionUtils;
import com.jsdc.core.base.BaseDao;
import com.jsdc.core.common.handler.QueryHandler;
import com.jsdc.rfid.model.CarryManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CarryManageDao extends BaseDao<CarryManage> {


    //分页查询
    public String selectPageList(CarryManage bean) {
        String sql = "SELECT * FROM carry_manage";
        QueryHandler queryHandler = getQueryHandler(sql);
        queryHandler.condition("is_del = 0 ");
        if (notEmpty(bean)) {
            //根据当前登录用户查询
            if (notEmpty(bean.getUser_id())) {
                queryHandler.condition("user_Id =" + bean.getUser_id());
            }
            if (notEmpty(bean.getDept_id())) {
                queryHandler.condition("dept_Id =" + bean.getDept_id());
            }
            if (notEmpty(bean.getCreation_user())) {
                queryHandler.condition("creation_user =" + bean.getCreation_user());
            }
            if (notEmpty(bean.getApproval_state())) {
                queryHandler.condition("approval_state =" + bean.getApproval_state());
            }
            if (notEmpty(bean.getNumbering())) {
                queryHandler.condition("numbering like '%" + bean.getNumbering() + "%'");
            }
            if (notEmpty(bean.getAssetnumber())) {
                queryHandler.condition("assetnumber like '%" + bean.getAssetnumber() + "%'");
            }
            if (notEmpty(bean.getAssetname())) {
                queryHandler.condition("assetname like '%" + bean.getAssetname() + "%'");
            }
            if (notEmpty(bean.getTransfer())) {
                queryHandler.condition("(assetname like '%" + bean.getTransfer() + "%' or assetnumber like '%" + bean.getTransfer() + "%')");
            }
            if (!CollectionUtils.isEmpty(bean.getAssetnumberList())) {
                String temp = "";
                temp += "assetnumber in (";
                for (String assetnumber : bean.getAssetnumberList()) {
                    // 判断是否为最后一个
                    if (assetnumber.equals(bean.getAssetnumberList().get(bean.getAssetnumberList().size() - 1))) {
                        temp += "'" + assetnumber + "'";
                    } else {
                        temp += "'" + assetnumber + "',";
                    }
                }
                temp += ")";
                queryHandler.condition(temp);
            }
            // ids
            if (!CollectionUtils.isEmpty(bean.getIds())) {
                String temp = "";
                temp += "id in (";
                for (Integer id : bean.getIds()) {
                    // 判断是否为最后一个
                    if (id.equals(bean.getIds().get(bean.getIds().size() - 1))) {
                        temp += "'" + id + "'";
                    } else {
                        temp += "'" + id + "',";
                    }
                }
                temp += ") ";
                queryHandler.condition(temp);
            }
            //时间范围 如2023-03-04 查询当月数据
            if (notEmpty(bean.getCreation_time_query())) {
                queryHandler.condition(" creation_time >= '" + bean.getCreation_time_query() + "-01 00:00:00'");
                queryHandler.condition(" creation_time <= '" + bean.getCreation_time_query() + "-31 23:59:59'");
            }
            // 申请人
            if (StringUtils.isNotBlank(bean.getCarry_name())) {
//                queryHandler.condition(" carry_Id in ( select id from sys_user where user_name like '%" + bean.getCarry_name() + "%' ) ");
                queryHandler.condition(" carry_name like '%" + bean.getCarry_name() + "%' ");
            }
            // 日期区间
            if (StringUtils.isNotBlank(bean.getTimeStr())) {
                String[] split = bean.getTimeStr().split(" - ");
                String startTime = split[0] + " 00:00:00";
                String endTime = split[1] + " 23:59:59";
                queryHandler.condition(" creation_time between '" + startTime + "' and '" + endTime + "'");
            }
            // 外携状态
            if (StringUtils.isNotBlank(bean.getCarry_state())) {
                queryHandler.condition(" carry_state = '" + bean.getCarry_state() + "'");
            }
            // 部门负责人查看
            if(null != bean.getDeptManager()){
//                creation_user in (select id from sys_user where dept = 1)
                queryHandler.condition(" creation_user in (select id from sys_user where department = " + bean.getDeptManager() + ")");
            }
        }
        queryHandler.order("creation_time desc");
        return queryHandler.getSql();
    }

    //详情
    public String selectByDetails(Integer id, String approval_state) {
        String sql = "";
        if (notEmpty(approval_state)) {
            sql = "SELECT * FROM carry_manage";
            QueryHandler queryHandler = getQueryHandler(sql);
            queryHandler.condition("is_del = '0 '");
            if (notEmpty(approval_state)) {
                queryHandler.condition("approval_state =" + approval_state);
            }
            return queryHandler.getSql();
        } else {
            sql = "SELECT * FROM carry_manage where is_del = 0 " +
                    " and BETWEEN CONCAT(creation_time,' 00:00:00') AND CONCAT(creation_time,' 23:59:59') ";
            QueryHandler queryHandler = getQueryHandler(sql);
            queryHandler.condition("carry_state = '1'");
            queryHandler.condition("approval_state = '2'");
            //根据当前登录用户查询
            if (notEmpty(id)) {
                queryHandler.condition("user_Id =" + id);
            }
            return queryHandler.getSql();
        }
    }


    public String selectManageCount() {
        String sql = "select type.id,type.assets_type_name as name,sum(TIMESTAMPDIFF(MINUTE ,bean.carry_time,bean.actual_time)) as value from carry_manage bean  " +
                "LEFT JOIN assets_manage ma on bean.asset_manage_id = ma.id  " +
                "LEFT JOIN assets_type type on type.id=ma.asset_type_id " +
                " where type.assets_type_name is not null  " +
                "group by type.assets_type_name ORDER BY value desc LIMIT 10  ";
        return sql;
    }
}
