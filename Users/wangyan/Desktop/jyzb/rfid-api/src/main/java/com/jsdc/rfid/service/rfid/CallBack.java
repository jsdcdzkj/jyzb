//package com.jsdc.rfid.service.rfid;
//
//import com.jsdc.core.base.Base;
//import com.jsdc.rfid.common.G;
//import com.rfidread.Interface.IAsynchronousMessage;
//import com.rfidread.Interface.ISearchDevice;
//import com.rfidread.Models.Device_Model;
//import com.rfidread.Models.GPI_Model;
//import com.rfidread.Models.Tag_Model;
//import com.rfidread.RFIDReader;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.*;
//
//@Slf4j
//public class CallBack implements IAsynchronousMessage, ISearchDevice {
//
//    static String ConnID = "";//设备ip地址和端口号
//
//    static Set<String> readType = new HashSet<>();//读卡任务类型
//
//    //盘点 设备列表
//    static Set<String> pdList = new HashSet<>();
//    //盘点 标签数据列表
//    static Set<String> pdEpc = new HashSet<>();
//    //扫描查询 设备列表
//    static List<String> cxList = new ArrayList<>();
//    //扫描查询 标签数据列表
//    static Set<String> cxEpc = new HashSet<>();
//    //外携报警 设备列表 进出记录
//    static List<String> carryAlarmList = new ArrayList<>();
//    //外携报警 标签数据列表 进出记录
//    static Map<String, Set<String>> carryAlarmEpc = new HashMap<>();
//
//    //定时扫描查询 设备列表
//    static List<String> searchList = new ArrayList<>();
//    //定时扫描查询 标签数据列表
//    static Map<String, Set<String>> searchEpc = new HashMap<>();
//
//    public static Set<String> getReadType() {
//        return readType;
//    }
//
//    public static void setReadType(Set<String> readType) {
//        CallBack.readType = readType;
//    }
//
//    public static Set<String> getPdList() {
//        return pdList;
//    }
//
//    public static void setPdList(Set<String> pdList) {
//        CallBack.pdList = pdList;
//    }
//
//    public static Set<String> getPdEpc() {
//        return pdEpc;
//    }
//
//    public static void setPdEpc(Set<String> pdEpc) {
//        CallBack.pdEpc = pdEpc;
//    }
//
//    public static List<String> getCxList() {
//        return cxList;
//    }
//
//    public static void setCxList(List<String> cxList) {
//        CallBack.cxList = cxList;
//    }
//
//    public static Set<String> getCxEpc() {
//        return cxEpc;
//    }
//
//    public static void setCxEpc(Set<String> cxEpc) {
//        CallBack.cxEpc = cxEpc;
//    }
//
//    public static List<String> getCarryAlarmList() {
//        return carryAlarmList;
//    }
//
//    public static void setCarryAlarmList(List<String> carryAlarmList) {
//        CallBack.carryAlarmList = carryAlarmList;
//    }
//
//    public static Map<String, Set<String>> getCarryAlarmEpc() {
//        return carryAlarmEpc;
//    }
//
//    public static void setCarryAlarmEpc(Map<String, Set<String>> carryAlarmEpc) {
//        CallBack.carryAlarmEpc = carryAlarmEpc;
//    }
//
//    public static List<String> getSearchList() {
//        return searchList;
//    }
//
//    public static void setSearchList(List<String> searchList) {
//        CallBack.searchList = searchList;
//    }
//
//    public static Map<String, Set<String>> getSearchEpc() {
//        return searchEpc;
//    }
//
//    public static void setSearchEpc(Map<String, Set<String>> searchEpc) {
//        CallBack.searchEpc = searchEpc;
//    }
//
//    static {
////        cxList.add("192.168.0.112");
////        cxList.add("192.168.0.110");
//    }
//
//    public CallBack() {
//
//    }
//
//    public void DebugMsg(String var1) {
//        System.out.println(var1);
//    }
//
//    public void DeviceInfo(Device_Model var1) {
//        System.out.println(var1);
//    }
//
//    public void GPIControlMsg(GPI_Model var1) {
//        System.out.println(var1);
//    }
//
//    public void OutPutScanData(byte[] var1) {
//        System.out.println(var1);
//    }
//
//    public void OutPutTags(Tag_Model var1) {
////        String ip = var1._ReaderName.substring(0, var1._ReaderName.indexOf(":"));
//        String ip = var1._ReaderName;
//        //判断是否为盘点数据
//        if (pdList.contains(ip)) {
//            //todo
//            pdEpc.add(var1._EPC);
//        }
//        //判断是否为查询数据
//        if (cxList.contains(ip)) {
//            //todo
//            cxEpc.add(var1._EPC);
//        }
//        //判断是否为进出记录/外携报警数据
//        if (carryAlarmList.contains(ip)) {
//            Set<String> stringList = new HashSet<>();
//            //根据设备IP获取map集合，若无数据，则新建list集合put
//            if (Base.empty(carryAlarmEpc.get(ip))){
//                stringList.add(var1._EPC);
//                carryAlarmEpc.put(ip,stringList);
//            } else {
//                //否则取出之前集合数据，新增一条epc记录
//                stringList = carryAlarmEpc.get(ip);
//                stringList.add(var1._EPC);
//                carryAlarmEpc.put(ip,stringList);
//            }
//        }
//        //定时扫描资产
//        if (searchList.contains(ip)) {
//            Set<String> stringList = new HashSet<>();
//            //根据设备IP获取map集合，若无数据，则新建list集合put
//           if (Base.empty(searchEpc.get(ip))){
//               stringList.add(var1._EPC);
//               searchEpc.put(ip,stringList);
//           } else {
//               //否则取出之前集合数据，新增一条epc记录
//               stringList = searchEpc.get(ip);
//               stringList.add(var1._EPC);
//               searchEpc.put(ip,stringList);
//           }
//        }
//        log.debug("EPC：" + var1._EPC + " ReaderSN:" + var1._ReaderSN + " Userdata:" + var1._UserData + " ReaderName：" + var1._ReaderName);
//    }
//
//    public void OutPutTagsOver() {
//        log.debug("OutPutTagsOver");
//        RfidService rfidService = new RfidService();
//        //读取结束 回调方法
//        if (readType.contains(G.RFID_READ_CHECK)) {
//            //资产盘点
//            rfidService.getInventoryList(getPdEpc());
//            readType.remove(G.RFID_READ_CHECK);
//        }
//        if (readType.contains(G.RFID_READ_SEARCH)) {
//            //扫描查询
//            RfidService.getList3();
//            readType.remove(G.RFID_READ_SEARCH);
//        }
//        if (readType.contains(G.RFID_READ_CARRY_ALARM)) {
//            //外携报警 进出记录
////            rfidService.getList4();
//            readType.remove(G.RFID_READ_CARRY_ALARM);
//        }
//        if (readType.contains(G.RFID_READ_SCANNING)) {
//            //定时扫描
////            rfidService.getList4();
//            readType.remove(G.RFID_READ_SCANNING);
//        }
//    }
//
//    public void PortClosing(String var1) {
//        System.out.println(var1);
//    }
//
//    //心跳
//    public void PortConnecting(String var1) {
//        log.debug(var1);
//        if (RFIDReader.GetServerStartUp()) {
//            System.out.println("A reader connected to this server: " + var1);
//            ConnID = var1;
//        }
//
//    }
//
//    public void WriteDebugMsg(String var1) {
//        System.out.println(var1);
//    }
//
//    public void WriteLog(String var1) {
//        System.out.println(var1);
//    }
//}
