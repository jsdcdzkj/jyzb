package com.jsdc.rfid.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManHourAssignExcelVo {
    // 人力成本
    private Double rlcb;
    // 工时占比
    private Double gszb;
    // 工时费
    private Double sjFee;

    private Integer columnIndex;
    private Integer rowIndex;

}
