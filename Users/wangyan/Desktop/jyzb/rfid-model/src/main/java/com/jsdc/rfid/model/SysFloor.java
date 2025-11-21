package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@TableName("sys_floor")
@Entity
@Table(name = "sys_floor")
@Getter
@Setter
public class SysFloor extends Model<SysFloor> implements Serializable {

    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    // 删除标记(0.显示1.删除)
    private Integer is_del;
    private String url;
    private String lat;
    private String lng;

    private Integer minzoom;
    private Integer maxzoom;
    private Integer zoom;
}
