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
 * 易耗品手动出库
 * @author xuaolong
 * @descirpet 耗材领用单表
 */
@Entity
@Table(name = "cons_handwork")
@TableName("cons_handwork")
@Data
@DynamicInsert
@DynamicUpdate
public class ConsHandwork extends Model<ConsHandwork> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键


    //出库单号
    private String out_code;
    @Transient
    @TableField(exist = false)
    private  String outbound_code;
    //领用部门
    private Integer dept_id;

    /**
     * 部门名字
     */
    @Transient
    @TableField(exist = false)
    private String department_name;
    //领用人
    private Integer use_id;
    @Transient
    @TableField(exist = false)
    private String use_name;//领用人名字

    //备注
    private String remark;
    //出库总数
    private Integer num ;
    //确认状态 // 1 完成 2 未完成
    private String is_finish;

    /**
     * 联系方式
     */
    private String phone;
    /**
     * 办公室
     */
    private String office;


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



}
