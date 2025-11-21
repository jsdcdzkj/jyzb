package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 图片表
 *
 * @author zonglina
 */
@Entity
@Table(name = "picture_img")
@TableName("picture_img")
@Data
@DynamicInsert
@DynamicUpdate
public class PictureImg implements Serializable {

    //主键
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //图片上传路径
    private String picurl;
    //申请单表
    private Integer typeid;
    //标记是否删除
    private Integer isdelete;
    //类型(1维修人员反馈,2.申请单表)
    private Integer type;


}
