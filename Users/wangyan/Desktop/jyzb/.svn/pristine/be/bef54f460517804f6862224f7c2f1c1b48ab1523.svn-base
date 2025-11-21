package com.jsdc.rfid.common.utils;

import com.jsdc.core.base.Base;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 无需Model注入，即可freemarker引用
 */
@Component
public class FreeMarkerUtils extends Base {

    private DateTimeFormatter DF_LONG = DateTimeFormat.forPattern("yyyyMMddHHmmss");

    /**
     * 字符串格式化显示日期yyyy-MM-dd HH:mm:ss
     */
    public String getFormatTime(String time) {
        if (notEmpty(time)) {
            time = DF_LONG.parseDateTime(time).toString("yyyy-MM-dd HH:mm:ss");
            return time;
        } else {
            return null;
        }
    }

    /**
     * 字符串格式化显示日期yyyy-MM-dd
     */
    public String getFormatTimeYMD(String time) {
        if (notEmpty(time)) {
            time = new DateTime(time.split(" ")[0] + "T" + time.split(" ")[1]).toString("yyyy-MM-dd");
            return time;
        } else {
            return null;
        }
    }

    public String getFormatTimeYMD(Object time) {
        return null;
    }

    public String getFormatTimeYMD(Date time) {
        if (notEmpty(time)) {
            return new DateTime(time).toString("yyyy-MM-dd");
        } else {
            return null;
        }
    }


    //获取年度
    public List<String> getFormatYears() {
        List<String> list = new ArrayList<>();
        DateTime dateTime = new DateTime();
        for (int i = 0; i < 3; i++) {
            list.add(dateTime.plusYears(-i).toString("yyyy"));
        }
        return list;
    }
}
