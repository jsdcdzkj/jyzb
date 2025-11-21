package vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 耗材数据统计vo
 */
@Data
public class ConsDataVo implements Serializable {

    private Integer id;
    //名称
    private String name;
    //数量
    private Integer amount;
    //时间
    private String time;
    //申领总数
    private Integer apply_num;
    //出库总数
    private Integer out_num;
    //耗材名称id-耗材类别id
    private String nameTypeId;
    //耗材名称-耗材类别
    private String nameType;

    //日均消耗/领用量
    private String daysApplyNum;
    // 当前库存
    private Integer stockNum;
    // 预计可用天数
    private String daysNum;
    // 父级部门
    private Integer parent_id;

}
