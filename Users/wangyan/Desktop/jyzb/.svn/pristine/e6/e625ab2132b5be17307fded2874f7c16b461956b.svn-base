package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 维修人员反馈
 *
 * @author zonglina
 */
@Entity
@Table(name = "feedback")
@TableName("feedback")
@Data
@DynamicInsert
@DynamicUpdate
public class Feedback  extends Model<Feedback> implements Serializable {
    //主键
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //开始修理时间
    private Date starttime;
    //修理结束时间
    private Date endtime;
    //维修状态(1成功 2失败)
    private Integer repair_status;
    //费用
    private Double cost;
    //申请单
    private String applysingle_id;
    //描述
    private String describes;
    @Transient
    @TableField(exist = false)
    private Integer state;
    @Transient
    @TableField(exist = false)
    private Integer userId;
    @Transient
    @TableField(exist = false)
    private String files;
}
