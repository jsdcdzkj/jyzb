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
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xuaolong
 * @descirpet 资产变更明细表
 */
@Entity
@Table(name = "change_detail")
@TableName("change_detail")
@Data
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeDetail extends Model<ChangeDetail> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键

    //变更单ID
    private Integer change_id;

    //资产ID
    private Integer assets_id;

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
    //资产名称
    @Transient
    @TableField(exist = false)
    private String asset_name;

    //资产品类
    @Transient
    @TableField(exist = false)
    private String assets_type_name;

    //所属管理员
    @Transient
    @TableField(exist = false)
    private String user_name;
    //规格型号
    @Transient
    @TableField(exist = false)
    private String specification;
    @Transient
    @TableField(exist = false)
    private String brand_name;
    @Transient
    @TableField(exist = false)
    private Integer num;

}
