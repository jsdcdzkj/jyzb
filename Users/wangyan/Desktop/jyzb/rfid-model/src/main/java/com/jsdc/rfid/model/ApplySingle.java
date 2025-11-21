package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @author zln
 * @descirpet 报修申请表
 */
@Entity
@Table(name = "apply_single")
@TableName("apply_single")
@Data
@DynamicInsert
@DynamicUpdate
public class ApplySingle extends Model<ApplySingle> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键
    //报修编号
    private String order_code;
    //申请人ID
    private Integer sqr_id;
    //申请人名称
    private String sqr_name;
    //申请部门ID
    private String dept_id;
    //申请部门名称
    private String dept_name;
    //资产Id
    private String assetmanage_id;
    //资产名称
    private String good_name;
    //资产编号
    private String good_number;
    //品类ID
    private String assettype_id;
    //品类名称
    private String assettype_name;
    //维修数量（默认1）
    private Integer amount;
    //设备异常描述
    private String represent;
    //紧急程度(1.特急2.较急3.一般)
    private Integer degree;
    //要求完成时间
    private Date claim;
    //维修人员ID
    private Integer repair_id;
    //维修人员名称
    private String repair_name;
    //预约维修完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date yytime;
    //实际维修完成时间
    private Date actual;
    //是否作废
    private String cancel;
    //作废原因
    private String zfyy;
    //申请单状态 1 录入 2 已派单 3 拒绝签单 4 签收 5 无法完成 6 确认维修 7 完成反馈 8 完成 9 已作废
    private Integer state;
    //1.好评 2.中评  3.差评
    private Integer grade;
    //评论
    private String grade_msg;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date creation_time;
    //是否删除
    private Integer is_del;
    //派单类型 1自动派单 2手动派单
    private Integer pd_type;
    //手机号
    private String phone;
    //点击开始维修时间
    private Date edittime;
    //拒绝原因 无法维修原因
    private String jjyy;
    //审批进度
    private String approval_state;
    //外部维修提交事件
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date extrnaltime;
    //外部维修原因
    private String cause;
    //是否外部维修 1为外部维修
    private Integer isexternal;
    //经办人
    private Integer managers;
    //维修单位
    private Integer unitid;

}
