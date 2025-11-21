package com.jsdc.rfid.dao.warehouse;

import cn.hutool.core.date.DateUtil;
import com.jsdc.rfid.dto.WarehousingStockDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;

@Repository
public class StatisticsWarehouseDao {

    public String equipNumStatistics(WarehousingStockDto warehousingStockDto){
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.equip_status, ");
        sql.append(" a.equip_type, ");
        sql.append(" sum(a.equip_in_num) as equip_in_num, ");
        sql.append(" sum(a.equip_out_num) as equip_out_num ");
        sql.append(" from warehousing_stock_detail a ");
        sql.append(" WHERE a.is_del = '0' and (a.warehouse_id is not null or a.use_dept is not null)");
        //部门过滤条件
        if(warehousingStockDto.getDeptIds()!=null){
            sql.append(" and a.dept_id in ( ").append(String.join(",",warehousingStockDto.getDeptIds())).append(" )");
        }
        if (warehousingStockDto.getStock_type() != null) {
            sql.append(" and a.stock_type = ").append(warehousingStockDto.getStock_type());
        }
        sql.append(" group by a.equip_status,a.equip_type ");
        return sql.toString();
    }

    public String expiredEquipNumStatistics(WarehousingStockDto warehousingStockDto){
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.equip_type, ");
        sql.append(" sum(a.equip_in_num) as equip_in_num, ");
        sql.append(" sum(a.equip_out_num) as equip_out_num ");
        sql.append(" from warehousing_stock_detail a ");
        sql.append(" WHERE a.is_del = '0' and (a.warehouse_id is not null or a.use_dept is not null)");
        //时间条件拼接
        sql.append(" and a.expired_date < '"+ DateUtil.formatDateTime(new Date()) +"'");
        //部门过滤条件
        if(warehousingStockDto.getDeptIds()!=null){
            sql.append(" and a.dept_id in ( ").append(String.join(",",warehousingStockDto.getDeptIds())).append(" )");
        }
        sql.append(" group by a.equip_type ");
        return sql.toString();
    }

    public String criticalEquipNumStatistics(WarehousingStockDto warehousingStockDto){
        Date now = new Date();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.equip_type, ");
        sql.append(" sum(a.equip_in_num) as equip_in_num, ");
        sql.append(" sum(a.equip_out_num) as equip_out_num ");
        sql.append(" from warehousing_stock_detail a ");
        sql.append(" WHERE a.is_del = '0' and (a.warehouse_id is not null or a.use_dept is not null)");
        //时间条件拼接
        sql.append(" and a.critical_date <= '"+ DateUtil.formatDateTime(now) +"'");
        sql.append(" and a.expired_date >= '"+ DateUtil.formatDateTime(now) +"'");
        //部门过滤条件
        if(warehousingStockDto.getDeptIds()!=null){
            sql.append(" and a.dept_id in ( ").append(String.join(",",warehousingStockDto.getDeptIds())).append(" )");
        }
        sql.append(" group by a.equip_type ");
        return sql.toString();
    }

    public String equipTypeStatistics(WarehousingStockDto warehousingStockDto){
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.equip_type, ");
        sql.append(" sum(a.equip_in_num) as equip_in_num, ");
        sql.append(" sum(a.equip_out_num) as equip_out_num ");
        sql.append(" from warehousing_stock_detail a ");
        sql.append(" WHERE a.is_del = '0' and (a.warehouse_id is not null or a.use_dept is not null)");
        //部门过滤条件
        if(warehousingStockDto.getDeptIds()!=null){
            sql.append(" and a.dept_id in ( ").append(String.join(",",warehousingStockDto.getDeptIds())).append(" )");
        }
        if(warehousingStockDto.getEquip_status()!=null){
            sql.append(" and a.equip_status = ").append(warehousingStockDto.getEquip_status());
        }
        sql.append(" group by a.equip_type ");
        return sql.toString();
    }

    public String equipDeptStatistics(WarehousingStockDto warehousingStockDto){
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.equip_status, ");
        sql.append(" a.equip_type, ");
        sql.append(" a.dept_id, ");
        sql.append(" sum(a.equip_in_num) as equip_in_num, ");
        sql.append(" sum(a.equip_out_num) as equip_out_num ");
        sql.append(" from warehousing_stock_detail a ");
        sql.append(" WHERE a.is_del = '0' and (a.warehouse_id is not null or a.use_dept is not null)");
        if(!StringUtils.isEmpty(warehousingStockDto.getStaticsTime())){
            sql.append(" and a.account_time <= '"+warehousingStockDto.getStaticsTime()+"'");
        }
        //部门过滤条件
        if(warehousingStockDto.getDeptIds()!=null){
            sql.append(" and a.dept_id in ( ").append(String.join(",",warehousingStockDto.getDeptIds())).append(" )");
        }
        sql.append(" group by a.equip_status,a.equip_type,a.dept_id ");
        return sql.toString();
    }

    public String carryOverStatistics(WarehousingStockDto warehousingStockDto){
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" a.year, ");
        sql.append(" a.month, ");
        sql.append(" a.equip_status, ");
        sql.append(" a.equip_type, ");
        sql.append(" sum(a.equip_num) as equip_num ");
        sql.append(" from warehousing_stock_carryover a ");
        sql.append(" WHERE a.is_del = '0' ");
        if(warehousingStockDto.getYear()!=null){
            sql.append(" and a.year = ").append(warehousingStockDto.getYear());
        }
        //部门过滤条件
        if(warehousingStockDto.getDeptIds()!=null){
            sql.append(" and a.dept_id in ( ").append(String.join(",",warehousingStockDto.getDeptIds())).append(" )");
        }
        sql.append(" group by a.year,a.month,a.equip_status,a.equip_type ");
        return sql.toString();
    }
}
