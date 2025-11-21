package com.jsdc.rfid.controller;

import com.jsdc.rfid.model.Supplier;
import com.jsdc.rfid.service.SupplierService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * 供应商
 */
@RequestMapping("supplier")
@Controller
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @RequestMapping("toIndex.do")
    public String toIndex(){
        return "system/supplier/index";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(){
        return "system/supplier/add";
    }

    @RequestMapping("toEdit.do")
    public String edit(@NonNull Integer id, Model model){
        Supplier supplier = supplierService.selectById(id);
        model.addAttribute("supplier", supplier);
        return "system/supplier/edit";
    }


    @RequestMapping("getList.do")
    @ResponseBody
    public ResultInfo getList(Supplier supplier){
        return ResultInfo.success(supplierService.getList(supplier));
    }

    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(Supplier supplier,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(supplierService.getPage(supplier, page, limit));
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(Supplier supplier){
        return ResultInfo.success(supplierService.add(supplier));
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(Supplier supplier){
        return ResultInfo.success(supplierService.edit(supplier));
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(Integer id){
        return ResultInfo.success(supplierService.delete(id));
    }
}
