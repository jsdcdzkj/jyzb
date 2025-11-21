package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsdc.rfid.model.util.ModelUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: ConsWarehousingManagement
 * Description:  易耗品入库管理
 *
 * @author zhangdequan
 */
@Entity
@TableName("cons_warehousing_management")
@Table(name = "cons_warehousing_management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsWarehousingManagement extends Model<ConsWarehousingManagement> implements Serializable {

    /**
     * 主键标识
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 入库单号
     */
    private String inbound_order_number;

    /**
     * 供应商
     */
    private Integer supplier_id;
    /**
     * 操作记录 入库
     */
    private String operation_record;
    /**
     * 入库金额
     */
    private String inbound_amount;

//    public String getInbound_amount() {
//        return ModelUtil.formatMoney(inbound_amount);
//    }

    // 采购申请id
    private Integer purchase_apply_id;

    /**
     * 入库类型 1.采购 2.手动
     */
    private Integer inbound_type;

    /**
     * 入库状态 未完成、已完成
     */
    private Integer inbound_status;
    /**
     * 入库数量
     */
    private Integer inbound_number;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    /**
     * 创建人id
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    /**
     * 更新人id
     */
    private Integer update_user;
    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;

    @Transient
    @TableField(exist = false)
    private String asset_name;

    @Transient
    @TableField(exist = false)
    private Integer warehouse_id;

    @Transient
    @TableField(exist = false)
    private String warehouse_name;

    @Transient
    @TableField(exist = false)
    private String supplier_name;
}
