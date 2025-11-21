package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.AssetsType;
import com.jsdc.rfid.model.SysDepartment;
import com.jsdc.rfid.model.SysPosition;
import com.jsdc.rfid.model.SysUser;
import com.jsdc.rfid.service.SysDepartmentService;
import com.jsdc.rfid.service.SysPositionService;
import com.jsdc.rfid.service.SysUserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vo.ResultInfo;

import java.util.List;

/**
 * 机构
 */
@RequestMapping("dept")
@Controller
public class SysDepartmentController {
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private SysPositionService positionService;
    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "system/department/index";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(Model model, Integer id) {
        //List<SysPosition> positionList = positionService.selectList(Wrappers.<SysPosition>lambdaQuery().eq(SysPosition::getIs_del, G.ISDEL_NO));
        QueryWrapper<SysDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        if (id != null) {
            queryWrapper.eq("id", id);
        } else {
            queryWrapper.eq("parent_dept", 0);
            queryWrapper.or();
            queryWrapper.isNull("parent_dept");
        }
        //List<SysDepartment> departments = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        List<SysDepartment> departments = departmentService.selectList(queryWrapper);
        model.addAttribute("depts", departments);
        model.addAttribute("deptId", id);
        //model.addAttribute("positions", positionList);
        return "/system/department/add";
    }

    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model) {
        SysDepartment department = departmentService.selectById(id);
        //List<SysPosition> positionList = positionService.selectList(Wrappers.<SysPosition>lambdaQuery().eq(SysPosition::getIs_del, G.ISDEL_NO));
        QueryWrapper<SysDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", "0");
        if (department.getParent_dept() != null) {
            queryWrapper.eq("id", department.getParent_dept());
        } else {
            queryWrapper.eq("parent_dept", 0);
            queryWrapper.or();
            queryWrapper.isNull("parent_dept");
        }
        //List<SysDepartment> departments = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        List<SysDepartment> departments = departmentService.selectList(queryWrapper);
        model.addAttribute("dept", department);
        model.addAttribute("depts", departments);
        //model.addAttribute("positions", positionList);
        return "system/department/edit";
    }

    /**
     * 获取list集合
     *
     * @param department
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(SysDepartment department) {
        SysUser sysUser = sysUserService.getUser();
        SysDepartment sysDepartment = departmentService.selectById(sysUser.getDepartment());
        List<SysDepartment> list = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery()
                .eq(SysDepartment::getParent_dept, sysDepartment.getId())
                .eq(SysDepartment::getIs_del, "0"));
        for (int i = 0; i < list.size(); i++) {
            List<SysDepartment> sysDepartments = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery()
                    .eq(SysDepartment::getParent_dept, list.get(i).getId())
                    .eq(SysDepartment::getIs_del, "0"));
            for (SysDepartment value : sysDepartments) {
                List<SysDepartment> departments = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery()
                        .eq(SysDepartment::getParent_dept, value.getId())
                        .eq(SysDepartment::getIs_del, "0"));
                list.addAll(departments);
            }
            list.addAll(sysDepartments);
        }
        list.add(sysDepartment);
        return ResultInfo.success(list);
    }


    /**
     * 树形图
     *
     * @param sysDepartment
     * @return
     */
    @RequestMapping(value = "getTree.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getTree(SysDepartment sysDepartment) {
        return ResultInfo.success(departmentService.getTreeList(sysDepartment));
    }

    /**
     * 树形图 二级
     *
     * @param sysDepartment
     * @return
     */
    @RequestMapping(value = "getTreeTwo.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getTreeTwo(SysDepartment sysDepartment) {
        return ResultInfo.success(departmentService.getTreeListTwo(sysDepartment));
    }

    /**
     * 机构 三级
     *
     * @param sysDepartment
     * @return
     */
    @RequestMapping(value = "getThree.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getThree(SysDepartment sysDepartment) {
        return ResultInfo.success(departmentService.getThree(sysDepartment));
    }

    /**
     * 仓库合并
     */
    @RequestMapping(value = "merge.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo merge(@RequestBody SysDepartment sysDepartment) {
        return ResultInfo.success(departmentService.merge(sysDepartment));
    }

    /**
     * 分页查询
     *
     * @param department
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(SysDepartment department,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit) {
        return ResultInfo.success(departmentService.getPage(department, page, limit));
    }

    @RequestMapping("loadTree.do")
    @ResponseBody
    public ResultInfo loadTree(SysDepartment department) {
        return ResultInfo.success(departmentService.loadTree(department));
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(@NonNull SysDepartment department) {
        SysDepartment a = departmentService.edit(department);
        if (null == a) {
            return ResultInfo.error("请检查名称和编码是否重复");
        }
        return ResultInfo.success(a);
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(@NonNull SysDepartment department) {
        SysDepartment a = departmentService.add(department);
        if (null == a) {
            return ResultInfo.error("请检查名称和编码是否重复");
        }
        return ResultInfo.success(a);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(@NonNull Integer id) {
        return ResultInfo.success(departmentService.delete(id));
    }

    /**
     * 导入
     */
    @RequestMapping(value = "/toImport.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo importInventoryManagementIn(MultipartFile file){
        return departmentService.importWarehousing(file);
    }
}
