package com.jsdc.rfid.controller;

import com.jsdc.rfid.model.SysLog;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.service.SysLogService;
import com.jsdc.rfid.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.List;

@RequestMapping("log")
@Controller
public class SysLogController {
    @Autowired
    private SysLogService logService;
    @Autowired
    private SysUserService userService;

    @RequestMapping("toIndex.do")
    public String toIndex(Model model){
        List<SysUser> users = userService.getList(null);
        model.addAttribute("users", users);
        return "system/log/index";
    }

    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(SysLog log,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(logService.getPage(log, page, limit));
    }


}
