package com.jsdc.rfid.controller;

import com.jsdc.rfid.model.SysPermission;
import com.jsdc.rfid.service.SysPermissionService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * 菜单
 */
@RequestMapping("permission")
@Controller
public class SysPermissionController {
    @Autowired
    private SysPermissionService permissionService;

    @RequestMapping("getTree.do")
    @ResponseBody
    public ResultInfo getTree(SysPermission sysPermission){
        return ResultInfo.success(permissionService.getTree(sysPermission));
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(@NonNull SysPermission sysPermission) {
        SysPermission a = permissionService.edit(sysPermission);
        if (null == a) {
            return ResultInfo.error("请检查名称和编码是否重复");
        }
        return ResultInfo.success(a);
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(@NonNull SysPermission sysPermission) {
        SysPermission a = permissionService.add(sysPermission);
        if (null == a) {
            return ResultInfo.error("请检查名称和编码是否重复");
        }
        return ResultInfo.success(a);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(@NonNull Integer id) {
        return ResultInfo.success(permissionService.delete(id));
    }
}
