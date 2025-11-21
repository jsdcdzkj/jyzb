package com.jsdc.rfid.service.warehouse;

import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.model.warehouse.WarehousingDelivery;
import com.jsdc.rfid.model.warehouse.WarehousingEnterDetail;

import java.util.List;


public interface DeliveryWarehouseService {

    PageInfo<WarehousingDelivery> pageQuery(Integer pageIndex, Integer pageSize, WarehousingDelivery warehousingDelivery);

    void add(WarehousingDelivery warehousingDelivery);

    WarehousingDelivery detail(Integer id);

    List<String> detailByName(Integer delivery_name, Integer warehouseId, Integer use_dept);

    List<String> detailByModel(String equip_model, Integer equip_name, Integer warehouseId, Integer use_dept);

}
