package com.jsdc.rfid.service.warehouse;

import com.jsdc.rfid.vo.EquipNumStatisticsVo;

import java.util.List;
import java.util.Map;

public interface StatisticsWarehouseService {

    EquipNumStatisticsVo equipNum(String type);

    List<Map<String,List<Map<String,Object>>>> equipNumList(String type);

    Map<String,Object> equipType(String type,Integer equip_status);

    Map<String, List<Map<String,Object>>> equipDept(String type);

}
