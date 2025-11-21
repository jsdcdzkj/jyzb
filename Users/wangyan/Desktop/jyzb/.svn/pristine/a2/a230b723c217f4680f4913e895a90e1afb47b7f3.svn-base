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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuaolong
 * @descirpet 资产处置单表
 */
@Entity
@Table(name = "management")
@TableName("management")
@Data
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Management extends Model<Management> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键
    //    单号
    private  String management_code;
//    申请部门
    private  Integer department_id;
    //    申请部门名字
    @Transient
    @TableField(exist = false)
    private  String department_name;
//    申请人
    private  Integer  apply_user;
    //申请人名字
    @Transient
    @TableField(exist = false)
    private  String apply_name;
//    申请日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private  Date apply_date;
    @Transient
    @TableField(exist = false)
    private String real_apply_date;

//    审批状态 1未送审、2未审批、3审批中、4审批通过 5审批退回
    private  String status;

//    原因备注
    private  String reason_detail;
//    处置单来源 1处置申请、2盘亏处理、3盘点异常处理
    private  String source;
//    处置完成标志 1完成 2未完成
    private  String  management_finish;

    @Transient
    @TableField(exist = false)
    private List<ManagementAssets> detail = new ArrayList<>();
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

    //关联的资产
    @Transient
    @TableField(exist = false)
    private List<AssetsManage> assetsManageList = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private List<Integer> assetsIdList = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private List<String> proposalList = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private String status_name;

    @Transient
    @TableField(exist = false)
    private List<Integer> ids = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private String currentTaskName;

    @Transient
    @TableField(exist = false)
    private ProcessConfigInfo info;

    /**
     * 获取微信传递来的userId的值
     */
    @Transient
    @TableField(exist = false)
    private Integer userId;

    @Transient
    @TableField(exist = false)
    private String taskName;

    @Transient
    @TableField(exist = false)
    private String assigneeName;

    // 当前环节ids
    @Transient
    @TableField(exist = false)
    private List<Integer> currentTaskIds;

    // 附件列表
    @Transient
    @TableField(exist = false)
    private String files;

    // 申请日期,查询条件
    @Transient
    @TableField(exist = false)
    private String timeStr;

}
