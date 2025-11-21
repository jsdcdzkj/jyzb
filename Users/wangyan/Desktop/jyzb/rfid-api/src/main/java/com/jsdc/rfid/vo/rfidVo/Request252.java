package com.jsdc.rfid.vo.rfidVo;

import lombok.Data;

/**
 * 2.5  6C标签操作
 * 2.5.2 写标签
 */
@Data
public class Request252 {
    // 连接标识
    private String ConnID;
    // 天线编号 同时指定天线1和天线2天线工作示例：3 ，详细说明请参见天线编号参数说明
    private int antNum;
    // 待写入的数据（16进制字符串）
    private String sWriteData;
    // 待匹配的EPC/TID数据
    private String sMatchData;
    // 匹配数据的起始下标
    private int matchWordStartIndex;
    // 标签的访问密码(8位16进制字符串)
    private String accessPassword;

    //同时指定天线1和天线2天线工作示例：3
    private int offset;
}
