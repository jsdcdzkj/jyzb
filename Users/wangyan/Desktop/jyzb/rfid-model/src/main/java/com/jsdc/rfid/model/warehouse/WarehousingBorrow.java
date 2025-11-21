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
 * 借用
 */
@Entity
@TableName("warehousing_borrow")
@Table(name = "warehousing_borrow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousingBorrow extends Model<WarehousingBorrow> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //1待审批 2借用中 3待归还 4已归还 5已驳回
    private Integer status = 1;

    //仓库id
    private Integer warehouse_id;

    //使用部门
    private Integer use_dept;
    @TableField(exist = false)
    @Transient
    private String use_dept_name;

    //所属部门id
    private Integer dept_id;
    @TableField(exist = false)
    @Transient
    private String dept_id_name;

    @TableField(exist = false)
    @Transient
    private String produceDate;

    //审批时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    //归还时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date returnTime;

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


    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date produce_date;

    @TableField(exist = false)
    @Transient
    private String equip_num;

    //开始时间
    private String startTime;
    //结束时间
    private String endTime;

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



    //出库时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date delivery_time;

    @TableField(exist = false)
    @Transient
    private String warehouse_name;

    @TableField(exist = false)
    @Transient
    private String delivery_type_desc;

    //装备状态 0:库存 1:在用
    private Integer equip_status;

    @TableField(exist = false)
    @Transient
    private String equip_status_desc;

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

    /**
     * 驳回理由
     */
    private String reason_rejection;

    /**
     * 申请编号/装备类型/装备名称 模糊查询
     */
    @TableField(exist = false)
    @Transient
    private String name;


    @TableField(exist = false)
    @Transient
    private String delivery_start_time;

    @TableField(exist = false)
    @Transient
    private String delivery_end_time;

    @TableField(exist = false)
    @Transient
    private List<WarehousingBorrowDetail> borrowDetails;

}
