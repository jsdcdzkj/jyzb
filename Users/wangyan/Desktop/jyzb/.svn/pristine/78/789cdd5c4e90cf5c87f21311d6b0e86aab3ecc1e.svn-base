package com.jsdc.rfid.common.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DCUserVo {

    private long id;//Id，唯一标识人员
    private String name;//姓名
    private String loginName;//登录名
    private String pwd;// 密码
    private Integer sortId;//排序号
    private String description;//说明
    private long orgAccountId;//人员所属单位Id
    private long orgLevelId;//人员职务级别Id

    private long orgPostId;//人员岗位Id

    private long orgDepartmentId;//人员所属部门Id


    private String telNumber;//手机号码
    private Integer enabled;//账户状态：true为启用，false为停用
    private Integer isDeleted;//删除标记：true为已删除
    private Integer isInternal;//是否内部人员
    private Integer isLoginable;//是否可登录
    private Integer isAssigned;//是否已分配人员
    private Integer isAdmin;//是否管理员，集团管理员、系统管理员、审计管理员或单位管理员返回true，否则为false
    private Integer isValid;//是否可用：已删除、被停用、不可登录、未分配或离职人员返回false，否则返回true
    private Integer state;//人员状态：1为在职，2 为离职
    private Integer status;//实体状态：1为正常、2为申请停用、3为申请删除、4为申请调离；此属性不常用，一般返回1
    private String birthday;//出生日期
    private String officeNum;//办公电话
    private String emailAddress;//电子邮件
    private Integer gender;//性别：-1为未指定、1为男、2为女
    private String location;//工作地


}
