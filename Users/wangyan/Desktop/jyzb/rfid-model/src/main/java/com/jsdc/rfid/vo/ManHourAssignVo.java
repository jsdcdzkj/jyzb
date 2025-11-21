package com.jsdc.rfid.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsdc.rfid.model.ManHourAssign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManHourAssignVo extends ManHourAssign {
    private String originFileName;
    private String newFileName;
    private String userName;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    // 0: 原文件  1: 新生成的文件
    private String type;
}
