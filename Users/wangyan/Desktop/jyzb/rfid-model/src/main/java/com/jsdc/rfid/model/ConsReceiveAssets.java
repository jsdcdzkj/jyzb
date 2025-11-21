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
 * @descirpet 易耗品领用与易耗品库存关联表
 */
@Entity
@Table(name = "cons_receive_assets")
@TableName("cons_receive_assets")
@Data
@DynamicInsert
@DynamicUpdate
public class ConsReceiveAssets  extends Model<ConsReceiveAssets> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键

    private Integer type ;//1申领明细、2手动出库明细

    private Integer receive_id; //关联单据ID
    /**
     * 易耗品ID
     */
    private Integer cons_id;

//    private Integer assets_id;//资产ID


    //耗材品类
    private Integer asset_type_id;
    //类型
    private String  consumable_name;
    //标签
    private String  name;
    //型号
    private String  model;

    //耗材品类名称
    @Transient
    @TableField(exist = false)
    private String asset_type_name;

    //耗材名称
    private Integer asset_name;

    //耗材名称
    @Transient
    @TableField(exist = false)
    private String real_asset_name;
    //规格型号
    private Integer specifications;

    //规格型号名称
    @Transient
    @TableField(exist = false)
    private String specifications_name;

    // 品牌
    private String brand ;
    // 品牌名称
    @Transient
    @TableField(exist = false)
    private String brand_name;

    //申领数量
    private Integer apply_num;

    //剩余库存
    @Transient
    @TableField(exist = false)
    private Integer reduce_num;

    //已出库数量
    private Integer use_num;

    @Transient
    @TableField(exist = false)
    private Integer inventory_num;//库存数量

    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date production_date;
    /**
     * 质保期(月)
     */
    private String warranty_period;

    /**
     * 仓库id
     */
    private Integer warehouse_id;
    /**
     * 仓库名称
     */
    @TableField(exist = false)
    @Transient
    private String warehouse_name;

    /**
     * 开始时间
     */
    @Transient
    @TableField(exist = false)
    private String start_time;
    /**
     * 结束时间
     */
    @Transient
    @TableField(exist = false)
    private String end_time;

    /**
     * 单价
     */
    @Transient
    @TableField(exist = false)
    private String unit_price;

    /**
     * 单位名称
     */
    @Transient
    @TableField(exist = false)
    private String unit_name;

    /**
     * 入库数量
     */
    @Transient
    @TableField(exist = false)
    private Integer in_num;

    /**
     * 出库数量
     */
    @Transient
    @TableField(exist = false)
    private Integer out_num;

    /**
     * 期末数量
     */
    @Transient
    @TableField(exist = false)
    private Integer end_num;
    /**
     * 总金额
     */
    @Transient
    @TableField(exist = false)
    private String total_price;
    /**
     * 备注
     */
    @Transient
    @TableField(exist = false)
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    @Transient
    @TableField(exist = false)
    private String real_create_time;

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
    private Integer num;

    //已出库数量 ,temp
    @Transient
    @TableField(exist = false)
    private Integer use_num_temp;









}
