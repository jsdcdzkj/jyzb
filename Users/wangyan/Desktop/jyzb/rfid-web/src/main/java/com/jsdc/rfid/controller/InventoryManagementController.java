package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.model.BrandManage;
import com.jsdc.rfid.model.InventoryManagement;
import com.jsdc.rfid.model.Warehouse;
import com.jsdc.rfid.model.WarehousingManagement;
import com.jsdc.rfid.service.BrandManageService;
import com.jsdc.rfid.service.InventoryManagementService;
import com.jsdc.rfid.service.WarehouseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库存管理控制器 + 入库管理控制器
 * @Author zhangdequan
 * @create 2022-04-24 17:18:15
 */
@Controller
@RequestMapping("/inventoryManagement")
public class InventoryManagementController extends BaseController {

    @Autowired
    private InventoryManagementService inventoryManagementService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private BrandManageService brandManageService;
    /**
     *  库存列表跳转
     * @return
     */
    @GetMapping(value = "/index")
    public String InventoryJob(Model model){
        // 得到仓库
        List<Warehouse> list = warehouseService.getList(null);
        model.addAttribute("warehouseList",list);
        // 得到品牌
        List<BrandManage> brandManages = brandManageService.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO));
        model.addAttribute("brandManages", brandManages);
        return "/stock/list/index";
    }

    /**
     *  入库管理跳转
     * @return
     */
    @GetMapping(value = "/warehousing/{name}")
    public String warehousing(@PathVariable String name, Model model){
        if(StringUtils.equals(name,"put") || StringUtils.equals(name,"put-hand")){
            List<Warehouse> list = warehouseService.getList(null);
            model.addAttribute("warehouseList",list);
        }else if(StringUtils.equals(name,"check") || StringUtils.equals(name,"edit")){
            // 得到品牌
            List<BrandManage> brandManages = brandManageService.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO));
            model.addAttribute("brandManages", brandManages);
        }
        return "/stock/warehousing/" + name;
    }

    /**
     * 库存列表查询
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toList(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                             @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                             InventoryManagement beanParam) {
        PageInfo<InventoryManagement> page = inventoryManagementService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }
     /**
     *  ID查询
     */
    @RequestMapping(value = "getById.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id){
        return inventoryManagementService.getById(id);
    }

    /**
     *  添加
     */
    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addInventoryManagement(InventoryManagement inventoryManagement){

        return inventoryManagementService.addInventoryManagement(inventoryManagement);
    }

    /**
     * 根据采购 存入库存
     */
    @RequestMapping(value = "toAddWarehousing.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addWarehousing(Integer purchaseApply_id){
        return inventoryManagementService.addInventoryManagementById(purchaseApply_id);
    }

    /**
     *  编辑
     */
    @RequestMapping(value = "toEdit.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editInventoryManagement(InventoryManagement inventoryManagement){

        return inventoryManagementService.editInventoryManagement(inventoryManagement);
    }

    /** ---------------------------------- 入库管理 ---------------------------------- **/

    /**
     * 入库列表查询
     * @param pageIndex
     * @param pageSize
     * @param beanParam
     * @return
     */
    @RequestMapping(value = "/warehousing/toList.do", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toListIn(@RequestParam(defaultValue = "1", value = "page") Integer pageIndex,
                               @RequestParam(defaultValue = "10", value = "limit") Integer pageSize,
                               WarehousingManagement beanParam) {
        PageInfo<WarehousingManagement> page = inventoryManagementService.toWarehousingList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }

    /**
     *  入库管理 ID查询
     */
    @RequestMapping(value = "/warehousing/getByIdIn.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getByIdIn(Integer id){
        return inventoryManagementService.getWarehousingById(id);
    }

    /**
     *  入库管理 添加
     */
    @RequestMapping(value = "/warehousing/toAddIn.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addInventoryManagementIn(WarehousingManagement warehousingManagement){

        return inventoryManagementService.addWarehousing(warehousingManagement);
    }

    /**
     *  编辑
     */
    @RequestMapping(value = "/warehousing/toEditIn.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editInventoryManagementIn(WarehousingManagement warehousingManagement){

        return inventoryManagementService.editWarehousing(warehousingManagement);
    }

    /**
     * 入库管理 导入模板下载
     */
    @RequestMapping(value = "/warehousing/download.do",method = RequestMethod.GET)
    public void download(HttpServletResponse response){
        inventoryManagementService.download(response);
    }

    /**
     * 入库管理 导入
     */
    @RequestMapping(value = "/warehousing/toImport.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo importInventoryManagementIn(MultipartFile file){

        return inventoryManagementService.importWarehousing(file);
    }
}
