package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.*;
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
 * @descirpet 资产变更单表
 */
@Entity
@Table(name = "change_info")
@TableName("change_info")
@Data
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeInfo extends Model<ChangeInfo> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键


     //变更单号
     private  String change_code;
     //变更后状态 1闲置、2使用、3领用、4借用、5调拨、6故障、7处置
     private  String status;
     //变更后使用人
     private  Integer use_id;
     //变更后使用人名字
    @Transient
    @TableField(exist = false)
    private  String use_name;

     //变更后部门
     private Integer department_id;
     //变更后的部门名称
    @Transient
    @TableField(exist = false)
     private  String department_name;
     //变更后位置
     private  Integer position;
    //变更后位置的名字
    @Transient
    @TableField(exist = false)
     private  String place_name;
     //申请人
     private  Integer apply_user;

     //申请人名字
     @Transient
     @TableField(exist = false)
     private  String apply_name;
    //申请人部门
    @Transient
    @TableField(exist = false)
    private  String apply_dept_name;

     //申请日期
     @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
     private Date apply_date;
     //审批状态 1未送审、2未审批、3审批中、4审批通过 5审批退回
     private  String approve_status;
     //备注
     private  String remark;

    //变更来源 1变更申请、2手动变更
    private  String source;


    @Transient
    @TableField(exist = false)
    private List<ChangeDetail> detail = new ArrayList<>();


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

    @Transient
    @TableField(exist = false)
    private List<AssetsManage> assetsManageList = new ArrayList<>();
    @Transient
    @TableField(exist = false)
    private String real_apply_date;
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

    @Transient
    @TableField(exist = false)
    private String jjyy;

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

    // 时间区间
    @Transient
    @TableField(exist = false)
    private String timeStr;
}
