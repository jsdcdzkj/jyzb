package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.BorrowAssets;
import org.springframework.stereotype.Repository;

@Repository
public class BorrowAssetsDao extends BaseDao<BorrowAssets> {

    public String getBorrowAssets(Integer id){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bs.assets_id,a.asset_code,a.asset_name,t.assets_type_name,u.user_name,b.borrow_code,bs.borrow_status,bs.real_date,bs.return_date,bs.return_deal_user,bs.id ");
        sql.append(" FROM borrow_assets bs ");
        sql.append(" LEFT JOIN assets_manage a ON bs.assets_id = a.id ");
        sql.append(" LEFT JOIN assets_type t ON a.asset_type_id = t.id ");
        sql.append(" LEFT JOIN sys_user u ON a.admin_user = u.id ");
        sql.append(" LEFT JOIN borrow b ON bs.borrow_id = b.id ");
        sql.append(" WHERE bs.is_del = '0' ");
        sql.append(" AND bs.borrow_id = "+id);
        return sql.toString();
    }


    //根据编号查询出逾期的资产
    public String beOverdue(Integer id){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.asset_code,a.asset_name,t.assets_type_name,u.user_name,b.borrow_code,bs.borrow_status,bs.real_date,bs.return_date,bs.return_deal_user,bs.id ");
        sql.append(" FROM borrow_assets bs ");
        sql.append(" LEFT JOIN assets_manage a ON bs.assets_id = a.id ");
        sql.append(" LEFT JOIN assets_type t ON a.asset_type_id = t.id ");
        sql.append(" LEFT JOIN sys_user u ON a.admin_user = u.id ");
        sql.append(" LEFT JOIN borrow b ON bs.borrow_id = b.id ");
        sql.append(" WHERE bs.is_del = '0' ");
        sql.append(" AND bs.borrow_id = "+id);
        sql.append(" AND (bs.borrow_status = '2' OR bs.borrow_status = '4') ");
        return sql.toString();
    }
}
