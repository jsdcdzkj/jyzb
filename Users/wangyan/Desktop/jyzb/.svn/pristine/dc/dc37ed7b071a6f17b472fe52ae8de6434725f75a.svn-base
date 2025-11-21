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
 * 设备表
 */
@TableName("equipment")
@Entity
@Table(name = "equipment")
@Getter
@Setter
public class Equipment extends Model<Equipment> implements Serializable {
    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备名称
     */
    private String equipment_name;
    /**
     * 设备类型 【字典 1读码机 2写码机 3读写一体机】
     */
    private Integer equipment_type;
    /**
     * 设备序列号
     */
    private String serialno;
    /**
     * 设备IP
     */
    private String ip;
    /**
     * 设备端口号
     */
    private String port;
    /**
     * 用处 【字典 1扫描 2报警】
     */
    private Integer equipment_usage;
    /**
     * 设备MAC
     */
    private String mac;
    /**
     * 设备位置
     */
    private Integer equipment_position;
    /**
     * 是否启用 0否 1是
     */
    private Integer is_enable;
    /**
     * 设备状态 0:离线 1:在线
     */
    private Integer status;
    /**
     * 连接方式 0:客户端 1:服务端
     */
    private Integer connect_type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    /**
     * 创建人
     */
    private Integer create_user;

    /**
     * 更新人
     */
    private Integer update_user;

    /**
     * 是否删除 0:否 1:是
     */
    private String is_del;

    /**
     * 报警状态 1正常 2报警
     */
    private Integer warning_status;
    /**
     * AB门类型 1A门 2B门
     */
    private Integer ab_type;

    /**
     * 所属位置名称
     */
    @Transient
    @TableField(exist = false)
    private String positionName;
    /**
     * 经度
     */
    @Transient
    @TableField(exist = false)
    private String lng;
    /**
     * 纬度
     */
    @Transient
    @TableField(exist = false)
    private String lat;

    @Transient
    @TableField(exist = false)
    private String usage_name;
    @Transient
    @TableField(exist = false)
    private String type_name;
    //地点id
    @Transient
    @TableField(exist = false)
    private Integer place_id;
}
