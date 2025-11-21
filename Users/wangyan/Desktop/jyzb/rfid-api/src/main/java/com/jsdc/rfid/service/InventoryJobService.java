//package com.jsdc.rfid.service;
//
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.jsdc.core.base.BaseService;
//import com.jsdc.rfid.common.G;
//import com.jsdc.rfid.dao.InventoryJobDao;
//import com.jsdc.rfid.enums.DataType;
//import com.jsdc.rfid.mapper.AssetsFileMemberMapper;
//import com.jsdc.rfid.mapper.AssetsTypeMapper;
//import com.jsdc.rfid.mapper.InventoryDetailMapper;
//import com.jsdc.rfid.mapper.InventoryJobMapper;
//import com.jsdc.rfid.model.*;
//import com.jsdc.rfid.service.rfid.CallBack;
//import com.jsdc.rfid.service.rfid.RfidService;
//import com.jsdc.rfid.utils.CommonDataTools;
//import lombok.AllArgsConstructor;
//import net.hasor.utils.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.multipart.MultipartFile;
//import vo.InventoryJobVo;
//import vo.ResultInfo;
//
//import javax.transaction.Transactional;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * ClassName: InventoryJobService
// * Description:
// * date: 2022/4/24 17:26
// *
// * @author bn
// */
//@Transactional
//@Service
//@AllArgsConstructor
//public class InventoryJobService extends BaseService<InventoryJobDao, InventoryJob> {
//
//
//    private InventoryJobMapper inventoryJobMapper;
//
//    private InventoryDetailService inventoryDetailService;
//
//    private InventoryDetailMapper inventoryDetailMapper;
//
//    private AssetsManageService assetsManageService;
//
//    private CommonDataTools commonDataTools;
//
//    private ManagementService managementService;
//
//    private final AssetsFileMemberMapper assetsFileMemberMapper;
//
//    private SysUserService sysUserService;
//
//    private final AssetsTypeMapper assetsTypeMapper;
//
//    private InventorySurplusService inventorySurplusService;
//
//    private SysPostService sysPostService;
//
//    private SysDepartmentService sysDepartmentService;
//
//    private AssetsTypeService assetsTypeService;
//
//    private RfidService rfidService;
//
//    private OperationRecordService operationRecordService;
//
//    /**
//     *  盘点任务列表展示
//     * @return
//     */
//    public PageInfo<InventoryJob> toList(Integer pageIndex, Integer pageSize, InventoryJobVo inventoryJobVo) {
//        PageHelper.startPage(pageIndex, pageSize, "create_time desc");
//        List<InventoryJob> inventoryJobs = selectList(Wrappers.<InventoryJob>lambdaQuery()
//                .like(StringUtils.isNotBlank(inventoryJobVo.getInventory_name()), InventoryJob::getInventory_name, inventoryJobVo.getInventory_name())
//                .like(StringUtils.isNotBlank(inventoryJobVo.getInventory_no()), InventoryJob::getInventory_no, inventoryJobVo.getInventory_no())
//                .eq(InventoryJob::getIs_del, "0")
//                .eq(StringUtils.isNotBlank(inventoryJobVo.getProgress_node()), InventoryJob::getProgress_node, inventoryJobVo.getProgress_node())
////                .orderByDesc(InventoryJob::getCreate_time)
//        );
//        return new PageInfo<>(inventoryJobs);
//    }
//
//    /**
//     *  增加
//     * @return
//     */
//    public void toAdd(InventoryJob inventoryJob) {
//        String deptIds = inventoryJob.getDept_ids();
//        // 去重
//        if (StringUtils.isNotBlank(deptIds)) {
//            List<String> list = Arrays.asList(deptIds.split(","));
//            list = list.stream().distinct().collect(Collectors.toList());
//            deptIds = String.join(",", list);
//        }
//        inventoryJob.setDept_ids(deptIds);
//
//        String typeIds = inventoryJob.getAsset_type_ids();
//        // 去重
//        if (StringUtils.isNotBlank(typeIds)) {
//            List<String> list = Arrays.asList(typeIds.split(","));
//            list = list.stream().distinct().collect(Collectors.toList());
//            typeIds = String.join(",", list);
//        }
//        inventoryJob.setAsset_type_ids(typeIds);
//
//        inventoryJob.setInventory_no(commonDataTools.getNo(DataType.INVENTORY_JOB.getType(), null));
//        inventoryJob.setCreate_time(new Date());
//        inventoryJob.setProgress_node("1");
//        inventoryJob.setCreate_user(sysUserService.getUser().getId());
//        inventoryJob.setIs_del(G.ISDEL_NO);
//        inventoryJob.insert();
//
//        operationRecordService.addOperationRecord(OperationRecord.builder()
//                .record("创建盘点任务").type("3").field_fk(inventoryJob.getInventory_no())
//                .build()
//        );
//
//        // 根据盘点范围获取所有资产
//        List<Integer> depts = new ArrayList<>();
//        if (StringUtils.isNotEmpty(inventoryJob.getDept_ids())) {
//            for (String dept_id : inventoryJob.getDept_ids().split(",")){
//                try {
//                    depts.add(Integer.valueOf(dept_id));
//                } catch (NumberFormatException ignored) {
//                }
//            }
//        }
//        List<Integer> assets = new ArrayList<>();
//
//        if (StringUtils.isNotEmpty(inventoryJob.getAsset_type_ids())) {
//            for (String asset_type_id : inventoryJob.getAsset_type_ids().split(",")){
//                try {
//                    assets.add(Integer.valueOf(asset_type_id));
//                } catch (NumberFormatException ignored) {
//                }
//            }
//        }
//
//        List<AssetsManage> assetsManages = assetsManageService.selectList(Wrappers.<AssetsManage>lambdaQuery()
//                .eq(AssetsManage::getIs_del, G.ISDEL_NO)
//                .in(AssetsManage::getDept_id, depts)
//                .in(AssetsManage::getAsset_type_id, assets)
//        );
//
//        // 生成盘点明细
//        assetsManages.forEach(x -> {
//            InventoryDetail inventoryDetail = new InventoryDetail();
//            inventoryDetail.setInventory_job_id(inventoryJob.getId());
//            inventoryDetail.setAsset_id(x.getId());
//            inventoryDetail.setAsset_name(x.getAsset_name());
//            inventoryDetail.setRfid(x.getRfid());
//            inventoryDetail.setInventory_status("1");
//            inventoryDetail.setIs_del(G.ISDEL_NO);
//            inventoryDetail.setCreate_time(new Date());
//            inventoryDetail.insert();
//        });
//
//    }
//
//    /**
//     * 修改
//     * @param inventoryJob
//     */
//    public void toEdit(InventoryJob inventoryJob) {
//        inventoryJob.setUpdate_time(new Date());
//        inventoryJob.setUpdate_user(sysUserService.getUser().getId());
//        inventoryJob.updateById();
//
//        operationRecordService.addOperationRecord(OperationRecord.builder().record("修改盘点任务").type("3").field_fk(inventoryJob.getInventory_no()).build());
//
//        // 原始数据
//        inventoryDetailService.update(null, Wrappers.<InventoryDetail>lambdaUpdate()
//                .set(InventoryDetail::getIs_del, G.ISDEL_YES)
//                .set(InventoryDetail::getUpdate_time, new Date())
//                .set(InventoryDetail::getUpdate_user, sysUserService.getUser().getId())
//                .eq(InventoryDetail::getInventory_job_id, inventoryJob.getId())
//                .eq(InventoryDetail::getIs_del, G.ISDEL_NO)
//        );
//
//        // 根据盘点范围获取所有资产
//        List<Integer> depts = inventoryJob.getDept_ids() == null ? new ArrayList<>() : Arrays.stream(inventoryJob.getDept_ids().split(",")).map(Integer::parseInt).collect(Collectors.toList());
//        List<Integer> assets = inventoryJob.getAsset_type_ids() == null ? new ArrayList<>() : Arrays.stream(inventoryJob.getAsset_type_ids().split(",")).map(Integer::parseInt).collect(Collectors.toList());
//
//        List<AssetsManage> assetsManages = assetsManageService.selectList(Wrappers.<AssetsManage>lambdaQuery()
//                .eq(AssetsManage::getIs_del, G.ISDEL_NO)
//                .in(AssetsManage::getDept_id, depts)
//                .in(AssetsManage::getAsset_type_id, assets)
//        );
//
//        // 生成盘点明细
//        assetsManages.forEach(x -> {
//            InventoryDetail inventoryDetail = new InventoryDetail();
//            inventoryDetail.setInventory_job_id(inventoryJob.getId());
//            inventoryDetail.setAsset_id(x.getId());
//            inventoryDetail.setRfid(x.getRfid());
//            inventoryDetail.setAsset_name(x.getAsset_name());
//            inventoryDetail.setInventory_status("1");
//            inventoryDetail.setIs_del("0");
//            inventoryDetail.setCreate_time(new Date());
//            inventoryDetail.insert();
//        });
//
//    }
//
//
//    /**
//     * 盘点
//     *
//     * @param inventoryJob
//     */
//    public void toInventory(InventoryJob inventoryJob) {
//
//        // 根据设备获取部门或者资产类别下所有资产rfid集合
//        List<AssetsManage> assetsManages = assetsManageService.selectList(Wrappers.<AssetsManage>lambdaQuery().eq(AssetsManage::getIs_del, "0"));
//
//        // 当前部门下rfid集合
//        List<String> rfids = assetsManages.stream().map(AssetsManage::getRfid).collect(Collectors.toList());
//
//
//        // 获取当前任务下所有资产
//        List<InventoryDetail> inventoryDetails = inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery().
//                eq(InventoryDetail::getIs_del, "0").eq(InventoryDetail::getInventory_status, "1").eq(InventoryDetail::getInventory_job_id, inventoryJob.getId()));
//
//        inventoryDetails.forEach(x -> {
//            AssetsManage assetsManage = assetsManageService.selectOne(Wrappers.<AssetsManage>lambdaQuery().
//                    eq(AssetsManage::getIs_del, "0").eq(AssetsManage::getId, x.getAsset_id()));
//
//            if (assetsManage != null && rfids.contains(assetsManage.getRfid())) {
//                // 修改盘点明细
//                x.setInventory_status(inventoryJob.getInventory_status());
//                x.setInventory_type("1");
//                inventoryDetailService.edit(x);
//            }
//        });
//    }
//
//
//    /**
//     * 生成处置单
//     *
//     * @param inventoryDetail
//     */
//    public void toManagement(InventoryDetail inventoryDetail) {
//        Management management = new Management();
//        // 盘亏
//        if (inventoryDetail.getInventory_status().equals("5")) {
//            management.setSource("2");
//
//        } else {// 异常
//            management.setSource("3");
//        }
//        management.setReason_detail(inventoryDetail.getConfirm_remark());
//
//        AssetsManage assetsManage = new AssetsManage();
//
//        assetsManage.setId(inventoryDetail.getAsset_id());
//
//        List<AssetsManage> assetsManages = new ArrayList<>();
//
//        assetsManages.add(assetsManage);
//
//        management.setAssetsManageList(assetsManages);
//
//        managementService.addManagement(management);
//
//        inventoryDetail.setManagement_id(management.getId());
//        inventoryDetail.setUpdate_time(new Date());
//        inventoryDetail.setUpdate_user(sysUserService.getUser().getId());
//        inventoryDetail.updateById();
//
//    }
//
//    /**
//     * RFID接口
//     *
//     * @param rfid
//     */
//    public void toRFID(String rfid) {
//
//        //根据rfid获取资产
//        AssetsManage assetsManages = assetsManageService.selectOne(Wrappers.<AssetsManage>lambdaQuery().
//                eq(AssetsManage::getRfid, rfid).eq(AssetsManage::getIs_del, "0"));
//
//        if (null == assetsManages) {
//            return;
//        }
//
//        // 筛选盘点明细 1.应盘数据 2.盘点任务进度节点为开始 3.盘点任务盘点方式包括远程盘点
//        List<InventoryDetail> inventoryDetails = inventoryJobMapper.toRFID(assetsManages.getId());
//
//
//        // 盘点明细修改
//        inventoryDetails.forEach(x -> {
//            // 修改盘点明细
//            x.setInventory_status("3");
//            x.setInventory_type("1");
//            inventoryDetailService.edit(x);
//        });
//
//
//    }
//
//
//    public InventoryJobVo getById(InventoryJobVo inventoryJob) {
//        InventoryJobVo inventoryJobVo = new InventoryJobVo();
//
//        inventoryJobVo.setInventoryJob(selectById(inventoryJob.getId()));
//
//        if (StringUtils.isNotEmpty(inventoryJobVo.getInventoryJob().getDept_ids())) {
//            List<Integer> depts = Arrays.stream(inventoryJobVo.getInventoryJob().getDept_ids().split(",")).map(Integer::parseInt).collect(Collectors.toList());
//            inventoryJobVo.getInventoryJob().setSysDepartments(sysDepartmentService.selectBatchIds(depts));
//        }
//
//        if (StringUtils.isNotEmpty(inventoryJobVo.getInventoryJob().getAsset_type_ids())) {
//            List<Integer> assets = Arrays.stream(inventoryJobVo.getInventoryJob().getAsset_type_ids().split(",")).map(Integer::parseInt).collect(Collectors.toList());
//
//            inventoryJobVo.getInventoryJob().setAssetsTypes(assetsTypeService.selectBatchIds(assets));
//        }
//
//
//        return inventoryJobVo;
//    }
//
//    public List<InventoryDetail> getInventoryDetail(Integer pageIndex, Integer pageSize, InventoryJobVo inventoryJobVo) {
//
//        PageHelper.startPage(pageIndex, pageSize);
////        List<InventoryDetail> inventoryDetails=inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery().
////                eq(InventoryDetail::getIs_del,"0").eq(InventoryDetail::getInventory_job_id,inventoryJobVo.getInventory_job_id()).
////                eq(InventoryDetail::getInventory_status,inventoryJobVo.getInventory_status()));
//
//        List<InventoryDetail> inventoryDetails = inventoryDetailMapper.getInventoryDetail(inventoryJobVo);
//        inventoryDetails.forEach(x -> {
//
//            x.setAssetsManage(assetsManageService.getById(x.getAsset_id()));
//
//        });
//
//
//        return inventoryDetails;
//
//    }
//
//    public void toDel(InventoryJob inventoryJob) {
//        inventoryJob.setUpdate_user(sysUserService.getUser().getId());
//        inventoryJob.setUpdate_time(new Date());
//        inventoryJob.updateById();
//        operationRecordService.addOperationRecord(OperationRecord.builder().record("删除盘点任务").type("3").field_fk(inventoryJob.getInventory_no()).build());
//    }
//
//    public void toStart(InventoryJob inventoryJob) {
//
//        inventoryJob.setUpdate_user(sysUserService.getUser().getId());
//        inventoryJob.setUpdate_time(new Date());
//        inventoryJob.setStart_time(new Date());
//        if (inventoryJob.getProgress_node().equals("3")) {
//            inventoryJob.setEnd_time(new Date());
//            operationRecordService.addOperationRecord(OperationRecord.builder().record("结束盘点任务").type("3").field_fk(inventoryJob.getInventory_no()).build());
//        } else {
//            operationRecordService.addOperationRecord(OperationRecord.builder().record("开始盘点任务").type("3").field_fk(inventoryJob.getInventory_no()).build());
//        }
//        inventoryJob.updateById();
//
//    }
//
//    public String toProgress(InventoryJobVo inventoryJobVo) {
//        int size = inventoryDetailService.selectCount(Wrappers.<InventoryDetail>lambdaQuery()
//                .eq(InventoryDetail::getInventory_job_id, inventoryJobVo.getInventory_job_id())
//                .eq(InventoryDetail::getIs_del, G.ISDEL_NO));
//
//        int num = inventoryDetailService.selectCount(Wrappers.<InventoryDetail>lambdaQuery()
//                .eq(InventoryDetail::getInventory_job_id, inventoryJobVo.getInventory_job_id())
//                .eq(InventoryDetail::getIs_del, G.ISDEL_NO)
//                .eq(InventoryDetail::getInventory_status, "1"));
//
//        if (num == 0) {
//            return "100%";
//        }
//
//        return (size * 100 - num * 100) / size + "%";
//
//    }
//
//    public ResultInfo toAddAsset(AssetsManage bean) {
//        // 判断是否存在
//        if (null == bean || null == bean.getAsset_type_id() || null == assetsTypeMapper.selectById(bean.getAsset_type_id())) {
//            return ResultInfo.error("资产类型不能为空");
//        }
//
//        if (null == bean.getQuantity() || bean.getQuantity() <= 0) {
//            return ResultInfo.error("数量不能为空");
//        }
//        // 资产编号（自动生成-根据所选类型品类、登记日期、自增规则进行自动拼接）
////        bean.setAsset_code(commonDataTools.getNo(DataType.ASSET_MANAGE.getType(), assetsTypeMapper.selectById(bean.getAsset_type_id())));
//// 定义num 数值
//        Integer num = bean.getQuantity();
//        for (int i = 0; i < num; i++) {
//            bean.setAsset_code(commonDataTools.getNo(DataType.ASSET_MANAGE.getType(), assetsTypeMapper.selectById(bean.getAsset_type_id())));
//            bean.setIs_del(String.valueOf(1));
//            bean.setRfid(bean.getAsset_code());
//            // 创建时间
//            bean.setCreate_time(new Date());
//            // 创建者
//            bean.setCreate_user(sysUserService.getUser().getId());
//            // 二维码
//            bean.setQr_code(assetsManageService.genratorQrCode(bean.getAsset_code()));
//            bean.insert();
//
//            // 附件处理
//            if (!CollectionUtils.isEmpty(bean.getFileMemberList())) {
//                for (Integer file_id : bean.getFileMemberList()) {
//                    assetsFileMemberMapper.insert(AssetsFileMember.builder().file_id(file_id).assets_id(bean.getId()).build());
//                }
//            }
//            operationRecordService.addOperationRecord(bean.getId(), bean.getAsset_code(), "资产登记", OperationRecordService.MODE_TYPE_ASSETS);
//
//            InventoryDetail inventoryDetail = new InventoryDetail();
//            inventoryDetail.setInventory_job_id(bean.getInventory_job_id());
//            inventoryDetail.setAsset_id(bean.getId());
//            inventoryDetail.setAsset_name(bean.getAsset_name());
//            inventoryDetail.setRfid(bean.getRfid());
//            inventoryDetail.setInventory_status("4");
//            inventoryDetail.setInventory_type("4");
//            inventoryDetail.setIs_del("0");
//            inventoryDetail.setCreate_user(sysUserService.getUser().getId());
//            inventoryDetail.setCreate_time(new Date());
//
//            inventoryDetail.insert();
//
//        }
//
//
//        return ResultInfo.success();
//
//
//    }
//
//    public ResultInfo toAsset(InventoryDetail inventoryDetail) {
//        AssetsManage assetsManage = assetsManageService.selectOne(Wrappers.<AssetsManage>lambdaQuery().
//                eq(AssetsManage::getId, inventoryDetail.getAsset_id()));
//
//
//        assetsManage.setIs_del("0");
//
//        assetsManage.updateById();
//
//        inventoryDetail.setUpdate_user(sysUserService.getUser().getId());
//        inventoryDetail.setUpdate_time(new Date());
//        inventoryDetail.updateById();
//
//        return ResultInfo.success();
//    }
//
//    public void startRFID(InventoryJob inventoryJob) {
//        List<String> ips = inventoryJobMapper.startRFID(inventoryJob);
//        rfidService.onInventory(ips);
//    }
//
//    public void stopRFID(InventoryJob inventoryJob) {
////        List<String> ips=inventoryJobMapper.startRFID(inventoryJob);
//        rfidService.stopInventory();
//        CallBack.getPdEpc().forEach(x -> {
//
//
//            InventoryDetail inventoryDetail = inventoryDetailService.selectOne(Wrappers.
//                    <InventoryDetail>lambdaQuery().eq(InventoryDetail::getIs_del, "0").
//                    eq(InventoryDetail::getInventory_job_id, inventoryJob.getId()).eq(InventoryDetail::getRfid, x));
//            if (inventoryDetail != null) {
//                inventoryDetail.setInventory_status("3");
//                inventoryDetail.setInventory_type("1");
//                inventoryDetailService.edit(inventoryDetail);
//            }
//
//        });
//
//        CallBack.getPdEpc().clear();
//        CallBack.getPdList().clear();
//    }
//
////    private static int toDigit(char ch, int index) {
////        int digit = Character.digit(ch, 16);
////        if (digit == -1) {
////            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
////        }
////        return digit;
////    }
//
////    public static String fromHex(String hex) {
////        /*兼容带有\x的⼗六进制串*/
////        hex = hex.replace("\\x","");
////        char[] data = hex.toCharArray();
////        int len = data.length;
////        if ((len & 0x01) != 0) {
////            throw new RuntimeException("字符个数应该为偶数");
////        }
////        byte[] out = new byte[len >> 1];
////        for (int i = 0, j = 0; j < len; i++) {
////            int f = toDigit(data[j], j) << 4;
////            j++;
////            f |= toDigit(data[j], j);
////            j++;
////            out[i] = (byte) (f & 0xFF);
////        }
////        return new String(out).trim();
////    }
//
//    public List<InventoryDetail> posRFID(InventoryJob inventoryJob) {
//
//        // 设备扫描出来的rfid集合
//        List<String> rfids = Arrays.asList(inventoryJob.getRfids().split(","));
//
////        List<String> rfids=rfidsData.stream().map(x->fromHex(x)).collect(Collectors.toList());
//
//        System.out.println(rfids);
//        // 当前任务下所有资产
//        List<InventoryDetail> details = inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery().
//                eq(InventoryDetail::getIs_del, "0").
//                eq(InventoryDetail::getInventory_job_id, inventoryJob.getId()).
//                eq(InventoryDetail::getInventory_status, "1"));
//
//        // 当前任务下rfid集合
//        List<InventoryDetail> detailList = details.stream().filter(x ->
//                rfids.contains(x.getRfid())).collect(Collectors.toList());
//
//        detailList.forEach(x -> {
//            x.setInventory_status("3");
//            x.setInventory_type("2");
//            x.updateById();
//        });
//
//
//        return detailList;
//
//
//    }
//
//    /**
//     * 文件上传
//     *
//     * @return
//     */
//    public ResultInfo fileUpload(Integer id, MultipartFile file) {
//        String encoding = "UTF-8";
//        InputStream in = null;
//        if (file.isEmpty()) {
//            return ResultInfo.error("文件不能为空");
//        } else {
//            try {
//                in = file.getInputStream();
//                InputStreamReader read = new InputStreamReader(in, encoding);//考虑到编码格式
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String lineTxt = null;
//
//                List<String> rfids = new ArrayList<>();
//                StringBuilder sb = new StringBuilder();
//                while ((lineTxt = bufferedReader.readLine()) != null) {
////                    rfids.add(lineTxt.trim());
//                    sb.append(lineTxt.trim());
//                }
//
//                read.close();
//
//                String[] split = sb.toString().split(",");
//                for (String s : split) {
//                    if (StringUtils.isBlank(s)) {
//                        continue;
//                    }
//                    // 转小写
//                    s = s.trim().toLowerCase();
//                    rfids.add(s);
//                }
//
//                rfids.stream().distinct().collect(Collectors.toList());
//                if (!CollectionUtils.isEmpty(rfids)) {
//                    // 当前任务下所有资产
//                    List<InventoryDetail> details = inventoryDetailService.selectList(Wrappers.<InventoryDetail>lambdaQuery()
//                            .eq(InventoryDetail::getIs_del, "0")
//                            .eq(InventoryDetail::getInventory_job_id, id)
//                            .eq(InventoryDetail::getInventory_status, "1"));
//                    // 当前任务下rfid集合
//                    List<InventoryDetail> detailList = details.stream().filter(x ->{
//                        String temp = x.getRfid().toLowerCase();
//                        return rfids.contains(temp);
//                    }).collect(Collectors.toList());
//                    if (CollectionUtils.isEmpty(detailList)) {
//                        return ResultInfo.error("未找到匹配的资产");
//                    }
//                    detailList.forEach(x -> {
//                        x.setInventory_status("3");
//                        x.setInventory_type("5");
//                        x.updateById();
//                    });
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return ResultInfo.success();
//    }
//
//    // 盘点概览统计
//    public PageInfo getInventoryDetailStatistics(Integer page, Integer limit, InventoryJobVo inventoryJobVo) {
//
//        PageHelper.startPage(page, limit);
//        List<Map<String,Object>> list = inventoryDetailMapper.getInventoryDetailStatistics(inventoryJobVo);
//
//        return new PageInfo<>(list);
//    }
//
//    // 盘点概览统计
//    public PageInfo getInventoryDetailStatisticsDept(Integer page, Integer limit, InventoryJobVo inventoryJobVo) {
//
//        PageHelper.startPage(page, limit);
//        List<Map<String,Object>> list = inventoryDetailMapper.getInventoryDept(inventoryJobVo);
////        for (Map<String, Object> temp : list){
////            Integer deptId = MapUtils.getInteger(temp, "id");
////            if (null == deptId){
////                continue;
////            }
////            inventoryJobVo.setDept_id(deptId);
////            temp.put("children", inventoryDetailMapper.getInventoryDeptDetail(inventoryJobVo));
////        }
//
//        return new PageInfo<>(list);
//    }
//
//    // 根据部门拿到当前位置
//    public List<String> getLocalByDept(Integer deptId) {
//        List<AssetsManage> a = assetsManageService.selectList(Wrappers.<AssetsManage>query()
//                .select("remarks")
//                .eq("dept_id",deptId).eq("is_del", G.ISDEL_NO));
//        if (CollectionUtils.isEmpty(a)) {
//            return new ArrayList<>();
//        }
//        List<String> b = a.stream().map(AssetsManage::getRemarks).distinct().collect(Collectors.toList());
//        return b;
//    }
//
//    public List<Map<String,Object>> getInventoryDetailStatisticsDept1(InventoryJobVo inventoryJobVo) {
//        return inventoryDetailMapper.getInventoryDeptDetail(inventoryJobVo);
//    }
//}
