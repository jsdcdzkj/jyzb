package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 流程历史表数据
 */
@Entity
@Table(name = "process_member_history")
@TableName("process_member_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessMemberHistory {

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
     * 流程备注
     */
    private String process_remark;
    /**
     * 业务流程处理的实体json字符串
     */
    private String bus_json;

    /**
     * 节点id
     */
    private Integer node_id;
    /**
     * 节点名称
     */
    private String node_name;

    /***
     * 附件ids
     */
    private String file_ids;


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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    /**
     * 更新人
     */
    private Integer update_user;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    /**
     * 更新人名称
     */
    @Transient
    @TableField(exist = false)
    private String update_user_name;
    /**
     * 创建人名称
     */
    @Transient
    @TableField(exist = false)
    private String create_user_name;

    @Transient
    @TableField(exist = false)
    private Integer count ;

    @Transient
    @TableField(exist = false)
    private List<FileManage> fileList;

}
