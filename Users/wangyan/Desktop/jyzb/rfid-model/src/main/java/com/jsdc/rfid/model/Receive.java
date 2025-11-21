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
 * @descirpet 资产领用单表
 */
@Entity
@Table(name = "receive")
@TableName("receive")
@Data
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Receive extends Model<Receive> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键

    private String receive_code;//领用单号

    private Integer department_id; //领用部门ID

    /**
     * 部门名字
     */
    @Transient
    @TableField(exist = false)
    private String department_name;

    private Integer use_id; //领用人ID

    @Transient
    @TableField(exist = false)
    private String use_name;//领用人名字

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date use_date;//领用日期



    @Transient
    @TableField(exist = false)
    private List<ReceiveAssets> detail = new ArrayList<>();


    private Integer handle_id;//经办人ID

    @Transient
    @TableField(exist = false)
    private String handle_name; //经办人名字

    private String remark; //备注

    private String status;//状态 1未送审、2未审批、3审批中、4审批通过 5审批退回

    /**
     * 联系方式
     */
    private String phone;
    /**
     * 办公室
     */
    private String office;

    /**
     * 作废标识 0：未作废 1：已作废
     */
    private String cancel_sign;

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


    //流程完成标志 1 完成
    private String finish_sign;
    @Transient
    @TableField(exist = false)
    private List<AssetsManage> assetsManageList = new ArrayList<>();
    @Transient
    @TableField(exist = false)
    private List<Integer> receiveAssetId = new ArrayList<>();
    @Transient
    @TableField(exist = false)
    private String real_use_date;
    @Transient
    @TableField(exist = false)
    private String statusName;
    @Transient
    @TableField(exist = false)
    private List<Integer> ids = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private String currentTaskName;


    /**
     * 流程判断条件
     */
    @Transient
    @TableField(exist = false)
    private Integer processNum;

    @Transient
    @TableField(exist = false)
    private ProcessConfigInfo info;

    @Transient
    @TableField(exist = false)
    private Integer userId;

    @Transient
    @TableField(exist = false)
    private Integer is_adopt;

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

    // 日期查询条件
    @Transient
    @TableField(exist = false)
    private String timeStr;

    // 领用日志
    @Transient
    @TableField(exist = false)
    private String assets_names;


}
