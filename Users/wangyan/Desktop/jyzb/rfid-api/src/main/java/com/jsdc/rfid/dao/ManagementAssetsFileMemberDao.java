package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.ManagementAssetsFileMember;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangdequan
 * @create 2022-08-24 15:50:59
 */
@Repository
public class ManagementAssetsFileMemberDao extends BaseDao<ManagementAssetsFileMember> {

    public String toList(ManagementAssetsFileMember beanParam){
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT * FROM management_assets_file_member");
        sql.append(" where is_del = 0 ");
        return sql.toString();
    }
}
