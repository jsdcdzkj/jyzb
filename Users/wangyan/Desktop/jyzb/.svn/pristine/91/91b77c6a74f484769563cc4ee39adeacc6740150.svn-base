package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * ClassName: ConsPurchaseApply
 * Description: 采购申请
 * 采购-审批-验收-生成入库单-入库
 * date: 2022/4/24 15:04
 *
 * @author bn
 */
@Entity
@TableName("cons_purchase_apply")
@Table(name = "cons_purchase_apply")
@Data
public class ConsPurchaseApply extends Model<ConsPurchaseApply> {


    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //采购编号
    private String purchase_no;
    //采购单名称
    private String purchase_name;
    //申请人
    private Integer apply_user;
    // 申请人名
    @Transient
    @TableField(exist = false)
    private String apply_user_name;
    //申请时间 不可编辑，默认为申请提交时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date apply_time;
    // 部门id
    private Integer dept_id;

    @Transient
    @TableField(exist = false)
    private String dept_name;

    //供应商
    private Integer supplier_id;

    @Transient
    @TableField(exist = false)
    private String supplier_name;

    //采购金额/预算金额 不可编辑，采购数量*采购单价 求和
    private String purchase_amount;
//    /**
//     * 重写purchase_amount的get方法
//     */
//    public String getPurchase_amount() {
//        return ModelUtil.formatMoneyOfNull(purchase_amount);
//    }
    //实际金额 验收数量*实际单价 求和 入库：入库数量*实际单价 求和
    private String actual_amount;
//    /**
//     * 重写actual_amount的get方法
//     */
//    public String getActual_amount() {
//        return ModelUtil.formatMoneyOfNull(actual_amount);
//    }
    //审批状态 1.待送审、2.未审批、3.审批中、4.审批通过、5.审批退回
    private String approve_status;
    //验收状态 1.待验收、2.已验收
    private String inspected_status;
    // 验收时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inspected_time;
    //入库单 1.未生成、2.已生成
    private String put_order;
    //入库状态 1.未入库、2.已入库
    private String put_status;

    //采购类型 1.采购、2.手动
    private String purchase_type;

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
    private String currentTaskName;


    @Transient
    @TableField(exist = false)
    private ProcessConfigInfo info;

    @Transient
    @TableField(exist = false)
    private String taskName;

    @Transient
    @TableField(exist = false)
    private String assigneeName;

    // 当前环节ids
    @Transient
    @TableField(exist = false)
    private List<Integer> currentTaskIds;

    // 附件列表
    @Transient
    @TableField(exist = false)
    private String files;

    // 申请时间
    @Transient
    @TableField(exist = false)
    private String applyTimeStr;

}
