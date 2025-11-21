package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rfid_config")
@TableName("rfid_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RFIDConfig {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 是否为AB门,0多设备,1为AB门, 2为报警设备
     */
    private Integer isab;

    /**
     * 打印机配置地址
     */

    private String printconfigadd;

    /**
     * 固定资产驳回(短信地址)
     */
    private String pathrfidreject;
    /**
     * 固定资产通知下个审批人(短信地址)
     */
    private String pathrfidsp;
    /**
     * 流程结束（流程结束通知提交人）
     */
    private String pathrfidpass;
    /**
     * 登录页logo图
     */
    private String loginimage;
    /**
     * 登录页logo图左上角
     */
    private String logolightimage;
    /**
     * 预览地址
     */
    private String previewurl;
    /**
     * 定时任务地址
     */
    private String quartzurl;
    /**
     * 地图ip地址
     */
    private String mapip;
    /**
     * 统一门户地址
     */
    private String portalurl;
    /**
     * 统一门户系统id
     */
    private String appid;
    /**
     * 统一门户系统秘钥
     */
    private String privatekey;
    /**
     * 统一门户token
     */
    private String token;
    /**
     * 统一门户token日期
     */
    private Date tokendate;
    /**
     * 部署标识
     */
    private String deployflag;


    /**
     * 是否删除
     */
    private String is_del;

    /**
     * 创建人
     */
    private Integer create_user;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新人
     */
    private Integer update_user;

    /**
     * 更新时间
     */
    private Date update_time;
    /**
     * 系统版本标识(1.市检察院, 2.新沂法院, 3.公司)
     */
    private Integer systemcode;
}
