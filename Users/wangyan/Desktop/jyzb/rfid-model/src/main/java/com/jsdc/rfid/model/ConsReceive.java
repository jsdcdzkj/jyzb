package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author xuaolong
 * @descirpet 耗材领用单表
 */
@Entity
@Table(name = "cons_receive")
@TableName("cons_receive")
@Data
@DynamicInsert
@DynamicUpdate
public class ConsReceive  extends Model<ConsReceive> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键

    private String receive_code;//耗材领用单编号

    private String outbound_code;//出库单号

    private Integer department_id; //领用部门ID



    /**
     * 部门名字
     */
    @Transient
    @TableField(exist = false)
    private String department_name;

    private Integer use_id; //领用人ID

    @Transient
    @TableField(exist = false)
    private String use_name;//领用人名字

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date apply_date;//申请日期

    @Transient
    @TableField(exist = false)
    private String real_use_date;


    private String remark; //备注

    private String status;//状态 1未送审、2未审批、3审批中、4审批通过 5审批退回、6已完成

    /**
     * 联系方式
     */
    private String phone;
    /**
     * 办公室
     */
    private String office;
    @Transient
    @TableField(exist = false)
    private String statusName;


    private Integer apply_num ; //申领总数

    private Integer out_num ; //出库总数


    private String is_finish;//完成标志 1.已完成 2.未完成
    /**
     * 作废标识 0：未作废 1：已作废
     */
    private String cancel_sign;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    @Transient
    @TableField(exist = false)
    private String real_create_time;

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

    /**
     * 生成出库单号用到的字段
     */
    private Date out_date;

    @Transient
    @TableField(exist = false)
    private List<Integer> ids = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private String currentTaskName;
    @Transient
    @TableField(exist = false)
    private List<ConsReceiveAssets> assetsList = new ArrayList<>();
    @Transient
    @TableField(exist = false)
    private String receiveAssets = "";

    @Transient
    @TableField(exist = false)
    private ProcessConfigInfo info;

    @Transient
    @TableField(exist = false)
    private String transfer;

    @Transient
    @TableField(exist = false)
    private Integer is_adopt;

    @Transient
    @TableField(exist = false)
    private Integer totalNum;


    // 耗材类型名称
    @Transient
    @TableField(exist = false)
    private String cateName;

    // 耗材类型名称
    @Transient
    @TableField(exist = false)
    private String assetName;

    // 申请人姓名
    @Transient
    @TableField(exist = false)
    private String userName;

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
}
