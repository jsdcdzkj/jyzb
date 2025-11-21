//package com.jsdc.rfid.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.jsdc.core.base.Base;
//import com.jsdc.rfid.vo.rfidVo.Request251;
//import com.jsdc.rfid.vo.rfidVo.Request252;
//import com.jsdc.rfid.vo.rfidVo.Request254;
//import com.rfidread.Interface.IAsynchronousMessage;
//import com.rfidread.RFIDReader;
//import com.rfidread.SampleCode;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * RFID 读写器接口
// */
//@Service
//@Transactional
//@SuppressWarnings("ALL")
//public class HttpClientService {
//
//    //设备
//    @Autowired
//    private EquipmentService equipmentService;
//
//    /**
//     * 2.5 6C 标签操作
//     * 2.5.1 读标签
//     */
//    public JSONObject readTag6C(String code, Request251 request251) {
//        JSONObject jsonObject = new JSONObject();
//        //rfid 读标签 接口 方法调用
//        int result = 0;
//        if (Base.notEmpty(code)) {
//            //停止循环读请使用“停止指令”
////            RFIDReader._Config.Stop(request251.getConnID());
//            if (code.equals("1")) {
//                // 只读EPC
//                result = RFIDReader._Tag6C.GetEPC(request251.getConnID(), request251.getAntNum(), request251.getReadType());
//            } else if (code.equals("2")) {
//                result = RFIDReader._Tag6C.GetEPC(request251.getConnID(), request251.getAntNum(), request251.getReadType(), request251.getAccessPassword());
//            } else if (code.equals("3")) {
//                // 匹配EPC 读EPC
//                result = RFIDReader._Tag6C.GetEPC_MatchEPC(request251.getConnID(), request251.getAntNum(), request251.getReadType(), request251.getSEPC());
//            } else if (code.equals("4")) {
//                // 匹配EPC 读EPC
//                result = RFIDReader._Tag6C.GetEPC_MatchEPC(request251.getConnID(), request251.getAntNum(), request251.getReadType(), request251.getSEPC(), request251.getMatchWordStartIndex());
//            } else if (code.equals("5")) {
//                // 匹配EPC 读EPC
//                result = RFIDReader._Tag6C.GetEPC_MatchEPC(request251.getConnID(), request251.getAntNum(), request251.getReadType(), request251.getSEPC(), request251.getMatchWordStartIndex(), request251.getAccessPassword());
//            }
//        }
//        if (result != 0) {
//            System.out.println("操作失败！");
//        } else {
//            System.out.println("操作成功！");
//        }
//        //0 操作成功， 非0 操作失败。
//        jsonObject.put("result", result);
//        return jsonObject;
//    }
//
//    /**
//     * 2.5.2 写标签
//     * 2.5.2.1 写EPC区
//     * 1.建议写标签数据的时候采用匹配TID写，即采用“函数4”和“函数5”。
//     * 2.详细返回值说明请参见附录说明
//     */
//    public JSONObject writeTag6CEPC(String code, Request252 request252) {
//        JSONObject jsonObject = new JSONObject();
//        //rfid 写标签 写EPC区 接口 方法调用
//        int result = 0;
//        if (Base.notEmpty(code)) {
//            if (code.equals("1")) {
//                result = RFIDReader._Tag6C.WriteEPC(request252.getConnID(), request252.getAntNum(), request252.getSWriteData());
//            } else if (code.equals("2")) {
//                // 匹配EPC写EPC区
//                result = RFIDReader._Tag6C.WriteEPC_MatchEPC(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getSMatchData(), request252.getMatchWordStartIndex());
//            } else if (code.equals("3")) {
//                // 匹配EPC写EPC区
//                result = RFIDReader._Tag6C.WriteEPC_MatchEPC(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getSMatchData(), request252.getMatchWordStartIndex(), request252.getAccessPassword());
//            } else if (code.equals("4")) {
//                // 匹配TID写EPC区
//                result = RFIDReader._Tag6C.WriteEPC_MatchTID(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getSMatchData(), request252.getMatchWordStartIndex());
//            } else if (code.equals("5")) {
//                // 匹配TID写EPC区
//                result = RFIDReader._Tag6C.WriteEPC_MatchTID(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getSMatchData(), request252.getMatchWordStartIndex(), request252.getAccessPassword());
//            }
//        }
//        if (result != 0) {
//            System.out.println("操作失败！");
//        } else {
//            System.out.println("操作成功！");
//        }
//        //0 操作成功， 非0 操作失败。
//        jsonObject.put("result", result);
//        return jsonObject;
//    }
//
//    /**
//     * 2.5.2 写标签
//     * 2.5.2.2 写Userdata区
//     */
//    public JSONObject writeTag6CUserData(String code, Request252 request252) {
//        JSONObject jsonObject = new JSONObject();
//        //rfid 写标签 写Userdata区 接口 方法调用
//        int result = 0;
//        if (Base.notEmpty(code)) {
//            if (code.equals("1")) {
//                result = RFIDReader._Tag6C.WriteUserData(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getOffset());
//            } else if (code.equals("2")) {
//                // 匹配EPC写EPC区
//                result = RFIDReader._Tag6C.WriteUserData_MatchEPC(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getOffset(), request252.getSMatchData(), request252.getMatchWordStartIndex());
//            } else if (code.equals("3")) {
//                // 匹配EPC写EPC区
//                result = RFIDReader._Tag6C.WriteUserData_MatchEPC(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getOffset(), request252.getSMatchData(), request252.getMatchWordStartIndex(), request252.getAccessPassword());
//            } else if (code.equals("4")) {
//                // 匹配TID写EPC区
//                result = RFIDReader._Tag6C.WriteUserData_MatchTID(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getOffset(), request252.getSMatchData(), request252.getMatchWordStartIndex());
//            } else if (code.equals("5")) {
//                // 匹配TID写EPC区
//                result = RFIDReader._Tag6C.WriteUserData_MatchTID(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getOffset(), request252.getSMatchData(), request252.getMatchWordStartIndex(), request252.getAccessPassword());
//            }
//        }
//        if (result != 0) {
//            System.out.println("操作失败！");
//        } else {
//            System.out.println("操作成功！");
//        }
//        //0 操作成功， 非0 操作失败。
//        jsonObject.put("result", result);
//        return jsonObject;
//    }
//
//    /**
//     * 2.5.2 写标签
//     * 2.5.2.3 写密码区
//     */
//    public JSONObject writeTag6CPassWord(String code, Request252 request252) {
//        JSONObject jsonObject = new JSONObject();
//        //rfid 写标签 写Userdata区 接口 方法调用
//        int result = 0;
//        if (Base.notEmpty(code)) {
//            if (code.equals("1")) {
//                // 写标签访问密码 sWriteData：密码内容（8位16进制字符串数据）
//                result = RFIDReader._Tag6C.WriteAccessPassWord(request252.getConnID(), request252.getAntNum(), request252.getSWriteData());
//            } else if (code.equals("2")) {
//                // 写标签访问密码 accessPassword：原标签访问密码（8位16进制字符串数据）
//                result = RFIDReader._Tag6C.WriteAccessPassWord(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getAccessPassword());
//            } else if (code.equals("3")) {
//                // 写标签访问密码 sMatchData：待匹配的TID数据 matchWordStartIndex：匹配数据的起始下标
//                result = RFIDReader._Tag6C.WriteAccessPassWord_MatchTID(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getSMatchData(), request252.getMatchWordStartIndex(), request252.getAccessPassword());
//            } else if (code.equals("4")) {
//                // 写标签销毁密码
//                result = RFIDReader._Tag6C.WriteDestroyPassWord(request252.getConnID(), request252.getAntNum(), request252.getSWriteData());
//            } else if (code.equals("5")) {
//                // 写标签销毁密码 accessPassword：原标签访问密码（8位16进制字符串数据）
//                result = RFIDReader._Tag6C.WriteDestroyPassWord(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getAccessPassword());
//            } else if (code.equals("6")) {
//                // 写标签销毁密码 sMatchData：待匹配的TID数据 matchWordStartIndex：匹配数据的起始下标
//                result = RFIDReader._Tag6C.WriteDestroyPassWord_MatchTID(request252.getConnID(), request252.getAntNum(), request252.getSWriteData(), request252.getSMatchData(), request252.getMatchWordStartIndex(), request252.getAccessPassword());
//            }
//        }
//        if (result != 0) {
//            System.out.println("操作失败！");
//        } else {
//            System.out.println("操作成功！");
//        }
//        //0 操作成功， 非0 操作失败。
//        jsonObject.put("result", result);
//        return jsonObject;
//    }
//
//    /**
//     * 2.5.4 灭活标签
//     * 1.建议灭活标签的时候采用匹配TID，即采用“函数3”。
//     * 2.详细返回值说明请参见附录说明
//     */
//    public JSONObject writeTag6CDestroy(String code, Request254 request254) {
//        JSONObject jsonObject = new JSONObject();
//        //rfid 写标签 写Userdata区 接口 方法调用
//        int result = 0;
//        if (Base.notEmpty(code)) {
//            if (code.equals("1")) {
//                result = RFIDReader._Tag6C.Destroy(request254.getConnID(), request254.getAntNum(), request254.getDestroyPassword());
//            } else if (code.equals("2")) {
//                result = RFIDReader._Tag6C.Destroy_MatchEPC(request254.getConnID(), request254.getAntNum(), request254.getDestroyPassword(), request254.getSMatchData(), request254.getMatchWordStartIndex());
//            } else if (code.equals("3")) {
//                result = RFIDReader._Tag6C.Destroy_MatchTID(request254.getConnID(), request254.getAntNum(), request254.getDestroyPassword(), request254.getSMatchData(), request254.getMatchWordStartIndex());
//            }
//        }
//        if (result != 0) {
//            System.out.println("操作失败！");
//        } else {
//            System.out.println("操作成功！");
//        }
//        //0 操作成功， 非0 操作失败。
//        jsonObject.put("result", result);
//        return jsonObject;
//    }
//
//    /**
//     * 2.3.8 读写器读写能力查询
//     * 最小发射功率|最大发射功率|天线数目|频段列表|RFID协议列表。
//     */
//    public String GetReaderProperty(String ConnID) {
//        JSONObject jsonObject = new JSONObject();
//        //返回值 最小发射功率|最大发射功率|天线数目|频段列表|RFID协议列表。 功率单位为dB
//        String result = RFIDReader._Config.GetReaderProperty(ConnID);
//        return result;
//    }
//
//    /**
//     * 2.2.21 重启读写器
//     */
//    public void ReSetReader(String ConnID) {
//        RFIDReader._Config.ReSetReader(ConnID);
//    }
//
////    /**
////     * 2.1.5 关闭单个连接
////     * connectID： 连接ID，如：”192.168.1.116:9090”
////     */
////    public void CloseConn(String connectID) {
////        //rfid 关闭单个连接 接口 方法调用
////        IAsynchronousMessage log = new SampleCode();
////        if (RFIDReader.CreateTcpConn(connectID, log)) {
////            RFIDReader.CloseConn(connectID);
////            if (RFIDReader._Config.GetReaderBaseBandSoftVersion(connectID).equals("") ||
////                    RFIDReader._Config.GetReaderBaseBandSoftVersion(connectID) == null) {
////                System.out.println("关闭连接成功！");
////            } else {
////                System.out.println("关闭连接失败！");
////            }
////        } else {
////            System.out.println("创建连接失败！");
////        }
////    }
////
////    /**
////     * 2.1.6 关闭所有连接
////     */
////    public void CloseAllConnect(String connectID) {
////        IAsynchronousMessage log = new SampleCode();
////        if (RFIDReader.CreateTcpConn(connectID, log)) {
////            RFIDReader.CloseAllConnect();
////            if (RFIDReader._Config.GetReaderBaseBandSoftVersion(connectID).equals("") ||
////                    RFIDReader._Config.GetReaderBaseBandSoftVersion(connectID) == null) {
////                System.out.println("当前连接异常！");
////            } else {
////                System.out.println("当前连接正常！");
////            }
////        } else {
////            System.out.println("创建连接失败！");
////        }
////    }
//
//
////    public static void main(String[] args) {
////        SampleCode example = new SampleCode();
////        RFIDReader.CreateTcpConn("192.168.0.112:9090", example);
////        int result = RFIDReader._Tag6C.GetEPC("192.168.0.112:9090", 1, eReadType.Single);
////        System.out.println(result);
//////        RFIDReader.CloseConn("192.168.0.112:9090");
////    }
//}
