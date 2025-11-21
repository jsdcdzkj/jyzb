package com.jsdc.rfid.utils;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * ClassName: ExcelUtil
 * Description: Excel 工具类
 * date: 2022/3/21 8:44
 *
 * @author bn
 */
@Slf4j
public final class ExcelUtils {
    /**
     * 防止实例化
     */
    private ExcelUtils() {}

    /**
     * 解析Excel文件
     * @param file Excel文件
     * @param objectClazz Excel解析后的转换对象类
     * @param <T>
     * @return 返回解析的数据（列表）
     */
    public static <T> List<T> read(MultipartFile file, Class objectClazz) {
        ExcelListener excelListener = new ExcelListener();
        try {
            EasyExcel.read(file.getInputStream(), objectClazz, excelListener).sheet().doRead();
            return excelListener.getDatas();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析Excel文件
     * @param file Excel文件
     * @param objectClazz Excel解析后的转换对象类
     * @param <T>
     * @return 返回解析的数据（列表）
     */
    public static <T> List<T> read(File file, Class objectClazz) {
        ExcelListener excelListener = new ExcelListener();
        try {
            EasyExcel.read(new FileInputStream(file), objectClazz, excelListener).sheet().doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return excelListener.getDatas();
    }

    /**
     * 设置响应流方法
     *
     * @param response
     * @param fileName
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
