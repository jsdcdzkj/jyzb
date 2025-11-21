package com.jsdc.rfid.dao;

import com.jsdc.core.base.BaseDao;
import com.jsdc.rfid.model.Consumable;
import org.springframework.stereotype.Repository;

@Repository
public class ConsumableDao extends BaseDao<Consumable> {

    public String earlyWarningList(){
        String sql = "\tSELECT\n" +
                "\tcc.consumable_name,\n" +
                "\tc.consumable_name consumable_name2,\n" +
                "\t\tc.prewarning_value,\n" +
                "\tSUM(cim.inventory_num) inventory_num\n" +
                "\t\n" +
                "\tFROM\n" +
                "\t\tcons_inventory_management cim \n" +
                "\tleft join consumable c on cim.consumable_id = c.id\n" +
                "\tleft join consumable cc on c.parent_id = cc.id\n" +
                "\tGROUP BY\n" +
                "\t\tcim.consumable_id"
                +" HAVING SUM(cim.inventory_num) <= c.prewarning_value";

        return sql;
    }
}
