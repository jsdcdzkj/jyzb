package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xuaolong
 * @descirpet 资产领用与资产关联表
 */
@Entity
@Table(name = "receive_assets")
@TableName("receive_assets")
@Data
@DynamicInsert
@DynamicUpdate
public class ReceiveAssets extends Model<ReceiveAssets> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键

    private Integer receive_id; //领用单ID

    private Integer assets_id;//资产ID

    private String receive_status;//领用状态 1待领用确认、2待归还、3已归还
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date real_date;//实际领用日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date return_date;//归还时间

    private Integer return_deal_user;//归还处理人
    @Transient
    @TableField(exist = false)
    private String return_deal_name;//归还处理人名称


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

    @Transient
    @TableField(exist = false)
    private String asset_code;//资产编号

    @Transient
    @TableField(exist = false)
    private String asset_name;//资产名称

    @Transient
    @TableField(exist = false)
    private String assets_type_name;//资产类型名称

    @Transient
    @TableField(exist = false)
    private String user_name; //所属管理员

    @Transient
    @TableField(exist = false)
    private String receive_code;//领用单号

    /**
     *  规格型号
     */
    @Transient
    @TableField(exist = false)
    private String specification;
    /**
     *  品牌id 【字典表 value值，字典类型为brand】
     */
    @Transient
    @TableField(exist = false)
    private Integer brand_id;
    @Transient
    @TableField(exist = false)
    private String realUseDate;
    @Transient
    @TableField(exist = false)
    private String realBackDate;
    @Transient
    @TableField(exist = false)
    private String statusName;
    @Transient
    @TableField(exist = false)
    private String brand_name;
    /**
     * 资产状态
     */
    @Transient
    @TableField(exist = false)
    private Integer assets_status;


}
