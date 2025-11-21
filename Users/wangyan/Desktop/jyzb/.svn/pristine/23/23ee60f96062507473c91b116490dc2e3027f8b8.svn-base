package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.AssetsFileMember;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-04-26 16:11:36
 */
@Repository
public class AssetsFileMemberDao extends BaseDao<AssetsFileMember> {

    public String toList(AssetsFileMember beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM assets_file_member");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
