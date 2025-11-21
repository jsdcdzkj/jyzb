package com.jsdc.rfid.vo.rfidVo;

import lombok.Data;

/**
 * 2.5  6C标签操作
 * 2.5.4 灭活标签
 */
@Data
public class Request254 {
    // 连接标识
    private String ConnID;
    // 天线编号 同时指定天线1和天线2天线工作示例：3 ，详细说明请参见天线编号参数说明
    private int antNum;
    // 销毁密码（16进制字符串）
    private String destroyPassword;
    // 要匹配的EPC数据(16进制字符串)
    private String sMatchData;
    // 匹配数据的字起始地址
    private int matchWordStartIndex;
}
