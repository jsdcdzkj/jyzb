package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户表
 */
@TableName("sys_user")
@Entity
@Table(name = "sys_user")
@Getter
@Setter
public class SysUser extends Model<SysUser> implements Serializable {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 姓名
     */
    private String user_name;
    /**
     * 登录名
     */
    private String login_name;
    /**
     * 密码
     */
    private String password;
    /**
     * 数字证书编码
     */
    private String numCode;
    /**
     * 部门
     */
    private Integer department;
    /**
     * 岗位
     */
    private Integer post;
    /**
     * 用户编码
     */
    private String user_code;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 联系方式
     */
    private String telephone;
    /**
     * 头像
     */
    private String head_portrait;
    /**
     * 是否启用
     */
    private Integer is_enable;
    /**
     * 是否为部门负责人 0:否 1:是
     */
    private Integer is_dept_leader;
    /**
     * 拼音
     */
    private String pinyin;

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

    /**
     * OA id
     */
    private long oa_user_id;

    /**
     * wx openId
     */
    private String openid;

    /**
     * 扫码签到，微信openid
     */
    private String sign_openid;

    /**
     * 是否离职 0:否 1:是
     */
    private Integer oa_is_use;
    /**
     * 是否同步统一登录 0,否, 1,是
     */
    private String isloginty;
    /**
     * 是否为仓库管理员 0:否 1:是
     */
    private Integer is_warehouse_manager;

    /**
     * 是否导入 0：否 1：是
     */
    private String is_import = "0";

    @Transient
    @TableField(exist = false)
    private String dept_name;
    @Transient
    @TableField(exist = false)
    private String new_time;
    @Transient
    @TableField(exist = false)
    private String position_name;

    @Transient
    @TableField(exist = false)
    private String inIds;

    @Transient
    @TableField(exist = false)
    private String noIds;

    @Transient
    @TableField(exist = false)
    private List<String> deptIds;

    @Transient
    @TableField(exist = false)
    private SysPost sysPost = new SysPost();

    public SysUser() {
    }

    public SysUser(Integer department) {
        this.department = department;
    }

}
