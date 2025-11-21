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
 * ClassName: AssetsManage
 * Description:  资产管理
 *
 * @author zhangdequan
 */
@Entity
@TableName("assets_manage")
@Table(name = "assets_manage")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetsManage extends Model<AssetsManage> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     *  资产状态 0-7
     *  0"闲置",1"使用",2"领用",3"变更",4"调拨",5"故障",6"处置",7"异常"
     */
    private Integer asset_state;
    /**
     *  处置状态 1-报废, 2-遗失
     */
    private Integer dispose_state;
    /**
     *  资产状态-记住之前的资产状态(报修模块使用)
     */
    private Integer asset_state_bx;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     *  单价
     */
    private String unit_price;
//    /**
//     * 重写单价的get方法
//     */
//    public String getUnit_price() {
//        if (unit_price == null) {
//            return "0";
//        }
//        // 判断金额后缀是否有小数点两位,如果没有则补全
//        if (unit_price.indexOf(".") == -1) {
//            unit_price = unit_price + ".00";
//        } else {
//            String[] split = unit_price.split("\\.");
//            if (split[1].length() == 1) {
//                unit_price = unit_price + "0";
//            }
//        }
//        return unit_price;
//    }
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
    private Integer position_id;
    /**
     *  购置日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date purchase_time;
    /**
     *  购置金额
     */
    private String purchase_amount;
//    /**
//     *  重写购置金额的get方法
//     */
//    public String getPurchase_amount() {
//        return ModelUtil.formatMoney(purchase_amount);
//    }
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
    private Integer supplier_id;
    /**
     * 供应商名称
     */
    @Transient
    @TableField(exist = false)
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date guarantee_end_time;
    /**
     *  处置日期（变卖、报废、遗失）
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
     *  备注(已改为当前位置)
     */
    private String remarks;
    /**
     * 备注
     */
    private String remarks2;
    /**
     *  二维码
     */
    private String qr_code;

    /**
     *  RFID
     *  资产标签EPC
     */
    private String rfid;

    /**
     *  RFID
     *  资产标签EPC
     */
    @Transient
    @TableField(exist = false)
    private String RFIDlist;
    /**
     * 登记类型 0.库外资产登记、1.库内资产登记
     */
    private Integer register_type;
    /**
     * 财务编码
     */
    private String finance_code;
    /**
     * 登记日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date register_time;
    /**
     * 折旧日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date depreciation_time;

    /**
     * 序列号
     */
    private String serial_number;
    /**
     * mac地址
     */
    private String mac_address;
    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date production_time;


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
     * 位置变动时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date position_change_time;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;
    /**
     * 资产标签EPC
     */
    @Transient
    @TableField(exist = false)
    private String asset_epc;
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


    @Transient
    @TableField(exist = false)
    private Integer inventory_job_id;
    /**
     * 创建人名称
     */
    @Transient
    @TableField(exist = false)
    private String create_user_name;

    /**
     * 品牌名称
     */
    @Transient
    @TableField(exist = false)
    private String brand_name;

    /**
     * 购置时间String
     */
    @Transient
    @TableField(exist = false)
    private String purchase_time_str;
    /**
     * 保修截至时间String
     */
    @Transient
    @TableField(exist = false)
    private String guarantee_end_time_str;
    /**
     * 报废日期String
     */
    @Transient
    @TableField(exist = false)
    private String scrap_time_str;
    /**
     * 计量单位名称
     */
    @Transient
    @TableField(exist = false)
    private String unit_name;

    /**
     * 库存id
     */
    @Transient
    @TableField(exist = false)
    private Integer inventoryManagement_id;

    /**
     * 净残值
     */
    @Transient
    @TableField(exist = false)
    private String net_residual_value;
    /**
     * 折旧日期String
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Transient
    @TableField(exist = false)
    private String depreciation_time_str;
    /**
     * 已使用月份
     */
    @Transient
    @TableField(exist = false)
    private String used_month;
    /**
     * 本月折旧
     */
    @Transient
    @TableField(exist = false)
    private String this_month_depreciation;
    /**
     * 累计折旧
     */
    @Transient
    @TableField(exist = false)
    private String accumulated_depreciation;
    /**
     * 净值
     */
    @Transient
    @TableField(exist = false)
    private String net_value;

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

    /**
     * 排除的id集合
     */
    @Transient
    @TableField(exist = false)
    private List<Integer> excludeIdList = new ArrayList<>();

    /**
     * 展示状态
     */
    private Integer pos_show_type;

    /**
     * 是否打印 0：未打印 1：已打印
     */
    private Integer is_print;
    //申请人
    @Transient
    @TableField(exist = false)
    private String user_id;

    //报修传值
    @Transient
    @TableField(exist = false)
    private String bx_type;


    /**
     *  前台选中值
     */
    @Transient
    @TableField(exist = false)
    private Integer state;
    /**
     *  前台选中值
     */
    @Transient
    @TableField(exist = false)
    private String checkName;
    /**
     *  前台选中值
     */
    @Transient
    @TableField(exist = false)
    private Integer checkId;

    @Transient
    @TableField(exist = false)
    private String asset_status_name;

    /**
     * 资产图片id
     */
    private Integer asset_img_id;

    /**
     * 资产图片
     */
    @Transient
    @TableField(exist = false)
    private FileManage imgFile = new FileManage();

    /**
     * 白名单标识 0：非白名单 1：白名单
     */
    @Transient
    @TableField(exist = false)
    private String white_flag;
}
