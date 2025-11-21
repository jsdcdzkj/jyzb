//package com.jsdc.rfid.controller.pos;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.jsdc.rfid.model.*;
//import com.jsdc.rfid.service.*;
//import net.hasor.utils.StringUtils;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Controller
//public class PosAppController {
//
//    @Autowired
//    private InventoryJobService inventoryJobService;
//
//    @Autowired
//    private InventoryDetailService inventoryDetailService;
//
//    @Autowired
//    private AssetsManageService assetsManageService;
//
//    @Autowired
//    private SysDepartmentService sysDepartmentService;
//
//    @Autowired
//    private AssetsTypeService assetsTypeService;
//
//    public String toList(Integer page, Integer limit) {
//        JSONObject jsonObject = new JSONObject();
//        PageHelper.startPage(page, limit);
//        List<InventoryJob> inventoryJobs = inventoryJobService.selectList(Wrappers.<InventoryJob>lambdaQuery().
//                eq(InventoryJob::getIs_del, "0").
//                eq(InventoryJob::getPos_flag, "1").
//                eq(InventoryJob::getProgress_node, "2")
//                .orderByDesc(InventoryJob::getIssue_time));
//        jsonObject.put("page", new PageInfo<>(inventoryJobs));
//        return jsonObject.toJSONString();
//    }
//
//    public String toInventoryDetail(String inventoryStatus, Integer inventory_job_id) {
//        List<String> inventory_status = Arrays.asList(inventoryStatus.split("-"));
//        List<InventoryDetail> inventoryDetails = inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery().
//                eq(InventoryDetail::getIs_del, "0")
//                .eq(InventoryDetail::getInventory_job_id, inventory_job_id).
//                in(InventoryDetail::getInventory_status, inventory_status));
//        if (CollectionUtils.isEmpty(inventoryDetails)) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("data", new ArrayList<>());
//            return jsonObject.toJSONString();
//        }
//        List<AssetsManage> a = assetsManageService.selectList(Wrappers.<AssetsManage>query()
//                .select("asset_code", "asset_name", "id", "remarks", "position_id", "dept_id")
//                .in("id", inventoryDetails.stream().map(InventoryDetail::getAsset_id).collect(Collectors.toList())));
//        Map<Integer, AssetsManage> assetsManageMap = a.stream().collect(Collectors.toMap(AssetsManage::getId, x -> x));
//        inventoryDetails.forEach(x -> {
//            AssetsManage assetsManage = assetsManageMap.get(x.getAsset_id());
//            assetsManage.setDept_name(sysDepartmentService.selectById(assetsManage.getDept_id()) == null?"": sysDepartmentService.selectById(assetsManage.getDept_id()).getDept_name());
//            x.setAssetsManage(assetsManage);
//        });
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("data", inventoryDetails);
//        return jsonObject.toJSONString();
//    }
//
//    public String getDeptList(Integer id) {
//        List<SysDepartment> list = sysDepartmentService.getList(new SysDepartment());
//        for (SysDepartment sysDepartment : list) {
//            sysDepartment.setValue(sysDepartment.getDept_name());
//        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("data", list);
//        return jsonObject.toJSONString();
//    }
//
//    public String getAssetClass(Integer id) {
//        List<AssetsType> list = assetsTypeService.getList(new AssetsType());
//        for (AssetsType assetsType : list) {
//            assetsType.setValue(assetsType.getAssets_type_name());
//        }
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("data", list);
//        return jsonObject.toJSONString();
//    }
//
//    public String assetsListAction(Integer dept_id, Integer asset_type_id, String asset_name, String asset_code) {
//        asset_name = asset_name == null ? "" : asset_name;
//        asset_code = asset_code == null ? "" : asset_code;
//        JSONObject jsonObject = new JSONObject();
//        AssetsManage assetsManage = AssetsManage.builder().dept_id(dept_id).asset_type_id(asset_type_id).asset_name(asset_name).asset_code(asset_code).build();
//        PageInfo<AssetsManage> pageInfos = assetsManageService.toLists(1, 1000000, assetsManage);
//        List<AssetsManage> list = pageInfos.getList();
////        for (AssetsManage assetsManage1 : list) {
////            if (assetsManage.getExcludeIdList().contains(assetsManage1.getAsset_code())){
////                assetsManage1.setPos_show_type(2);
////            }else{
////                assetsManage1.setPos_show_type(1);
////            }
////        }
//        jsonObject.put("data", list);
//        return jsonObject.toJSONString();
//    }
//
//    public String getAssetsManageByCode(String asset_code) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("data", assetsManageService.getAssetsManageByCode(asset_code));
//        return jsonObject.toJSONString();
//    }
//
//    public String updateAssets(String rfidArr) {
//        JSONObject jsonObject = new JSONObject();
//        // 设备扫描出来的rfid集合
//        List<String> rfids = Arrays.asList(rfidArr.split("-"));
////        AssetsManage assetsManage = assetsManageService.getAssetsManageByCode(code);
////        assetsManage.setPos_show_type(2);
//        for (String rfid : rfids) {
//            if (StringUtils.isBlank(rfid)) {
//                continue;
//            }
//            assetsManageService.update(null, Wrappers.<AssetsManage>lambdaUpdate()
//                    .eq(AssetsManage::getRfid, rfid)
//                    .set(AssetsManage::getPos_show_type, 2));
//        }
//
////        jsonObject.put("data", assetsManage);
//        return jsonObject.toJSONString();
//    }
//
//    public String posRFID(String rfidArr, Integer inventory_job_id) {
//        JSONObject jsonObject = new JSONObject();
//        // 设备扫描出来的rfid集合
//        List<String> rfids = Arrays.asList(rfidArr.split("-"));
//        // 当前任务下所有资产
//        List<InventoryDetail> details = inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery()
//                .eq(InventoryDetail::getIs_del, "0")
//                .eq(InventoryDetail::getInventory_job_id, inventory_job_id)
//                .eq(InventoryDetail::getInventory_status, "1"));
//        // 当前任务下rfid集合
//        List<InventoryDetail> detailList = details.stream().filter(x ->
//                rfids.contains(x.getRfid())).collect(Collectors.toList());
//        detailList.forEach(x -> {
//            x.setInventory_status("3");
//            x.setInventory_type("2");
//            x.updateById();
//        });
//        jsonObject.put("data", detailList);
//        return jsonObject.toJSONString();
//    }
//}
