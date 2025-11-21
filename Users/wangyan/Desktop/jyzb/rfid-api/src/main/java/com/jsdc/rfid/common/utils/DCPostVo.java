package com.jsdc.rfid.common.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DCPostVo {
    private long id;//ID
    private String name;// 岗位名称
    private Integer sortId;// 排序号
    private Integer isDeleted;// 是否删除 0 正常 1.删除
    private Integer enabled;// 是否启用 0 启用 1.停用
    private String description;// 描述
}
