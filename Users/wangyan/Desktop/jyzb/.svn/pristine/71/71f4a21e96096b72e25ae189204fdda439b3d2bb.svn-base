package com.jsdc.rfid.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.dto.WarehousingStockDto;
import com.jsdc.rfid.model.AssetsType;
import com.jsdc.rfid.model.Consumable;
import com.jsdc.rfid.model.warehouse.WarehousingStock;
import com.jsdc.rfid.service.AssetsTypeService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 类别
 */
@Controller
@RequestMapping("assetsType")
public class AssetsTypeController extends BaseController {

    @Autowired
    private AssetsTypeService assetsTypeService;

    @RequestMapping(value = "toList.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(AssetsType assetsType){

        return ResultInfo.success(assetsTypeService.getList(assetsType));
    }

    @RequestMapping(value = "getTree.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getTree(AssetsType assetsType){

        return ResultInfo.success(assetsTypeService.getTree(assetsType));
    }

    /**
     * 类型 导出
     */
    @RequestMapping(value = "exportAllTreeData.do",  method = RequestMethod.GET)
    public void exportAllTreeData(HttpServletResponse response) throws IOException {
        JSONArray treeData = assetsTypeService.getFullTreeData();
        String fileName = "资产类型树状数据_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)  + ".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",  "attachment;filename=" +
                URLEncoder.encode(fileName,  "UTF-8"));

        try (ServletOutputStream out = response.getOutputStream();
             BigExcelWriter writer = ExcelUtil.getBigWriter())  {

            writer.addHeaderAlias("treeTitle",  "树形名称")
                    .addHeaderAlias("fullPath", "全路径")
                    .addHeaderAlias("level", "层级")
                    .setOnlyAlias(true);

            writer.write(treeData,  true);
            writer.flush(out,  true);
        }
    }



    @RequestMapping("toIndex.do")
    public String toIndex(){
        return "system/assetsType/index";
    }

    @RequestMapping("toImportIndex.do")
    public String toImportIndex(){
        return "system/assetsType/import";
    }

    @RequestMapping("toAdd.do")
    public String toAdd(Model model,Integer id){
        QueryWrapper<AssetsType> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("is_del","0");
        if(id!=null){
            queryWrapper.eq("id",id);
        }else{
            queryWrapper.eq("parent_id","0");
        }
        model.addAttribute("assetsTypeList", assetsTypeService.selectList(queryWrapper));
        model.addAttribute("assetsTypeId",id);
        return "system/assetsType/add";
    }

    @RequestMapping("toEdit.do")
    public String toEdit(Integer id, Model model){
        AssetsType assetsType = assetsTypeService.selectById(id);
        model.addAttribute("assetsType", assetsType);
        QueryWrapper<AssetsType> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("is_del","0");
        queryWrapper.eq("id",assetsType.getParent_id());
        model.addAttribute("assetsTypeList", assetsTypeService.selectList(queryWrapper));
        return "system/assetsType/edit";
    }

    @RequestMapping("getPage.do")
    @ResponseBody
    public ResultInfo getPage(AssetsType assetsType,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer limit){
        return ResultInfo.success(assetsTypeService.getPage(assetsType, page, limit));
    }

    @RequestMapping("loadTree.do")
    @ResponseBody
    public ResultInfo loadTree(AssetsType assetsType){
        return ResultInfo.success(assetsTypeService.loadTree(assetsType));
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResultInfo add(AssetsType assetsType){
        AssetsType type = assetsTypeService.add(assetsType);
        if (null == type){
            return ResultInfo.error("请检查名称和编码是否重复");
        }
        return ResultInfo.success(type);
    }

    @RequestMapping("edit.do")
    @ResponseBody
    public ResultInfo edit(AssetsType assetsType){
        AssetsType type = assetsTypeService.edit(assetsType);
        if (null == type){
            return ResultInfo.error("请检查名称和编码是否重复");
        }
        return ResultInfo.success(type);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResultInfo del(Integer id){
        return ResultInfo.success(assetsTypeService.delete(id));
    }

    @RequestMapping(value = "/toExportTemplate.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo toExportTemplate(HttpServletResponse response){
        return ResultInfo.success(assetsTypeService.toExportTemplate(response));
    }

    /**
     * 资产登记导入
     */
    @RequestMapping(value = "/toImport.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toAssetImport(Integer fileId,String is_save){
        return assetsTypeService.toImport(fileId,is_save);
    }

    @RequestMapping("goErrorPage.do")
    public String goErrorPage(){
        return "system/assetsType/errorTable";
    }
}

