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
import java.util.List;

/**
 * 岗位表
 */
@TableName("sys_post")
@Entity
@Table(name = "sys_post")
@Getter
@Setter
public class SysPost extends Model<SysPost> implements Serializable {
    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 岗位名称
     */
    private String post_name;
    /**
     * 岗位代码
     */
    private String post_code;
    /**
     * 所属部门
     */
    private Integer dept_id;
    /**
     * 数据权限
     */
    private Integer data_permission;

    /**
     * 用户类型 用于展示工作台  1管理;  2员工;  3运维;
     */
    @TableField(value = "user_type")
    private Integer user_type;

    /**
     * 自定义权限
     */
    private String custom_permission;
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
     * 是否删除 0:否 1:是
     */
    private String is_del;

    /**
     * OA id
     */
    private long oa_post_id;
    /**
     * 签到权限
     */
    private Integer sign_data_permission;
    @Transient
    @TableField(exist = false)
    private String permissionIds;
    @Transient
    @TableField(exist = false)
    private List<SysPermission> permissionList;

}
