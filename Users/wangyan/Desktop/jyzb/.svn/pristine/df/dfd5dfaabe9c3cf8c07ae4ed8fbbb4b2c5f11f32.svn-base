package com.jsdc.rfid.model;


import com.baomidou.mybatisplus.annotation.IdType;
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
 * 异常告警表 - 关联表
 * 外携异常
 */
@Entity
@Table(name = "carry_abnormal_member")
@TableName("carry_abnormal_member")
@Data
@DynamicInsert
@DynamicUpdate
public class CarryAbnormalMember extends Model<CarryAbnormal> implements Serializable{

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键

    //外携异常id
    private Integer carry_abnormal_id;

    //位置id
    private Integer position_id;

    //位置信息
    private String position_info;

    //创建时间
    private Date create_time;


}
