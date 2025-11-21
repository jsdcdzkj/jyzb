package com.jsdc.rfid.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import javax.persistence.Transient;


@Data
public class ConsumableVo extends Model<ConsumableVo> {


//耗材类型一级名称
    private String consumable_name;
    //耗材类型二级名称
    private String consumable_name2;

    //    预警值
    private String prewarning_value;

    /**
     * 库存数量
     */
    private Integer inventory_num;


    @Transient
    @TableField(exist = false)
    private Integer page ;

    @Transient
    @TableField(exist = false)
    private Integer limit ;

}
