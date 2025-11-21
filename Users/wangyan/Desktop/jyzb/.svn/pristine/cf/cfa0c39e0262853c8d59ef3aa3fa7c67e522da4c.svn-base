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
 * 统计进出表数据信息
 */
@Entity
@TableName("cons_inout_statistics")
@Table(name = "cons_inout_statistics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsInAndOutStatistics extends Model<ConsInAndOutStatistics> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //类型
    private String consumable_name;
    /**
     * 标签
     */

    private String name;

    /**
     * 规格类型
     */
    private String model;
    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date production_date;
    /**
     * 质保期(月)
     */
    private String warranty_period;
    /**
     * 仓库id
     */
    private Integer warehouse_id;
    /**
     * 仓库名称
     */
    @Transient
    @TableField(exist = false)
    private String warehouse_name;
    /**
     * 单位id
     */
    private Integer unit_id;
    /**
     * 单位名称
     */
    @Transient
    @TableField(exist = false)
    private String unit_name;
    /**
     * 单价
     */
    private String unit_price;

//    public String getUnit_price() {
//        return ModelUtil.formatMoneyOfNull(unit_price);
//    }
    /**
     * 期初数量
     */
    private Integer initial_number;
    /**
     * 入库数量
     */
    private Integer in_number;
    /**
     * 出库数量
     */
    private Integer out_number;
    /**
     * 期末数量
     */
    private Integer final_number;
    /**
     * 总金额
     */
    private String total_amount;

//    public String getTotal_amount() {
//        return ModelUtil.formatMoneyOfNull(total_amount);
//    }

    /**
     * 部门
     */
    private Integer dept_id;
    /**
     * 部门名称
     */
    @Transient
    @TableField(exist = false)
    private String dept_name;
    /**
     * 领用人
     */
    private Integer user_id;
    /**
     * 领用人名称
     */
    @Transient
    @TableField(exist = false)
    private String user_name;
    /**
     * 类型(1:入库,2:出库)
     */
    private Integer type;

    /**
     * 规格型号名称
     */
    @Transient
    @TableField(exist = false)
    private String specifications_name;

    @Transient
    @TableField(exist = false)
    private Integer login_user;

    /**
     * 查询开始时间
     */
    @Transient
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date start_time;
    /**
     * 查询结束时间
     */
    @Transient
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date end_time;

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
}
