//package com.jsdc.rfid.Bean;
//
//import cn.hutool.log.Log;
//import cn.hutool.log.LogFactory;
//import com.jsdc.rfid.common.G;
//import com.jsdc.rfid.model.Equipment;
//import com.jsdc.rfid.service.EquipmentService;
//import com.jsdc.rfid.service.rfid.RfidService;
//import com.rfidread.RFIDReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//@Component("BeanDefineConfigue")
//public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent> {
//    Log log = LogFactory.get(BeanDefineConfigue.class);
//
//    @Autowired
//    private EquipmentService equipmentService;
//    @Autowired
//    private RfidService rfidService;
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        log.info(">>>>>>>>>>>>>>>>>>>>开始执行设备TCP连接初始化");
//        //项目启动时先进行设备的TCP连接
//        //首先断开所有连接
//        rfidService.closeAllConnect();
//        List<Equipment> equipmentList = equipmentService.getList(null);
//        equipmentList.forEach(x -> {
//            //停止循环读请使用“停止指令”
//            RFIDReader._Config.Stop(x.getIp() + ":" + x.getPort());
//
//            //设备作为客户端，采用监听的方式
//            if (Objects.equals(x.getConnect_type(), G.EQUIPMENT_CONNECT_CLIENT)) {
//                rfidService.openTcpServer(x.getIp(), x.getPort());
//            } else {
//                if (rfidService.tcpConnect(x)) {
//                    //在线
//                    x.setStatus(G.EQUIPMENT_ONLINE);
//                    x.setUpdate_time(new Date());
//                    equipmentService.updateById(x);
//                    log.info("设备:" + x.getEquipment_name() + "[ip:" + x.getIp() + ", port:" + x.getPort() + "], tcp连接成功");
//                } else {
//                    //离线
//                    x.setStatus(G.EQUIPMENT_OFFLINE);
//                    x.setUpdate_time(new Date());
//                    equipmentService.updateById(x);
//                    log.info("设备:" + x.getEquipment_name() + "[ip:" + x.getIp() + ", port:" + x.getPort() + "], tcp连接失败");
//                }
//            }
//        });
//        log.info(">>>>>>>>>>>>>>>>>>>>设备TCP连接初始化结束");
//    }
//}
