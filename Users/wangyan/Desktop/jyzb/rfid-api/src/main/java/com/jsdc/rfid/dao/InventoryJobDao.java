package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.InventoryJob;
import org.springframework.stereotype.Repository;

/**
 * ClassName: InventoryJobDao
 * Description:
 * date: 2022/4/24 17:27
 *
 * @author bn
 */
@Repository
public class InventoryJobDao extends BaseDao<InventoryJob> {

    public String startRFID(InventoryJob inventoryJob){
        StringBuilder sql=new StringBuilder();
        sql.append("SELECT DISTINCT CONCAT(equ.ip,':',equ.`port`) ip ");
        sql.append("FROM inventory_detail detail ");
        sql.append("LEFT JOIN assets_manage am ON detail.asset_id = am.id ");
        sql.append("left join sys_department sd on sd.id=am.dept_id ");
        sql.append("LEFT JOIN equipment equ ON sd.dept_position = equ.equipment_position ");
        sql.append("WHERE equ.ip IS NOT NULL ");
        sql.append("AND equ.`port` IS NOT NULL ");
        sql.append("AND detail.is_del = '0' ");
        sql.append("AND am.is_del = '0' ");
        sql.append("AND equ.is_del='0' ");
        sql.append("AND detail.inventory_status='1' ");
        if (notEmpty(inventoryJob.getId())){
            sql.append("AND detail.inventory_job_id= "+inventoryJob.getId());
        }




        return sql.toString();
    }

    public String toRFID(Integer id){
        StringBuilder sql=new StringBuilder();

        sql.append("SELECT detail.* FROM inventory_detail detail ");
        sql.append("LEFT JOIN inventory_job job ON detail.inventory_job_id = job.id ");
        sql.append("WHERE detail.is_del = '0' ");
        sql.append("AND job.is_del = '0' ");
        sql.append("AND detail.inventory_status = '1' ");
        sql.append("AND INSTR('1',job.inventory_status) ");
        sql.append("AND job.progress_node='2' ");


        return sql.toString();
    }

    public String getAssetsManages(InventoryJob inventoryJob){
        StringBuilder sql=new StringBuilder();

        sql.append("SELECT am.* FROM assets_manage am ");
        sql.append("INNER JOIN inventory_detail detail ON am.id = detail.asset_id ");
        sql.append("where am.is_del='0' and detail.is_del='0' ");
        if (notEmpty(inventoryJob.getId())){
            sql.append(" and detail.inventory_job_id="+inventoryJob.getId());
        }

        return sql.toString();
    }
}
