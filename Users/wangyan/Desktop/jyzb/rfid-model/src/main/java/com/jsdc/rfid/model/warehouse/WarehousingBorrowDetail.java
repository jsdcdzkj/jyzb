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
 * 借用详情
 */
@Entity
@TableName("warehousing_borrow_detail")
@Table(name = "warehousing_borrow_detail")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehousingBorrowDetail extends Model<WarehousingBorrowDetail> implements Serializable {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //主表id
    private Integer warehousing_borrow_id;

    //仓库id
    private Integer warehouse_id;

    //仓库名称
    @Transient
    @TableField(exist = false)
    private String warehouse_name;

    //借出数量
    private Integer equip_num;

    //出场日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date produce_date;
}
