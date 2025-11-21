package com.jsdc.rfid.enums;

import java.util.List;

public enum ProcessEnums {
    DAYU("1","大于"),
    DAYU_DENG("2","大于等于"),
    XIAOYU("3","小于"),
    XIAOYU_DENG("4","小于等于"),
    DENG("5","等于"),
    BUDENG("6","不等于"),
    // 品类等于
    PINDENG("11","资产品类包含"),
        // 品类不等于
    PINBUDENG("12","资产品类不包含"),
    // 耗材品类等于
    HAODENG("13","耗材类型包含"),
    // 耗材品类不等于
    HAOBUDENG("14","耗材类型不包含"),

    ;

    private String code;
    private String name;

    ProcessEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code) {
        ProcessEnums[] carTypeEnums = values();
        for (ProcessEnums carTypeEnum : carTypeEnums) {
            if (carTypeEnum.getCode().equals(code)) {
                return carTypeEnum.getName();
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        ProcessEnums[] carTypeEnums = values();
        for (ProcessEnums carTypeEnum : carTypeEnums) {
            if (carTypeEnum.getName().equals(name)) {
                return carTypeEnum.getCode();
            }
        }
        return null;
    }

    /**
     * 根据code进行计算
     */
    public static boolean calculate(String code, Integer value2, Integer currentNum, List<Integer> types) {
        switch (code) {
            case "1":
                return currentNum > value2;
            case "2":
                return currentNum >= value2;
            case "3":
                return currentNum < value2;
            case "4":
                return currentNum <= value2;
            case "5":
                return currentNum.equals(value2);
            case "6":
                return !currentNum.equals(value2);
            case "11":
                // 判断value 是否包含在types中
                return types.contains(value2);
            case "12":
                return !types.contains(value2);
            case "13":
                return types.contains(value2);
            case "14":
                return !types.contains(value2);
            default:
                return false;
        }
    }
}
