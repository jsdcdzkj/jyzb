package com.jsdc.rfid.common.springmvc;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 日期编辑器
 * <p>
 * 根据日期字符串长度判断是长日期还是短日期。只支持yyyy-MM-dd，yyyy-MM-dd HH:mm:ss两种格式。
 * 扩展支持yyyy,yyyy-MM日期格式
 */
@Component
public class StringToDateConverter implements Converter<String, Date> {

    public static final DateTimeFormatter DF_LONG = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DF_SHORT = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DF_YEAR = DateTimeFormat.forPattern("yyyy");
    public static final DateTimeFormatter DF_MONTH = DateTimeFormat.forPattern("yyyy-MM");
    /**
     * 短类型日期长度
     */
    public static final int SHORT_DATE = 10;
    public static final int YEAR_DATE = 4;
    public static final int MONTH_DATE = 7;

    @Override
    public Date convert(String source) {
        source = source.trim();
        if (!StringUtils.hasText(source)) {
            return null;
        }
        if (source.length() <= YEAR_DATE) {
            return DF_YEAR.parseDateTime(source).toDate();
        } else if (source.length() <= MONTH_DATE) {
            return DF_MONTH.parseDateTime(source).toDate();
        } else if (source.length() <= SHORT_DATE) {
            return DF_SHORT.parseDateTime(source).toDate();
        } else {
            return DF_LONG.parseDateTime(source).toDate();
        }
    }
}
