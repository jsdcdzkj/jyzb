package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 地点表
 */
@TableName("sys_place")
@Entity
@Table(name = "sys_place")
@Getter
@Setter
public class SysPlace extends Model<SysPlace> implements Serializable {
    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 地点名称
     */
    private String place_name;

    /**
     * 地点描述
     */
    private String place_desc;

    private String url;//地图地址
    private String lat;//纬度
    private String lng;//经度

    private Integer minzoom;//最小缩放值
    private Integer maxzoom;//最大缩放值
    private Integer zoom;//初始缩放值

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
}
