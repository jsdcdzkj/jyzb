package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 部门表
 */
@TableName("sys_department")
@Entity
@Table(name = "sys_department")
@Getter
@Setter
public class SysDepartment extends Model<SysDepartment> implements Serializable {

    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 部门名称
     */
    private String dept_name;

    /**
     * 部门编码
     */
    private String dept_code;
    /**
     * 父级部门
     */
    @TableField(fill = FieldFill.UPDATE)
    private Integer parent_dept;

    private String parent_code;

    /**
     * 是否导入 0：否 1：是
     */
    private String is_import = "0";

    /**
     * 位置
     */
    private Integer dept_position;

    /**
     * 是否启用
     */
    private Integer is_enable;

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
     * OA id
     */
    private long oa_dept_id;

    /**
     * 是否删除 0:否 1:是
     */
    private String is_del;

    /**
     * 原始派出所
     */
    @Transient
    @TableField(exist = false)
    private List<Integer> source_ids;

    /**
     * 更换派出所
     */
    @Transient
    @TableField(exist = false)
    private Integer new_id;

    @Transient
    @TableField(exist = false)
    private String value;

    //是否开启权限 默认开启
    @Transient
    @TableField(exist = false)
    private Integer is_permission = 1;
    //部门下的用户
    @Transient
    @TableField(exist = false)
    private List<SysUser> users;

    @Transient
    @TableField(exist = false)
    private List<SysDepartment> children = new ArrayList<>();
}
