package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@TableName("assets_inandout_collect")
@Table(name = "assets_inandout_collect")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetsInAndOutCollect {
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
     * ab门状态 1-A门,2-B门
     */
    private Integer abstatus;

    /**
     * 完成状态(0-未完成,1-已完成)
     */
    private Integer finishstatus;

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
}
