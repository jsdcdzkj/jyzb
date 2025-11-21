package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 维修申请表
 */
@Entity
@TableName("repair_apply")
@Table(name = "repair_apply")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairApply extends Model<RepairApply> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 维修编号
     */
    private String repair_code;

    /**
     * 维修申请人
     */
    private Integer repair_user;
    /**
     * 维修申请人名字
     */
    @Transient
    @TableField(exist = false)
    private String repair_user_name;
    /**
     * 申请部门
     */
    private Integer department_id;
    /**
     * 申请部门名字
     */
    @Transient
    @TableField(exist = false)
    private String department_name;
    /**
     * 预计费用总额
     */
    private String total_cost;
    /**
     * 大写金额
     */
    private String total_cost_upper;
    /**
     * 维修申请状态 1未送审、2未审批、3审批中、4审批通过 5审批退回
     */
    private String status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    @Transient
    @TableField(exist = false)
    private String create_time_str;
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


    // 当前处理节点-流程查看中使用
    @Transient
    @TableField(exist = false)
    private String currentTaskName;
    /**
     * 流程判断条件
     */
    @Transient
    @TableField(exist = false)
    private Integer processNum;

    @Transient
    @TableField(exist = false)
    private ProcessConfigInfo info;

    @Transient
    @TableField(exist = false)
    private Integer userId;

    @Transient
    @TableField(exist = false)
    private Integer is_adopt;

    // 任务名称
    @Transient
    @TableField(exist = false)
    private String taskName;

    // 办理人集合
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

    // 维修资产列表
    @Transient
    @TableField(exist = false)
    List<RepairApplyMember> applyMembers = new ArrayList<>();

    // id集合
    @Transient
    @TableField(exist = false)
    private List<Integer> ids;

    // 时间
    @Transient
    @TableField(exist = false)
    private String timeStr;

}
