package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.SysFloor;
import com.jsdc.rfid.model.SysPlace;
import com.jsdc.rfid.model.SysPosition;
import com.jsdc.rfid.service.SysFloorService;
import com.jsdc.rfid.service.SysPlaceService;
import com.jsdc.rfid.service.SysPositionService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.List;

@RequestMapping("position")
@Controller
public class SysPositionController {

    @Autowired
    private SysPositionService positionService;
    @Autowired
    private SysPlaceService placeService;
    @Autowired
    private SysFloorService floorService;

    @RequestMapping("toIndex.do")
    public String toIndex(Model model){
        List<SysPlace> places = placeService.getList(null);
        model.addAttribute("places", places);
        return "system/position/index";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(Model model){
        List<SysPlace> places = placeService.getList(null);
        List<SysFloor> floors = floorService.selectList(Wrappers.<SysFloor>lambdaQuery().eq(SysFloor::getIs_del, G.ISDEL_NO));
        SysFloor floor = floors.get(0);
        model.addAttribute("places", places);
        model.addAttribute("floors", floors);
        model.addAttribute("floor", floor);
        return "system/position/add";
    }

    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model){
        SysPosition position = positionService.selectById(id);
        List<SysPlace> places = placeService.getList(null);
        model.addAttribute("places", places);
        model.addAttribute("position", position);
        List<SysFloor> floors = floorService.selectList(Wrappers.<SysFloor>lambdaQuery().eq(SysFloor::getIs_del, G.ISDEL_NO));
        model.addAttribute("floors", floors);
        return "system/position/edit";
    }

    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(SysPosition position,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(positionService.getPage(position, page, limit));
    }

    @RequestMapping("getMarker.do")
    @ResponseBody
    public ResultInfo getMarker(@NonNull Integer id){
        return ResultInfo.success(positionService.getMarker(id));
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(@NonNull SysPosition position){
        return ResultInfo.success(positionService.add(position));
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(@NonNull SysPosition position){
        return ResultInfo.success(positionService.edit(position));
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(Integer id){
        return ResultInfo.success(positionService.del(id));
    }

    /**
     * 根据地点id查询位置
     */
    @RequestMapping("getListByPlaceId.do")
    @ResponseBody
    public ResultInfo getListByPlaceId(Integer placeId){
        return ResultInfo.success(positionService.getListByPlaceId(placeId));
    }
}
