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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存
 */
@Entity
@TableName("warehousing_stock")
@Table(name = "warehousing_stock")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousingStock extends Model<WarehousingStock> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    //文件id
    private Integer fileId;

    //仓库id
//    private Integer  warehouse_id;
//
//    @TableField(exist = false)
//    @Transient
//    private String warehouse_name;

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

    @TableField(exist = false)
    @Transient
    private Integer equip_num;

    //部门使用数量
    @TableField(exist = false)
    @Transient
    private Integer dept_num;

    //仓库使用数量
    @TableField(exist = false)
    @Transient
    private Integer warehouse_num;

    @TableField(exist = false)
    @Transient
    private Integer equip_stock_num;

    @TableField(exist = false)
    @Transient
    private Integer equip_use_num;


    //入库日期
    @TableField(exist = false)
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date enter_time;

    //入库日期
    @TableField(exist = false)
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date enter_time_format;

    //所属部门id
    @TableField(exist = false)
    @Transient
    private Integer dept_id;

    //装备状态 0:库存 1:在用
    @TableField(exist = false)
    @Transient
    private Integer equip_status;

    //仓库id
    @TableField(exist = false)
    @Transient
    private Integer warehouse_id;

    //使用部门
    @TableField(exist = false)
    @Transient
    private Integer use_dept;

    @TableField(exist = false)
    @Transient
    private String homeObjectDesc;

    @TableField(exist = false)
    @Transient
    private Integer equip_in_num;

    @TableField(exist = false)
    @Transient
    private Integer equip_out_num;

    @TableField(exist = false)
    @Transient
    private BigDecimal totalPrice;

    @TableField(exist = false)
    @Transient
    private String dept_name;

    @TableField(exist = false)
    @Transient
    private Integer homeObject;

//
//    //出库日期
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date delivery_date;

//    //装备状态 0:库存 1:在用
//    private Integer equip_status;

//    @TableField(exist = false)
//    @Transient
//    private Integer equip_unit;
//
//    @TableField(exist = false)
//    @Transient
//    private Integer supplier_id;
//
//    @TableField(exist = false)
//    @Transient
//    private String supplier_name;
//
//    @TableField(exist = false)
//    @Transient
//    private String equip_type_name;

//    @TableField(exist = false)
//    @Transient
//    private String equip_unit_name;

//    使用年限
    @TableField(exist = false)
    @Transient
    private Integer use_year;
//
    //临期日期
    @TableField(exist = false)
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date critical_date_format;
    //过期日期
    @TableField(exist = false)
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expired_date_format;
    //出厂日期
    @TableField(exist = false)
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date produce_date_format;

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

    /**
     * 导入时间
     */
    private String date_import;

}
