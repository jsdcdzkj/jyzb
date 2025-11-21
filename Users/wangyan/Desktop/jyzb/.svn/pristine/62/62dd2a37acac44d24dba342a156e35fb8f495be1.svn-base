package com.jsdc.rfid.model.util;

import net.hasor.utils.StringUtils;

public class ModelUtil {

    //金额显示格式化
    public static String formatMoney(String money) {
        if (StringUtils.isBlank(money) || money.equals("") || ".00".equals(money.trim())) {
            return "0.00";
        }
        String[] moneyArr = money.split("\\.");
        if (moneyArr.length == 1) {
            return money + ".00";
        } else if (moneyArr.length == 2) {
            if (moneyArr[1].length() == 1) {
                return money + "0";
            } else {
                return money;
            }
        } else {
            return "0.00";
        }
    }

    //金额显示格式化
    public static String formatMoneyOfNull(String money) {
        if (StringUtils.isBlank(money) || ".00".equals(money.trim())) {
            return "";
        }
        // 判断金额后缀是否有小数点两位,如果没有则补全
        if (money.indexOf(".") == -1) {
            money = money + ".00";
        } else {
            String[] split = money.split("\\.");
            if (split[1].length() == 1) {
                money = money + "0";
            }
        }
        return money;
    }
}
