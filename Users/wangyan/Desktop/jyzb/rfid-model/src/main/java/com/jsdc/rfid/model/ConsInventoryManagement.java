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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: ConsInventoryManagement
 * Description: 易耗品库存管理
 *
 * @author zhangdequan
 */
@Entity
@TableName("cons_inventory_management")
@Table(name = "cons_inventory_management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsInventoryManagement extends Model<ConsInventoryManagement> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 耗材类别
     */
    private Integer asset_type_id;
    //耗材类型ID
    private Integer consumable_id;
    //标签
    private String name;
    private String label;
    //型号
    private String model;
    /**
     * 耗材名称id
     */
    private Integer asset_name_id;
    /**
     * 耗材名称
     */
    @Transient
    @TableField(exist = false)
    private String asset_name;

    //耗材类型名称
    @Transient
    @TableField(exist = false)
    private String consumable_name;
    /**
     * 规格型号
     */
    private Integer specifications;
    /**
     * 型号名称
     */
    @Transient
    @TableField(exist = false)
    private String specifications_name;
    /**
     * 仓库id
     */
    private Integer warehouse_id;
    /**
     * 仓库名称
     */
    @TableField(exist = false)
    @Transient
    private String warehouse_name;
    /**
     * 单价
     */
    private String unit_price;

//    public String getUnit_price() {
//        return ModelUtil.formatMoneyOfNull(unit_price);
//    }
    /**
     * 实际金额
     */
    private String actual_amount;

//    public String getActual_amount() {
//        return ModelUtil.formatMoney(actual_amount);
//    }
    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date warehousing_time;
    /**
     * 入库方式,1-采购入库,2-手动入库
     */
    private Integer warehousing_mode;
    /**
     * 库存数量
     */
    private Integer inventory_num;
    /**
     * 计量单位
     */
    private Integer unit_of_measurement;
    /**
     * 品牌
     */
    private Integer brand_id;
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
     * 品牌名称
     */
    @Transient
    @TableField(exist = false)
    private String brand_name;
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
     * 入库单集合
     */
    @Transient
    @TableField(exist = false)
    private List<WarehousingManagement> warehousingManagementList = new ArrayList<>();

    /**
     * 耗材类别名称
     */
    @Transient
    @TableField(exist = false)
    private String assetTypeName;

    /**
     * 创建人名称
     */
    @Transient
    @TableField(exist = false)
    private String createUserName;

    /**
     * 计量单位名称
     */
    @Transient
    @TableField(exist = false)
    private String unit_name;

    @Transient
    @TableField(exist = false)
    private Integer state;

    @Transient
    @TableField(exist = false)
    private Integer category_id;
    @Transient
    @TableField(exist = false)
    private Integer assets_type_id;
    @Transient
    @TableField(exist = false)
    private Integer specification_id;
}
