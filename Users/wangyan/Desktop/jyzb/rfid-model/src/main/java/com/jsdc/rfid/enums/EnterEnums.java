package com.jsdc.rfid.enums;

import lombok.Getter;

@Getter
public enum EnterEnums {

     PURCHASE(2,"采购"),
     MANUAL(1,"手动")
    ;
    private Integer type;

    private String desc;

    EnterEnums(Integer type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public static String getDesc(Integer type) {
        EnterEnums[] enterEnums = values();
        for (EnterEnums enterEnum : enterEnums) {
            if (enterEnum.type.equals(type)) {
                return enterEnum.desc;
            }
        }
        return null;
    }

}
