//package com.jsdc.rfid.Bean;
//
//import cn.hutool.log.Log;
//import cn.hutool.log.LogFactory;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.jsdc.rfid.common.G;
//import com.jsdc.rfid.model.Equipment;
//import com.jsdc.rfid.service.EquipmentService;
//import com.jsdc.rfid.service.SysCronService;
//import com.jsdc.rfid.service.rfid.RfidService;
//import com.rfidread.RFIDReader;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//@Component
//public class TaskSchedule {
//
//    Log log = LogFactory.get(TaskSchedule.class);
//
//    @Autowired
//    @SuppressWarnings("all")
//    private SysCronService sysCronService;
//    @Autowired
//    @SuppressWarnings("all")
//    private RfidService rfidService;
//    @Autowired
//    @SuppressWarnings("all")
//    private ThreadPoolTaskExecutor taskExecutor;
//
//    @Autowired
//    private EquipmentService equipmentService;
//
//    /**
//     * 重启设备信息
//     */
//    @Scheduled(cron = "0 0/30 * * * ?")
//    public void getToken() {
//        log.info(">>>>>>>>>>>>>>>>>>>>开始执行设备TCP重新连>>>>>>>>>>>>>>>>>>>>");
//        // 判断初始化tcp状态没有启动的设备，有没有安装好，如果安装完成，则进行初始化任务.
//        List<Equipment> equipmentList = equipmentService.selectList(Wrappers.<Equipment>lambdaQuery().eq(Equipment::getIs_del, G.ISDEL_NO));
//        if(CollectionUtils.isEmpty(equipmentList)){
//            log.info(">>>>>>>>>>>>>>>>>>>>目前没有配置的读写器设备......");
//            return;
//        }
//        for (Equipment x : equipmentList){
//            //停止循环读请使用“停止指令”
//            RFIDReader._Config.Stop(x.getIp() + ":" + x.getPort());
//
//            //设备作为客户端，采用监听的方式
//            if (Objects.equals(x.getConnect_type(), G.EQUIPMENT_CONNECT_CLIENT)) {
////                rfidService.openTcpServer(x.getIp(), x.getPort());
//            } else {
//
//                String ipPath = x.getIp() + ":" + x.getPort();
//                if (!RFIDReader.CheckConnect(ipPath)){
//                    log.info(">>>>>>>>>>>>>>>>>>>>开始执行设备(" + x.getEquipment_name() + ")TCP连接初始化");
//                    RFIDReader.CloseConn(ipPath);
//                    if (rfidService.tcpConnect(x)) {
//                        //在线
//                        x.setStatus(G.EQUIPMENT_ONLINE);
//                        x.setUpdate_time(new Date());
//                        equipmentService.updateById(x);
//                        log.info("设备:" + x.getEquipment_name() + "[ip:" + x.getIp() + ", port:" + x.getPort() + "], tcp连接成功");
//                    }else {
//                        x.setStatus(G.EQUIPMENT_OFFLINE);
//                        x.setUpdate_time(new Date());
//                        equipmentService.updateById(x);
//                        log.info("设备:" + x.getEquipment_name() + "[ip:" + x.getIp() + ", port:" + x.getPort() + "], tcp连接失败");
//                    }
//                }
//            }
//        }
//    }
//
//}
