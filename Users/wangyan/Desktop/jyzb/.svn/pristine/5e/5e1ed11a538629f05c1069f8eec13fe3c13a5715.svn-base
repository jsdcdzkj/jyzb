package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * ClassName: InventoryDetail
 * Description: 盘点详情
 * date: 2022/4/24 16:49
 *
 * @author bn
 */
@Entity
@TableName("inventory_detail")
@Table(name = "inventory_detail")
@Data
public class InventoryDetail extends Model<InventoryDetail> {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //盘点任务id
    private Integer inventory_job_id;
    //资产id 关联盘盈
    private Integer asset_id;
    // rfid
    private String rfid;

    // 资产名称
    private String asset_name;

    //盘点状态 1.应盘、2.未盘、3.正常、4.盘盈、5.盘亏、6.异常
    private String inventory_status;
    //盘点方式 1.远程盘点、2.手持机盘点、3.扫码盘点、4.手工盘点. 5.离线盘点
    private String inventory_type;

    //确认人
    private Integer confirm_user;
    //正常状态备注
    private String confirm_remark;
    // 异常备注
    private String error_remark;
     //报修id 关联报修
    private Integer apply_single_id;
    //处置id 关联处置
    private Integer management_id;

    // 是否处理 1：已处理 2.为处理
    private String is_deal;

    /**
     *  关联资产
     */
    @Transient
    @TableField(exist = false)
    private AssetsManage assetsManage;


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
