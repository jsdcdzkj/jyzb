package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 岗位权限表
 */
@TableName("sys_post_permission")
@Entity
@Table(name = "sys_post_permission")
@Getter
@Setter
public class SysPostPermission extends Model<SysPostPermission> implements Serializable {
    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 岗位id
     */
    private Integer post_id;

    /**
     * 菜单id
     */
    private Integer permission_id;

    /**
     * 是否删除 0:否 1:是
     */
    private String is_del;

    @Transient
    @TableField(exist = false)
    private String permission_code;
}
