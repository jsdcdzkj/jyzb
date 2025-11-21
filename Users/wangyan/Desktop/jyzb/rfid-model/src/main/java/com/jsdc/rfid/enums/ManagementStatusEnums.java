package com.jsdc.rfid.enums;

import lombok.Getter;

@Getter
public enum ManagementStatusEnums {
    SCRAP("2","报废"),
    LOST("3","遗失"),
    TRANSFER1("4","无偿调拨"),
    DONATE("5","对外捐赠"),
    SELL1("6","出售"),
    SELL2("7","出让"),
    TRANSFER2("8","转让"),
    PERMUTATION("9","置换"),
    ;

    ManagementStatusEnums(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private String type;

    private String desc;

    private String type() {
        return this.type;
    }

    private String desc() {
        return this.desc;
    }

    public static String getValue(String type) {
        ManagementStatusEnums[] carTypeEnums = values();
        for (ManagementStatusEnums carTypeEnum : carTypeEnums) {
            if (carTypeEnum.type().equals(type)) {
                return carTypeEnum.desc();
            }
        }
        return null;
    }

    public static String getType(String desc) {
        ManagementStatusEnums[] carTypeEnums = values();
        for (ManagementStatusEnums carTypeEnum : carTypeEnums) {
            if (carTypeEnum.desc().equals(desc)) {
                return carTypeEnum.type();
            }
        }
        return null;
    }
}
