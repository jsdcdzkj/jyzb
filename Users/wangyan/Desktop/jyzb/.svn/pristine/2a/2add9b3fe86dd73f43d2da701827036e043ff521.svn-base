//package com.jsdc.rfid.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.jsdc.core.base.BaseController;
//import com.jsdc.rfid.model.*;
//import com.jsdc.rfid.service.*;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.*;
//import vo.InventoryJobVo;
//import vo.ResultInfo;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * ClassName: PosController
// * Description:
// * date: 2022/5/11 8:59
// *
// * @author bn
// */
//@Controller
//@RequestMapping("pos")
//@AllArgsConstructor
//public class PosController extends BaseController {
//
//    private InventoryJobService inventoryJobService;
//
//    private InventoryDetailService inventoryDetailService;
//
//    private AssetsManageService assetsManageService;
//
//    private SysDepartmentService sysDepartmentService;
//
//    private AssetsTypeService assetsTypeService;
//
//
//    /**
//     * 主页
//     *
//     * @return
//     */
//    @RequestMapping(value = "main.do", method = RequestMethod.GET)
//    public String main() {
//        return "/pos/main";
//    }
//
//    /**
//     * 扫一扫
//     *
//     * @return
//     */
//    @RequestMapping(value = "qrcode.do", method = RequestMethod.GET)
//    public String qrcode(String code, Model model) {
//        System.out.println("扫一扫");
//        JSONObject jsonObject = JSON.parseObject(code);
//        model.addAttribute("asset", assetsManageService.getAssetsManageByCode(jsonObject.getString("asset_code")));
//        return "/pos/detail";
//    }
//
//    /**
//     * 去盘点
//     *
//     * @return
//     */
//    @RequestMapping(value = "pandian.do", method = RequestMethod.GET)
//    public String pandian(Integer id, Model model) {
//        model.addAttribute("id", id);
//        return "/pos/pandian";
//    }
//
//    /**
//     * 资产扫描
//     *
//     * @return
//     */
//    @RequestMapping(value = "fill_info.do", method = RequestMethod.GET)
//    public String fill_info() {
//        return "/pos/fill_info";
//    }
//
//    /**
//     * 资产扫描结果
//     *
//     * @return
//     */
//    @RequestMapping(value = "scanresult.do", method = RequestMethod.GET)
//    public String scanresult(@RequestParam(value = "code", required = false) String code,
//                             @RequestParam(value = "deptId", required = false) Integer deptId,
//                             @RequestParam(value = "typeId", required = false) Integer typeId,
//                             @RequestParam(value = "name", required = false) String name,
//                             Model model) {
//        PageInfo<AssetsManage> pageInfos = assetsManageService.toLists(1, 1000000, AssetsManage.builder().asset_code(code)
//                .dept_id(deptId).asset_type_id(typeId).asset_name(name).build());
//        List<AssetsManage> list = pageInfos.getList();
//        model.addAttribute("list", list);
//        List<AssetsManage> unList = new ArrayList<>();
//        model.addAttribute("unList", list);
//
//        model.addAttribute("code", code);
//        model.addAttribute("deptId", deptId);
//        model.addAttribute("typeId", typeId);
//        model.addAttribute("name", name);
//        return "/pos/scanresult";
//    }
//
//    /**
//     * 资产列表,已扫描,未扫描
//     */
//    @RequestMapping(value = "assetsListAction.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo assetsListAction(AssetsManage assetsManage) {
//        PageInfo<AssetsManage> pageInfos = assetsManageService.toLists(1, 1000000, assetsManage);
//        List<AssetsManage> list = pageInfos.getList();
//        if (CollectionUtils.isEmpty(assetsManage.getExcludeIdList())){
//            list.forEach(x -> x.setPos_show_type(1));
//            return ResultInfo.success(list);
//        }
//        for (AssetsManage assetsManage1 : list) {
//            if (assetsManage.getExcludeIdList().contains(assetsManage1.getId())){
//                assetsManage1.setPos_show_type(2);
//            }else{
//                assetsManage1.setPos_show_type(1);
//            }
//        }
//        return ResultInfo.success(list);
//    }
//
//
//
//    /**
//     * 盘点任务列表
//     *
//     * @param inventoryJob
//     * @return
//     */
//    @RequestMapping(value = "toList.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toList(@RequestParam(required = false, defaultValue = "1") Integer page,
//                             @RequestParam(required = false, defaultValue = "10") Integer limit,
//                             InventoryJob inventoryJob) {
//        PageHelper.startPage(page, limit);
//        List<InventoryJob> inventoryJobs = inventoryJobService.selectList(Wrappers.<InventoryJob>lambdaQuery().
//                eq(InventoryJob::getIs_del, "0").
//                eq(InventoryJob::getPos_flag, "1").
//                eq(InventoryJob::getProgress_node, "2")
//                .orderByDesc(InventoryJob::getIssue_time));
//        return ResultInfo.success(inventoryJobs);
//    }
//
//    /**
//     * 盘点详情列表
//     *
//     * @param inventoryJobVo
//     * @return
//     */
//    @RequestMapping(value = "toInventoryDetail.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toInventoryDetail(InventoryJobVo inventoryJobVo) {
//        List<String> inventory_status = Arrays.asList(inventoryJobVo.getInventory_status().split(","));
//        List<InventoryDetail> inventoryDetails = inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery().
//                eq(InventoryDetail::getIs_del, "0").eq(InventoryDetail::getInventory_job_id, inventoryJobVo.getInventory_job_id()).
//                in(InventoryDetail::getInventory_status, inventory_status));
//
//        inventoryDetails.forEach(x -> {
//
//            x.setAssetsManage(assetsManageService.getById(x.getAsset_id()));
//
//        });
//
//
//        return ResultInfo.success(inventoryDetails);
//
//    }
//
//
//    /**
//     *  pos机RFID盘点
//     * @return
//     */
//    @RequestMapping(value = "posRFID.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo posRFID(InventoryJob inventoryJob){
//
//        return ResultInfo.success(inventoryJobService.posRFID(inventoryJob));
//    }
//
//    /**
//     *  查看资产详情
//     * @return
//     */
//    @RequestMapping(value = "viewAssetManage.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo viewAssetManage(String asset_code){
//
//        return ResultInfo.success(assetsManageService.getAssetsManageByCode(asset_code));
//    }
//
//    /**
//     * 得到部门集合
//     */
//    @RequestMapping(value = "getDeptList.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo getDeptList(){
//        List<SysDepartment> list = sysDepartmentService.getList(new SysDepartment());
//        for (SysDepartment sysDepartment : list) {
//            sysDepartment.setValue(sysDepartment.getDept_name());
//        }
//        return ResultInfo.success(list);
//    }
//
//    /**
//     * 得到资产品类
//     */
//    @RequestMapping(value = "getAssetClass.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo getAssetClass(){
//        List<AssetsType> list = assetsTypeService.getList(new AssetsType());
//        for (AssetsType assetsType : list) {
//            assetsType.setValue(assetsType.getAssets_type_name());
//        }
//        return ResultInfo.success(list);
//    }
//
//}
