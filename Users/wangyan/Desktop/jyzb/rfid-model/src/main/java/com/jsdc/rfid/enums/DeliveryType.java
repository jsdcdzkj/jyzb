package com.jsdc.rfid.enums;

import lombok.Getter;

@Getter
public enum DeliveryType {
    DIAOBO(1,"调拨"),
    JIEYONG(2,"借用"),
    CHUZHI(3,"处置")

    ;
    private Integer type;

    private String desc;

    DeliveryType(Integer type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public static String getDesc(Integer type) {
        DeliveryType[] deliveryTypeEnums = values();
        for (DeliveryType  delivery: deliveryTypeEnums) {
            if (delivery.type.equals(type)) {
                return delivery.desc;
            }
        }
        return null;
    }
}
