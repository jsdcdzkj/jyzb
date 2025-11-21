package com.jsdc.rfid.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.jsdc.core.base.BaseController;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.BrandManageMapper;
import com.jsdc.rfid.mapper.ConsumableMapper;
import com.jsdc.rfid.model.*;
import com.jsdc.rfid.service.*;
import com.jsdc.rfid.utils.CommonDataTools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vo.ResultInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 易耗品库存管理
 * @Author zhangdequan
 * @create 2022-06-07 11:53:30
 */
@Controller
@RequestMapping("/consInventoryManagement")
public class ConsInventoryManagementController extends BaseController {

    @Autowired
    private ConsInventoryManagementService consInventoryManagementService;
    @Autowired
    private ConsCategoryService consCategoryService;
    @Autowired
    private ConsAssettypeService consAssettypeService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private ConsumableService consumableService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private BrandManageService brandManageService;
    @Autowired
    private CommonDataTools commonDataTools;
    @Autowired
    private ConsumableMapper consumableMapper;
    @Autowired
    private BrandManageMapper brandManageMapper;


    /**
     *  易耗品库存管理跳转
     * @return
     */
    @GetMapping(value = "/{parentName}/{name}")
    public String warehousing(@PathVariable String parentName, @PathVariable String name, Model model){
        if(StringUtils.equals(name,"put") || StringUtils.equals(name,"put-hand")){
            List<Warehouse> list = warehouseService.getList(null);
            model.addAttribute("warehouseList",list);
        }
        // 得到类型
        List<Consumable> consumables = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                .eq(Consumable::getParent_id, 0)
                .eq(Consumable::getIs_del, G.ISDEL_NO)
        );
        for (Consumable consumable : consumables) {
            List<Consumable> consumableList = consumableService.selectList(Wrappers.<Consumable>lambdaQuery()
                    .eq(Consumable::getParent_id, consumable.getId())
                    .eq(Consumable::getIs_del, G.ISDEL_NO)
            );
            consumable.setChildren(consumableList);
        }
        model.addAttribute("consumables", consumables);

        // 得到仓库
        List<Warehouse> warehouses = warehouseService.selectList(Wrappers.<Warehouse>lambdaQuery()
                .eq(Warehouse::getIs_del, G.ISDEL_NO)
        );
        model.addAttribute("warehouses", warehouses);
        // 得到单位
        List<SysDict> sysDicts = sysDictService.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getIs_del, G.ISDEL_NO).eq(SysDict::getType, "unit"));
        model.addAttribute("sysDicts", sysDicts);
        // 得到品牌
        List<BrandManage> brandManages = brandManageService.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO));
        model.addAttribute("brandManages", brandManages);


        model.addAttribute("user", sysUserService.getUser());



        Set<String> result = null;
        // 型号
        Set<String> models = null;
        List<ConsInventoryManagement> a = consInventoryManagementService.selectList(Wrappers.<ConsInventoryManagement>lambdaQuery()
                .eq(ConsInventoryManagement::getIs_del, G.ISDEL_NO)
        );
        result = new HashSet<>();
        models = new HashSet<>();
        if (!CollectionUtils.isEmpty(a)){
            for (ConsInventoryManagement consumable : a){
                if (net.hasor.utils.StringUtils.isNotBlank(consumable.getName())){
                    result.add(consumable.getName());
                }
                if (net.hasor.utils.StringUtils.isNotBlank(consumable.getModel())){
                    models.add(consumable.getModel());
                }
            }
        }
        model.addAttribute("labels", result);
        model.addAttribute("models", models);
        return "/haocai/" + parentName + "/" + name;
    }

    /**
     *  易耗品库存管理跳转
     * @return
     */
    @GetMapping(value = "/addNumPage")
    public String addNumPage(Integer id, Model model){
        ConsInventoryManagement consInventoryManagement = consInventoryManagementService.selectById(id);
        //得到名称
        Map<Integer, ConsAssettype> assetNameMap = commonDataTools.getConsAssettypeMap();
//        //得到品类
//        Map<Integer, ConsCategory> categoryMap = commonDataTools.getConsCategoryMap();
//        //得到仓库
//        Map<Integer, Warehouse> warehouseMap = commonDataTools.getWarehouseMap();
//        //得到单位
//        Map<Integer, SysDict> unitMap = commonDataTools.getSysDictMap("unit");
//        //得到规格型号
//        Map<Integer, ConsSpecification> specificationMap = commonDataTools.getConsSpecificationMap();
        // 耗材类型名称
        consInventoryManagement.setAsset_name(commonDataTools.getValue(assetNameMap, consInventoryManagement.getAsset_name_id(), "name"));
        if(null != consInventoryManagement.getConsumable_id()){
            consInventoryManagement.setConsumable_name(consumableMapper.selectById(consInventoryManagement.getConsumable_id()).getConsumable_name());
        }
//        consInventoryManagement.setAssetTypeName(commonDataTools.getValue(categoryMap, consInventoryManagement.getAsset_type_id(), "name"));
//        consInventoryManagement.setWarehouse_name(commonDataTools.getValue(warehouseMap, consInventoryManagement.getWarehouse_id(), "warehouse_name"));
//        BrandManage brandManage = brandManageMapper.selectById(consInventoryManagement.getBrand_id());
//        consInventoryManagement.setBrand_name(null != brandManage ? brandManage.getBrand_name() : "");
//        consInventoryManagement.setUnit_name(commonDataTools.getValue(unitMap, consInventoryManagement.getUnit_of_measurement(), "label"));
//        consInventoryManagement.setSpecifications_name(commonDataTools.getValue(specificationMap, consInventoryManagement.getSpecifications(), "typename"));

        model.addAttribute("bean", consInventoryManagement);

        List<SysDict> units = sysDictService.selectList(Wrappers.<SysDict>lambdaQuery().eq(SysDict::getType, "unit").eq(SysDict::getIs_del, G.ISDEL_NO));
        model.addAttribute("units", units);

        List<BrandManage> brands = brandManageMapper.selectList(Wrappers.<BrandManage>lambdaQuery().eq(BrandManage::getIs_del, G.ISDEL_NO));
        model.addAttribute("brands", brands);

        List<Warehouse> warehouses = warehouseService.selectList(Wrappers.<Warehouse>lambdaQuery().eq(Warehouse::getIs_del, G.ISDEL_NO));
        model.addAttribute("warehouses", warehouses);
        return "/haocai/list/addNum";
    }

    /**
     * 得到易耗品品类列表
     */
    @PostMapping(value = "/getConsCategoryList.do")
    @ResponseBody
    public ResultInfo getConsInventoryManagementList(){
        return ResultInfo.success(consCategoryService.selectList(Wrappers.<ConsCategory>lambdaQuery().eq(ConsCategory::getIs_del, String.valueOf(0))));
    }

    /**
     * 得到易耗品名称列表
     */
    @PostMapping(value = "/getConsNameList.do")
    @ResponseBody
    public ResultInfo getConsNameList(){
        return ResultInfo.success(consAssettypeService.selectList(Wrappers.<ConsAssettype>lambdaQuery().eq(ConsAssettype::getIs_del, String.valueOf(0))));
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
                             ConsInventoryManagement beanParam) {
        PageInfo<ConsInventoryManagement> page = consInventoryManagementService.toList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }
    /**
     *  ID查询
     */
    @RequestMapping(value = "getById.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getById(Integer id){
        return consInventoryManagementService.getById(id);
    }

    /**
     *  添加
     */
    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addInventoryManagement(ConsInventoryManagement inventoryManagement){
        // 入库时间
        inventoryManagement.setWarehousing_time(new Date());
        return consInventoryManagementService.addInventoryManagement(inventoryManagement);
    }

    /**
     *  添加库存
     */
    @RequestMapping(value = "updateNum.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo updateNum(ConsInventoryManagement inventoryManagement){
        // 判断id 和 inventory_num是否为空
        if(null == inventoryManagement.getId() ){
            return ResultInfo.error("参数错误");
        }
        try {
            consInventoryManagementService.updateNum(inventoryManagement);

        } catch (Exception e) {
            return ResultInfo.error("操作失败: " + e.getMessage());
        }
        return ResultInfo.success("操作成功");
    }

    /**
     * 根据采购 存入库存
     */
    @RequestMapping(value = "toAddWarehousing.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addWarehousing(Integer purchaseApply_id){
        return consInventoryManagementService.addInventoryManagementById(purchaseApply_id);
    }

    /**
     *  编辑
     */
    @RequestMapping(value = "toEdit.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editInventoryManagement(ConsInventoryManagement inventoryManagement){

        return consInventoryManagementService.editInventoryManagement(inventoryManagement);
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
                               ConsWarehousingManagement beanParam) {
        PageInfo<ConsWarehousingManagement> page = consInventoryManagementService.toWarehousingList(pageIndex, pageSize, beanParam);

        return ResultInfo.success(page);
    }

    /**
     *  入库管理 ID查询
     */
    @RequestMapping(value = "/warehousing/getByIdIn.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo getByIdIn(Integer id){
        return consInventoryManagementService.getWarehousingById(id);
    }

    /**
     *  入库管理 添加
     */
    @RequestMapping(value = "/warehousing/toAddIn.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo addInventoryManagementIn(ConsWarehousingManagement warehousingManagement){

        return consInventoryManagementService.addWarehousing(warehousingManagement);
    }

    /**
     *  编辑
     */
    @RequestMapping(value = "/warehousing/toEditIn.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo editInventoryManagementIn(ConsWarehousingManagement warehousingManagement){

        return consInventoryManagementService.editWarehousing(warehousingManagement);
    }

    /**
     * 入库管理 导入模板下载
     */
    @RequestMapping(value = "/warehousing/download.do",method = RequestMethod.GET)
    public void download(HttpServletResponse response){
        consInventoryManagementService.download(response);
    }

    /**
     * 入库管理 导入
     */
    @RequestMapping(value = "/warehousing/toImport.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo importInventoryManagementIn(MultipartFile file){

        return consInventoryManagementService.importWarehousing(file);
    }

    /**
     * 耗材导出模板
     */
    @RequestMapping(value = "/toExportTemplate.do",method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo toExportTemplate(HttpServletResponse response){
        return ResultInfo.success(consInventoryManagementService.toExportTemplate(response));
    }

    /**
     * 资产登记导入
     */
    @RequestMapping(value = "/toHaocaiImport.do",method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo toHaocaiImport(Integer fileId){
        return consInventoryManagementService.toAssetImport(fileId);
    }
}
