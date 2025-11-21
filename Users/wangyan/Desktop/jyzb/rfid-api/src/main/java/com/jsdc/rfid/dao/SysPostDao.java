package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.SysPost;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class SysPostDao extends BaseDao<SysPost> {

    public String getPage(SysPost post){
        StringBuilder sql = new StringBuilder();
        sql.append(" select");
        sql.append(" p.*,");
        sql.append(" d.dept_name");
        sql.append(" from");
        sql.append(" sys_post p");
        sql.append(" left join sys_department d on p.dept_id = d.id");
        sql.append(" where 1=1");
        if(null != post){
            if(StringUtils.isNotEmpty(post.getPost_code())){
                sql.append(" and p.post_code like '%").append(StringUtils.trim(post.getPost_code())).append("%'");
            }
            if(StringUtils.isNotEmpty(post.getPost_name())){
                sql.append(" and p.post_name like '%").append(StringUtils.trim(post.getPost_name())).append("%'");
            }
            if(null != post.getDept_id()){
                sql.append(" and p.dept_id = ").append(post.getDept_id());
            }
        }
        sql.append(" and p.is_del = ").append(G.ISDEL_NO);
        sql.append(" and p.is_enable = ").append(G.ISENABLE_YES);
        return sql.toString();
    }
}
