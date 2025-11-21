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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * 位置表
 */
@TableName("sys_position")
@Entity
@Table(name = "sys_position")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysPosition extends Model<SysPosition> implements Serializable {
    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 位置名称
     */
    private String position_name;

    /**
     * 位置描述
     */
    private String position_desc;

    /**
     * 所属地点
     */
    private Integer place_id;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 层数
     */
    private Integer floor;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    /**
     * 创建人
     */
    private Integer create_user;

    /**
     * 更新人
     */
    private Integer update_user;

    /**
     * 是否删除 0:否 1:是
     */
    private String is_del;

    /**
     * 地点名称
     */
    @Transient
    @TableField(exist = false)
    private String place_name;

}
