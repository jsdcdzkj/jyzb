package com.jsdc.rfid.controller;

import cn.hutool.core.lang.tree.Tree;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.SysDepartmentService;
import com.jsdc.rfid.service.SysPermissionService;
import com.jsdc.rfid.service.SysPostPermissionService;
import com.jsdc.rfid.service.SysPostService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色
 */
@RequestMapping("post")
@Controller
public class SysPostController {

    @Autowired
    private SysPostService postService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysPostPermissionService sysPostPermissionService;

    @RequestMapping("toIndex.do")
    public String toIndex(Model model){
        List<SysDepartment> departmentList = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("deptList", departmentList);
        return "system/post/index";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(Model model){
        List<SysDepartment> departmentList = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("depts", departmentList);
        return "system/post/add";
    }

    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model){
        SysPost post = postService.selectById(id);
        List<SysDepartment> departmentList = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("post", post);
        model.addAttribute("depts", departmentList);
        return "system/post/edit";
    }

    @RequestMapping("toTree.do")
    public String toTree(Integer id, Model model){
        JSONArray treeData = permissionService.getTree(new SysPermission());
        model.addAttribute("treeData", treeData);
        model.addAttribute("id", id);
        return "system/post/tree";
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(@NonNull SysPost post){
        SysPost sysPost = postService.add(post);
        if (null == sysPost){
            return ResultInfo.error("编码不能重复");
        }
        return ResultInfo.success(sysPost);
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(@NonNull SysPost post){
        SysPost sysPost = postService.edit(post);
        if (null == sysPost){
            return ResultInfo.error("编码不能重复");
        }
        return ResultInfo.success(sysPost);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ResultInfo detail(@NonNull Integer id) {
        // 查询 SysPost
        SysPost post = postService.selectById(id);

        // 设置权限 ID 字符串
        post.setPermissionIds(sysPostPermissionService.getPermissionIds(id));

        // 返回结果
        return ResultInfo.success(post);
    }


    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(@NonNull Integer id){
        return ResultInfo.success(postService.del(id));
    }

    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(SysPost post,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(postService.getPage(post, page, limit));
    }

    @RequestMapping("getPermissions.do")
    @ResponseBody
    public ResultInfo getPermissions(Integer id){
        return ResultInfo.success(postService.getPermissions(id));
    }

    @RequestMapping("getList.do")
    @ResponseBody
    public ResultInfo getList(SysPost post){
        return ResultInfo.success(postService.getList(post));
    }
}
