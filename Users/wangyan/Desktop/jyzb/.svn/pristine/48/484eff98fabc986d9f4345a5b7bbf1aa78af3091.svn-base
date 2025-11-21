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

/**
 * 正在运行的节点表
 */
@Entity
@Table(name = "process_member_node")
@TableName("process_member_node")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessMemberNode {

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 流程id
     */
    private Integer process_member_id;
    /**
     * 节点id
     */
    private Integer node_id;
    /**
     * 完成状态 0-未完成，1-已完成, 2-未经过
     */
    private Integer finish_status;
    /**
     * 办理类型0-岗位,1-人员,
     */
    private Integer assignee_type;
    /**
     * 办理人
     */
    private Integer assignee;



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
