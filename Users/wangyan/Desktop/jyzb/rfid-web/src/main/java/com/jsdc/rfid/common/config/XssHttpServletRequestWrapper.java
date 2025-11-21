package com.jsdc.rfid.common.config;


import com.jsdc.rfid.common.utils.EmojiFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 防止sql注入,xss攻击
 * 前端可以对输入信息做预处理，后端也可以做处理。
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static String key = "and|exec|execute|insert|select|delete|update|count|drop|%|chr|mid|master|truncate|" +
            "char|declare|sitename|net user|xp_cmdshell|;|or|+|like'|and|exec|execute|insert|create|drop|" +
            "table|from|grant|use|group_concat|column_name|" +
            "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|" +
            "chr|mid|master|truncate|char|declare|--|like|#|$";
    private static Set<String> notAllowedKeyWords = new HashSet<String>(0);
    private static String replacedString = "";

    static {
        String keyStr[] = key.split("\\|");
        for (String str : keyStr) {
            notAllowedKeyWords.add(str);
        }
    }

    private String currentUrl;

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
        currentUrl = servletRequest.getRequestURI();
    }


    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> values = super.getParameterMap();
        if (values == null) {
            return null;
        }
        Map<String, String[]> result = new HashMap<>();
        for (String key : values.keySet()) {
            String encodedKey = cleanXSS(key);
            int count = values.get(key).length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = cleanXSS(values.get(key)[i]);
            }
            result.put(encodedKey, encodedValues);
        }
        return result;
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = null;
        if (!name.contains("Accept")){
            value = super.getHeader(name);
        }
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String valueP) {
        // valueP = ESAPI.encoder().canonicalize(valueP);

        // 避免空字符串
        valueP = valueP.replaceAll("", "");

        // 避免script 标签
        Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
        valueP = scriptPattern.matcher(valueP).replaceAll("");


        // 删除单个的 </script> 标签
        scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        valueP = scriptPattern.matcher(valueP).replaceAll("");

        // 删除单个的<script ...> 标签
        scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        valueP = scriptPattern.matcher(valueP).replaceAll("");

        // 避免 eval(...) 形式表达式
        scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        valueP = scriptPattern.matcher(valueP).replaceAll("");

        // 避免 e­xpression(...) 表达式
        scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        valueP = scriptPattern.matcher(valueP).replaceAll("");

        // 避免 javascript: 表达式
        scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        valueP = scriptPattern.matcher(valueP).replaceAll("");

        // 避免 vbscript: 表达式
        scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
        valueP = scriptPattern.matcher(valueP).replaceAll("");

        // 避免 οnlοad= 表达式
        scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        valueP = scriptPattern.matcher(valueP).replaceAll("");

        // 避免 onXX= 表达式
        scriptPattern = Pattern.compile("onclick(.+?)(?=\")", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        valueP = scriptPattern.matcher(valueP).replaceAll("");
        scriptPattern = Pattern.compile("onchange(.+?)(?=\")", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        valueP = scriptPattern.matcher(valueP).replaceAll("");
        scriptPattern = Pattern.compile("onsubmit(.+?)(?=\")", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        valueP = scriptPattern.matcher(valueP).replaceAll("");
        scriptPattern = Pattern.compile("onblur(.+?)(?=\")", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        valueP = scriptPattern.matcher(valueP).replaceAll("");

        return cleanSqlKeyWords(valueP);
    }
    private String cleanSqlKeyWords(String value) {
        String paramValue = value;
        for (String keyword : notAllowedKeyWords) {
            if (paramValue.length() > keyword.length() + 4
                    && (paramValue.contains(" " + keyword) || paramValue.contains(keyword + " ") || paramValue.contains(" " + keyword + " "))) {
                paramValue = StringUtils.replace(paramValue, keyword, replacedString);
            }
        }
        // % 报错
        paramValue = StringUtils.replace(paramValue, "%", "");
        paramValue = java.net.URLDecoder.decode(paramValue);
        paramValue = EmojiFilter.filterEmoji(paramValue);
        // dWKKP00DQp5M5ezn/yJf/ofZwittwgAFZS6WEDVsXcAuiuRLZYzSyVnr5dzB25rbgkXGCf1OV/PeeWPlwxF5rPItKvzk1avCN1lSXrbL5T2mO8xE6R83xQkmIS6vWINFl9qHux2l7NNByYOTdN5+11mzj4fe00ucptstJ3fMPNE=
        String reg = "";
        Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        paramValue = sqlPattern.matcher(paramValue).replaceAll("");
        return paramValue;
    }

}
