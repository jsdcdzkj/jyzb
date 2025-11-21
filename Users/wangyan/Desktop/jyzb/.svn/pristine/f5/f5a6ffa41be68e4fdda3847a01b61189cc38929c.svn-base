package com.jsdc.rfid.controller.warehouse;

import com.jsdc.rfid.service.warehouse.StatisticsWarehouseService;
import com.jsdc.rfid.service.warehouse.SystemNoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import javax.annotation.Resource;

/**
 * 装备概览
 */
@Controller
@RequestMapping("/statisticsWarehouse")
public class StatisticsWarehouseController {

    @Resource
    private StatisticsWarehouseService statisticsWarehouseService;

    @Resource
    private SystemNoticeService systemNoticeService;

    /**
     * 装备数量统计
     * @param type 通用装备/特种装备/车辆/应急物资
     */
    @RequestMapping(value = "equipNum.do", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo equipNum(String type) {
        return ResultInfo.success(statisticsWarehouseService.equipNum(type));
    }

    /**
     * 装备类型分析
     * @param type 通用装备/特种装备/车辆/应急物资
     */
    @RequestMapping(value = "equipType.do", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo equipType(String type,Integer equip_status) {
        return ResultInfo.success(statisticsWarehouseService.equipType(type,equip_status));
    }

    /**
     * 分局装备统计
     * @param type 通用装备/特种装备/车辆/应急物资
     */
    @RequestMapping(value = "equipDept.do", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo equipDept(String type) {
        return ResultInfo.success(statisticsWarehouseService.equipDept(type));
    }

    /**
     * 装备趋势统计
     * @param type 通用装备/特种装备/车辆/应急物资
     */
    @RequestMapping(value = "equipNumList.do", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo equipNumList(String type) {
        return ResultInfo.success(statisticsWarehouseService.equipNumList(type));
    }

    /**
     * 系统通知
     */
    @RequestMapping(value = "noticeList.do", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo noticeList(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                                 @RequestParam(defaultValue = "10", value = "limit") Integer pageSize) {
        return ResultInfo.success(systemNoticeService.pageQuery(pageIndex,pageSize));
    }
}
