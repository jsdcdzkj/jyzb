package com.jsdc.rfid.dao;

import com.alibaba.excel.util.CollectionUtils;
import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ApplySingle;
import com.jsdc.rfid.vo.ApplySingleVo;
import org.springframework.stereotype.Repository;

@Repository
public class ApplySingleDao extends BaseDao<ApplySingle> {


    /**
     * menu_type 1、流程中单据 2、驳回单据 3、历史记录
     *
     * @param bean state 申请单状态 1 录入 2 已派单 3 拒绝签单 4 签收 5 无法完成 6 确认维修 7 完成反馈 8 完成 9 已作废
     * @returnpageList
     */
    public String selectPageList(ApplySingleVo bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select bean.*, ma.use_people as use_people, ma.dept_id as dwId,dic.label as stateName" +
                " from apply_single bean LEFT JOIN sys_dict dic on dic.value = bean.state and type='bx_state' " +
                "LEFT JOIN  assets_manage ma on bean.assetManage_id = ma.id where bean.is_del = 0  ");
        if (notEmpty(bean)) {
            if ("1".equals(bean.getMenu_type())) {//流程中单据
                buffer.append(" and bean.state in ('2','4','6') ");
            } else if ("2".equals(bean.getMenu_type())) {//驳回单据
                buffer.append(" and  bean.state in('1','3','5','-1') ");
            } else if ("3".equals(bean.getMenu_type())) {//历史记录
                buffer.append(" and  bean.state in('7','8') ");
            } else if ("4".equals(bean.getMenu_type())) {//待接单
                buffer.append(" and bean.state in ('2','4','6')");
            } else if ("5".equals(bean.getMenu_type())) {//待维修
                buffer.append(" and bean.state in  ('4','6')");
            }
            buffer.append(parmData(bean));
            if ("3".equals(bean.getMenu_type()) || "4".equals(bean.getMenu_type())) {
                buffer.append(" order by bean.creation_time desc");
            } else {
                buffer.append(" order by bean.state,bean.degree,bean.creation_time ");
            }
        }
        return buffer.toString();
    }

    /**
     * 1、未派单 2、已派单 3、已完成
     *
     * @param bean
     * @return
     */
    public String selectYPDPage(ApplySingleVo bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select bean.*, ma.use_people as use_people, ma.dept_id as dwId,dic.label as stateName" +
                " from apply_single bean LEFT JOIN sys_dict dic on dic.value = bean.state and type='bx_state' " +
                "LEFT JOIN  assets_manage ma on bean.assetManage_id = ma.id where bean.is_del = 0  ");
        if (notEmpty(bean)) {
            //未派单
            if ("1".equals(bean.getMenu_type())) {
                buffer.append(" and bean.state in('1','3','5','-1')");
            } else if ("2".equals(bean.getMenu_type())) {
                buffer.append(" and  bean.state not in('1','3','5','7','8','-1') ");
            } else if ("3".equals(bean.getMenu_type())) {
                buffer.append(" and  bean.state  in ('7','8') ");
            }
            buffer.append(parmData(bean));
        }
        buffer.append(" order by bean.degree,bean.state,bean.creation_time  ");
        return buffer.toString();
    }

    /**
     * 手机端-维修记录
     *
     * @param bean
     * @return
     */
    public String selectByHistoryList(ApplySingle bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select bean.*, ma.use_people as use_people, ma.dept_id as dwId,dic.label as stateName" +
                " from apply_single bean LEFT JOIN sys_dict dic on dic.`value` = bean.state and type='bx_state' " +
                "LEFT JOIN  assets_manage ma on bean.assetManage_id = ma.id where bean.is_del = 0  ");
        if (notEmpty(bean)) {
            buffer.append(parmData(bean));
            buffer.append(" and bean.state in('7','8') ");
        }
        buffer.append(" order by bean.degree,bean.state,bean.creation_time  ");
        System.out.println(buffer.toString());
        return buffer.toString();
    }


    /**
     * 更改状态
     *
     * @param bean
     * @return
     */
    public String updateById(ApplySingleVo bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("update apply_single set state = '" + bean.getState() + "'");
        if (notEmpty(bean)) {
            buffer.append(" , grade= '" + bean.getGrade() + "'");
            buffer.append(" , grade_msg= '" + bean.getGrade_msg() + "'");
        }
        buffer.append(" where id= " + bean.getApplyId());
        return buffer.toString();
    }

    /**
     * 申请单状态 1 录入 2 已派单 3 拒绝签单 4 签收 5 无法完成 6 确认维修 7 完成反馈 8 完成 9 已作废
     *
     * @return
     */
    public String getJqList() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select * from apply_single bean where 1 = 1");
        buffer.append("and bean.state in('3','5') and bean.is_del = 0 ");
        return buffer.toString();
    }


    //服务评价
    public String selectEvaluationData(ApplySingleVo bean) {
        String sql = "select s.user_name as realName,sum(CASE  WHEN bean.grade = 0 THEN 1 ELSE 0 END) as praise, " +
                " sum(CASE  WHEN bean.grade = 1 THEN 1 ELSE 0 END) as commonly, " +
                " sum(CASE  WHEN bean.grade = 2 THEN 1 ELSE 0 END) as negative  " +
                " from apply_single bean LEFT JOIN sys_user s on bean.repair_id=s.id " +
                " where bean.is_del='0' and state='8' ";
        if (notEmpty(bean.getCheckType())) {
            if (bean.getCheckType() == 1) {//今年
                sql += " and YEAR(bean.creation_time) = YEAR(NOW()) ";
            } else if (bean.getCheckType() == 2) {//上月
                sql += " and PERIOD_DIFF(date_format(now(),'%Y%m') , date_format(bean.creation_time,'%Y%m')) =1 ";
            } else {//自定义
                sql += " and bean.creationTime BETWEEN '" + bean.getStart() + "' AND '" + bean.getEnd() + "'";
            }
        } else {
            sql += " and YEAR(bean.creation_time) = YEAR(NOW()) ";
        }
        sql += "GROUP BY s.user_name ";
        return sql;
    }

    //统计维修人员所属申请单数量
    public String selectApplyCount(ApplySingle bean) {
        StringBuffer buffer = new StringBuffer();
        String sql = "select bean.id as repair_id ,count(apply.id) as num,IFNULL('1999-01-05 00:00:00', max(apply.creation_time)) as creation_time from sys_user bean LEFT JOIN sys_post post ON bean.post = post.id " +
                "LEFT  JOIN apply_single apply on bean.id = apply.repair_id and state='2' " +
                "where bean.is_del = 0 and post.post_code='wxry' " +
                "GROUP BY bean.id ORDER BY num ";
        buffer.append(sql);
        return buffer.toString();
    }

    //导出功能
    public String repairExport(ApplySingle bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" select * from apply_single ");
        buffer = parmData(bean);
        if (notEmpty(bean.getState())) {
            buffer.append(" bean.state = '" + bean.getState() + "' ");
        } else {
            buffer.append(" bean.state  in ('7','8')  ");
        }
        return buffer.toString();
    }

    public String selectByDetails(Integer id) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select bean.*,us.user_name as use_people, det.dept_name as dw_name " +
                " from apply_single bean LEFT JOIN  assets_manage ma on bean.assetManage_id = ma.id " +
                " LEFT JOIN sys_department det on det.id=ma.dept_id " +
                " LEFT JOIN sys_user us on us.id=ma.use_people where bean.is_del = 0  ");
        if (notEmpty(id)) {
            buffer.append(" and bean.id= '" + id + "'");
        }
        return buffer.toString();
    }

    //导出统计
    public String exportCount(ApplySingle bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" select bean.stdmode.parent.parent.stdmodeName as parentName,bean.stdmode.parent.stdmodeName as name,count(bean.stdmode.parent.id) as singlecount from apply_single bean");
        buffer = parmData(bean);
        if (notEmpty(bean.getState())) {
            buffer.append(" bean.state = '" + bean.getState() + "' ");
        } else {
            buffer.append(" bean.state  in ('7','8')  ");
        }
        return buffer.toString();
    }


    //参数
    public StringBuffer parmData(ApplySingle bean) {
        StringBuffer buffer = new StringBuffer();
        if (notEmpty(bean.getRepair_name())) {
            buffer.append(" and bean.repair_name like '%" + bean.getRepair_name().trim() + "%' ");
        }
        if (notEmpty(bean.getDept_id())) {
            buffer.append(" and bean.dept_id = '" + bean.getDept_id() + "' ");
        }
        if (notEmpty(bean.getRepair_id())) {
            buffer.append(" and bean.repair_id = '" + bean.getRepair_id() + "' ");
        }
        if (notEmpty(bean.getDegree())) {
            buffer.append(" and bean.degree = '" + bean.getDegree() + "' ");
        }
        if (notEmpty(bean.getGood_name())) {
            buffer.append(" and bean.good_name like '%" + bean.getGood_name().trim() + "%' ");
        }
        if (notEmpty(bean.getGood_number())) {
            buffer.append(" and bean.good_number like '%" + bean.getGood_number().trim() + "%' ");
        }
        if (notEmpty(bean.getOrder_code())) {
            buffer.append(" and bean.order_code like '%" + bean.getOrder_code().trim() + "%' ");
        }
        if (notEmpty(bean.getState())) {
            buffer.append(" and bean.state = '" + bean.getState() + "' ");
        }
        if (notEmpty(bean.getSqr_id())) {
            buffer.append(" and bean.sqr_id = '" + bean.getSqr_id() + "' ");
        }
        if (notEmpty(bean.getSqr_name())) {
            buffer.append(" and bean.sqr_name like '%" + bean.getSqr_name().trim() + "%' ");
        }
        if (notEmpty(bean.getAssetmanage_id())) {
            buffer.append(" and bean.assetManage_id = '" + bean.getAssetmanage_id() + "' ");
        }
        return buffer;
    }

    public String statisticsByDept() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" count( 1 ) value,");
        sql.append(" min(d.dept_name) name,");
        sql.append(" min(d.id) deptId");
        sql.append(" FROM");
        sql.append(" apply_single a ");
        sql.append(" left join sys_department d on a.dept_Id = d.id");
        sql.append(" WHERE");
        sql.append(" a.is_del = 0");
        sql.append(" and a.state != 9");
        sql.append(" and a.state != 1");
        sql.append(" and d.id is not null ");
        sql.append(" GROUP BY");
        sql.append(" a.dept_id");
        return sql.toString();
    }


    /**
     * 报修管理
     * 20230128做变更
     *
     * @param bean
     * @return
     */
    public String pageList(ApplySingleVo bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select bean.*, ma.use_people as use_people, ma.dept_id as dwId " +
                " from apply_single bean LEFT JOIN  assets_manage ma on bean.assetManage_id = ma.id where bean.is_del = 0  ");
        if (notEmpty(bean)) {
            buffer.append(parmData(bean));
            if (notEmpty(bean.getTransfer())) {
                buffer.append(" and (bean.order_code like '%" + bean.getTransfer() + "%' or " +
                        " bean.good_name like '%" + bean.getTransfer() + "%' or  " +
                        " bean.good_number like '%" + bean.getTransfer() + "%' )");
            }
        }
        buffer.append(" order by bean.state,bean.degree,bean.creation_time ");
        return buffer.toString();
    }

    /**
     * 待审批列表
     */
    public String getApprovedListByPage(ApplySingleVo bean) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.*,u.user_name as manager,em.unitName as unit  ");
        sql.append(" FROM");
        sql.append(" apply_single a ");
        sql.append(" LEFT JOIN sys_user u ON a.managers = u.id");
        sql.append(" LEFT JOIN externalmaintenance em ON a.unitId = em.id");
        sql.append(" WHERE 1=1");
        sql.append(" and a.is_del = 0");
        sql.append(" and a.isExternal = 1");
        if (notEmpty(bean.getOrder_code())) {
            sql.append(" and a.order_code like %").append(bean.getOrder_code()).append("%");
        }
        if (notEmpty(bean.getGood_name())) {
            sql.append(" and a.good_name like %").append(bean.getGood_name()).append("%");
        }
        // ids
        if (!CollectionUtils.isEmpty(bean.getIds())) {
            String temp = "";
            temp += " and a.id in (";
            for (Integer id : bean.getIds()) {
                // 判断是否为最后一个
                if (id.equals(bean.getIds().get(bean.getIds().size() - 1))) {
                    temp += "'" + id + "'";
                } else {
                    temp += "'" + id + "',";
                }
            }
            temp += ") ";
            sql.append(temp);
        }
//        sql.append(" GROUP BY");
//        sql.append(" a.dept_id");
        return sql.toString();
    }
    /**
     *
     * @param bean
     * @return
     */
    public String getRepairList(ApplySingleVo bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT\n" +
                "\tbean.*,\n" +
                "\tma.use_people AS use_people,\n" +
                "\tma.dept_id AS dwId \n" +
                "FROM\n" +
                "\tapply_single bean\n" +
                "\tLEFT JOIN assets_manage ma ON bean.assetManage_id = ma.id \n" +
                "WHERE\n" +
                "\tbean.is_del = 0 \n" +
                "\tAND bean.state = '2'  \n" +
                "\tAND bean.repair_id = ").append(bean.getRepair_id());
        buffer.append(" order by bean.state,bean.degree,bean.creation_time ");
        return buffer.toString();
    }

    /**
     * 报修本月总数
     * @param bean
     * @return
     */
    public String getRepairMonthList(ApplySingleVo bean) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT\n" +
                "\tbean.*,\n" +
                "\tma.use_people AS use_people,\n" +
                "\tma.dept_id AS dwId \n" +
                "FROM\n" +
                "\tapply_single bean\n" +
                "\tLEFT JOIN assets_manage ma ON bean.assetManage_id = ma.id \n" +
                "WHERE\n" +
                "\tbean.is_del = 0 \n");
        buffer.append(" AND bean.creation_time >= '").append(bean.getStart()).append("'");
        buffer.append(" AND bean.creation_time < '").append(bean.getEnd()).append("'");
        buffer.append(" order by bean.state,bean.degree,bean.creation_time ");
        return buffer.toString();
    }

}
