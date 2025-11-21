package com.jsdc.rfid.model.warehouse;

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
import java.util.Date;

/**
 * 出库详情
 */
@Entity
@TableName("warehousing_delivery_detail")
@Table(name = "warehousing_delivery_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousingDeliveryDetail extends Model<WarehousingDeliveryDetail> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private Integer delivery_id;

    private String delivery_no;

    //出库数量
    private Integer delivery_num;

    private Integer equip_type;

    @TableField(exist = false)
    @Transient
    private String equip_type_name;

    @TableField(exist = false)
    @Transient
    private String assets_type_code;

    private Integer equip_name;

    @TableField(exist = false)
    @Transient
    private String equip_name_desc;

    private String equip_model;

    private Integer equip_unit;

    @TableField(exist = false)
    @Transient
    private String equip_unit_name;

//    private Integer supplier_id;

    //处置方式
//    private String disposal_way;

    //使用部门
    private Integer use_dept;

    @TableField(exist = false)
    @Transient
    private Integer dept_id;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date produce_date;

    /**
     * 创建人id
     */
    private Integer create_user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

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
}
