package com.jsdc.rfid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zln
 * @descirpet 外携申请表
 */
@Entity
@Table(name = "carry_manage")
@TableName("carry_manage")
@Data
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarryManage extends Model<CarryManage> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键
    private String numbering;//外携单编号
    /**
     *  RFID
     *  资产标签EPC
     */
    private String rfid;
    private String assetnumber;//资产编号
    private String specification;//规格型号
    private Integer asset_manage_id;//资产Id
    private String assetname;//资产名称
    private Integer user_id;//使用人
    private Integer dept_id;//外携部门
    private String dept_name;//外携部门
    private Integer carry_id;//外携申请人
    private String carry_name;//外携申请人
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date app_time;//申请时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date estimate_time;//预计返回时间
    private Date actual_time;//实际返回时间
    private String carry_cause;//外携原因
    private String remark;//备注
    private String approval_state;//审批状态 0待审批 1 审批拒绝/未授权 2审批通过/已授权 3.已撤单  4.未送审  5.审批中
    private String carry_state;//外携状态（1.已外携、0.未外携）
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date carry_time;//外携时间
    private String overdue;//逾期未归(0.已归还 1.未归还)
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
    //资产epc
    @Transient
    @TableField(exist = false)
    private String asset_epc;

    //资产品类
    @Transient
    @TableField(exist = false)
    private Integer asset_type_id;

    //资产编号
    @Transient
    @TableField(exist = false)
    private List<String> assetnumberList = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private List<Integer> ids = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private String currentTaskName;

    @Transient
    @TableField(exist = false)
    private ProcessConfigInfo info;

    //微信端userId
    @Transient
    @TableField(exist = false)
    private Integer wx_userId;

    //微信端检索传值
    @Transient
    @TableField(exist = false)
    private String transfer;

    @Transient
    @TableField(exist = false)
    private Integer is_adopt;

    @Transient
    @TableField(exist = false)
    private String creation_time_query;

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

    // 查询条件
    @Transient
    @TableField(exist = false)
    private String timeStr;

    // 部门负责人查看权限
    @Transient
    @TableField(exist = false)
    private Integer deptManager;
}
