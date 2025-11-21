package com.jsdc.rfid.controller;

import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.WarehouseUserMapper;
import com.jsdc.rfid.model.SysDepartment;
import com.jsdc.rfid.model.SysPost;
import com.jsdc.rfid.model.Warehouse;
import com.jsdc.rfid.model.WarehouseUserMember;
import com.jsdc.rfid.service.SysDepartmentService;
import com.jsdc.rfid.service.SysPostService;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.service.WarehouseService;
import com.jsdc.rfid.utils.CommonDataTools;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库
 */
@Slf4j
@RequestMapping("warehouse")
@Controller
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private SysDepartmentService departmentService;
    @Autowired
    private SysPostService postService;
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WarehouseUserMapper warehouseUserMapper;

    @RequestMapping("toIndex.do")
    public String toIndex(){
        return "system/warehouse/index";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(Model model){
        //部门查询
        SysDepartment sysDepartment = new SysDepartment();
        sysDepartment.setId(sysUserService.getUser().getDepartment());
        model.addAttribute("deptList",departmentService.getTree(sysDepartment));
        return "system/warehouse/add";
    }

    @RequestMapping("toEdit.do")
    public String edit(@NonNull Integer id, Model model){
        Warehouse warehouse = warehouseService.selectById(id);
        SysDepartment dept = departmentService.selectById(warehouse.getDept_id());
        warehouse.setDept_name(dept.getDept_name());
        model.addAttribute("warehouse", warehouse);
        //部门查询
        SysDepartment sysDepartment = new SysDepartment();
        sysDepartment.setId(sysUserService.getUser().getDepartment());
        model.addAttribute("deptList",departmentService.getTree(sysDepartment));
        return "system/warehouse/edit";
    }

    @RequestMapping("toPermPage.do")
    public String toPermPage(@NonNull Integer id, Model model){
        Warehouse warehouse = warehouseService.selectById(id);
        model.addAttribute("warehouse", warehouse);

        List<WarehouseUserMember> member = warehouseUserMapper.selectList(Wrappers.<WarehouseUserMember>lambdaQuery().eq(WarehouseUserMember::getWarehouse_id, id));
        if (CollectionUtils.isEmpty(member)){
            model.addAttribute("member", "-1");
        } else {
            // 根据聚合member集合,user_id逗号分割
            List<Integer> a = member.stream().map(WarehouseUserMember::getUser_id).distinct().collect(Collectors.toList());
            model.addAttribute("member", a.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }

        List<SysDepartment> departmentList = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("deptList", departmentList);
        List<SysPost> posts = postService.selectList(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getIs_del, G.ISDEL_NO));
        model.addAttribute("posts", posts);
        return "system/warehouse/perm";
    }

    /**
     * 跳转资产展示页面
     *
     * @return
     */
    @RequestMapping("toSelect.do")
    public String toSelect(Integer warehouse_id, Model model){
        if (null == warehouse_id){
            log.error("warehouse_id is null");
            return "system/warehouse/perm";
        }
        //获取所有资产类型
        Warehouse warehouse = warehouseService.selectById(warehouse_id);
        model.addAttribute("warehouse", warehouse);

        List<WarehouseUserMember> member = warehouseUserMapper.selectList(Wrappers.<WarehouseUserMember>lambdaQuery().eq(WarehouseUserMember::getWarehouse_id, warehouse_id));
        if (CollectionUtils.isEmpty(member)){
            model.addAttribute("member", "-1");
        } else {
            // 根据聚合member集合,user_id逗号分割
            List<Integer> a = member.stream().map(WarehouseUserMember::getUser_id).distinct().collect(Collectors.toList());
            model.addAttribute("member", a.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }

        List<SysDepartment> departmentList = departmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
        model.addAttribute("deptList", departmentList);
        List<SysPost> posts = postService.selectList(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getIs_del, G.ISDEL_NO));
        model.addAttribute("posts", posts);

        // 得到已经绑定的用户集合id
        List<WarehouseUserMember> users = warehouseUserMapper.selectList(Wrappers.<WarehouseUserMember>lambdaQuery().eq(WarehouseUserMember::getWarehouse_id, warehouse_id));
        if (CollectionUtils.isEmpty(users)){
            model.addAttribute("users", "-1");
        } else {
            List<Integer> userIds = users.stream().map(WarehouseUserMember::getUser_id).distinct().collect(Collectors.toList());
            model.addAttribute("users", userIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        return "system/warehouse/select";
    }

    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(Warehouse warehouse,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(warehouseService.getPage(warehouse, page, limit));
    }

    @RequestMapping("toList.do")
    @ResponseBody
    public ResultInfo toList(Warehouse warehouse){
        return ResultInfo.success(warehouseService.getList(warehouse));
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(Warehouse warehouse){
        try {
            warehouseService.add(warehouse);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(Warehouse warehouse)  {
        try {
            warehouseService.edit(warehouse);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping("editObject.do")
    @ResponseBody
    public ResultInfo editObject(Warehouse warehouse)  {
        try {
            warehouseService.editObject(warehouse);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping("/{warehouse_id}/editUsersByWareId.do")
    @ResponseBody
    public ResultInfo editUsersByWareId(@PathVariable Integer warehouse_id, @RequestBody List<String> userIds)  {
        try {
            warehouseService.editUsersByWareId(warehouse_id, userIds);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping("/{warehouse_id}/delUsersByWareId.do")
    @ResponseBody
    public ResultInfo editUsersByWareId(@PathVariable Integer warehouse_id, Integer user_id)  {
        try {
            warehouseUserMapper.delete(Wrappers.<WarehouseUserMember>lambdaQuery().eq(WarehouseUserMember::getWarehouse_id, warehouse_id).eq(WarehouseUserMember::getUser_id, user_id));
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(Integer id){
        return ResultInfo.success(warehouseService.delete(id));
    }
}
