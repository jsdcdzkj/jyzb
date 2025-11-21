package com.jsdc.rfid.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * ClassName: AssetsFileMember
 * Description: 资产管理附件关联表
 *
 * @author zhangdequan
 */
@Entity
@TableName("assets_file_member")
@Table(name = "assets_file_member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetsFileMember {

    /**
     * 编号
     */
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资产管理编号
     */
    private Integer assets_id;

    /**
     * 附件编号
     */
    private Integer file_id;

}
