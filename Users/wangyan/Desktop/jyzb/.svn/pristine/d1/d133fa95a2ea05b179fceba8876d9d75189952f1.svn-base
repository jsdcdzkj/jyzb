package com.jsdc.rfid.dao.warehouse;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.model.warehouse.WarehousingStockCarryOver;
import org.springframework.stereotype.Repository;

@Repository
public class WarehouseStockCarryOverDao extends BaseDao<WarehousingStockCarryOver> {


    public String carryOverList(WarehousingStockDto warehousingStockDto){
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.equip_status, ");
        sql.append(" a.equip_type, ");
        sql.append(" a.equip_num, ");
        sql.append(" a.year, ");
        sql.append(" a.month ");
        sql.append(" from warehousing_stock_carryover a ");
        sql.append(" WHERE a.is_del = '0' ");
        //部门过滤条件
        if(warehousingStockDto.getDeptIds()!=null){
            sql.append(" and a.dept_id in ( ").append(String.join(",",warehousingStockDto.getDeptIds())).append(" )");
        }
        if(warehousingStockDto.getYear()!=null){
            sql.append(" and a.year = ").append(warehousingStockDto.getYear());
        }
        if(warehousingStockDto.getMonth()!=null){
            sql.append(" and a.month = ").append(warehousingStockDto.getMonth());
        }
        return sql.toString();
    }


}
