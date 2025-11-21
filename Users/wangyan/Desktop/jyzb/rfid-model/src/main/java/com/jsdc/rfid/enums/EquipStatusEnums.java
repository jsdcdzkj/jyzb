package com.jsdc.rfid.enums;

import lombok.Getter;

@Getter
public enum EquipStatusEnums {

    STOCK(0,"库存"),
    INUSE(1,"在用")
    ;
    private Integer type;

    private String desc;

    EquipStatusEnums(Integer type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public static String getDesc(Integer type) {
        EquipStatusEnums[] enterEnums = values();
        for (EquipStatusEnums enterEnum : enterEnums) {
            if (enterEnum.type.equals(type)) {
                return enterEnum.desc;
            }
        }
        return null;
    }
}
