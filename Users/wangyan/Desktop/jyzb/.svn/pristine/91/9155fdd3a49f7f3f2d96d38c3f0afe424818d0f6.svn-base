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
 * 流程配置
 */
@Entity
@Table(name = "process_config")
@TableName("process_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessConfig {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 流程名称
     */
    private String process_name;
    /**
     * 流程编码
     */
    private String process_code;
    /**
     * 流程描述
     */
    private String process_desc;


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
     * 节点集合
     */
    @Transient
    @TableField(exist = false)
    private List<ProcessConfigInfo> processConfigInfos;

    /**
     * 是否为新增,0-否，1-是
     */
    @Transient
    @TableField(exist = false)
    private Integer isNew;

    /**
     * 当前节点
     */
    @Transient
    @TableField(exist = false)
    private List<ProcessConfigInfo> currentInfos;

    /**
     * 当前流程信息
     */
    @Transient
    @TableField(exist = false)
    private ProcessMember processMember;
}
