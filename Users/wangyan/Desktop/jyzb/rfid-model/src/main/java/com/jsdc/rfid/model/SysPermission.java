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
 * 权限菜单
 */
@TableName("sys_permission")
@Entity
@Table(name = "sys_permission")
@Getter
@Setter
public class
SysPermission extends Model<SysPermission> implements Serializable {
    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限名称
     */
    private String permission_name;

    /**
     * 权限编码
     */
    private String permission_code;

    /**
     * 权限类型 0：菜单 1：按钮
     */
    private Integer permission_type;

    /**
     * 父级权限
     */
    private Integer parent_permission;

    /**
     * 路由链接
     */
    private String route_url;

    /**
     * 路由名称
     */
    private String route_name;

    /**
     * vue路径路劲
     */
    private String vue_path;

    /**
     * 重定向类型
     */
    private String redirect_type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示 0:否 1:是
     */
    private Integer is_show;

    /**
     * icon
     */
    private String icon;

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

    @Transient
    @TableField(exist = false)
    private String ids;
}
