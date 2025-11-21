//package com.jsdc.rfid.Bean;
//
//import cn.hutool.log.Log;
//import cn.hutool.log.LogFactory;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.jsdc.rfid.mapper.RFIDConfigMapper;
//import com.jsdc.rfid.model.RFIDConfig;
//import com.jsdc.rfid.model.SysCron;
//import com.jsdc.rfid.service.EquipmentService;
//import com.jsdc.rfid.service.SysCronService;
//import com.jsdc.rfid.service.rfid.RfidService;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.SchedulingConfigurer;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.scheduling.config.ScheduledTaskRegistrar;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.Executors;
//
///**
// * 定时任务 配合数据库动态执行
// */
//@Lazy(false)
//@Component("CompleteScheduleConfig")
//@Configuration
//@EnableScheduling
//public class CompleteScheduleConfig implements SchedulingConfigurer {
//    Log log = LogFactory.get(CompleteScheduleConfig.class);
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
//    private RFIDConfigMapper rfidConfigMapper;
//
//    @Autowired
//    private EquipmentService equipmentService;
//
//    /**
//     * 项目运行自启
//     * 执行定时任务.
//     */
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        log.info(">>>>>>>>>>>>>>>>>>>>开始执行定时任务初始化");
//
//        //设定一个长度10的定时任务线程池
//        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
//
//        log.info("线程开始时间：" + LocalDateTime.now());
//        //2.1 从数据库获取执行周期
//        List<SysCron> sysCronList = sysCronService.getList(null);
//        for (SysCron sysCron : sysCronList) {
//            taskRegistrar.addTriggerTask(
//                    //1.添加任务内容(Runnable)
//                    () -> System.out.println("执行定时任务: " + sysCron.getType() + " " + sysCron.getBz() + " " + LocalDateTime.now().toLocalTime()),
//                    //2.设置执行周期(Trigger)
//                    triggerContext -> {
//                        //2.2 合法性校验.
//                        if (StringUtils.isEmpty(sysCron.getCorn())) {
//                            // Omitted Code ..
//                            return null;
//                        }
//                        // 1.判断是否是AB门检测资产
//                        List<RFIDConfig> configs = rfidConfigMapper.selectList(Wrappers.<RFIDConfig>lambdaQuery().eq(RFIDConfig::getIs_del, "0"));
//                        RFIDConfig config = null;
//                        if (CollectionUtils.isEmpty(configs)) {
//                            // 如果RFID配置表为空，则创建默认配置
//                            config = RFIDConfig.builder().isab(1).create_time(new Date()).name("RFID配置").build();
//                            rfidConfigMapper.insert(config);
//                        }else{
//                            config = configs.get(0);
//                        }
//
////                        // 判断初始化tcp状态没有启动的设备，有没有安装好，如果安装完成，则进行初始化任务.
////                        List<Equipment> equipmentList = equipmentService.selectList(Wrappers.<Equipment>lambdaQuery()
////                                .eq(Equipment::getStatus, 0).eq(Equipment::getIs_del, G.ISDEL_NO)
////                        );
////                        if(!CollectionUtils.isEmpty(equipmentList)){
////                            for (Equipment x : equipmentList){
////                                //停止循环读请使用“停止指令”
////                                RFIDReader._Config.Stop(x.getIp() + ":" + x.getPort());
////
////                                //设备作为客户端，采用监听的方式
////                                if (Objects.equals(x.getConnect_type(), G.EQUIPMENT_CONNECT_CLIENT)) {
////                                    rfidService.openTcpServer(x.getIp(), x.getPort());
////                                } else {
////                                    if (rfidService.tcpConnect(x)) {
////                                        //在线
////                                        x.setStatus(G.EQUIPMENT_ONLINE);
////                                        x.setUpdate_time(new Date());
////                                        equipmentService.updateById(x);
////                                        log.info("设备:" + x.getEquipment_name() + "[ip:" + x.getIp() + ", port:" + x.getPort() + "], tcp连接成功");
////                                    } else {
////                                        //离线
////                                        x.setStatus(G.EQUIPMENT_OFFLINE);
////                                        x.setUpdate_time(new Date());
////                                        equipmentService.updateById(x);
////                                        log.info("设备:" + x.getEquipment_name() + "[ip:" + x.getIp() + ", port:" + x.getPort() + "], tcp连接失败");
////                                    }
////                                }
////                            }
////                        }
//                        // 判断是否为AB门检测资产
//                        if(config.getIsab() == 1){
//                            if (sysCron.getType().equals("3")) {
//                                //3定时扫描定位
//                                Runnable runnable = () -> {
//                                    rfidService.abTimeAccessRecord();
//                                };
//                                taskExecutor.execute(runnable);
//                            }
//                        } else if (config.getIsab() == 2) {
//                            //2定时扫描定位
//                            Runnable runnable = () -> {
//                                rfidService.inOutTimeAccessRecord();
//                            };
//                            taskExecutor.execute(runnable);
//                        } else{
//                            //类型 1报警任务 2定时扫描定位
//                            if (sysCron.getType().equals("1")) {
//                                //1报警任务
//                                Runnable runnable = () -> {
//                                    rfidService.timeAccessRecord();
//                                };
//                                taskExecutor.execute(runnable);
//                            } else if (sysCron.getType().equals("2")) {
//                                //2定时扫描定位
//                                Runnable runnable = () -> {
//                                    rfidService.onTimingScanning();
//                                };
//                                taskExecutor.execute(runnable);
//
////                            new Thread(new Runnable() {
////                                @Override
////                                public void run() {
////                                    rfidService.onTimingScanning();
////                                }
////                            }).start();
//
//                            }
//                        }
//                        //2.3 返回执行周期(Date)
//                        return new CronTrigger(sysCron.getCorn()).nextExecutionTime(triggerContext);
//                    }
//            );
//        }
//        log.info(">>>>>>>>>>>>>>>>>>>>定时任务初始化结束");
//    }
//
//}