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
 * ClassName: InventoryManagement
 * Description: 库存管理
 *
 * @author zhangdequan
 */
@Entity
@TableName("inventory_management")
@Table(name = "inventory_management")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryManagement extends Model<InventoryManagement> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 资产类别
     */
    private Integer asset_type_id;
    /**
     * 资产名称
     */
    private String asset_name;
    /**
     * 规格型号
     */
    private String specifications;
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

    @TableField(exist = false)
    @Transient
    private String assetsTypeName;
    /**
     * 单价
      */
    private String unit_price;
//    /**
//     * 重写单价
//     */
//    public String getUnit_price() {
//        return ModelUtil.formatMoneyOfNull(unit_price);
//    }
    /**
     * 实际单价
     */
    private String actual_amount;
//    /**
//     * 重写单价
//     */
//    public String getActual_amount() {
//        return ModelUtil.formatMoneyOfNull(actual_amount);
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
     * 资产类别名称
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
    private String timeStr;


}
