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
 * @descirpet 资产处置单与资产关联表
 */
@Entity
@Table(name = "management_assets")
@TableName("management_assets")
@Data
@DynamicInsert
@DynamicUpdate
public class ManagementAssets extends Model<ManagementAssets> implements Serializable {


    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键


    //    处置单ID
    private Integer management_id;
    //    资产ID
    private Integer assets_id;
    //    处置建议 2"报废", 3"遗失", 4"无偿调拨", 5"对外捐赠", 6"出售", 7"出让", 8"转让", 9"置换"
    private String proposal;
    @Transient
    @TableField(exist = false)
    private String proposal_name;
    //    处置状态 1已处置、2未处置
    private String status;
    //    处置结果   2"报废", 3"遗失", 4"无偿调拨", 5"对外捐赠", 6"出售", 7"出让", 8"转让", 9"置换"
    private String reason;
    @Transient
    @TableField(exist = false)
    private String reason_name;
    //    处置金额
    private String money;

    //    处置人
    private Integer management_user;
    @Transient
    @TableField(exist = false)
    private String management_name;

    //    处置时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date management_date;

    @Transient
    @TableField(exist = false)
    private String deal_date;
    /**
     * 置换资产ID
     */
    private String replace_assets_code;

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

    //资产编号
    @Transient
    @TableField(exist = false)
    private String asset_code;

    /**
     * 资产名称
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
     * 关联处置单名称
     */
    @Transient
    @TableField(exist = false)
    private String management_code;
    @Transient
    @TableField(exist = false)
    private String statusName;
    @Transient
    @TableField(exist = false)
    private String specification;
    @Transient
    @TableField(exist = false)
    private String brand_name;

    /**
     * 得到附件ids
     */
    @Transient
    @TableField(exist = false)
    private String attachment_ids;

}
