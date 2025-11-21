package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.*;
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
 * @descirpet 资产借用与资产关联表
 */
@Entity
@Table(name = "borrow_assets")
@TableName("borrow_assets")
@Data
@DynamicInsert
@DynamicUpdate
public class BorrowAssets extends Model<BorrowAssets> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键

    // 借用单ID
    private Integer borrow_id ;
    // 资产ID
    private Integer assets_id;
    //借用状态 1待借用确认、2待归还、3已归还 4延期未归还
    private String borrow_status;
    //实际借用日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date real_date;
    //归还时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date return_date;
    //归还处理人
    private Integer return_deal_user;


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
     * 资产编号
     */
    @Transient
    @TableField(exist = false)
    private String asset_code;

    /**
     *  资产名称
     */
    @Transient
    @TableField(exist = false)
    private String asset_name;

    /**
     * 资产类型名称
     */
    @Transient
    @TableField(exist = false)
    private String assets_type_name;

    /**
     * 所属管理员
     */
    @Transient
    @TableField(exist = false)
    private String user_name;

    /**
     * 关联的借用单号
     */
    @Transient
    @TableField(exist = false)
    private String borrow_code;

    /**
     * 归还处理人名字
     */
    @Transient
    @TableField(exist = false)
    private String return_deal_name;

    @Transient
    @TableField(exist = false)
    private String statusName;
    @Transient
    @TableField(exist = false)
    private String realUseDate;
    @Transient
    @TableField(exist = false)
    private String realBackDate;

    @Transient
    @TableField(exist = false)
    private String specification;

    @Transient
    @TableField(exist = false)
    private String brand_name;
}
