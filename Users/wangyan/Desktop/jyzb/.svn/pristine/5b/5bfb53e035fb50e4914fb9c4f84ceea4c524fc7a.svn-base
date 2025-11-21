//package com.jsdc.rfid.service.rfid;
//
//import cn.hutool.core.date.DateUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.jsdc.core.base.Base;
//import com.jsdc.rfid.common.G;
//import com.jsdc.rfid.mapper.AssetsAccessRecordMapper;
//import com.jsdc.rfid.mapper.AssetsInAndOutCollectMapper;
//import com.jsdc.rfid.model.*;
//import com.jsdc.rfid.service.*;
//import com.rfidread.Enumeration.eReadType;
//import com.rfidread.RFIDReader;
//import com.rfidread.Tag6C;
//import lombok.NonNull;
//import lombok.Synchronized;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//public class RfidService {
//
//    @Autowired
//    private CarryManageService carryManageService;
//    @Autowired
//    private CarryAbnormalService carryAbnormalService;
//    @Autowired
//    private EquipmentService equipmentService;
//    @Autowired
//    private AssetsAccessRecordService assetsAccessRecordService;
//    @Autowired
//    private AssetsAccessRecordMapper assetsAccessRecordMapper;
//    @Autowired
//    private AssetsManageService assetsManageService;
//    @Autowired
//    private AssetsTrajectoryService assetsTrajectoryService;
//
//    @Autowired
//    private AssetsInAndOutCollectMapper inAndOutCollectMapper;
//
//    @Autowired
//    private CarryWhiteService carryWhiteService;
//
//
//    /**
//     * tcp连接
//     *
//     * @param equipment
//     * @return
//     */
//    public Boolean tcpConnect(@NonNull Equipment equipment) {
//        String ip = equipment.getIp();
//        String port = equipment.getPort();
//        CallBack callBack = new CallBack();
//        String connectId = ip + ":" + port;
//        try {
//            if (RFIDReader.CreateTcpConn(connectId, callBack)) {
//                System.out.println("tcp连接成功!");
//                // single单次，Inventory循环（单次或循环）
//    //            int count = read(ip, port, 1, eReadType.Single);
//    //            System.out.println(count);
//    //            System.out.println("single单次EPC");
//                return true;
//            } else {
//                System.out.println("tcp连接失败!");
//                return false;
//            }
//        } catch (Exception e) {
//            System.out.println("tcp连接失败!");
//            return false;
//        }
//    }
//
//    /**
//     * tcp监听
//     *
//     * @param serverIP
//     * @param serverPort
//     */
//    public void openTcpServer(String serverIP, String serverPort) {
//        CallBack callBack = new CallBack();
//        Boolean flag = RFIDReader.OpenTcpServer(serverIP, serverPort, callBack);
//        System.out.println(flag);
//    }
//
//    /**
//     * 开始读
//     *
//     * @param ip
//     * @param port
//     * @param antNum
//     * @param readType
//     * @return
//     */
//    public int read(String ip, String port, int antNum, eReadType readType) {
//        String connectId = ip + ":" + port;
//        return RFIDReader._Tag6C.GetEPC(connectId, antNum, readType);
//    }
//
//    /**
//     * 检索
//     *
//     * @param ip
//     * @param port
//     * @param antNum
//     * @param readType
//     * @param epc
//     * @return
//     */
//    public int readMatch(String ip, String port, int antNum, eReadType readType, String epc) {
//        String connectId = ip + ":" + port;
//        int count = RFIDReader._Tag6C.GetEPC_MatchEPC(connectId, antNum, readType, epc);
//        RFIDReader._Config.Stop(connectId);
//        return count;
//    }
//
//    /**
//     * 关闭所有连接
//     */
//    public static void closeAllConnect() {
//        RFIDReader.CloseAllConnect();
//    }
//
//    /**
//     * 关闭所有连接
//     */
//    public static void closeConnect(String ip, String port) {
//        String connectId = ip + ":" + port;
//        RFIDReader.CloseConn(connectId);
//    }
//
//    //盘点 开始读卡
//    public void onInventory(List<String> pdList) {
//        CallBack.readType.add(G.RFID_READ_CHECK);
//        CallBack.pdList = new HashSet<>(pdList);
//
//        LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
////        wrappers.eq(Equipment::getIp, str);
//        //读写一体机
//        wrappers.eq(Equipment::getEquipment_type, 3);
//        wrappers.eq(Equipment::getEquipment_usage, 1);
//        wrappers.eq(Equipment::getIs_del, 0);
//        List<Equipment> equipmentList = equipmentService.selectList(wrappers);
//        Set<String> ipList = new HashSet<>();
//        for (Equipment equipment : equipmentList) {
//            ipList.add(equipment.getIp() + ":" + equipment.getPort());
//        }
//        for (String ip : pdList) {
//            int antNum = 15;
//            //根据ip获取设备信息
////            String str = ip.substring(0, ip.indexOf(":"));
//            if (Base.notEmpty(ip)) {
//                if (ipList.size() > 0 && ipList.contains(ip)) {
//                    antNum = 1;
//                }
//            }
//            int finalAntNum = antNum;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int count = RFIDReader._Tag6C.GetEPC(ip, finalAntNum, eReadType.Inventory);
//                    System.out.println(count);
//                }
//            }).start();
//        }
//        System.out.println("盘点 开始读卡 end++++++++++++++++++++++");
//    }
//
//    //停止盘点
//    public void stopInventory() {
//        for (String ip : CallBack.pdList) {
//            //停止循环读请使用“停止指令”
//            RFIDReader._Config.Stop(ip);
//        }
//    }
//
//    /**
//     * 盘点 结束读卡 回调方法
//     */
//    public void getInventoryList(Set<String> pdEpc) {
//        System.out.println("pdList+++++++++++++");
//        System.out.println(pdEpc);
////        System.out.println(CallBack.pdEpc);
//
//
//    }
//
//    //盘点 测试
//    public void test() {
////        CallBack callBack = new CallBack();
////        if (RFIDReader.CreateTcpConn("192.168.0.112:9090", callBack)) {
////            System.out.println("tcp连接成功!");
////        } else {
////            System.out.println("tcp连接失败!");
////        }
////        CallBack.readType.add(G.RFID_READ_CHECK);
////        List<String> pdList = new ArrayList<>();
////        pdList.add("192.168.0.112");
////        callBack.setPdList(pdList);
//        // single单次，Inventory循环（单次或循环）
////        int count = RFIDReader._Tag6C.GetEPC("192.168.0.112:9090", 1, eReadType.Inventory);
////        System.out.println(count);
//    }
//
//    //报警
//    public void test2() {
////        CallBack callBack = new CallBack();
////        if (RFIDReader.CreateTcpConn("192.168.0.110:9090", callBack)) {
////            System.out.println("tcp连接成功!");
////        } else {
////            System.out.println("tcp连接失败!");
////        }
////        CallBack.readType.add(G.RFID_READ_ALARM);
////        List<String> alarmList = new ArrayList<>();
////        alarmList.add("192.168.0.110");
//////        callBack.setAlarmList(alarmList);
////        // single单次，Inventory循环（单次或循环）
////        int count = RFIDReader._Tag6C.GetEPC("192.168.0.110:9090", 1, eReadType.Inventory);
////        System.out.println(count);
//    }
//
//    //查询
//    public void test3() {
////        if (RFIDReader.CreateTcpConn("192.168.0.110:9090", callBack)) {
////            System.out.println("tcp连接成功!");
////        } else {
////            System.out.println("tcp连接失败!");
////        }
//        CallBack.readType.add(G.RFID_READ_SEARCH);
////        List<String> cxList = new ArrayList<>();
////        cxList.add("192.168.0.112:9090");
////        cxList.add("192.168.0.110:9090");
////        CallBack.cxList = cxList;
//        // single单次，Inventory循环（单次或循环）
//        int count = RFIDReader._Tag6C.GetEPC("192.168.0.199:9090", 1, eReadType.Inventory);
////        int count2 = RFIDReader._Tag6C.GetEPC("192.168.0.112:9090", 1, eReadType.Inventory);
//        log.debug(count + "");
////        System.out.println(count2);
//    }
//
//    /**
//     * 接收读卡结果数据方法 盘点
//     */
//    public void getList(Set<String> pdEpc) {
//        log.info("pdList+++++++++++++");
//        log.info(pdEpc.toString());
//        log.info(CallBack.cxEpc.toString());
//    }
//
//    /**
//     * 接收读卡结果数据方法 报警
//     */
//    public void getList2(Set<String> alarmEpc) {
//        System.out.println("alarmList2+++++++++++++");
//        System.out.println(alarmEpc);
//        System.out.println(CallBack.cxEpc);
//    }
//
//    /**
//     * 接收读卡结果数据方法 查询
//     */
//    public static void getList3() {
//        System.out.println("cxList3+++++++++++++");
//        System.out.println(CallBack.cxEpc);
//    }
//
//    /**
//     * 外携报警 回调方法
//     * 接收读卡结果数据方法 查询
//     */
//    public void getList4() {
//        System.out.println("cxList4+++++++++++++");
//        System.out.println(CallBack.cxEpc);
//    }
//
//    public void stop() {
//        //停止循环读请使用“停止指令”
//        RFIDReader._Config.Stop("192.168.0.112:9090");
//    }
//
//    public void stop2() {
//        //停止循环读请使用“停止指令”
//        RFIDReader._Config.Stop("192.168.0.199:9090");
//    }
//
//    public void stop3() {
//        //停止循环读请使用“停止指令”
//        RFIDReader.CloseAllConnect();
//    }
//
//    /**
//     * 10分钟执行一次
//     * 进出记录/未授权外携报警
//     */
////    @Scheduled(cron = "0 */1 * * * ?")
//    public void timeAccessRecord() {
//        //检测资产进出记录
//
//        //添加读卡类型：外携告警
//        CallBack.readType.add(G.RFID_READ_CARRY_ALARM);
//        //获取报警设备列表
//        Equipment equipment = new Equipment();
//        //用处 【字典 1扫描 2报警】
//        equipment.setEquipment_usage(2);
//        List<String> equipmentList = equipmentService.getIpList(equipment);
//        //外携报警设备IP列表集合
//        CallBack.carryAlarmList = equipmentList;
//        for (String ip : equipmentList) {
//            int antNum = 15;
//            //根据ip获取设备信息
//            String str = ip.substring(0, ip.indexOf(":"));
//            if (Base.notEmpty(str)) {
//                LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                wrappers.eq(Equipment::getIp, str);
//                //读写一体机
//                wrappers.eq(Equipment::getEquipment_type, 3);
//                wrappers.eq(Equipment::getIs_del, 0);
//                List<Equipment> equipment2 =  equipmentService.selectList(wrappers);
//                if (!CollectionUtils.isEmpty(equipment2)) {
//                    antNum = 1;
//                }
//            }
//            int count = RFIDReader._Tag6C.GetEPC(ip, antNum, eReadType.Inventory);
//            System.out.println(count);
//        }
//
//        try {
//
//            Thread.sleep(10 * 1000); //暂停20秒
//            //停止检测
//            for (String ip : CallBack.carryAlarmList) {
//                //停止循环读请使用“停止指令”
//                RFIDReader._Config.Stop(ip);
//            }
//            Thread.sleep(2 * 1000); //暂停2秒
//            if (CallBack.carryAlarmList.size() > 0 && CallBack.carryAlarmEpc.size() > 0) {
//                onSearchAll();//扫描所有设备
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * AB门判断
//     * 10分钟执行一次
//     * 进出记录/未授权外携报警
//     */
////    @Scheduled(cron = "0 */1 * * * ?")
//    public void abTimeAccessRecord() {
//        //添加读卡类型：外携告警
//        CallBack.readType.add(G.RFID_READ_CARRY_ALARM);
//        //获取报警设备列表
//        Equipment equipment = new Equipment();
//        //用处 【字典 1扫描 2报警】
//        equipment.setEquipment_usage(2);
//        List<String> equipmentList = equipmentService.getIpList(equipment);
//        //外携报警设备IP列表集合
//        CallBack.carryAlarmList = equipmentList;
//        for (String ip : equipmentList) {
//            int antNum = 15;
//            //根据ip获取设备信息
//            String str = ip.substring(0, ip.indexOf(":"));
//            if (Base.notEmpty(str)) {
//                LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                wrappers.eq(Equipment::getIp, str);
//                //读写一体机
//                wrappers.eq(Equipment::getEquipment_type, 3);
//                wrappers.eq(Equipment::getIs_del, 0);
//                List<Equipment> equipment2 =  equipmentService.selectList(wrappers);
//                if (!CollectionUtils.isEmpty(equipment2)) {
//                    antNum = 1;
//                }
//            }
//            int count = RFIDReader._Tag6C.GetEPC(ip, antNum, eReadType.Inventory);
//            log.debug("RFIDReader._Tag6C.GetEPC: " + count);
//        }
//
//        try {
////            System.out.println("RFIDReader:开始读取数据的时间：" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            Thread.sleep(2 * 1000); //暂停20秒
//            //停止检测
//            for (String ip : CallBack.carryAlarmList) {
//                //停止循环读请使用“停止指令”
//                RFIDReader._Config.Stop(ip);
//            }
//            Thread.sleep(1 * 1000); //暂停2秒
////            System.out.println("RFIDReader:停止读取数据的时间：" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            if (CallBack.carryAlarmList.size() > 0 && CallBack.carryAlarmEpc.size() > 0) {
//                abOnSearchAll();//扫描所有设备
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * ab门判断
//     * 扫描所有标签EPC
//     */
//    @Synchronized
//    public void abOnSearchAll() {
//        //报警设备检测到的EPC集合
//        log.info("报警设备-----AB门------检测到的EPC集合++++++++++++++++++++++++当前时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(CallBack.carryAlarmEpc);
//
//        try {
//            List<CarryWhite> whites = carryWhiteService.selectList(Wrappers.<CarryWhite>lambdaQuery()
//                    .eq(CarryWhite::getIs_del, G.ISDEL_NO).eq(CarryWhite::getWhite_flag, G.ISDEL_YES)
//            );
//            List<String> whiteList = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(whites)) {
//                // rfid 去重, 转小写,去除前后空格
//                whiteList = whites.stream().map(CarryWhite::getRfid).distinct().map(String::toLowerCase).map(String::trim).collect(Collectors.toList());
//            }
//            log.info("查询白名单内容:" + whiteList);
//            Thread.sleep(2 * 1000); //暂停5秒
//            //停止检测
//            for (String ip : CallBack.cxList) {
//                //停止循环读请使用“停止指令”
//                RFIDReader._Config.Stop(ip);
//            }
////            System.out.println("扫描所有标签EPC++++++++++++++++++++++++当前时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            System.out.println(CallBack.cxEpc);
//            //查询资产
//            for (String ip : CallBack.carryAlarmEpc.keySet()) {
//                //根据ip获取设备信息
//                String str = ip.substring(0, ip.indexOf(":"));
//                Equipment equipment2 = new Equipment();
//                if (Base.notEmpty(str)) {
//                    LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                    wrappers.eq(Equipment::getIp, str);
//                    wrappers.eq(Equipment::getIs_del, 0);
//
//                    List<Equipment> a = equipmentService.selectList(wrappers);
//                    if (!CollectionUtils.isEmpty(a)) {
//                        equipment2 = a.get(0);
//                    }
////                    equipment2 = equipmentService.selectOne(wrappers);
//                }
//                for (String epc : CallBack.carryAlarmEpc.get(ip)) {
//                    // 判断epc 是否在白名单中,如果在,则跳过
//                    if (!CollectionUtils.isEmpty(whiteList) && whiteList.contains(epc.toLowerCase().trim())){
//                        continue;
//                    }
//                    // 根据EPC-完成状态-ab门状态 ,查询资产采集表
//                    List<AssetsInAndOutCollect> records = inAndOutCollectMapper.selectList(Wrappers.<AssetsInAndOutCollect>query()
//                            .eq("assetepc", epc)
//                            .eq("isdel", "0")
//                            .orderByDesc("update_time")
//                    );
//                    if(CollectionUtils.isEmpty(records)){
//                        //生成出入资产采集表记录
//                        AssetsInAndOutCollect assetsInAndOutCollect = new AssetsInAndOutCollect();
//                        assetsInAndOutCollect.setAssetepc(epc);
//                        // 根据epc得到资产信息
//                        List<AssetsManage> assetsManages = assetsManageService.selectList(Wrappers.<AssetsManage>lambdaQuery().eq(AssetsManage::getRfid, epc)
//                                .eq(AssetsManage::getIs_del,"0"));
//                        if(CollectionUtils.isEmpty(assetsManages)){
//                            continue;
//                        }
//                        AssetsManage assetsManage = assetsManages.get(0);
//                        // 新增采集表信息
//                        assetsInAndOutCollect.setAssetcode(assetsManage.getAsset_code());
//                        assetsInAndOutCollect.setAssetname(assetsManage.getAsset_name());
//                        assetsInAndOutCollect.setAssetid(assetsManage.getId());
//                        assetsInAndOutCollect.setEquipmentid(equipment2.getId());
//                        // 新增ab门判断
//                        assetsInAndOutCollect.setAbstatus(equipment2.getAb_type());
//                        // 新增更新日期
//                        assetsInAndOutCollect.setUpdatetime(new Date());
//                        assetsInAndOutCollect.setCreateuser(1);
//                        assetsInAndOutCollect.setCreatetime(new Date());
//                        assetsInAndOutCollect.setIsdel("0");
//                        inAndOutCollectMapper.insert(assetsInAndOutCollect);
//                    }else{
//                        //判断是否有进出记录,得到最新的一条记录
//                        AssetsInAndOutCollect assetsInAndOutCollect = records.get(0);
//                        // 判断原记录是否是A门
//                        if(assetsInAndOutCollect.getAbstatus() == 1){
//                            // 判断当前设备是否是B门
//                            if(equipment2.getAb_type() == 2){
//                                //生成资产外出记录
//                                AssetsAccessRecord assetsAccessRecord = new AssetsAccessRecord();
//                                assetsAccessRecord.setAssetepc(epc);
//                                //进出状态 1进 2出
//                                assetsAccessRecord.setAccessstatus("2");
//                                assetsAccessRecordService.onSave(assetsAccessRecord, equipment2);
//                                System.out.println("生成AB门资产外出记录***********************");
//                            }
//                        }else {
//                            // 否则是B门
//                            // 判断当前设备是否是A门
//                            if(equipment2.getAb_type() == 1){
//                                //生成资产外出记录
//                                AssetsAccessRecord assetsAccessRecord = new AssetsAccessRecord();
//                                assetsAccessRecord.setAssetepc(epc);
//                                //进出状态 1进 2出
//                                assetsAccessRecord.setAccessstatus("1");
//                                assetsAccessRecordService.onSave(assetsAccessRecord, equipment2);
//                                System.out.println("生成AB门资产进入记录***********************");
//                            }
//                        }
//                        // 更新出入资产采集表记录,更新时间
//                        assetsInAndOutCollect.setAbstatus(equipment2.getAb_type());
//                        assetsInAndOutCollect.setUpdatetime(new Date());
//                        assetsInAndOutCollect.setCreatetime(new Date());
//                        inAndOutCollectMapper.updateById(assetsInAndOutCollect);
//                        System.out.println("采集AB门更新时间变更***********************");
//                    }
//                }
//            }
//            //结束
//            CallBack.carryAlarmEpc.clear();
//            CallBack.carryAlarmList.clear();
//            CallBack.cxList.clear();
//            CallBack.cxEpc.clear();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    /**
//     * 外携进出判断
//     * 10分钟执行一次
//     * 进出记录/未授权外携报警
//     */
////    @Scheduled(cron = "0 */1 * * * ?")
//    @Synchronized
//    public void inOutTimeAccessRecord() {
//        //添加读卡类型：外携告警
//        CallBack.readType.add(G.RFID_READ_CARRY_ALARM);
//        //获取报警设备列表
//        Equipment equipment = new Equipment();
//        //用处 【字典 1扫描 2报警】
//        equipment.setEquipment_usage(2);
//        List<String> equipmentList = equipmentService.getIpList(equipment);
//        //外携报警设备IP列表集合
//        CallBack.carryAlarmList = equipmentList;
//        for (String ip : equipmentList) {
//            int antNum = 15;
//            //根据ip获取设备信息
//            String str = ip.substring(0, ip.indexOf(":"));
//            if (Base.notEmpty(str)) {
//                LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                wrappers.eq(Equipment::getIp, str);
//                //读写一体机
//                wrappers.eq(Equipment::getEquipment_type, 3);
//                wrappers.eq(Equipment::getIs_del, 0);
//                List<Equipment> equipment2 =  equipmentService.selectList(wrappers);
//                if (!CollectionUtils.isEmpty(equipment2)) {
//                    antNum = 1;
//                }
//            }
//            int count = RFIDReader._Tag6C.GetEPC(ip, antNum, eReadType.Inventory);
//            log.debug("RFIDReader._Tag6C.GetEPC: " + count);
//        }
//
//        try {
////            System.out.println("RFIDReader:开始读取数据的时间：" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            Thread.sleep(2 * 1000); //暂停20秒
//            //停止检测
//            for (String ip : CallBack.carryAlarmList) {
//                //停止循环读请使用“停止指令”
//                RFIDReader._Config.Stop(ip);
//            }
//            Thread.sleep(1 * 1000); //暂停2秒
////            System.out.println("RFIDReader:停止读取数据的时间：" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            if (CallBack.carryAlarmList.size() > 0 && CallBack.carryAlarmEpc.size() > 0) {
//                inOUtOnSearchAll();//扫描所有设备
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 外携进出判断
//     * 扫描所有标签EPC
//     */
//    @Synchronized
//    public void inOUtOnSearchAll() {
//        //报警设备检测到的EPC集合
//        log.info("报警设备检测到的EPC集合++++++++++++++++++++++++当前时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(CallBack.carryAlarmEpc);
//
//        try {
//            List<CarryWhite> whites = carryWhiteService.selectList(Wrappers.<CarryWhite>lambdaQuery()
//                    .eq(CarryWhite::getIs_del, G.ISDEL_NO).eq(CarryWhite::getWhite_flag, G.ISDEL_YES)
//            );
//            List<String> whiteList = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(whites)) {
//                // rfid 去重, 转小写,去除前后空格
//                whiteList = whites.stream().map(CarryWhite::getRfid).distinct().map(String::toLowerCase).map(String::trim).collect(Collectors.toList());
//            }
//            log.info("查询白名单内容:" + whiteList);
//            Thread.sleep(2 * 1000); //暂停5秒
//            //停止检测
//            for (String ip : CallBack.cxList) {
//                //停止循环读请使用“停止指令”
//                RFIDReader._Config.Stop(ip);
//            }
////            System.out.println("扫描所有标签EPC++++++++++++++++++++++++当前时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//            System.out.println(CallBack.cxEpc);
//            //查询资产
//            for (String ip : CallBack.carryAlarmEpc.keySet()) {
//                //根据ip获取设备信息
//                String str = ip.substring(0, ip.indexOf(":"));
//                Equipment equipment2 = new Equipment();
//                if (Base.notEmpty(str)) {
//                    LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                    wrappers.eq(Equipment::getIp, str);
//                    wrappers.eq(Equipment::getIs_del, 0);
//                    equipment2 = equipmentService.selectOne(wrappers);
//                }
//                for (String epc : CallBack.carryAlarmEpc.get(ip)) {
//                    // 判断epc 是否在白名单中,如果在,则跳过
//                    if (!CollectionUtils.isEmpty(whiteList) && whiteList.contains(epc.toLowerCase().trim())){
//                        continue;
//                    }
////                    //判断是否有进出记录,得到最新的一条记录
////                    List<AssetsAccessRecord> records = assetsAccessRecordMapper.selectList(Wrappers.<AssetsAccessRecord>lambdaQuery()
////                            .eq(AssetsAccessRecord::getAsset_epc, epc)
////                            .eq(AssetsAccessRecord::getIs_del, "0")
////                            .orderByDesc(AssetsAccessRecord::getUpdate_time)
////                    );
//
//                    /**
//                     * 进出状态 1进 2出
//                     * access_status
//                     */
////                    if(records.size() == 0){
//                        //生成资产外出记录
//                        AssetsAccessRecord assetsAccessRecord = new AssetsAccessRecord();
//                        assetsAccessRecord.setAssetepc(epc);
//                        //进出状态 1进 2出
//                        assetsAccessRecord.setAccessstatus("2");
//                        assetsAccessRecordService.onSave(assetsAccessRecord, equipment2);
//                        System.out.println("生成资产外出记录***********************");
////                    }else {
////                        if(records.get(0).getAccess_status().equals("2")){
////                            //生成资产外出记录
////                            AssetsAccessRecord assetsAccessRecord = new AssetsAccessRecord();
////                            assetsAccessRecord.setAsset_epc(epc);
////                            //进出状态 1进 2出
////                            assetsAccessRecord.setAccess_status("1");
////                            assetsAccessRecordService.onSave(assetsAccessRecord, equipment2);
////                            System.out.println("生成资产进入记录***********************");
////                        } else {
////                            //生成资产外出记录
////                            AssetsAccessRecord assetsAccessRecord = new AssetsAccessRecord();
////                            assetsAccessRecord.setAsset_epc(epc);
////                            //进出状态 1进 2出
////                            assetsAccessRecord.setAccess_status("2");
////                            assetsAccessRecordService.onSave(assetsAccessRecord, equipment2);
////                            System.out.println("生成资产外出记录***********************");
////                        }
////                    }
//                }
//            }
//            //结束
//            CallBack.carryAlarmEpc.clear();
//            CallBack.carryAlarmList.clear();
//            CallBack.cxList.clear();
//            CallBack.cxEpc.clear();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 扫描所有标签EPC
//     */
//    public void onSearchAll() {
//        //报警设备检测到的EPC集合
//        System.out.println("报警设备检测到的EPC集合++++++++++++++++++++++++");
//        System.out.println(CallBack.carryAlarmEpc);
//
//        //添加读卡类型：扫描所有设备
//        CallBack.readType.add(G.RFID_READ_SEARCH);
//        //获取扫描查询设备列表
//        Equipment equipment = new Equipment();
//        //用处 【字典 1扫描 2报警】
//        equipment.setEquipment_usage(1);
//        List<String> equipmentList = equipmentService.getIpList(equipment);
//        //扫描查询设备IP列表集合
//        CallBack.cxList = equipmentList;
//        for (String ip : equipmentList) {
//            int antNum = 15;
//            //根据ip获取设备信息
//            String str = ip.substring(0, ip.indexOf(":"));
//            if (Base.notEmpty(str)) {
//                LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                wrappers.eq(Equipment::getIp, str);
//                //读写一体机
//                wrappers.eq(Equipment::getEquipment_type, 3);
//                wrappers.eq(Equipment::getIs_del, 0);
//                List<Equipment> equipment2list = equipmentService.selectList(wrappers);
//                if (!CollectionUtils.isEmpty(equipment2list)) {
//                    antNum = 1;
//                }
//            }
//            int count = RFIDReader._Tag6C.GetEPC(ip, antNum, eReadType.Inventory);
//            System.out.println(count);
//        }
//
//        try {
//            Thread.sleep(10 * 1000); //暂停5秒
//            //停止检测
//            for (String ip : CallBack.cxList) {
//                //停止循环读请使用“停止指令”
//                RFIDReader._Config.Stop(ip);
//            }
//            List<CarryWhite> whites = carryWhiteService.selectList(Wrappers.<CarryWhite>lambdaQuery()
//                    .eq(CarryWhite::getIs_del, G.ISDEL_NO).eq(CarryWhite::getWhite_flag, G.ISDEL_YES)
//            );
//            List<String> whiteList = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(whites)) {
//                // rfid 去重, 转小写,去除前后空格
//                whiteList = whites.stream().map(CarryWhite::getRfid).distinct().map(String::toLowerCase).map(String::trim).collect(Collectors.toList());
//            }
//            log.info("查询白名单内容:" + whiteList);
//            System.out.println("扫描所有标签EPC++++++++++++++++++++++++");
//            System.out.println(CallBack.cxEpc);
//            //查询资产
//            for (String ip : CallBack.carryAlarmEpc.keySet()) {
//                //根据ip获取设备信息
//                String str = ip.substring(0, ip.indexOf(":"));
//                Equipment equipment2 = new Equipment();
//                if (Base.notEmpty(str)) {
//                    LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                    wrappers.eq(Equipment::getIp, str);
//                    wrappers.eq(Equipment::getIs_del, 0);
//                    List<Equipment> a = equipmentService.selectList(wrappers);
//                    if (!CollectionUtils.isEmpty(a)){
//                        equipment2 = a.get(0);
//                    }
//                }
//                for (String epc : CallBack.carryAlarmEpc.get(ip)) {
//                    // 判断epc 是否在白名单中,如果在,则跳过
//                    if (!CollectionUtils.isEmpty(whiteList) && whiteList.contains(epc.toLowerCase().trim())){
//                        continue;
//                    }
//
//                    if (CallBack.cxEpc.contains(epc)) {
//                        //生成资产进入记录
//                        AssetsAccessRecord assetsAccessRecord = new AssetsAccessRecord();
//                        assetsAccessRecord.setAssetepc(epc);
//                        //进出状态 1进 2出
//                        assetsAccessRecord.setAccessstatus("1");
//                        assetsAccessRecordService.onSave(assetsAccessRecord, equipment2);
//                        System.out.println("生成资产进入记录***********************");
//                    } else {
//                        //生成资产外出记录
//                        AssetsAccessRecord assetsAccessRecord = new AssetsAccessRecord();
//                        assetsAccessRecord.setAssetepc(epc);
//                        //进出状态 1进 2出
//                        assetsAccessRecord.setAccessstatus("2");
//                        assetsAccessRecordService.onSave(assetsAccessRecord, equipment2);
//                        System.out.println("生成资产外出记录***********************");
//                    }
//                }
//            }
//            //结束
//            CallBack.carryAlarmEpc.clear();
//            CallBack.carryAlarmList.clear();
//            CallBack.cxList.clear();
//            CallBack.cxEpc.clear();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 10分钟执行一次
//     * 定时扫描
//     * 位置变动
//     */
////    @Scheduled(cron = "0 */1 * * * ?")
//    public void onTimingScanning() {
//        //添加读卡类型：定时扫描
//        CallBack.readType.add(G.RFID_READ_SCANNING);
//        //获取扫描查询设备列表
//        Equipment equipment = new Equipment();
//        //用处 【字典 1扫描 2报警】
//        equipment.setEquipment_usage(1);
//        List<String> equipmentList = equipmentService.getIpList(equipment);
//        //扫描查询设备IP列表集合
//        CallBack.searchList = equipmentList;
//        for (String ip : equipmentList) {
//            int antNum = 15;
//            //根据ip获取设备信息
//            String str = ip.substring(0, ip.indexOf(":"));
//            if (Base.notEmpty(str)) {
//                LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                wrappers.eq(Equipment::getIp, str);
//                //读写一体机
//                wrappers.eq(Equipment::getEquipment_type, 3);
//                wrappers.eq(Equipment::getIs_del, 0);
////                Equipment equipment2 = equipmentService.selectOne(wrappers);
//                if (equipmentService.selectCount(wrappers) > 0) {
//                    antNum = 1;
//                }
//            }
//            int count = RFIDReader._Tag6C.GetEPC(ip, antNum, eReadType.Inventory);
//            System.out.println(count);
//        }
//
//        try {
//            List<CarryWhite> whites = carryWhiteService.selectList(Wrappers.<CarryWhite>lambdaQuery()
//                    .eq(CarryWhite::getIs_del, G.ISDEL_NO).eq(CarryWhite::getWhite_flag, G.ISDEL_YES)
//            );
//            List<String> whiteList = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(whites)) {
//                // rfid 去重, 转小写,去除前后空格
//                whiteList = whites.stream().map(CarryWhite::getRfid).distinct().map(String::toLowerCase).map(String::trim).collect(Collectors.toList());
//            }
//            log.info("查询白名单内容:" + whiteList);
//            Thread.sleep(10 * 1000); //暂停5秒
//            //停止检测
//            for (String ip : CallBack.searchList) {
//                //停止循环读请使用“停止指令”
//                RFIDReader._Config.Stop(ip);
//            }
//            Thread.sleep(2 * 1000); //暂停2秒
//            System.out.println("定时扫描位置变动++++++++++++++++++++++++");
//            System.out.println(CallBack.searchEpc);
//            //查询资产
//            for (String ip : CallBack.searchEpc.keySet()) {
//                //根据ip获取设备信息
//                String str = ip.substring(0, ip.indexOf(":"));
//                Equipment equipment2 = null;
//                if (Base.notEmpty(str)) {
//                    LambdaQueryWrapper<Equipment> wrappers = Wrappers.<Equipment>lambdaQuery();
//                    wrappers.eq(StringUtils.isNotEmpty(str), Equipment::getIp, str);
//                    wrappers.eq(Equipment::getIs_del, 0);
//                    List<Equipment> a = equipmentService.selectList(wrappers);
//                    if (!CollectionUtils.isEmpty(a)){
//                        equipment2 = a.get(0);
//                    }
//                }
//                for (String epc : CallBack.searchEpc.get(ip)) {
//                    // 判断epc 是否在白名单中,如果在,则跳过
//                    if (!CollectionUtils.isEmpty(whiteList) && whiteList.contains(epc.toLowerCase().trim())){
//                        continue;
//                    }
//                    //根据资产epc标签获取资产信息
//                    LambdaQueryWrapper<AssetsManage> wrappers2 = Wrappers.<AssetsManage>lambdaQuery();
//                    wrappers2.eq(StringUtils.isNotEmpty(epc), AssetsManage::getRfid, epc);
//                    wrappers2.eq(AssetsManage::getIs_del, 0);
//                    List<AssetsManage> a = assetsManageService.selectList(wrappers2);
//                    AssetsManage assetsManage = CollectionUtils.isEmpty(a) ? null : a.get(0);
//                    if (Base.notEmpty(equipment2) && Base.notEmpty(assetsManage)) {
//                        //与历史位置做对比
//                        LambdaQueryWrapper<AssetsTrajectory> wrappers3 = Wrappers.<AssetsTrajectory>lambdaQuery();
//                        wrappers3.eq(StringUtils.isNotEmpty(epc), AssetsTrajectory::getAsset_epc, epc);
////                        wrappers3.eq(Base.notEmpty(assetsManage.getPosition_id()), AssetsTrajectory::getPosition_id, assetsManage.getPosition_id());
////                        wrappers3.eq(Base.notEmpty(equipment2.getEquipment_position()), AssetsTrajectory::getChange_position_id, equipment2.getEquipment_position());
//                        wrappers3.eq(AssetsTrajectory::getIs_del, 0);
//                        wrappers3.orderByDesc(AssetsTrajectory::getUpdate_time, AssetsTrajectory::getId);
//                        wrappers3.last("LIMIT 1");
//                        AssetsTrajectory assetsTrajectory = assetsTrajectoryService.selectOne(wrappers3);
//                        //若最后一次轨迹变动位置和当前位置一致，则更新时间，否则生成新的轨迹记录
//                        if (Base.notEmpty(assetsTrajectory) && assetsTrajectory.getChange_position_id().equals(Objects.requireNonNull(equipment2).getEquipment_position())) {
//                            assetsTrajectory.setUpdate_time(new Date());
//                            assetsTrajectoryService.updateById(assetsTrajectory);
//                        } else {
//                            assetsTrajectory = new AssetsTrajectory();
//                            assetsTrajectory.setAsset_epc(epc);
//                            //存放位置
//                            if (assetsManage != null) {
//                                assetsTrajectory.setPosition_id(assetsManage.getPosition_id());
//                            }
//                            //变更位置
//                            assetsTrajectory.setChange_position_id(Objects.requireNonNull(equipment2).getEquipment_position());
//                            assetsTrajectory.setEquipment_id(Objects.requireNonNull(equipment2).getId());
//                            // 资产id
//                            if (assetsManage != null) {
//                                assetsTrajectory.setAsset_id(assetsManage.getId());
//                                // 资产编号
//                                assetsTrajectory.setAsset_code(assetsManage.getAsset_code());
//                                // 资产名称
//                                assetsTrajectory.setAsset_name(assetsManage.getAsset_name());
//                                assetsTrajectory.setUser_id(assetsManage.getUse_people());
//                            }
//                            assetsTrajectoryService.onSave(assetsTrajectory);
//
//                            //更新资产表位置变动时间
//                            assetsManage.setPosition_change_time(new Date());
//                            assetsManageService.updateById(assetsManage);
//                            System.out.println("生成位置变动数据******************************");
//
//                            //若读卡设备位置和资产位置不同，则生成位置变动记录
//                            if (!equipment2.getEquipment_position().equals(assetsManage.getPosition_id())) {
//                                //生成位置变动报警记录
//                                CarryAbnormal carryAbnormal = new CarryAbnormal();
//                                carryAbnormal.setRfid(assetsManage.getRfid());
//                                //资产id
//                                carryAbnormal.setAssets_id(assetsManage.getId());
//                                //资产编号
//                                carryAbnormal.setAssetnumber(assetsManage.getAsset_code());
//                                //规格型号
//                                carryAbnormal.setSpecification(assetsManage.getSpecification());
//                                //资产名称
//                                carryAbnormal.setAssetname(assetsManage.getAsset_name());
//                                //存放部门
//                                carryAbnormal.setDept_id(assetsManage.getDept_id());
//                                //存放位置
//                                carryAbnormal.setPosition_id(assetsManage.getPosition_id());
//                                //变更位置
//                                carryAbnormal.setChange_position_id(equipment2.getEquipment_position());
//                                carryAbnormal.setDevice_id(equipment2.getId());
//                                //是否处理报警异常 0否（默认） 1是
//                                carryAbnormal.setIs_handle("0");
//                                //异常状态：1 未授权外携告警 2 位置变换告警 3 标签损毁告警
//                                carryAbnormal.setError_status("2");
//                                carryAbnormalService.onChangeSave(carryAbnormal);
//
//                                System.out.println("生成报警数据******************************");
//                                //设备更新为报警状态
//                                //报警状态 1正常 2报警
//                                equipment2.setWarning_status(2);
//                                equipmentService.updateById(equipment2);
//                                System.out.println("设备更新为报警状态******************************");
//                            }
//                        }
//                    }
//                }
//            }
//            //结束
//            CallBack.searchList.clear();
//            CallBack.searchEpc.clear();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
