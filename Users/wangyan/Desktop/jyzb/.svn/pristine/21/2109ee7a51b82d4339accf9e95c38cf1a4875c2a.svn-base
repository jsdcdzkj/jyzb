package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商表
 */
@TableName("supplier")
@Entity
@Table(name = "supplier")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  Supplier extends Model<Supplier> implements Serializable {

    /**
     * 自增id
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 供应商名称
     */
    private String supplier_name;
    /**
     * 供应商编码
     */
    private String supplier_code;
    /**
     * 拼音简码
     */
    private String pinyin;
    /**
     * 公司全称
     */
    private String company_name;
    /**
     * 地址
     */
    private String address;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系方式
     */
    private String telephone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 证照（附件）
     */
    private Integer fileid;
    /**
     * 经营范围
     */
    private String business_scope;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否启用
     */
    private Integer is_enable;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date update_time;

    /**
     * 创建人
     */
    private Integer create_user;

    /**
     * 更新人
     */
    private Integer update_user;

    /**
     * 是否删除 0:否 1:是
     */
    private String is_del;

}
