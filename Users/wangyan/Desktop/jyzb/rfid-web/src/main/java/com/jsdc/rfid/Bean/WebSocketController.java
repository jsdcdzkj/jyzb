//package com.jsdc.rfid.Bean;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
///**
// * 长连接通讯
// * WebSocketController
// * @author zdq
// *
// */
//@Controller
//@Slf4j
//public class WebSocketController {
//
//    @Autowired
//    private SimpMessagingTemplate template;
//
//
////    @Scheduled(fixedRate = 5000)
//    public void sendMessage() {
//        try {
//            template.convertAndSendToUser("1","/query/mark", "执行任务计划表更新");
//        } catch (Exception e) {
//            log.error(e.getMessage(),e);
//        }
//    }
//
//}
