package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName: InventorySurplus
 * Description: 盘点盘盈
 * date: 2022/4/24 16:53
 *
 * @author bn
 */
@Entity
@TableName("inventory_surplus")
@Table(name = "inventory_surplus")
@Data
public class InventorySurplus extends Model<InventorySurplus> {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *  盘点明细id
     */
    private Integer inventory_job_id;



    /**
     * 资产编号
     */
    private String asset_code;
    /**
     *  资产品类
     */
    private Integer asset_type_id;
    /**
     *  资产名称
     */
    private String asset_name;
    /**
     *  规格型号
     */
    private String specification;
    /**
     *  品牌id 【字典表 value值，字典类型为brand】
     */
    private Integer brand_id;
    /**
     *  资产状态
     */
    private Integer asset_state;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     *  单价
     */
    private String unit_price;
    /**
     * 计量单位
     */
    private String unit;
    /**
     *  存放部门
     */
    private Integer dept_id;
    /**
     *  存放位置
     */
    private Integer place_id;
    /**
     *  购置日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date purchase_time;
    /**
     *  购置金额
     */
    private String purchase_amount;
    /**
     *  所属管理员
     */
    private Integer admin_user;
    /**
     *  使用人
     */
    private Integer use_people;
    /**
     *  英文名称
     */
    private String esname;
    /**
     *  供应商名称
     */
    private String supplier_name;
    /**
     *  登记人
     */
    private Integer register_user;
    /**
     *  厂牌号码
     */
    private String brand_number;
    /**
     *  保修期
     */
    private Integer guarantee;
    /**
     *  保修截止
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date guarantee_end_time;
    /**
     *  处置日期（变卖、报废、遗失）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date scrap_time;
    /**
     *  规定年限
     */
    private Integer age_limit;
    /**
     *  开启折旧
     */
    private Integer is_depreciation;
    /**
     *  折旧方法
     */
    private String depreciation_method;
    /**
     *  净残率
     */
    private String net_residual_rate;
    /**
     *  备注
     */
    private String remarks;
    /**
     *  二维码
     */
    @Column(name = "qr_code",columnDefinition="text")
    private String qr_code;
    /**
     *  RFID
     */
    private String rfid;
    /**
     * 登记类型 0.库外资产登记、1.库内资产登记
     */
    private Integer register_type;
    /**
     * 登记日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date register_time;

    /**
     * 位置变动时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date position_change_time;
    /**
     * 折旧日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date depreciation_time;

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
     * 存放部门名称
     */
    @Transient
    @TableField(exist = false)
    private String dept_name;

    /**
     * 资产品类名称
     */
    @Transient
    @TableField(exist = false)
    private String asset_type_name;

    /**
     * 存放位置名称
     */
    @Transient
    @TableField(exist = false)
    private String place_name;

    /**
     * 所属管理员名称
     */
    @Transient
    @TableField(exist = false)
    private String admin_user_name;

    /**
     * 使用人名称
     */
    @Transient
    @TableField(exist = false)
    private String use_people_name;

    /**
     * 登记人名称
     */
    @Transient
    @TableField(exist = false)
    private String register_user_name;



    /**
     * 创建人名称
     */
    @Transient
    @TableField(exist = false)
    private String create_user_name;

    /**
     * 附件管理集合
     */
    @Transient
    @TableField(exist = false)
    private List<FileManage> fileManageList = new ArrayList<>();

    /**
     * 附件管理集合 关联表
     */
    @Transient
    @TableField(exist = false)
    private List<Integer> fileMemberList = new ArrayList<>();

    /**
     * 状态集合
     */
    @Transient
    @TableField(exist = false)
    private List<String> assetStatusList = new ArrayList<>();
}
