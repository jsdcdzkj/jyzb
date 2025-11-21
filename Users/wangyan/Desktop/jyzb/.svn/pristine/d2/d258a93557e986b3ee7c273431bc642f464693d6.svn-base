package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 日志表
 */
@TableName("sys_log")
@Entity
@Table(name = "sys_log")
@Getter
@Setter
public class SysLog extends Model<SysLog> implements Serializable {
    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作类型
     */
    private String operate_type;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operate_time;
    /**
     * 操作人
     */
    private Integer user_id;
    /**
     * 操作内容
     */
    private String content;
    /**
     * 操作人姓名
     */
    private String user_name;
    /**
     * IP
     */
    private String ip;

    @Transient
    @TableField(exist = false)
    private String start_time;

    @Transient
    @TableField(exist = false)
    private String end_time;

}
