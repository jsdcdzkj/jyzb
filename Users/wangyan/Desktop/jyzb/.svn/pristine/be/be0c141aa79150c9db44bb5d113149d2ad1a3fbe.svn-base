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
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * ClassName: InventoryJob
 * Description: 盘点任务
 * date: 2022/4/24 16:43
 *
 * @author bn
 */
@Entity
@TableName("inventory_job")
@Table(name = "inventory_job")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryJob extends Model<InventoryJob> {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //盘点单号
    private String inventory_no;
    //盘点名称
    private String inventory_name;
    //计划开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date plan_start_time;
    //实际开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start_time;
    //完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end_time;
    //盘点方式 1.远程盘点、2.手持机盘点、3.扫码盘点、4.手工盘点
    private String inventory_status;
    // 下发手持机盘点 1 开始 2. 结束, 3.离线
    private String pos_flag;

    //是否允许使用人自盘 1.是 2 否
    private String is_inventory;
    //盘点范围 按1.部门、2.资产分类
    private String inventory_range;
    // 设备盘点状态 1.开始 2.结束
    private String device_flag;
    // 部门ids
    @Size(max = 6000) // 设置字段长度为50
    private String dept_ids;
    @Transient
    @TableField(exist = false)
    private List<SysDepartment> sysDepartments;
    @Transient
    @TableField(exist = false)
    private List<AssetsType> assetsTypes;
    // 资产品类ids
    @Size(max = 6000) // 设置字段长度为50
    private String asset_type_ids;
    //进度节点 1.未开始、2.开始、3.结束
    private String progress_node;


    //处理人
    private Integer handle_user;
    //处理意见
    private String handle_remark;
    // 离线下发进度 0.未开始 1.进行中
    private Integer is_lx_xf;
    // 离线回传 0.未开始 1.进行中
    private Integer is_lx_hc;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issue_time;


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
     * rfid标签集合
     */
    @Transient
    @TableField(exist = false)
    private String rfids;

    /**
     * 当前登陆人
     */
    @Transient
    @TableField(exist = false)
    private String current_user;
}
