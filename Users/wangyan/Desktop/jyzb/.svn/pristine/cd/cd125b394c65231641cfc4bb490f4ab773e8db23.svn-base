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
 * ClassName: WarehousingEnterDetail
 * Description: 入库明细
 *
 * @author hanch
 */
@Entity
@TableName("warehousing_enter_detail")
@Table(name = "warehousing_enter_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousingEnterDetail extends Model<WarehousingEnterDetail> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer enter_id;

    private String enter_no;

    //private String equip_type;
    private Integer equip_type;

    @TableField(exist = false)
    @Transient
    private String equip_type_name;

    @TableField(exist = false)
    @Transient
    private String assets_type_code;

    //private String equip_name;
    private Integer equip_name;

    @TableField(exist = false)
    @Transient
    private String equip_name_desc;

    //型号
    private String equip_model;
    //数量
    private Integer equip_num;
    //单位
    private Integer equip_unit;
    //计量单位
    private String measurement_unit;

    @TableField(exist = false)
    @Transient
    private String equip_unit_name;

    //使用部门
    private Integer use_dept;

    @TableField(exist = false)
    @Transient
    private String use_dept_name;

    //仓库id
    private Integer  warehouse_id;

    @TableField(exist = false)
    @Transient
    private String warehouse_name;

    //装备状态 0:库存 1:在用
    private Integer equip_status;

    @TableField(exist = false)
    @Transient
    private String equip_status_desc;

    //单价
    private BigDecimal unit_price;

    //使用年限
    private Integer use_year;

    //出厂日期
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
