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
import java.util.Date;

/**
 * ClassName: PurchaseDetail
 * Description: 采购明细
 * date: 2022/4/24 15:10
 *
 * @author bn
 */
@Entity
@TableName("purchase_detail")
@Table(name = "purchase_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDetail extends Model<PurchaseDetail> {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //采购申请id
    private Integer purchase_apply_id;
    // 入库单id
    private Integer warehousing_manage_id;
    //资产品类id 关联资产品类
    private Integer assets_type_id;
    // 资产品类名
    @Transient
    @TableField(exist = false)
    private String assets_type_name;
    //资产名称 根据采购明细中的资产品类id，去重资产名称
    private String assets_name;
    //资产型号 根据采购明细中的资产品类id，去重资产名称，去重型号
    private String assets_model;
    //资产品牌
    private Integer assets_brand;
    //采购单价/预算单价
    private String purchase_price;
    // 实际单价
    private String actual_price;
    // 验收数量
    private String accept_num;
    // 入库数量
    private Integer inbound_num;
    //采购数量
    private Integer purchase_num;
    //计量单位 关联字典
    private Integer units;
    @Transient
    @TableField(exist = false)
    private String units_name;
    @Transient
    @TableField(exist = false)
    private String brand_name;

    // 仓库id
    private Integer warehouse_id;

    /**
     * 异常原由
     * zdq
     */
    private String exception_reason;


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


    @Transient
    @TableField(exist = false)
    private Integer userId;

    @Transient
    @TableField(exist = false)
    private Integer updateIndex;

}
