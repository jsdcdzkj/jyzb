//package com.jsdc.rfid.vo.rfidVo;
//
//import com.rfidread.Enumeration.eReadType;
//import lombok.Data;
//
///**
// * 2.5  6C标签操作
// * 2.5.1 读标签
// */
//@Data
//public class Request251 {
//    // 只读EPC
//    // 连接标识
//    private String ConnID;
//    // 天线编号 同时指定天线1和天线2天线工作示例：3 ，详细说明请参见天线编号参数说明
//    private int antNum;
//    // single单次，Inventory循环（单次或循环）
//    private eReadType readType;
//    // accessPassword：标签访问密码(8位16进制字符串)
//    private String accessPassword;
//    // sEPC : 要匹配的EPC值(16进制字符串)
//    private String sEPC;
//    // matchWordStartIndex: 匹配数据的起始下标
//    private int matchWordStartIndex;
//
//}
