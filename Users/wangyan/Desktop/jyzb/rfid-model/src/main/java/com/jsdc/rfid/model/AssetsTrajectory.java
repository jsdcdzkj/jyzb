package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资产轨迹
 *
 * @author thr
 */
@Entity
@TableName("assets_trajectory")
@Table(name = "assets_trajectory")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetsTrajectory extends Model<AssetsTrajectory> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产id
     */
    private Integer asset_id;
    /**
     * 资产标签EPC
     */
    private String asset_epc;
    /**
     * 资产编号
     */
    private String asset_code;
    /**
     * 资产名称
     */
    private String asset_name;
    /**
     * 设备id
     */
    private Integer equipment_id;
    /**
     * 存放位置
     */
    private Integer position_id;
    /**
     * 变动位置
     */
    private Integer change_position_id;

    /**
     * 使用人
     */
    private Integer user_id;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    /**
     * 创建人id
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    /**
     * 更新人id
     */
    private Integer update_user;
    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;

    /**
     * 存放位置名称
     */
    @Transient
    @TableField(exist = false)
    private String place_name;
    /**
     * 变动位置名称
     */
    @Transient
    @TableField(exist = false)
    private String change_position_name;

    /**
     * 创建人名称
     */
    @Transient
    @TableField(exist = false)
    private String create_user_name;
    /**
     * 使用人名称
     */
    @Transient
    @TableField(exist = false)
    private String user_name;


}
