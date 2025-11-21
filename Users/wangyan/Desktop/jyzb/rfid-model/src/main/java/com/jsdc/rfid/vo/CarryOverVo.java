package com.jsdc.rfid.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarryOverVo {


    //统计时间
//    private String staticsFromTime;
//
//    private String staticsEndTime;

    //是否结算 0 否 1 是
    private Integer status = 0;

    private Integer equipNum = 0;

    private Integer equipInNum = 0;

    private Integer equipOutNum = 0;



}
