package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ClassName: InventoryWarehousingMember
 * Description:  入库库存关联表
 *
 * @author zhangdequan
 */
@Entity
@TableName("inventory_warehousing_member")
@Table(name = "inventory_warehousing_member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryWarehousingMember extends Model<InventoryWarehousingMember> implements Serializable {

    /**
     * 主键标识
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 库存id
     */
    private Integer inventory_id;
    /**
     * 入库id
     */
    private Integer warehousing_id;

    /**
     * 是否删除 0：未删除 1：已删除
     */
    private String is_del;

}
