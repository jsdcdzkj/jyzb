package com.jsdc.rfid.controller;

import com.jsdc.rfid.service.SysFloorService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

@RequestMapping("floor")
@Controller
public class SysFloorController {

    @Autowired
    private SysFloorService floorService;

    @RequestMapping("getMap.do")
    @ResponseBody
    public ResultInfo getMap(@NonNull Integer id){
        return ResultInfo.success(floorService.selectById(id));
    }
}
