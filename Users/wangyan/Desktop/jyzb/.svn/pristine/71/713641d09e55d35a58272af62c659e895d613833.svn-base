package com.jsdc.rfid.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: ExcelListener
 * Description: 回调监视器
 * date: 2022/3/21 8:44
 *
 * @author bn
 */
@Getter
public class ExcelListener extends AnalysisEventListener {

    private List datas = new ArrayList<>();

    /**
     * 读取Excel文件
     * @param o
     * @param analysisContext
     */
    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        if (o != null) {
            datas.add(o);
            Field[] declaredFields = o.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(CheckNoNull.class)) {
                    declaredField.setAccessible(true);
                    // 得到待解密的串
                    try {
                        Object checkObj = declaredField.get(o);
                        if (checkObj == null) {
                            datas.remove(o);
                        }
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}

