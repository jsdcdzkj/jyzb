package com.jsdc.rfid.vo;

import com.jsdc.rfid.model.warehouse.WarehousingStockDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStockDetailVo {

    //统计时间
    private String staticsTime;

    //装备总数
    private Integer equipNum;

    //仓库数
    private Integer warehouseNum;

    //使用部门数
    private Integer deptNum;

    //总价值
    private BigDecimal totalPrice;

    //库存明细
    private List<WarehousingStockDetail> stockDetails;
}
