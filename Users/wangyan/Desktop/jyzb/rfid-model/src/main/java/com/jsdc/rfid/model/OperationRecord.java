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
 * ClassName: OperationRecord
 * Description:  资产操作记录表
 *
 * @author zhangdequan
 */
@Entity
@TableName("operation_record")
@Table(name = "operation_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationRecord extends Model<OperationRecord> implements Serializable {

    /**
     * 主键标识
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *关联外键（资产编号）
     */
    private String field_fk;
    /**
     * 操作外键id
     */
    private Integer operate_id;
    /**
     * 操作记录
     */
    private String record;
    /**
     * 模块类型 1资产管理 2.资产采购 3.资产盘点 4、领用、5、借用 6、变更 7处置 8 外携 10 报修11易耗品领用 12.易耗品手动出库
     */
    private String type;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;
    /**
     * 创建人id
     */
    private Integer create_user;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;
    /**
     * 更新人id
     */
    private Integer update_user;
    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;


    /**
     * 1领用、 2、变更 3处置 4、易耗品领用, 5. 维修
     */
    private String kind;

    /**
     * 获取微信传递来的userId的值
     */
    @Transient
    @TableField(exist = false)
    private Integer userId;

}
