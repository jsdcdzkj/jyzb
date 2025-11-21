package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.PurchaseApply;
import com.jsdc.rfid.service.PurchaseApplyService;
import org.springframework.stereotype.Repository;
import vo.PurchaseApplyVo;

/**
 * ClassName: PurchaseApplyDao
 * Description:
 * date: 2022/4/24 16:59
 *
 * @author bn
 */
@Repository
public class PurchaseApplyDao extends BaseDao<PurchaseApply> {

    public String toList(PurchaseApplyVo purchaseApplyVo){
        StringBuilder sql=new StringBuilder();

        sql.append(" ");

        return sql.toString();
    }

    public String purchaseFromSupplierTo10(){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" count( 1 ) total,");
        sql.append(" s.supplier_name name ");
        sql.append(" FROM");
        sql.append(" purchase_apply a ");
        sql.append(" LEFT JOIN supplier s on a.supplier_id = s.id");
        sql.append(" WHERE");
        sql.append(" a.is_del = 0 ");
        sql.append(" AND a.supplier_id IS NOT NULL ");
        sql.append(" GROUP BY");
        sql.append(" a.supplier_id ");
        sql.append(" ORDER BY");
        sql.append(" count( 1 ) DESC ");
        sql.append(" LIMIT 10");
        return sql.toString();
    }
}
