package com.jsdc.rfid.controller.warehouse;

import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.model.warehouse.WarehousingDelivery;
import com.jsdc.rfid.service.warehouse.DeliveryWarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

import javax.annotation.Resource;

/**
 * 出库管理
 */
@Controller
@RequestMapping("/deliverWarehouse")
public class DeliveryWarehouseController {

    @Resource
    private DeliveryWarehouseService deliveryWarehouseService;


    /**
     * 列表
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             WarehousingDelivery warehousingDelivery) {
        PageInfo<WarehousingDelivery> page = deliveryWarehouseService.pageQuery(pageIndex, pageSize, warehousingDelivery);
        return ResultInfo.success(page);
    }

    /**
     * 根据名称查询型号
     *
     * @param equip_name 装备名称
     * @param warehouseId   仓库id
     * @param use_dept       使用部门id
     */
    @RequestMapping(value = "detailByName.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(Integer equip_name, Integer warehouseId, Integer use_dept) {
        return ResultInfo.success(deliveryWarehouseService.detailByName(equip_name, warehouseId, use_dept));
    }

    /**
     * 根据型号查询批次
     * @param equip_model 出库单名称
     * @param equip_name 装备名称
     * @param warehouseId   仓库id
     * @param use_dept       使用部门id
     */
    @RequestMapping(value = "detailByModel.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(String equip_model, Integer equip_name, Integer warehouseId, Integer use_dept) {
        return ResultInfo.success(deliveryWarehouseService.detailByModel(equip_model, equip_name, warehouseId, use_dept));
    }

    /**
     * 新增
     */
    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo add(@RequestBody WarehousingDelivery warehousingDelivery) {
        deliveryWarehouseService.add(warehousingDelivery);
        return ResultInfo.success();
    }

    /**
     * 详情
     */
    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo detail(Integer id) {
        return ResultInfo.success(deliveryWarehouseService.detail(id));
    }

}
