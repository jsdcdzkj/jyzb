package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuaolong
 * @descirpet 资产借用单表
 */
@Entity
@Table(name = "borrow")
@TableName("borrow")
@Data
@DynamicInsert
@DynamicUpdate
public class Borrow extends Model<Borrow> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键

//    借用单号
    private String borrow_code;
//    借用部门
    private Integer department_id;
    //借用部门名称
    @Transient
    @TableField(exist = false)
    private String department_name;
//    借用人
    private Integer use_id;

    //借用人名称
    @Transient
    @TableField(exist = false)
    private String use_name;

//    借用日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date use_date;
    @Transient
    @TableField(exist = false)
    private  String real_use_date;
//    经办人
    private Integer handle_id;


    //经办人名称
    @Transient
    @TableField(exist = false)
    private String handle_name;

//    备注
     private String  remark;
//    审批状态 1未送审、2未审批、3审批中、4审批通过 5审批退回
     private  String status;
//    预计归还日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private  Date estimate_return_date;
    @Transient
    @TableField(exist = false)
    private  String real_return_date;
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

    @Transient
    @TableField(exist = false)
    private String real_create_time;

    /**
     * 更新人id
     */
    private Integer update_user;
    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;


    //    借用明细
    @Transient
    @TableField(exist = false)
    private List<BorrowAssets> detail = new ArrayList<>();

    // 新增时选用的资产
    @Transient
    @TableField(exist = false)
    private List<AssetsManage> AssetsManageList = new ArrayList<>();
    @Transient
    @TableField(exist = false)
    private String statusName;

}
