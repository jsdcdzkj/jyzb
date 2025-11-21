package com.jsdc.rfid.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum AssetsStatusEnums {
    IDLE(0,"闲置"),
    USE(1,"使用"),
    USED(2,"领用"),
    BORROW(3,"借用"),
    TRANSFER(4,"调拨"),
    FAULT(5,"故障"),
    DISPOSE_OF(6,"处置"),
    ABNORMAL(7,"异常"),
    ;

    AssetsStatusEnums(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private Integer type;

    private String desc;

    private Integer type() {
        return this.type;
    }

    private String desc() {
        return this.desc;
    }

    public static String getValue(Integer type) {
        AssetsStatusEnums[] carTypeEnums = values();
        for (AssetsStatusEnums carTypeEnum : carTypeEnums) {
            if (carTypeEnum.type().equals(type)) {
                return carTypeEnum.desc();
            }
        }
        return null;
    }

    public static Integer getType(String desc) {
        AssetsStatusEnums[] carTypeEnums = values();
        for (AssetsStatusEnums carTypeEnum : carTypeEnums) {
            if (carTypeEnum.desc().equals(desc)) {
                return carTypeEnum.type();
            }
        }
        return null;
    }

    // 得到所有的枚举,以集合的形式返回
    public static List<Map<String,Object>> getAll() {
        List<Map<String,Object>> list = new ArrayList<>();
        for (AssetsStatusEnums c : AssetsStatusEnums.values()) {
            Map<String,Object> map = new HashMap<>();
            map.put("type", c.type());
            map.put("desc", c.desc());
            list.add(map);
        }
        return list;
    }
}
