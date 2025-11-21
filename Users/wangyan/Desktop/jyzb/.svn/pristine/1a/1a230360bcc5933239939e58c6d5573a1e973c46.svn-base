package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.SysPostPermission;
import org.springframework.stereotype.Repository;

@Repository
public class SysPostPermissionDao extends BaseDao<SysPostPermission> {

    /**
     * 根据当前登录的岗位查询菜单
     *
     * @param post_id
     * @return
     */
    public String selectByPostPermissionCode(String post_id) {
        StringBuilder builder = new StringBuilder();
        builder.append("select permission_code from sys_permission pe LEFT JOIN sys_post_permission pp on pe.id = pp.permission_id ");
        builder.append(" where pe.permission_code in ('bxsq','lczdj','wdwx','lysq','czsq','hcly') and pp.is_del ='0' and pp.post_id='" + post_id + "' GROUP BY pe.permission_code ");
        return builder.toString();
    }

}
