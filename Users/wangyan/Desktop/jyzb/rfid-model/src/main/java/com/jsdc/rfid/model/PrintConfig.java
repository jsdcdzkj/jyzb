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
@Table(name = "print_config")
@TableName("print_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintConfig {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 该对象是TrueType文字
     */
    private String objectname;
    /**
     * 文字内容
     */
    private String objectdata;
    /**
     * 文字X坐标，单位mm
     */
    private Integer xposition;
    /**
     * 文字Y坐标，单位mm
     */
    private Integer yposition;
    /**
     * 文字的字体
     */
    private String textfont;
    /**
     * 粗体
     */
    private Integer fontstyle;
    /**
     * 文字的大小
     */
    private Integer fontsize;
    /**
     * 条码的横向缩放系数，1~99，数字越大条码越宽
     */
    private Integer barcodescale;
    /**
     * 排序
     */
    private Integer sort;



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
}
