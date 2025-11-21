package com.jsdc.rfid.controller;

import com.jsdc.rfid.model.SysPlace;
import com.jsdc.rfid.service.SysPlaceService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

@RequestMapping("place")
@Controller
public class SysPlaceController {

    @Autowired
    private SysPlaceService placeService;

    @RequestMapping("toIndex.do")
    public String toIndex(){
        return "system/place/index";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(){
        return "system/place/add";
    }

    @RequestMapping("toEdit.do")
    public String toEdit(@NonNull Integer id, Model model){
        SysPlace place = placeService.selectById(id);
        model.addAttribute("place", place);
        return "system/place/edit";
    }

    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit,
                              SysPlace place){
        return ResultInfo.success(placeService.getPage(page, limit, place));
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(@NonNull SysPlace place){
        return ResultInfo.success(placeService.add(place));
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(@NonNull SysPlace place){
        return ResultInfo.success(placeService.edit(place));
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(@NonNull Integer id){
        return ResultInfo.success(placeService.del(id));
    }

    @RequestMapping("getMap.do")
    @ResponseBody
    public ResultInfo getMap(@NonNull Integer id){
        return ResultInfo.success(placeService.selectById(id));
    }
}
