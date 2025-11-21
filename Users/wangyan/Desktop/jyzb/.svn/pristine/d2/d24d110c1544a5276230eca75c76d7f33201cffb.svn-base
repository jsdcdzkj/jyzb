package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 异常告警表
 *
 * @author zln
 * @descirpet 外携异常
 */
@Entity
@Table(name = "carry_abnormal")
@TableName("carry_abnormal")
@Data
@DynamicInsert
@DynamicUpdate
public class CarryAbnormal extends Model<CarryAbnormal> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键
    /**
     *  RFID
     *  资产标签EPC
     */
    private String rfid;
    private String numbering;//外携单编号
    private Integer assets_id;//资产id
    private String assetnumber;//资产编号
    private String specification;//规格型号
    private String assetname;//资产名称
    private Integer user_id;//使用人
    private Integer dept_id;//存放部门
    private String location;//存放位置
    private Integer position_id;//存放位置
    /**
     * 变动位置
     */
    private Integer change_position_id;
    //设备id
    private Integer equipment_id;

    private String carry_time;//外携时间
    private String is_repaid;//是否返回 0否 1是
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date creation_time;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date update_time;//修改时间
    private Integer creation_user;//创建人
    private Integer update_user;//修改人
    //逻辑删除 0否 1是
    private String is_del;

    //异常状态：1 未授权外携告警 2 位置变换告警 3 标签损毁告警
    private String error_status;
    //是否处理报警异常 0否（默认） 1是
    private String is_handle;
    //处理内容
    private String handle_content;
    //处理日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handle_time;
    //处理人
    private Integer handle_user;

    /**
     *  资产状态
     */
    @Transient
    @TableField(exist = false)
    private Integer asset_state;
    //部门名称
    @Transient
    @TableField(exist = false)
    private String dept_name;
    //用户名称
    @Transient
    @TableField(exist = false)
    private String user_name;
    //处理人名称
    @Transient
    @TableField(exist = false)
    private String handle_user_name;
    //设备id
    @Transient
    @TableField(exist = false)
    private Integer device_id;
    /**
     * 存放位置
     */
    @Transient
    @TableField(exist = false)
    private String position_name;
    /**
     * 变动位置
     */
    @Transient
    @TableField(exist = false)
    private String change_position_name;
    //资产epc
    @Transient
    @TableField(exist = false)
    private String asset_epc;

    /**
     * 创建时间-查询条件 年份+月份
     */
    @Transient
    @TableField(exist = false)
    private String creation_time_query;

    @Transient
    @TableField(exist = false)
    private String notice;

}
