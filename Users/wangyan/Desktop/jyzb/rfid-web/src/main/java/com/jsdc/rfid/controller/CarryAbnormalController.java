package com.jsdc.rfid.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.mapper.CarryAbnormalMemberMapper;
import com.jsdc.rfid.model.CarryAbnormal;
import com.jsdc.rfid.model.CarryAbnormalMember;
import com.jsdc.rfid.model.Equipment;
import com.jsdc.rfid.service.CarryAbnormalService;
import com.jsdc.rfid.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

import java.util.List;

/**
 * @author zln
 * @descript 外携异常管理
 * @date 2022-04-24
 */
@Controller
@RequestMapping("/carryabnormal")
public class CarryAbnormalController extends BaseController {

    @Autowired
    private CarryAbnormalService carryAbnormalService;
    @Autowired
    private CarryAbnormalMemberMapper carryAbnormalMemberMapper;
    @Autowired
    private EquipmentService equipmentService;

    //跳转分页页面
    @RequestMapping(value = "pageList.do")
    public String pageList() {
        return "/carryabnormal/page";
    }

    //跳转分页页面
    @RequestMapping(value = "pageLogList.do")
    public String pageLogList(Integer id, Model model) {
        List<CarryAbnormalMember> members = carryAbnormalMemberMapper.selectList(Wrappers.<CarryAbnormalMember>query()
                .eq("carry_abnormal_id", id)
                .orderByDesc("create_time")
        );
        model.addAttribute("members", members);
        return "/carryabnormal/view-log";
    }

    //分页数据
    @RequestMapping(value = "pageList.json")
    @ResponseBody
    public ResultInfo pageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, CarryAbnormal bean) {
        bean.setError_status("1");
        PageInfo pageInfo = carryAbnormalService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    //分页数据
    @RequestMapping(value = "homePageList.json")
    @ResponseBody
    public ResultInfo homePageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, CarryAbnormal bean) {
        bean.setError_status("1");
        PageInfo pageInfo = carryAbnormalService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    // 最近5分钟的异常数据信息
    @RequestMapping(value = "fiveMinData.json")
    @ResponseBody
    public ResultInfo fiveMinData( CarryAbnormal bean) {
        return ResultInfo.success(carryAbnormalService.fiveMinData(bean));
    }

    // 查询最近3分钟有没有异常数据
    @RequestMapping(value = "threeMinData.json")
    @ResponseBody
    public ResultInfo threeMinData() {
        return ResultInfo.success(carryAbnormalService.threeMinData());
    }

    //分页数据
    @GetMapping(value = "getPageLists.json")
    @ResponseBody
    public ResultInfo getPageLists(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, CarryAbnormal bean) {
//        if (notEmpty(bean)) {
//            if (notEmpty(bean.getEquipment_id())) {
//                Equipment equipment = equipmentService.selectById(bean.getEquipment_id());
//                //用处 【字典 1扫描 2报警】
//                if (equipment.getEquipment_usage() == 2) {
//                    //异常状态：1 未授权外携告警 2 位置变换告警 3 标签损毁告警
////                    bean.setError_status("1");
//                    bean.setChange_position_id(equipment.getEquipment_position());
//                    bean.setPosition_id(null);
//                } else if (equipment.getEquipment_usage() == 1) {
//                    bean.setError_status("2");
//                    bean.setChange_position_id(equipment.getEquipment_position());
//                    bean.setPosition_id(null);
//                }
//            }
//        }
        PageInfo pageInfo = carryAbnormalService.selectPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

    //跳转详情、修改、新增页面
    @RequestMapping(value = "selectByCarryAbnormalId.do")
    public String selectByCarryManageId() {
        return "carrymanage/edit";
    }

    //详情、修改、新增读取数据
    @RequestMapping(value = "selectByCarryAbnormalId.json")
    @ResponseBody
    public String selectByCarryAbnormalId(CarryAbnormal bean) {
        return carryAbnormalService.selectByCarryAbnormalId(bean);
    }

    //删除功能
    @RequestMapping(value = "deleteCarryAbnormal.json")
    @ResponseBody
    public ResultInfo deleteCarryAbnormal(Integer id) {
        Integer count = carryAbnormalService.deleteCarryAbnormal(id);
        if (count > 0) {
            return ResultInfo.success("操作成功！");
        } else {
            return ResultInfo.success("操作失败！");
        }
    }

    //保存功能
    @RequestMapping(value = "saveCarryAbnormal.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveCarryAbnormal(CarryAbnormal bean) {
        Integer count = carryAbnormalService.saveCarryAbnormal(bean);
        if (count > 0) {
            return ResultInfo.success("操作成功！");
        } else {
            return ResultInfo.success("操作失败！");
        }
    }

    /**
     * 处理报警
     */
    @RequestMapping(value = "warningUpd.json", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo warningUpd(CarryAbnormal bean) {
        Integer count = carryAbnormalService.warningUpd(bean);
        JSONObject jsonObject = new JSONObject();
        if (count > 0) {
            bean = carryAbnormalService.selectById(bean.getId());
            //根据存放位置查询报警信息是否存在未处理的数据
            List<CarryAbnormal> list = carryAbnormalService.getListByPositionId(bean);
            //若不存在未处理的数据，则修改设备状态
            if (CollectionUtils.isEmpty(list)) {
                Equipment equipment = new Equipment();
                //变动位置
                equipment.setEquipment_position(bean.getChange_position_id());
                //报警状态 1正常 2报警
                equipment.setWarning_status(2);
                //根据存放位置查询设备列表
                List<Equipment> equipmentList = equipmentService.getList(equipment);
                equipmentList.forEach(x -> {
                    x.setWarning_status(1);
                    equipmentService.updateById(x);
                });
                //替换报警图标为正常图标 标识
                jsonObject.put("status", "1");
                return ResultInfo.success(jsonObject);
            }
            jsonObject.put("status", "0");
            return ResultInfo.success(jsonObject);
        } else {
            return ResultInfo.success("操作失败！");
        }
    }

    /**
     * 预警历史 分页数据
     */
    @RequestMapping(value = "getPageList.json")
    @ResponseBody
    public ResultInfo getPageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit, CarryAbnormal bean) {

        PageInfo pageInfo = carryAbnormalService.getPageList(page, limit, bean);
        return ResultInfo.success(pageInfo);
    }

}
