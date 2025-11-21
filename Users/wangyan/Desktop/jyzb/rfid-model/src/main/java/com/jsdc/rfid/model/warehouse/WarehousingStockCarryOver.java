package com.jsdc.rfid.model.warehouse;

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
import java.util.Date;

/**
 * 月结记录
 */
@Entity
@TableName("warehousing_stock_carryover")
@Table(name = "warehousing_stock_carryover")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousingStockCarryOver extends Model<WarehousingStockCarryOver> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    //装备状态 0:库存 1:在用 2:处置
    private Integer equip_status;

    //所属部门id
    private Integer dept_id;

    private Integer equip_type;

    private Integer equip_num;

    private Integer year;

    private Integer month;

    /**
     * 创建人id
     */
    private Integer create_user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date create_time;

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
