package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 仓库表 用户表
 */
@TableName("warehouse_user_member")
@Entity
@Table(name = "warehouse_user_member")
@Getter
@Setter
public class WarehouseUserMember extends Model<WarehouseUserMember> implements Serializable {

    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private Integer user_id;

    private Integer warehouse_id;
}
