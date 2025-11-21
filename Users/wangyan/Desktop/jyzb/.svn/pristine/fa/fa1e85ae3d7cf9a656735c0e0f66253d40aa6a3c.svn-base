package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.model.SysDict;
import com.jsdc.rfid.service.SysDictService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vo.ResultInfo;

/**
 * 数据字典
 */
@RequestMapping("dict")
@Controller
public class SysDictController {
    @Autowired
    private SysDictService dictService;

    @RequestMapping("toIndex.do")
    public String toIndex(){
        return "system/dict/index";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(){
        return "system/dict/add";
    }

    @RequestMapping("toEdit.do")
    public String edit(@NonNull Integer id, Model model){
        SysDict dict = dictService.selectById(id);
        model.addAttribute("dict", dict);
        return "system/dict/edit";
    }

    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(SysDict dict,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(dictService.getPage(dict, page, limit));
    }

    @GetMapping("/{type}/getDictByType.do")
    @ResponseBody
    public ResultInfo getDictByType(@PathVariable String type){
        return ResultInfo.success(dictService.selectList(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getIs_del, String.valueOf(0)).eq(SysDict::getType, type)));
    }

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo add(SysDict dict){
        return ResultInfo.success(dictService.add(dict));
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(SysDict dict){
        return ResultInfo.success(dictService.edit(dict));
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(Integer id){
        return ResultInfo.success(dictService.del(id));
    }
}
