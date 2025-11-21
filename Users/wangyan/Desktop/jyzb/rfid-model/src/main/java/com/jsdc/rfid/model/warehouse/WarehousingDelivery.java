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
import java.util.List;

/**
 * 出库
 */
@Entity
@TableName("warehousing_delivery")
@Table(name = "warehousing_delivery")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousingDelivery extends Model<WarehousingDelivery> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //文件id
    private Integer fileId;

    //文件id
    @TableField(exist = false)
    @Transient
    private String fileName;

    //出库单号
    private String delivery_no;

    //出库单名称
    private String delivery_name;

    //说明
    private String description;

    //仓库id
    private Integer warehouse_id;

    private Integer use_dept;

    //共享装备 所属部门
    @TableField(exist = false)
    @Transient
    private Integer borrow_use_dept;

    //出库时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date delivery_time;

    @TableField(exist = false)
    @Transient
    private String use_dept_name;

    @TableField(exist = false)
    @Transient
    private String warehouse_name;

    //出库类型: 1:调拨: 2:借用 3:处置
    private Integer delivery_type;

    @TableField(exist = false)
    @Transient
    private String delivery_type_desc;

    //装备状态 0:库存 1:在用
    private Integer equip_status;

    @TableField(exist = false)
    @Transient
    private String equip_status_desc;

    //所属部门id
    private Integer dept_id;
    //共享装备 所属部门id
    @TableField(exist = false)
    @Transient
    private Integer borrow_dept_id;


    /**
     * 创建人id
     */
    private Integer create_user;

    @TableField(exist = false)
    @Transient
    private String create_user_name;

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

    @TableField(exist = false)
    @Transient
    private List<WarehousingDeliveryDetail> deliveryDetails;

    @TableField(exist = false)
    @Transient
    private String delivery_start_time;

    @TableField(exist = false)
    @Transient
    private String delivery_end_time;

}
