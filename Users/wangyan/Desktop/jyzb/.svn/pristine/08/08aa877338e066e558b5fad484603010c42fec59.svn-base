package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.RFIDConfigMapper;
import com.jsdc.rfid.model.Equipment;
import com.jsdc.rfid.model.RFIDConfig;
import com.jsdc.rfid.model.SysDict;
import com.jsdc.rfid.model.SysPosition;
import com.jsdc.rfid.service.EquipmentService;
import com.jsdc.rfid.service.SysDictService;
import com.jsdc.rfid.service.SysPositionService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.List;

/**
 * 设备
 */
@RequestMapping("equipment")
@Controller
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private SysPositionService positionService;
    @Autowired
    private SysDictService dictService;

    @Autowired
    private RFIDConfigMapper rfidConfigMapper;


    @RequestMapping("toIndex.do")
    public String toIndex(Model model){
        List<SysPosition> positionList = positionService.selectList(Wrappers.<SysPosition>lambdaQuery().eq(SysPosition::getIs_del, G.ISDEL_NO));
        model.addAttribute("positions", positionList);

        //设备类型
        SysDict dict2 = new SysDict();
        dict2.setType("equipment_type");
        List<SysDict> typeList = dictService.getList(dict2);
        model.addAttribute("typeList", typeList);
        return "system/equipment/index";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(Model model){
        List<SysPosition> positionList = positionService.selectList(Wrappers.<SysPosition>lambdaQuery().eq(SysPosition::getIs_del, G.ISDEL_NO));
        SysDict dict = new SysDict();
        dict.setType("equipment_usage");
        List<SysDict> dicts = dictService.getList(dict);
        model.addAttribute("positions", positionList);
        model.addAttribute("dicts", dicts);

        //设备类型
        SysDict dict2 = new SysDict();
        dict2.setType("equipment_type");
        List<SysDict> typeList = dictService.getList(dict2);
        model.addAttribute("typeList", typeList);
        model.addAttribute("abTypeList", dictService.selectList(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getType, "ab_type").eq(SysDict::getIs_del, G.ISDEL_NO)));
        //abType
        List<RFIDConfig> rfidConfig = rfidConfigMapper.selectList(Wrappers.<RFIDConfig>lambdaQuery().eq(RFIDConfig::getIs_del, G.ISDEL_NO));
        model.addAttribute("isAB", CollectionUtils.isEmpty(rfidConfig)?0:rfidConfig.get(0).getIsab());
        return "/system/equipment/add";
    }

    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model){
        List<SysPosition> positionList = positionService.selectList(null);
        List<Equipment> equipmentList = equipmentService.selectList(null);
        Equipment equipment = equipmentService.selectById(id);
        SysDict dict = new SysDict();
        dict.setType("equipment_usage");
        List<SysDict> dicts = dictService.getList(dict);
        model.addAttribute("equipment", equipment);
        model.addAttribute("equipments", equipmentList);
        model.addAttribute("positions", positionList);
        model.addAttribute("dicts", dicts);

        //设备类型
        SysDict dict2 = new SysDict();
        dict2.setType("equipment_type");
        List<SysDict> typeList = dictService.getList(dict2);
        model.addAttribute("typeList", typeList);
        model.addAttribute("abTypeList", dictService.selectList(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getType, "ab_type").eq(SysDict::getIs_del, G.ISDEL_NO)));

        //abType
        List<RFIDConfig> rfidConfig = rfidConfigMapper.selectList(Wrappers.<RFIDConfig>lambdaQuery().eq(RFIDConfig::getIs_del, G.ISDEL_NO));
        model.addAttribute("isAB", CollectionUtils.isEmpty(rfidConfig)?0:rfidConfig.get(0).getIsab());
        return "system/equipment/edit";
    }

    /**
     * 分页查询
     * @param equipment
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(Equipment equipment,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(equipmentService.getPage(equipment, page, limit));
    }

    /**
     * 列表
     */
    @RequestMapping("getEquipmentList.json")
    @ResponseBody
    public ResultInfo getEquipmentList(Equipment equipment){
        return ResultInfo.success(equipmentService.getLists(equipment));
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(@NonNull Equipment equipment){
        return ResultInfo.success(equipmentService.edit(equipment));
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(@NonNull Equipment equipment){
        return ResultInfo.success(equipmentService.add(equipment));
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(@NonNull Integer id){
        return ResultInfo.success(equipmentService.del(id));
    }
}
