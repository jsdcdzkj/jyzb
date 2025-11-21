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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 资产进出记录
 *
 * @author thr
 */
@Entity
@TableName("assets_access_record")
@Table(name = "assets_access_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetsAccessRecord extends Model<AssetsAccessRecord> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产id
     */
    private Integer assetid;
    /**
     * 资产编号
     */
    private String assetcode;
    /**
     * 资产标签EPC
     */
    private String assetepc;
    /**
     * 资产名称
     */
    private String assetname;
    /**
     * 设备id
     */
    private Integer equipmentid;
    /**
     * 设备位置
     */
    private Integer positionid;
    /**
     * 使用人
     */
    private Integer userid;
    /**
     * 进出状态 1进 2出
     */
    private String accessstatus;
    /**
     * ab门状态 1-A门,2-B门,3-AB门
     */
    private Integer abstatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createtime;
    /**
     * 创建人id
     */
    private Integer createuser;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatetime;
    /**
     * 更新人id
     */
    private Integer updateuser;
    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String isdel;
    /**
     * 创建人名称
     */
    @Transient
    @TableField(exist = false)
    private String create_user_name;
    /**
     * 进出状态 1进 2出
     */
    @Transient
    @TableField(exist = false)
    private String access_status_name;

    /**
     * 使用人
     */
    @Transient
    @TableField(exist = false)
    private String user_name;
    /**
     * 设备位置
     */
    @Transient
    @TableField(exist = false)
    private String position_name;
}
