package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.InventoryDetail;
import org.springframework.stereotype.Repository;
import vo.InventoryJobVo;

/**
 * ClassName: InventoryDetailDao
 * Description:
 * date: 2022/4/24 17:24
 *
 * @author bn
 */
@Repository
public class InventoryDetailDao extends BaseDao<InventoryDetail> {

    public String getInventoryDetail(InventoryJobVo inventoryJobVo){
        StringBuilder sql=new StringBuilder();
        sql.append("select detail.* from  inventory_detail detail ");
        sql.append("left join assets_manage manage on detail.asset_id=manage.id  ");
        sql.append("where detail.is_del='0' ");
        if (notEmpty(inventoryJobVo.getInventory_job_id())){
            sql.append(" and detail.inventory_job_id="+inventoryJobVo.getInventory_job_id());
        }
        if (notEmpty(inventoryJobVo.getInventory_status())){
            sql.append(" and detail.inventory_status='"+inventoryJobVo.getInventory_status()+"'");
        }
        if (notEmpty(inventoryJobVo.getDept_id())){
            sql.append(" and manage.dept_id ="+inventoryJobVo.getDept_id());
        }
        if (notEmpty(inventoryJobVo.getUse_people())){
            sql.append(" and manage.use_people="+inventoryJobVo.getUse_people());
        }
        if (notEmpty(inventoryJobVo.getAsset_type_id())){
            sql.append(" and manage.asset_type_id="+inventoryJobVo.getAsset_type_id());
        }

        return sql.toString();
    }

    public String getInventoryDetailStatistics(InventoryJobVo inventoryJobVo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("sd.dept_name, ");
        sql.append("am.remarks, ");
        sql.append("count(*) as zcount , ");
        sql.append("sum(CASE WHEN ind.inventory_status = 1 THEN 1 ELSE 0 END) AS ypcount, ");
        sql.append("sum(CASE WHEN ind.inventory_status = 3 THEN 1 ELSE 0 END) AS zccount, ");
        sql.append("sum(CASE WHEN ind.inventory_status = 6 THEN 1 ELSE 0 END) AS yccount ");
        sql.append("FROM ");
        sql.append("inventory_detail ind ");
        sql.append("LEFT JOIN assets_manage am ON am.id = ind.asset_id ");
        sql.append("LEFT JOIN sys_department sd ON sd.id = am.dept_id ");
        sql.append("WHERE ");
        sql.append("ind.inventory_job_id = '").append(inventoryJobVo.getInventory_job_id()).append("' ");
        sql.append("and ind.is_del = '0' ");
        sql.append("and am.is_del = '0' ");
        if (notEmpty(inventoryJobVo.getDept_id())){
            sql.append(" and sd.id = '").append(inventoryJobVo.getDept_id()).append("' ");
        }
        if (notEmpty(inventoryJobVo.getLocation())){
            sql.append(" and am.remarks = '").append(inventoryJobVo.getLocation()).append("' ");
        }
        sql.append("GROUP BY ");
        sql.append("am.remarks, ");
        sql.append("sd.dept_name ");
        sql.append("ORDER BY sd.dept_name ");
        return sql.toString();
    }

    public String getInventoryDept(InventoryJobVo inventoryJobVo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("sd.id, ");
        sql.append("sd.dept_name, ");
        sql.append("count(*) as zcount , ");
        sql.append("sum(CASE WHEN ind.inventory_status = 1 THEN 1 ELSE 0 END) AS ypcount, ");
        sql.append("sum(CASE WHEN ind.inventory_status = 3 THEN 1 ELSE 0 END) AS zccount, ");
        sql.append("sum(CASE WHEN ind.inventory_status = 6 THEN 1 ELSE 0 END) AS yccount ");
        sql.append("FROM ");
        sql.append("inventory_detail ind ");
        sql.append("LEFT JOIN assets_manage am ON am.id = ind.asset_id ");
        sql.append("LEFT JOIN sys_department sd ON sd.id = am.dept_id ");
        sql.append("WHERE ");
        sql.append("ind.inventory_job_id = '").append(inventoryJobVo.getInventory_job_id()).append("' ");
        sql.append("and ind.is_del = '0' ");
        sql.append("and am.is_del = '0' ");
        sql.append("GROUP BY ");
        sql.append("sd.dept_name ");
        sql.append("ORDER BY sd.dept_name ");
        return sql.toString();
    }

    public String getInventoryDeptDetail(InventoryJobVo inventoryJobVo){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("am.remarks, ");
        sql.append("count(*) as zcount , ");
        sql.append("sum(CASE WHEN ind.inventory_status = 1 THEN 1 ELSE 0 END) AS ypcount, ");
        sql.append("sum(CASE WHEN ind.inventory_status = 3 THEN 1 ELSE 0 END) AS zccount, ");
        sql.append("sum(CASE WHEN ind.inventory_status = 6 THEN 1 ELSE 0 END) AS yccount ");
        sql.append("FROM ");
        sql.append("inventory_detail ind ");
        sql.append("LEFT JOIN assets_manage am ON am.id = ind.asset_id ");
        sql.append("LEFT JOIN sys_department sd ON sd.id = am.dept_id ");
        sql.append("WHERE ");
        sql.append("ind.inventory_job_id = '").append(inventoryJobVo.getInventory_job_id()).append("' ");
        sql.append("and ind.is_del = '0' ");
        sql.append("and am.is_del = '0' ");
        sql.append("and am.dept_id = ' ").append(inventoryJobVo.getDept_id()).append("' ");
        sql.append("GROUP BY ");
        sql.append("am.remarks ");
        return sql.toString();
    }
}
