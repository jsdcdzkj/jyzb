package com.jsdc.rfid.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 领用流程控制
 */
@Entity
@Table(name = "process_member")
@TableName("process_member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessMember {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 流程id
     */
    private Integer process_id;
    /**
     * 业务id
     */
    private Integer bus_id;
    /**
     * 流程key
     */
    private String process_key;
    /**
     * 申请人的部门领导id
     */
    private Integer apply_dept_leader_id;


    /**
     * 是否删除
     */
    private String is_del;

    /**
     * 创建人
     */
    private Integer create_user;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新人
     */
    private Integer update_user;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 流程节点集合
     */
    @Transient
    @TableField(exist = false)
    private List<ProcessMemberNode> nodes;

    /**
     * 当前的node
     */
    @Transient
    @TableField(exist = false)
    private Integer node_id;

    @Transient
    @TableField(exist = false)
    private Integer userId;

    /**
     * 是否可以撤回 0：不可以 1：可以
     */
    @Transient
    @TableField(exist = false)
    private Integer is_revoke;

    // 1.通过, 2.驳回, 3.终止
    @Transient
    @TableField(exist = false)
    private Integer msgStatus;

    // 是否撤回  1：撤回 0：未撤回
    @Transient
    @TableField(exist = false)
    private Integer isCH;

    // 驳回理由Reason
    @Transient
    @TableField(exist = false)
    private String rejectReason;

    // 一键审批
    @Transient
    @TableField(exist = false)
    private String approvalReason;
}
