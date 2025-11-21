package com.jsdc.rfid.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SysUserDao extends BaseDao<SysUser> {


    public QueryWrapper<SysUser> queryByName(String name, String pass) {
        QueryWrapper<SysUser> queryHandler = new QueryWrapper<>();
        if (notEmpty(name)) {
            queryHandler.eq("login_name", name);
        }
        if (notEmpty(pass)) {
            queryHandler.eq("password", pass);
        }
        return queryHandler;
    }

    public QueryWrapper<SysUser> queryByName(String name) {
        QueryWrapper<SysUser> queryHandler = new QueryWrapper<>();
        if (notEmpty(name)) {
            queryHandler.eq("login_name", name);
        }
        return queryHandler;
    }

    public String getList(SysUser user){
        StringBuilder sql = new StringBuilder();
        sql.append(" select");
        sql.append(" u.*,");
        sql.append(" d.dept_name,");
        sql.append(" p.post_name");
        sql.append(" from ");
        sql.append(" sys_user u");
        sql.append(" left join sys_department d on u.department = d.id");
        sql.append(" left join sys_post p on u.post = p.id");
        sql.append(" where 1=1");
        if(null != user){
            //部门过滤条件
            if(user.getDeptIds()!=null){
                sql.append(" and d.id in ( ").append(String.join(",",user.getDeptIds())).append(" )");
            }
            if(StringUtils.isNotEmpty(user.getLogin_name())){
                sql.append(" and u.login_name like '%" + user.getLogin_name().trim() +"%'");
            }
            if(StringUtils.isNotEmpty(user.getUser_name())){
                sql.append(" and u.user_name like '%" + user.getUser_name().trim() +"%'");
            }
            if(StringUtils.isNotEmpty(user.getTelephone())){
                sql.append(" and u.telephone like'%" + user.getTelephone().trim() +"%'");
            }
            if(null != user.getDepartment()){
                sql.append(" and u.department = " + user.getDepartment());
            }
            if(null != user.getPost()){
                sql.append(" and u.post = " + user.getPost());
            }
            if (StringUtils.isNotBlank(user.getInIds())) {
                List<Integer> inIds = new ArrayList<>();
                for (String inId : user.getInIds().split(",")) {
                    if (StringUtils.isNotBlank(inId)) {
                        inIds.add(Integer.valueOf(inId));
                    }
                }
                sql.append(" and u.id in (" );
                for (int i = 0; i < inIds.size(); i++) {
                    if (i == inIds.size() - 1) {
                        sql.append(inIds.get(i));
                    } else {
                        sql.append(inIds.get(i)).append(",");
                    }
                }
                sql.append(")");
            }

            if (StringUtils.isNotBlank(user.getNoIds())) {
                List<Integer> noIds = new ArrayList<>();
                for (String inId : user.getNoIds().split(",")) {
                    if (StringUtils.isNotBlank(inId)) {
                        noIds.add(Integer.valueOf(inId));
                    }
                }
                sql.append(" and u.id not in (" );
                for (int i = 0; i < noIds.size(); i++) {
                    if (i == noIds.size() - 1) {
                        sql.append(noIds.get(i));
                    } else {
                        sql.append(noIds.get(i)).append(",");
                    }
                }
                sql.append(")");
            }
        }
        sql.append(" and u.is_del = 0");
        sql.append(" and u.is_enable = 1");
        return sql.toString();
    }
}
