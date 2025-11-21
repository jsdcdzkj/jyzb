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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流程配置
 */
@Entity
@Table(name = "process_config_info")
@TableName("process_config_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessConfigInfo {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 流程配置id
     */
    private Integer process_config_id;

    /**
     * 节点名称
     */
    private String node_name;

    /**
     * 节点处理角色/处理人
     */
    private Integer node_handler;
    /**
     * 节点处理角色/处理人名称
     */
    @TableField(exist = false)
    private String node_handler_name;

    /**
     * 节点type,0-角色，1-处理人,默认0
     */
    private Integer node_type;
    /**
     * 节点顺序
     */
    private Integer node_order;
    /**
     * 父节点
     */
    private String parent_node;
    /**
     * 父节点名称
     */
    @TableField(exist = false)
    private String parent_node_name;
    /**
     * 条件判断
     */
    private String process_condition;
    /**
     * 节点描述
     */
    private String node_desc;
    /**
     * 是否提前结束流程 0-否，1-是
     */
    private Integer is_end;



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

    @Transient
    @TableField(exist = false)
    private List<String> parent_node_list = new ArrayList<>();

}
