package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "fastentrance")
@TableName("fastentrance")
@Data
@DynamicInsert
@DynamicUpdate
public class FastEntrance extends Model<FastEntrance> implements Serializable {
    //主键
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 菜单id
     */
    private Integer permissionid;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 菜单名称
     */
    @Transient
    @TableField(exist = false)
    private String permissionName;
    /**
     * 菜单路径
     */
    @Transient
    @TableField(exist = false)
    private String permissionUrl;
    /**
     * 菜单图标
     */
    @Transient
    @TableField(exist = false)
    private String permissionIcon;


}
