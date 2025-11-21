//package com.jsdc.rfid.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.github.pagehelper.PageInfo;
//import com.jsdc.core.base.BaseController;
//import com.jsdc.rfid.common.G;
//import com.jsdc.rfid.enums.DataType;
//import com.jsdc.rfid.mapper.InventoryDetailMapper;
//import com.jsdc.rfid.model.*;
//import com.jsdc.rfid.service.*;
//import com.jsdc.rfid.utils.CommonDataTools;
//import com.jsdc.rfid.utils.PostUtils;
//import com.jsdc.rfid.utils.sendUsb.DeviceManager;
//import com.jsdc.rfid.utils.sendUsb.IDataCallbackListener;
//import lombok.AllArgsConstructor;
//import net.hasor.utils.StringUtils;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.collections.MapUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import vo.InventoryJobVo;
//import vo.ResultInfo;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * ClassName: InventoryJobController
// * Description: 盘点任务
// * date: 2022/4/24 17:21
// *
// * @author bn
// */
//@Controller
//@RequestMapping("inventoryJob")
//@AllArgsConstructor
//public class InventoryJobController extends BaseController {
//
//
//    private InventoryJobService inventoryJobService;
//
//
//    private InventoryDetailService inventoryDetailService;
//
//
//    private SysUserService sysUserService;
//
//
//    private AssetsTypeService typeService;
//
//    private SysDepartmentService departmentService;
//
//    private CommonDataTools commonDataTools;
//
//    private ApplySingleService applySingleService;
//
//    private OperationRecordService operationRecordService;
//
//    private AssetsManageService assetsManageService;
//
//    private SysUserService userService;
//
//    private SysDepartmentService sysDepartmentService;
//
//    private InventoryDetailMapper inventoryDetailMapper;
//
//    @Autowired
//    private SimpMessagingTemplate template;
//
//
//    /**
//     *  首页跳转
//     * @return
//     */
//    @RequestMapping(value = "goInventoryJob.do")
//    public String goInventoryJob(){
//        return "/property/Inventory/index";
//    }
//
//
//    /**
//     *  新增跳转
//     * @return
//     */
//    @RequestMapping(value = "goPageAdd.do")
//    public String goPageAdd(){
//        return "/property/Inventory/add";
//    }
//
//    /**
//     *  编辑跳转
//     * @return
//     */
//    @RequestMapping(value = "goPageEdit.do")
//    public String goPageEdit(Integer id,String inventory_no, Model model){
//        model.addAttribute("id",id);
//        model.addAttribute("inventory_no",inventory_no);
//        InventoryJob job = inventoryJobService.selectById(id);
//        model.addAttribute("job", job);
//
//        List<Integer> deptIds = new ArrayList<>();
//        if (StringUtils.isNotBlank(job.getDept_ids())) {
//            String[] split = job.getDept_ids().split(",");
//            for (String s : split) {
//                deptIds.add(Integer.valueOf(s));
//            }
//        }
//        model.addAttribute("deptIds", deptIds);
//
//        List<Integer> assetTypeIds = new ArrayList<>();
//        if (StringUtils.isNotBlank(job.getAsset_type_ids())) {
//            String[] split = job.getAsset_type_ids().split(",");
//            for (String s : split) {
//                assetTypeIds.add(Integer.valueOf(s));
//            }
//        }
//        model.addAttribute("assetTypeIds", assetTypeIds);
//        return "/property/Inventory/edit";
//    }
//
//    /**
//     *  盘点处理
//     * @return
//     */
//    @RequestMapping(value = "goPageDeal.do")
//    public String goPageDeal(Integer id, Model model){
//        model.addAttribute("id",id);
//        return "/property/Inventory/deal";
//    }
//
//    /**
//     *  视图跳转
//     * @return
//     */
//    @RequestMapping(value = "goPageView.do")
//    public String goPageView(Integer id,Model model){
//        model.addAttribute("id",id);
//        return "/property/Inventory/view";
//    }
//
//
//    /**
//     *  盘盈跳转
//     * @return
//     */
//    @RequestMapping(value = "goPageSurplus.do")
//    public String goPageSurplus(Integer id,Model model){
//        model.addAttribute("id",id);
//        model.addAttribute("user",sysUserService.getUser());
//        return "/property/Inventory/surplus";
//    }
//
//
//    /**
//     *  去盘点跳转
//     * @return
//     */
//    @RequestMapping(value = "goPageInvent.do")
//    public String goPageInvent(Integer id,Model model){
//        model.addAttribute("id",id);
//        model.addAttribute("user",sysUserService.getUser());
//        return "/property/Inventory/goInvent";
//    }
//
//    /**
//     *  日志详情
//     * @param inventory_no
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "toLogView.do")
//    public String toLogView(String inventory_no,Model model){
//        List<OperationRecord> operationRecords=operationRecordService.
//                selectList(Wrappers.<OperationRecord>lambdaQuery().
//                        eq(OperationRecord::getField_fk,inventory_no).
//                        eq(OperationRecord::getType,"3").
//                        eq(OperationRecord::getIs_del,"0").orderByDesc(OperationRecord::getCreate_time));
//        model.addAttribute("operationRecords",operationRecords);
//
//        return "/property/Inventory/view-log";
//    }
//
//    /**
//     * 维修
//     * @return
//     */
//    @RequestMapping(value = "goPageSingle.do")
//     public String goPageSingle(Integer id,Model model){
//        //资产品类
//        List<AssetsType> list = typeService.selectList(new QueryWrapper<AssetsType>().eq("is_del", "0").eq("is_enable", "1"));
//        model.addAttribute("list", list);
//        SysUser user = sysUserService.getUser();
//        model.addAttribute("user", user);
//        //部门数据
//        List<SysDepartment> dwList = departmentService.selectList(new QueryWrapper<SysDepartment>().eq("is_del", "0"));
//        model.addAttribute("dwList", dwList);
//        //维修人员
//        List<SysUser> userList = sysUserService.selectList(new QueryWrapper<SysUser>().eq("is_del", "0").eq("post", PostUtils.wxry));
//        model.addAttribute("userList", userList);
//        String code = commonDataTools.getNo(DataType.APPLYSINGLE_BX.getType(), null);
//        model.addAttribute("code", code);
//        model.addAttribute("id",id);
//        return "/property/Inventory/singleadd";
//     }
//
//
//    /**
//     *  保存保修单
//     * @param applySingle
//     * @param files
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "saveSingle.json", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo saveSingle(ApplySingle applySingle, String files,Integer inventory_detail_id) {
//        Integer count = applySingleService.saveSingle(applySingle, files,inventory_detail_id);
//        if (count == 0) {
//            return ResultInfo.success(count);
//        } else {
//            return ResultInfo.error("失败");
//        }
//    }
//
//
//    /**
//     *  盘点任务列表展示
//     * @param page
//     * @param limit
//     * @return
//     */
//    @RequestMapping(value = "toList.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toList(@RequestParam(defaultValue = "1") Integer page,
//                             @RequestParam(defaultValue = "10") Integer limit, InventoryJobVo inventoryJobVo){
//
//
//        PageInfo<InventoryJob> pageInfo=inventoryJobService.toList(page,limit,inventoryJobVo);
//        return ResultInfo.success(pageInfo);
//    }
//
//    /**
//     *  进度条
//     * @param inventoryJobVo
//     * @return
//     */
//    @RequestMapping(value = "toProgress.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toProgress(InventoryJobVo inventoryJobVo){
//
//        return ResultInfo.success(inventoryJobService.toProgress(inventoryJobVo));
//    }
//
//
//
//    /**
//     *  根据id获取数据
//     * @return
//     */
//    @RequestMapping(value = "getById.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo getById(InventoryJobVo inventoryJobVo){
//
//
//
//        return ResultInfo.success(inventoryJobService.getById(inventoryJobVo));
//    }
//
//    /**
//     *  根据id获取数据
//     * @return
//     */
//    @RequestMapping(value = "getOne.do",method = RequestMethod.POST)
//    @ResponseBody
//    public InventoryJob getOne(InventoryJobVo inventoryJobVo){
//        return inventoryJobService.selectById(inventoryJobVo.getInventory_job_id());
//    }
//
//
//    /**
//     *  获取盘点详情
//     * @return
//     */
//    @RequestMapping(value = "getInventoryDetail.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo getInventoryDetail(@RequestParam(defaultValue = "1") Integer page,
//                                         @RequestParam(defaultValue = "10") Integer limit,InventoryJobVo inventoryJobVo){
//
//
//        PageInfo<InventoryDetail> pageInfo=new PageInfo<>(inventoryJobService.getInventoryDetail(page,limit,inventoryJobVo));
//
//        return ResultInfo.success(pageInfo);
//    }
//
//    /**
//     *  获取盘点详情统计
//     * @return
//     */
//    @RequestMapping(value = "getInventoryDetailStatistics.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo getInventoryDetailStatistics(@RequestParam(defaultValue = "1") Integer page,
//                                         @RequestParam(defaultValue = "10") Integer limit,InventoryJobVo inventoryJobVo){
//        return ResultInfo.success(inventoryJobService.getInventoryDetailStatistics(page,limit,inventoryJobVo));
//    }
//
//    //
//    @RequestMapping(value = "getLocalByDept.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo getLocalByDept(Integer deptId){
//        return ResultInfo.success(inventoryJobService.getLocalByDept(deptId));
//    }
//
//    /**
//     *  开始RFID盘点
//     * @return
//     */
//    @RequestMapping(value = "startRFID.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo startRFID(InventoryJob inventoryJob){
//
//        inventoryJobService.startRFID(inventoryJob);
//
//        return ResultInfo.success();
//    }
//
//    /**
//     *  结束RFID盘点
//     * @return
//     */
//    @RequestMapping(value = "stopRFID.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo stopRFID(InventoryJob inventoryJob){
//
//        inventoryJobService.stopRFID(inventoryJob);
//
//        return ResultInfo.success();
//    }
//
//
//
//    /**
//     *  生成处置单
//     * @return
//     */
//    @RequestMapping(value = "toManagement.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toManagement(InventoryDetail inventoryDetail) {
//        inventoryJobService.toManagement(inventoryDetail);
//        return ResultInfo.success();
//    }
//
//
//
//
//    /**
//     *  下发手持机盘点任务
//     * @return
//     */
//    @RequestMapping(value = "goPosRFID.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo goPosRFID(InventoryJob inventoryJob){
//        inventoryJob.setUpdate_time(new Date());
//        inventoryJob.setUpdate_user(sysUserService.getUser().getId());
//        inventoryJob.setIssue_time(new Date());
//        inventoryJobService.updateById(inventoryJob);
//        return ResultInfo.success();
//    }
//
//
//    /**
//     *  删除
//     * @return
//     */
//    @RequestMapping(value = "toDel.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toDel(InventoryJob inventoryJob){
//        inventoryJobService.toDel(inventoryJob);
//        return ResultInfo.success();
//    }
//
//    /**
//     *  开始、结束盘点任务
//     * @return
//     */
//    @RequestMapping(value = "toStart.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toStart(InventoryJob inventoryJob){
//        if (inventoryJob.getProgress_node().equals("3")){
//            List<InventoryDetail> inventoryDetails=inventoryDetailService.
//                    selectList(Wrappers.<InventoryDetail>lambdaQuery().
//                            eq(InventoryDetail::getInventory_job_id,inventoryJob.getId()).
//                            eq(InventoryDetail::getIs_del,"0").
//                            eq(InventoryDetail::getInventory_status,"1"));
//            if (inventoryDetails.size()>0){
//                operationRecordService.addOperationRecord(OperationRecord.builder().
//                        record("尚有应盘资产，无法结束任务").type("3").
//                        field_fk(inventoryJob.getInventory_no()).build());
//                return ResultInfo.error("尚有应盘资产，无法结束任务");
//            }
//        }
//
//        inventoryJobService.toStart(inventoryJob);
//        return ResultInfo.success();
//    }
//
//    /**
//     *  增加
//     * @return
//     */
//    @RequestMapping(value = "toAdd.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toAdd(@RequestBody Map<String, Object> requestBody){
//        String is_inventory = (String) requestBody.get("is_inventory");
//        String plan_start_time = (String) requestBody.get("plan_start_time");
//        // string 转 DATE
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;
//        try {
//            date = sdf.parse(plan_start_time);
//        } catch (Exception e) {
//            return ResultInfo.error("时间格式错误");
//        }
//        String inventory_name = (String) requestBody.get("inventory_name");
//        String inventory_status = (String) requestBody.get("inventory_status");
//        String asset_type_ids = null;
//        try {
//            asset_type_ids = (String) requestBody.get("asset_type_ids");
//        } catch (Exception e) {
//            try {
//                asset_type_ids = (Integer) requestBody.get("asset_type_ids") + "";
//            } catch (Exception ignored) {
//            }
//        }
//        String dept_ids = null;
//        try {
//            dept_ids = (String) requestBody.get("dept_ids");
//        } catch (Exception e) {
//            try {
//                dept_ids = (Integer) requestBody.get("dept_ids") + "";
//            } catch (Exception ignored) {
//            }
//        }
//        try {
//            inventoryJobService.toAdd(InventoryJob.builder()
//                    .is_inventory(is_inventory)
//                    .inventory_name(inventory_name)
//                    .plan_start_time(date)
//                    .inventory_status(inventory_status)
//                    .asset_type_ids(asset_type_ids)
//                    .dept_ids(dept_ids)
//                    .build()
//            );
//        } catch (Exception e) {
//            log.error(e.getMessage(),e);
//            return ResultInfo.error("失败:" + e.getMessage());
//        }
//        return ResultInfo.success();
//    }
//
//    /**
//     *  修改
//     * @return
//     */
//    @RequestMapping(value = "toEdit.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toEdit(@RequestBody Map<String, Object> requestBody){
//        if (null == requestBody.get("id")){
//            ResultInfo.error("编辑失败" );
//        }
//        Integer id = (Integer) requestBody.get("id");
//        String is_inventory = (String) requestBody.get("is_inventory");
//        String plan_start_time = (String) requestBody.get("plan_start_time");
//        // string 转 DATE
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;
//        try {
//            date = sdf.parse(plan_start_time);
//        } catch (Exception e) {
//            return ResultInfo.error("时间格式错误");
//        }
//        String inventory_name = (String) requestBody.get("inventory_name");
//        String inventory_status = (String) requestBody.get("inventory_status");
//        String asset_type_ids = null;
//        try {
//            asset_type_ids = (String) requestBody.get("asset_type_ids");
//        } catch (Exception e) {
//            try {
//                asset_type_ids = (Integer) requestBody.get("asset_type_ids") + "";
//            } catch (Exception ignored) {
//            }
//        }
//        String dept_ids = null;
//        try {
//            dept_ids = (String) requestBody.get("dept_ids");
//        } catch (Exception e) {
//            try {
//                dept_ids = (Integer) requestBody.get("dept_ids") + "";
//            } catch (Exception ignored) {
//            }
//        }
//        try {
//            inventoryJobService.toEdit(InventoryJob.builder()
//                    .id(Integer.valueOf(id))
//                    .is_inventory(is_inventory)
//                    .inventory_name(inventory_name)
//                    .plan_start_time(date)
//                    .inventory_status(inventory_status)
//                    .asset_type_ids(asset_type_ids)
//                    .dept_ids(dept_ids)
//                    .build()
//            );
//        } catch (Exception e) {
//            log.error(e.getMessage(),e);
//            return ResultInfo.error("失败:" + e.getMessage());
//        }
//        return ResultInfo.success();
//    }
//
//    /**
//     * 添加盘盈
//     * @return
//     */
//    @RequestMapping(value = "toAddAsset.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toAddAsset(AssetsManage assetsManage){
//        assetsManage.setRegister_time(new Date());
//        assetsManage.setPosition_change_time(new Date());
//        return inventoryJobService.toAddAsset(assetsManage);
//    }
//
//
//    /**
//     * 盘盈处理
//     * @param
//     * @return
//     */
//    @RequestMapping(value = "toAsset.do",method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo toAsset(InventoryDetail inventoryDetail){
//
//        return inventoryJobService.toAsset(inventoryDetail);
//    }
//
//    /**
//     * 上传盘点的文件
//     */
//    @RequestMapping(value = "/{id}/uploadFile",method = RequestMethod.POST, consumes = "multipart/*")
//    @ResponseBody
//    public ResultInfo upload(@PathVariable Integer id, MultipartHttpServletRequest request) {
//        MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();// 为了获取文件，这个类是必须的
//        List<MultipartFile> list = map.get("file");// 获取到文件的列表
//        return inventoryJobService.fileUpload(id, list.get(0));
//    }
//
//    public static String resData;
//
//    /**
//     * 传输init,传输数据
//     */
//    @RequestMapping(value = "transmit.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo transmit(String id) {
//        try {
//            inventoryJobService.updateById(InventoryJob.builder().id(Integer.valueOf(id)).is_lx_hc(1).build());
//            DeviceManager.getInstance().init();
//            boolean isOk = DeviceManager.getInstance().initLoaclSocket(7777, 8888);
//            DeviceManager.getInstance().sendData("OK");
//            resData = "";
//            if (isOk) {
//                DeviceManager.getInstance().setDataCallbackListener(new IDataCallbackListener() {
//
//                    @Override
//                    public void onDataReceived(String data) {
//                        System.out.println("接收到的数据：" + data);
//                        if (data.lastIndexOf("##") != -1) {
//                            data = data.substring(0, data.length() - 2);
//                            // 如果 null 是前几个字符,则去除
//                            if (resData.lastIndexOf("null") != -1) {
//                                data = data.substring(4);
//                            }
//                            resData += data;
//                            int num = 0;
//                            if (StringUtils.isNotBlank(resData)) {
//                                if (JSON.isValid(resData)) {
//                                    Map<String, Object> result = JSON.parseObject(resData, Map.class);
//                                    String jobid = MapUtils.getString(result, "jobid");
//
//                                    if (StringUtils.equals(id, jobid)) {
//                                        List<String> rfids = (List<String>) MapUtils.getObject(result, "epcs", List.class);
//                                        // 保存盘点数据
//                                        num = inventoryDetailService.update(null, Wrappers.<InventoryDetail>lambdaUpdate().
//                                                set(InventoryDetail::getInventory_status,"3").
//                                                set(InventoryDetail::getInventory_type,"5").
//                                                eq(InventoryDetail::getInventory_job_id,jobid).
//                                                in(InventoryDetail::getRfid,rfids).
//                                                eq(InventoryDetail::getIs_del,G.ISDEL_NO)
//                                        );
//                                        inventoryJobService.updateById(InventoryJob.builder().id(Integer.valueOf(id)).is_lx_hc(0).build());
//                                    }
//                                }
//                            }
////                            template.convertAndSendToUser(String.valueOf(sysUserService.getUser().getId()), "/query/usbTask", num);
//                            DeviceManager.getInstance().stopSendThread();
//                            DeviceManager.getInstance().stopReceiveThread();
//                            DeviceManager.getInstance().releaseSocket();
//
//                            //return createJobsFile(jobs) && createDetailFile(job, details);
//                        } else if (!data.isEmpty()) {
//                            resData += data;
//                        }
//                        if (data.equals("start")) {
//                            DeviceManager.getInstance().sendData("OK");
//                        }
//                    }
//                });
//                DeviceManager.getInstance().receiveData();
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        return ResultInfo.success();
//    }
//
//    /**
//     * 传输init,传输数据
//     */
//    @RequestMapping(value = "transmitData.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo transmitData(String id, String data) {
//        try {
//            inventoryJobService.updateById(InventoryJob.builder().id(Integer.valueOf(id)).is_lx_hc(1).build());
//            if (StringUtils.isNotBlank(data)) {
//                if (JSON.isValid(data)) {
//                    Map<String, Object> result = JSON.parseObject(data, Map.class);
//                    String jobid = MapUtils.getString(result, "jobid");
//
//                    if (StringUtils.equals(id, jobid)) {
//                        List<String> rfids = (List<String>) MapUtils.getObject(result, "epcs", List.class);
//                        // 保存盘点数据
//                        inventoryDetailService.update(null, Wrappers.<InventoryDetail>lambdaUpdate().
//                                set(InventoryDetail::getInventory_status,"3").
//                                set(InventoryDetail::getInventory_type,"5").
//                                eq(InventoryDetail::getInventory_job_id,jobid).
//                                in(InventoryDetail::getRfid,rfids).
//                                eq(InventoryDetail::getIs_del,G.ISDEL_NO)
//                        );
//                        inventoryJobService.updateById(InventoryJob.builder().id(Integer.valueOf(id)).is_lx_hc(0).build());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        return ResultInfo.success();
//    }
//
//    /**
//     * 传输init,传输数据
//     */
//    @RequestMapping(value = "transmitStop.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo transmitStop() {
//        try {
//            DeviceManager.getInstance().stopReceiveThread();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return ResultInfo.success();
//    }
//
//    /**
//     * 下发手持机离线盘点任务, 手持机数据存储
//     */
//    @RequestMapping(value = "goPosRFIDOffline.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo goPosRFIDOffline(InventoryJob inventoryJob) {
//        // 设置离线状态,并进行更新
//        inventoryJob.setPos_flag("3");
//        inventoryJob.setIs_lx_xf(1);
//        inventoryJobService.updateById(inventoryJob);
//
//        // 查询出所有离线状态的盘点任务
//        List<InventoryJob> inventoryJobs = inventoryJobService.selectList(Wrappers.<InventoryJob>lambdaQuery()
//                .eq(InventoryJob::getPos_flag, "3")
//                .eq(InventoryJob::getIs_del, G.ISDEL_NO));
//        // 组装JSON数据
//        JSONObject jsonObject = new JSONObject();
//        // 组装数据 任务id^^任务名称^^开始时间^^结束时间*
//        List<Map<String, Object>> listJobs = new ArrayList<>();
//        for (InventoryJob job : inventoryJobs) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", job.getId());
//            map.put("name", job.getInventory_name());
//            map.put("code", job.getInventory_no());
//            map.put("startTime", dateToString(job.getPlan_start_time()));
//            map.put("end", dateToString(job.getEnd_time()));
//            listJobs.add(map);
//
//            // 如果id和当前id一样,则是本次任务,标记正在进行的任务
//            if (job.getId().equals(inventoryJob.getId())) {
//                Map<String,Object> runJob = new HashMap<>();
//                runJob.put("id", job.getId());
//                runJob.put("name", job.getInventory_name());
//                runJob.put("code", job.getInventory_no());
//                runJob.put("startTime", dateToString(job.getPlan_start_time()));
//                runJob.put("end", dateToString(job.getEnd_time()));
//                runJob.put("jobid", inventoryJob.getId());
//                jsonObject.put("runJob", runJob);
//            }
//        }
//        jsonObject.put("jobs", listJobs);
//        List<InventoryDetail> inventoryDetails = inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery()
//                .eq(InventoryDetail::getInventory_job_id, inventoryJob.getId())
//                .eq(InventoryDetail::getIs_del, G.ISDEL_NO)
//        );
//        if (CollectionUtils.isEmpty(inventoryDetails)) {
//            return ResultInfo.error("该任务下没有盘点资产");
//        }
//        List<Integer> assetsIds = inventoryDetails.stream().map(InventoryDetail::getAsset_id).collect(Collectors.toList());
//        if (CollectionUtils.isEmpty(assetsIds)) {
//            return ResultInfo.error("该任务下没有盘点资产");
//        }
//        List<AssetsManage> assetsManages = assetsManageService.selectList(Wrappers.<AssetsManage>lambdaQuery()
//                .select(AssetsManage::getAsset_code,
//                        AssetsManage::getAsset_name,
//                        AssetsManage::getUse_people,
//                        AssetsManage::getDept_id,
//                        AssetsManage::getRfid,
//                        AssetsManage::getRemarks)
//                .in(AssetsManage::getId, assetsIds)
//                .eq(AssetsManage::getIs_del, G.ISDEL_NO)
//        );
//        if (CollectionUtils.isEmpty(assetsManages)) {
//            return ResultInfo.error("该任务下没有盘点资产");
//        }
//
//        // 根据使用人去找用户
//        List<Integer> userIds = assetsManages.stream().distinct().map(AssetsManage::getUse_people).filter(Objects::nonNull).collect(Collectors.toList());
//        Map<Integer, String> userMap = userService.selectList(Wrappers.<SysUser>query()
//                .select("id", "user_name")
//                .in("id", userIds)
//                .eq("is_del", G.ISDEL_NO)
//        ).stream().collect(Collectors.toMap(SysUser::getId, SysUser::getUser_name));
//        // 根据部门去找部门
//        List<Integer> deptIds = assetsManages.stream().distinct().map(AssetsManage::getDept_id).filter(Objects::nonNull).collect(Collectors.toList());
//        Map<Integer, String> deptMap = sysDepartmentService.selectList(Wrappers.<SysDepartment>query()
//                .select("id", "dept_name")
//                .in("id", deptIds)
//                .eq("is_del", G.ISDEL_NO)
//        ).stream().collect(Collectors.toMap(SysDepartment::getId, SysDepartment::getDept_name));
//
//        //资产编码##资产名称##使用人##部门##RFID
//        List<Map<String, Object>> listDetails = new ArrayList<>();
//        for (AssetsManage detail : assetsManages) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("code", detail.getAsset_code());
//            map.put("name", detail.getAsset_name());
//            map.put("user", userMap == null ? "" : (null == userMap.get(detail.getUse_people()) ? "" : userMap.get(detail.getUse_people())));
//            map.put("dept", deptMap == null ? "" : (null == deptMap.get(detail.getDept_id()) ? "" : deptMap.get(detail.getDept_id())));
//            map.put("rfid", detail.getRfid());
//            map.put("jobid", inventoryJob.getId());
//            map.put("location", detail.getRemarks());
//            listDetails.add(map);
//        }
//        jsonObject.put("details", listDetails);
//
//        // 建立通讯
//        DeviceManager.getInstance().init();
//        boolean isOk = DeviceManager.getInstance().initLoaclSocket(7777, 8888);
//        if (!isOk) {
//            return ResultInfo.error("建立通讯失败");
//        }
//
//        DeviceManager.getInstance().sendData(jsonObject.toJSONString() + "##");
//        DeviceManager.getInstance().setDataCallbackListener(new IDataCallbackListener() {
//
//            @Override
//            public void onDataReceived(String data) {
////                template.convertAndSendToUser(String.valueOf(sysUserService.getUser().getId()), "/query/mark", data);
//                inventoryJob.setIs_lx_xf(0);
//                inventoryJobService.updateById(inventoryJob);
//                System.out.println("----------------------------------->" + data);
//                DeviceManager.getInstance().stopSendThread();
//                DeviceManager.getInstance().stopReceiveThread();
//                DeviceManager.getInstance().releaseSocket();
//            }
//        });
//        DeviceManager.getInstance().receiveData();
//
//        return ResultInfo.success();
//    }
//
//
//    /**
//     * 下发手持机离线盘点任务, 手持机数据存储
//     */
//    @RequestMapping(value = "goPosRFIDOfflineData.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultInfo goPosRFIDOfflineData(InventoryJob inventoryJob) {
//        // 设置离线状态,并进行更新
//        inventoryJob.setPos_flag("3");
//        inventoryJob.setIs_lx_xf(1);
//        inventoryJobService.updateById(inventoryJob);
//
//        // 查询出所有离线状态的盘点任务
//        List<InventoryJob> inventoryJobs = inventoryJobService.selectList(Wrappers.<InventoryJob>lambdaQuery()
//                .eq(InventoryJob::getPos_flag, "3")
//                .eq(InventoryJob::getIs_del, G.ISDEL_NO));
//        // 组装JSON数据
//        JSONObject jsonObject = new JSONObject();
//        // 组装数据 任务id^^任务名称^^开始时间^^结束时间*
//        List<Map<String, Object>> listJobs = new ArrayList<>();
//        for (InventoryJob job : inventoryJobs) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", job.getId());
//            map.put("name", job.getInventory_name());
//            map.put("code", job.getInventory_no());
//            map.put("startTime", dateToString(job.getPlan_start_time()));
//            map.put("actualTime", dateToString(job.getStart_time()));
//            map.put("end", dateToString(job.getEnd_time()));
//            listJobs.add(map);
//
//            // 如果id和当前id一样,则是本次任务,标记正在进行的任务
//            if (job.getId().equals(inventoryJob.getId())) {
//                Map<String,Object> runJob = new HashMap<>();
//                runJob.put("id", job.getId());
//                runJob.put("name", job.getInventory_name());
//                runJob.put("code", job.getInventory_no());
//                runJob.put("startTime", dateToString(job.getPlan_start_time()));
//                runJob.put("actualTime", dateToString(job.getStart_time()));
//                runJob.put("end", dateToString(job.getEnd_time()));
//                runJob.put("jobid", inventoryJob.getId());
//
//                // 得到概览数据
////                InventoryJobVo inventoryJobVo = new InventoryJobVo();
////                inventoryJobVo.setInventory_job_id(inventoryJob.getId());
////                List<Map<String,Object>> list = inventoryDetailMapper.getInventoryDept(inventoryJobVo);
////                for (Map<String, Object> temp : list){
////                    Integer deptId = MapUtils.getInteger(temp, "id");
////                    if (null == deptId){
////                        continue;
////                    }
////                    inventoryJobVo.setDept_id(deptId);
////                    temp.put("children", inventoryDetailMapper.getInventoryDeptDetail(inventoryJobVo));
////                }
////                runJob.put("glData", JSON.toJSONString(list));
//                jsonObject.put("runJob", runJob);
//            }
//        }
//        jsonObject.put("jobs", listJobs);
//        List<InventoryDetail> inventoryDetails = inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery()
//                .eq(InventoryDetail::getInventory_job_id, inventoryJob.getId())
//                .eq(InventoryDetail::getIs_del, G.ISDEL_NO)
//        );
//        if (CollectionUtils.isEmpty(inventoryDetails)) {
//            return ResultInfo.error("该任务下没有盘点资产");
//        }
//        List<Integer> assetsIds = inventoryDetails.stream().map(InventoryDetail::getAsset_id).collect(Collectors.toList());
//        if (CollectionUtils.isEmpty(assetsIds)) {
//            return ResultInfo.error("该任务下没有盘点资产");
//        }
//        List<AssetsManage> assetsManages = assetsManageService.selectList(Wrappers.<AssetsManage>lambdaQuery()
//                .select(AssetsManage::getAsset_code,
//                        AssetsManage::getAsset_name,
//                        AssetsManage::getUse_people,
//                        AssetsManage::getDept_id,
//                        AssetsManage::getRfid,
//                        AssetsManage::getRemarks)
//                .in(AssetsManage::getId, assetsIds)
//                .eq(AssetsManage::getIs_del, G.ISDEL_NO)
//        );
//        if (CollectionUtils.isEmpty(assetsManages)) {
//            return ResultInfo.error("该任务下没有盘点资产");
//        }
//
//        // 根据使用人去找用户
//        List<Integer> userIds = assetsManages.stream().distinct().map(AssetsManage::getUse_people).filter(Objects::nonNull).collect(Collectors.toList());
//        Map<Integer, String> userMap = userService.selectList(Wrappers.<SysUser>query()
//                .select("id", "user_name")
//                .in("id", userIds)
//                .eq("is_del", G.ISDEL_NO)
//        ).stream().collect(Collectors.toMap(SysUser::getId, SysUser::getUser_name));
//        // 根据部门去找部门
//        List<Integer> deptIds = assetsManages.stream().distinct().map(AssetsManage::getDept_id).filter(Objects::nonNull).collect(Collectors.toList());
//        Map<Integer, String> deptMap = sysDepartmentService.selectList(Wrappers.<SysDepartment>query()
//                .select("id", "dept_name")
//                .in("id", deptIds)
//                .eq("is_del", G.ISDEL_NO)
//        ).stream().collect(Collectors.toMap(SysDepartment::getId, SysDepartment::getDept_name));
//
//        //资产编码##资产名称##使用人##部门##RFID
//        List<Map<String, Object>> listDetails = new ArrayList<>();
//        for (AssetsManage detail : assetsManages) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("code", detail.getAsset_code());
//            map.put("name", detail.getAsset_name());
//            map.put("user", userMap == null ? "" : (null == userMap.get(detail.getUse_people()) ? "" : userMap.get(detail.getUse_people())));
//            map.put("dept", deptMap == null ? "" : (null == deptMap.get(detail.getDept_id()) ? "" : deptMap.get(detail.getDept_id())));
//            map.put("rfid", detail.getRfid());
//            map.put("jobid", inventoryJob.getId());
//            map.put("location", StringUtils.isNotBlank(detail.getRemarks())?detail.getRemarks():"");
//            listDetails.add(map);
//        }
//        jsonObject.put("details", listDetails);
//
//
//        //得到所有部门
//        List<SysDepartment> sysDepartments = sysDepartmentService.selectList(Wrappers.<SysDepartment>lambdaQuery().eq(SysDepartment::getIs_del, G.ISDEL_NO));
//        List<Map<String, Object>> listDept = new ArrayList<>();
//        for (SysDepartment sysDepartment : sysDepartments) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", sysDepartment.getId());
//            map.put("name", sysDepartment.getDept_name());
//            map.put("deptId", sysDepartment.getId());
//            listDept.add(map);
//        }
//        jsonObject.put("depts", listDept);
//
//        // 得到所有品类
//        List<AssetsType> assetsTypes = typeService.selectList(Wrappers.<AssetsType>query()
//                .select("id", "assets_type_name")
//                .eq("is_del", G.ISDEL_NO)
//        );
//        List<Map<String, Object>> typeMapAll = new ArrayList<>();
//        for (AssetsType assetsType : assetsTypes) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("id", assetsType.getId());
//            map.put("name", assetsType.getAssets_type_name());
//            map.put("typeId", assetsType.getId());
//            typeMapAll.add(map);
//        }
//        jsonObject.put("types", typeMapAll);
//
//
//        return ResultInfo.success(jsonObject);
//    }
//
//    /**
//     * 日期转换 date 转 string
//     */
//    private String dateToString(Date date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // 判断是否为空
//        if (date == null) {
//            return "";
//        }
//        // 判断如果转换错误
//        try {
//            return sdf.format(date);
//        } catch (Exception e) {
//            return "";
//        }
//    }
//
//
//}
