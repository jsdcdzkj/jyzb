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
 * @author thr
 * @descirpet 定时任务
 */
@Entity
@Table(name = "sys_cron")
@TableName("sys_cron")
@Data
@DynamicInsert
@DynamicUpdate
public class SysCron extends Model<SysCron> implements Serializable {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键
    //定时时间
    private String corn;
    //类型 1报警任务 2定时扫描定位
    private String type;
    //备注
    private String bz;
    //是否删除
    private Integer is_del;

}
