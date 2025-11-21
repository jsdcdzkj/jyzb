package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.Consumable;
import com.jsdc.rfid.service.ConsumableService;
import com.jsdc.rfid.service.SysUserService;
import com.jsdc.rfid.vo.ConsumableVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/consumable")
public class ConsumableController {

    @Autowired
    private ConsumableService consumableService;
    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("toIndex.do")
    public String toIndex(){
        return "system/consumable/index";
    }

    /**
     * 跳转材料类型新增页面
     *
     * @return
     */
    @RequestMapping("toAddConsumable")
    public String toAddBorrow(Model model,String id) {
        QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("is_del","0");
        if(StringUtils.isNotEmpty(id)){
            queryWrapper.eq("id",id);
        }else{
            queryWrapper.eq("parent_id","0");
        }
        model.addAttribute("consumableList", consumableService.selectList(queryWrapper));
        return "system/consumable/add";
    }


    /**
    *跳转材料类型修改页面
    * Author wzn
    * Date 2023/5/15 15:11
    */
    @RequestMapping("toEdit")
    public String toEdit(String id ,Model model) {
        Consumable consumable = consumableService.selectById(id) ;
        model.addAttribute("consumable",consumable) ;

        QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("is_del","0");
        queryWrapper.eq("parent_id","0");
        model.addAttribute("consumableList", consumableService.selectList(queryWrapper));
        return "system/consumable/edit";
    }


    /**
    *跳转导入页面
    * Author wzn
    * Date 2023/5/15 17:35
    */
    @RequestMapping("import")
    public String toimport() {
        return "system/consumable/import";
    }

    @RequestMapping("/consumableList")
    @ResponseBody
    public ResultInfo consumableList(Consumable consumable){
        PageInfo<Consumable> consumableList = consumableService.consumableList(consumable) ;

        return ResultInfo.success(consumableList);
    }

    @RequestMapping("/consumablePageList")
    @ResponseBody
    public ResultInfo consumablePageList(ConsumableVo consumableVo){
        PageInfo<ConsumableVo> consumableList = consumableService.consumablePageList(consumableVo) ;
        return ResultInfo.success(consumableList);
    }


    @RequestMapping(value = "consumableCountList",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo consumableCountList(){
        return ResultInfo.success(consumableService.consumableCountList());
    }


    /**
    *耗材类型新增
    * Author wzn
    * Date 2023/5/15 11:11
    */
    @RequestMapping(value = "addConsumable", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addConsumable(Consumable consumable) {
        return consumableService.addConsumable(consumable);
    }

    /**
    *耗材类型修改
    * Author wzn
    * Date 2023/5/15 11:12
    */
    @PostMapping("/updateConsumable")
    @ResponseBody
    public ResultInfo updateConsumable( Consumable consumable) {
        try {
            consumableService.updateConsumable(consumable);
        } catch (Exception e) {
            return ResultInfo.error(e.getMessage());
        }
        return ResultInfo.success();
    }

    /**
    *耗材类型删除
    * Author wzn
    * Date 2023/5/15 11:14
    */
    @PostMapping("/delConsumable")
    @ResponseBody
    public ResultInfo delBuild( Integer  id) {

        Consumable consumable = new Consumable() ;
        consumable.setId(id);
        consumable.setIs_del("1");
        consumableService.updateById(consumable);

        // 把关联的下级的父id全部释放为0
        consumableService.update(null, Wrappers.<Consumable>lambdaUpdate().set(Consumable::getParent_id, 0).eq(Consumable::getParent_id, id));
        return ResultInfo.success();
    }


    @RequestMapping(value = "info", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo info(String id) {
        return ResultInfo.success(consumableService.selectById(id)) ;
    }



    /**
    *耗材类型导入模板下载
    * Author wzn
    * Date 2023/5/15 17:34
    */
    @RequestMapping(value = "/toExportTemplate.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo toExportTemplate(HttpServletResponse response){
        return ResultInfo.success(consumableService.toExportTemplate(response));
    }


    @RequestMapping(value = "/toImport.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toHaocaiImport(Integer fileId){
        return consumableService.toAssetImport(fileId);
    }


    @RequestMapping(value = "allConsumable",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo allConsumable(String parent_id){
        QueryWrapper<Consumable> queryWrapper = new QueryWrapper<>() ;
        queryWrapper.eq("is_del","0") ;
        if(StringUtils.isNotBlank(parent_id)){
            queryWrapper.eq("parent_id",parent_id) ;
        }else {
            queryWrapper.eq("parent_id","0") ;
        }
        return ResultInfo.success(consumableService.selectList(queryWrapper));
    }

    @RequestMapping(value = "treeConsumable",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo treeConsumable(String parent_id) {
        // 得到类型
        List<Consumable> consumables = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getParent_id, 0)
                .eq(Consumable::getIs_del, G.ISDEL_NO)
        );
        List<Map<String, Object>> result = new ArrayList<>();
        for (Consumable consumable : consumables) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", consumable.getConsumable_name());
            map.put("icon", "&#xe62d;");
            map.put("id", consumable.getId());
            map.put("spread", false);
            List<Consumable> consumableList = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                    .eq(Consumable::getParent_id, consumable.getId())
                    .eq(Consumable::getIs_del, G.ISDEL_NO)
            );
            List<Map<String, Object>> children = new ArrayList<>();
            for (Consumable a : consumableList){
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", a.getConsumable_name());
                map1.put("icon", "&#xe62d;");
                map1.put("id", a.getId());
                map1.put("spread", false);
                children.add(map1);
            }
            if (!CollectionUtils.isEmpty(children)) {
                map.put("children", children);
            }
            result.add(map);
//            consumable.setChildren(consumableList);
        }
        // 设置name属性
//        "name": "财务部",
//                "checked": false,
//                "id": 19,
//                "open": false
        return ResultInfo.success(result);
    }
}
