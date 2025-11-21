package com.jsdc.rfid.vo;


import com.jsdc.rfid.model.ApplySingle;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2020/3/5.
 */
@Data
public class ApplySingleVo extends ApplySingle implements Serializable {
    private String realName;
    private Integer applyId;
    private Integer praise;//好评
    private Integer commonly;//一般
    private Integer negative;//差评
    private String start;//开始时间
    private String end;//结束时间
    private Integer checkType;//今年/上月/自定义
    private Integer pageSize;
    private Integer pageIndex;
    private String typeState;
    private String stateName;
    private Integer picType;
    private String use_people;//领用人
    private String dw_name;//领用部门ID
    private String assetTypeName;//品类名称
    private String imgUrl;
    private String menu_type;//跳转相关页面
    private String logs;//日志
    private String files;
    //岗位中的数据权限
    private Integer data_permission;
    private List<Integer> ids;
    //微信端搜索传值标识
    private String transfer;
    /**
     * 经办人
     */
    private String manager;
    /**
     * 维修单位名称
     */
    private String unit;

    private String currentTaskName;
}
