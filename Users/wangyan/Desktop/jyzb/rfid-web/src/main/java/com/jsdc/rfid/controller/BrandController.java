package com.jsdc.rfid.controller;

import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.model.BrandManage;
import com.jsdc.rfid.service.BrandManageService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

/**
 * 品牌
 */
@Controller
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandManageService brandManageService;

    /**
     * 跳转列表页
     *
     * @return
     */
    @RequestMapping("toIndex.do")
    public String toIndex() {
        return "brand/index";
    }

    @RequestMapping("edit.do")
    public String toAdd() {
        return "brand/edit";
    }

    @RequestMapping("toEdit.do")
    public String edit(@NonNull Integer id, Model model){
        BrandManage dict = brandManageService.selectById(id);
        model.addAttribute("obj", dict);
        return "brand/edit";
    }

    /**
     * 品牌管理 列表查询
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1",value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             BrandManage beanParam) {
        PageInfo<BrandManage> page = brandManageService.toList(pageIndex, pageSize, beanParam);
        return ResultInfo.success(page);
    }

    /**
     *  添加
     */
    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo add(BrandManage bean){
        return brandManageService.save(bean);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "remove.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo delete(@RequestParam("id") Integer id){
        return brandManageService.remove(id);
    }
}
