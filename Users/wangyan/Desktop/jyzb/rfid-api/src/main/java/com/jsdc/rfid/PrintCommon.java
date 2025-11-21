package com.jsdc.rfid;

import com.ZMPrinter.*;
import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.mapper.PrintConfigMapper;
import com.jsdc.rfid.mapper.RFIDConfigMapper;
import com.jsdc.rfid.model.AssetsManage;
import com.jsdc.rfid.model.PrintConfig;
import com.jsdc.rfid.model.RFIDConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 打印机公共类
 *
 * @author Administrator
 */
@Component
public class PrintCommon {
    private zmprinter thiszmprinter;
    private zmlabel thiszmlabel;

    private static RFIDConfigMapper rfidConfigMapper = SpringUtils.getBean(RFIDConfigMapper.class);

    private static PrintConfigMapper printConfigMapper = SpringUtils.getBean(PrintConfigMapper.class);

    private static PrintCommon instance;

    public static PrintCommon getInstance() {
        if (instance == null) {
            instance = new PrintCommon();
        }
        return instance;
    }

    private PrintCommon() {

        // 查到rfid配置类
        List<RFIDConfig> configs = rfidConfigMapper.selectList(Wrappers.<RFIDConfig>lambdaQuery().eq(RFIDConfig::getIs_del, "0"));
        String address = "";
        if(CollectionUtils.isEmpty(configs)){
            address = "192.168.0.132";
        }else{
            address = configs.get(0).getPrintconfigadd();
        }
        thiszmprinter = new zmprinter();
        //打印机接口类型
        thiszmprinter.printerinterface = printerstyle.NET;
        //打印机名称，网络打印机可自定义
        thiszmprinter.printername = "ZMIN Printer";
        //打印机分辨率，203dpi、300dpi或600dpi
        thiszmprinter.printerdpi = 203;
        //打印速度，
        thiszmprinter.printSpeed = 4;
        //打印黑度
        thiszmprinter.printDarkness = 10;
        //打印机的ip地址，端口号可以不设置，默认是9100
        thiszmprinter.printernetip = address;
        thiszmlabel = new zmlabel();
        //标签的宽度，单位是mm
        thiszmlabel.labelwidth = 75;
        //标签的高度，单位是mm
        thiszmlabel.labelheight = 33;
        //标签之间的行间距，单位是mm
        thiszmlabel.labelrowgap = 2;
    }

    public void printRFID(AssetsManage bean) {
        //定义打印操作对象
        printutility zmprintoperate = new printutility();
        //定义标签上需要打印的对象列表
        List<zmlabelobject> thiszmlabelobjectlist = new ArrayList<>();
        //region 定义标签内需要打印的各个对象
        //清空列表，重新添加
        thiszmlabelobjectlist.clear();

        List<PrintConfig> printConfigs = printConfigMapper.selectList(Wrappers.<PrintConfig>lambdaQuery()
                .eq(PrintConfig::getIs_del, "0").orderByAsc(PrintConfig::getSort)
        );

        for (int i = 0; i< printConfigs.size(); i++){
            PrintConfig printConfig = printConfigs.get(i);
            //定义标签上需要打印的对象
            zmlabelobject thiszmlabelobject = new zmlabelobject();
            //该对象是TrueType文字
            thiszmlabelobject.objectName = printConfig.getObjectname();
            //文字内容
            String content = "";
            switch (i){
                case 0:
                    content = printConfig.getObjectdata();
                    break;
                case 1:
                    content = bean.getAsset_code();
                    break;
                case 2:
                    if("财务编码".equals(printConfig.getObjectdata())) {
                        content = printConfig.getObjectdata() + ":" + (null == bean.getFinance_code()?"":bean.getFinance_code());
                    }else{
                        content = printConfig.getObjectdata() + ":" + (null == bean.getUse_people_name()?"":bean.getUse_people_name());
                    }
                    break;
                case 3:
                    content = printConfig.getObjectdata() + ":" + (null == bean.getAsset_name()?"":bean.getAsset_name());
                    if(StringUtils.length(bean.getAsset_name())>7){
                        //定义标签上需要打印的对象
                        zmlabelobject thiszmlabelobject1 = new zmlabelobject();
                        thiszmlabelobject1.objectName = printConfig.getObjectname();
                        thiszmlabelobject1.objectdata = "" + bean.getAsset_name().substring(7);
                        thiszmlabelobject1.Xposition = printConfig.getXposition() + 5;
                        thiszmlabelobject1.Yposition = printConfig.getYposition() + 5;
                        thiszmlabelobject1.textfont = printConfig.getTextfont();
                        thiszmlabelobject1.fontstyle = printConfig.getFontstyle();
                        thiszmlabelobject1.fontsize = printConfig.getFontsize();
                        thiszmlabelobjectlist.add(thiszmlabelobject1);

                        content = printConfig.getObjectdata() + ":" + (null == bean.getAsset_name()?"":bean.getAsset_name().substring(0,7));
                    }
                    break;
                default:
                    break;
            }
            if ("barcode".equals(printConfig.getObjectname())){
                //该对象是条码
                thiszmlabelobject.objectName = printConfig.getObjectname();
                //条码内容
                thiszmlabelobject.objectdata = bean.getAsset_code();
                //条码X坐标，单位mm
                thiszmlabelobject.Xposition = printConfig.getXposition();
                //条码Y坐标，单位mm
                thiszmlabelobject.Yposition = printConfig.getYposition();
                //条码的类型
                thiszmlabelobject.barcodekind = barcodestyle.QR_Code;
                //条码的横向缩放系数，1~99，数字越大条码越宽
                thiszmlabelobject.barcodescale = printConfig.getBarcodescale();
                //将条码对象添加进对象列表
                thiszmlabelobjectlist.add(thiszmlabelobject);
            }else{
                thiszmlabelobject.objectdata = content;
                //条码X坐标，单位mm
                thiszmlabelobject.Xposition = printConfig.getXposition();
                //条码Y坐标，单位mm
                thiszmlabelobject.Yposition = printConfig.getYposition();
                //文字的字体
                thiszmlabelobject.textfont = printConfig.getTextfont();
                //粗体
                thiszmlabelobject.fontstyle = printConfig.getFontstyle();
                //文字的大小
                thiszmlabelobject.fontsize = printConfig.getFontsize();
                //将文字对象添加进对象列表
                thiszmlabelobjectlist.add(thiszmlabelobject);
            }

        }

//        zmlabelobject thiszmlabelobject01 = new zmlabelobject();
//        thiszmlabelobject01.objectName = "truetype";//该对象是TrueType文字
//        thiszmlabelobject01.objectdata = "资产编号";//文字内容
//        thiszmlabelobject01.Xposition = 5;//文字X坐标，单位mm
//        thiszmlabelobject01.Yposition = 1;//文字Y坐标，单位mm
//        thiszmlabelobject01.textfont = "黑体";//文字的字体
//        thiszmlabelobject01.fontstyle = 1;//粗体
//        thiszmlabelobject01.fontsize = 10;//文字的大小
//        thiszmlabelobjectlist.add(thiszmlabelobject01);//将文字对象添加进对象列表
//
//
//        zmlabelobject thiszmlabelobject02 = new zmlabelobject();
//        thiszmlabelobject02.objectName = "truetype";//该对象是TrueType文字
//        thiszmlabelobject02.objectdata = bean.getAsset_code();//文字内容
//        thiszmlabelobject02.Xposition = 5;//文字X坐标，单位mm
//        thiszmlabelobject02.Yposition = 8;//文字Y坐标，单位mm
//        thiszmlabelobject02.textfont = "黑体";//文字的字体
//        thiszmlabelobject02.fontstyle = 1;//粗体
//        thiszmlabelobject02.fontsize = 10;//文字的大小
//        thiszmlabelobjectlist.add(thiszmlabelobject02);//将文字对象添加进对象列表
//
//        zmlabelobject thiszmlabelobject03 = new zmlabelobject();
//        thiszmlabelobject03.objectName = "truetype";//该对象是TrueType文字
////        thiszmlabelobject03.objectdata = "使用人：" + (null == bean.getUse_people_name()?"":bean.getUse_people_name());//文字内容
//        thiszmlabelobject03.objectdata = "财务编码：" + (null == bean.getFinance_code()?"":bean.getFinance_code());//文字内容
//        thiszmlabelobject03.Xposition = 5;//文字X坐标，单位mm
//        thiszmlabelobject03.Yposition = 13.5f;//文字Y坐标，单位mm
//        thiszmlabelobject03.textfont = "宋体";//文字的字体
//        thiszmlabelobject03.fontstyle = 1;//粗体
//        thiszmlabelobject03.fontsize = 10;//文字的大小
//        thiszmlabelobjectlist.add(thiszmlabelobject03);//将文字对象添加进对象列表
//
//        zmlabelobject thiszmlabelobject04 = new zmlabelobject();
//        zmlabelobject thiszmlabelobject07 = new zmlabelobject();
//        if(StringUtils.isNotBlank(bean.getAsset_name())){
//            if(StringUtils.length(bean.getAsset_name())>7){
//                thiszmlabelobject04.objectName = "truetype";//该对象是TrueType文字
//                thiszmlabelobject04.objectdata = "名称:" + bean.getAsset_name().substring(0,7);//文字内容
//                thiszmlabelobject04.Xposition = 5;//文字X坐标，单位mm
//                thiszmlabelobject04.Yposition = 18;//文字Y坐标，单位mm
//                thiszmlabelobject04.textfont = "宋体";//文字的字体
//                thiszmlabelobject04.fontstyle = 1;//粗体
//                thiszmlabelobject04.fontsize = 10;//文字的大小
//                thiszmlabelobjectlist.add(thiszmlabelobject04);//将文字对象添加进对象列表
//
//                thiszmlabelobject07.objectName = "truetype";//该对象是TrueType文字
//                thiszmlabelobject07.objectdata = "" + bean.getAsset_name().substring(7);//文字内容
//                thiszmlabelobject07.Xposition = 5;//文字X坐标，单位mm
//                thiszmlabelobject07.Yposition = 22;//文字Y坐标，单位mm
//                thiszmlabelobject07.textfont = "宋体";//文字的字体
//                thiszmlabelobject07.fontstyle = 1;//粗体
//                thiszmlabelobject07.fontsize = 10;//文字的大小
//                thiszmlabelobjectlist.add(thiszmlabelobject07);//将文字对象添加进对象列表
//            }else{
//                thiszmlabelobject04.objectName = "truetype";//该对象是TrueType文字
//                thiszmlabelobject04.objectdata = "名称:" + bean.getAsset_name();//文字内容
//                thiszmlabelobject04.Xposition = 5;//文字X坐标，单位mm
//                thiszmlabelobject04.Yposition = 18;//文字Y坐标，单位mm
//                thiszmlabelobject04.textfont = "宋体";//文字的字体
//                thiszmlabelobject04.fontstyle = 1;//粗体
//                thiszmlabelobject04.fontsize = 10;//文字的大小
//                thiszmlabelobjectlist.add(thiszmlabelobject04);//将文字对象添加进对象列表
//            }
//        }
//
//
//
//        zmlabelobject thiszmlabelobject05 = new zmlabelobject();
//        thiszmlabelobject05.objectName = "barcode";//该对象是条码
//        thiszmlabelobject05.objectdata = bean.getAsset_code();//条码内容
//        thiszmlabelobject05.Xposition = 55;//条码X坐标，单位mm
//        thiszmlabelobject05.Yposition = 5;//条码Y坐标，单位mm
//        thiszmlabelobject05.barcodekind = barcodestyle.QR_Code;//条码的类型
//        thiszmlabelobject05.barcodescale = 8;//条码的横向缩放系数，1~99，数字越大条码越宽
//        thiszmlabelobjectlist.add(thiszmlabelobject05);//将条码对象添加进对象列表


        zmlabelobject thiszmlabelobject06 = new zmlabelobject();
        thiszmlabelobject06.objectName = "rfiduhf";//该对象是RFID读写
        thiszmlabelobject06.objectdata = bean.getAsset_code();//RFID需要写入的内容
        thiszmlabelobject06.RFIDDatablock = 0;//写入区域，0为EPC，1为USER
        thiszmlabelobject06.RFIDDatatype = 0;//数据类型：0为文本，1为16进制
        thiszmlabelobject06.RFIDerrortimes = 1;//错误重试次数
        thiszmlabelobjectlist.add(thiszmlabelobject06);//将RFID对象添加进对象列表

        //region 将标签生成打印数据
        byte[] zmpclecommand = zmprintoperate.CreateLabelCommand(thiszmprinter, thiszmlabel, thiszmlabelobjectlist);//生成标签数据
        if (zmpclecommand == null) {
            System.out.println("生成标签数据异常为空.");
            throw new RuntimeException("生成标签数据异常为空!");
        }
        System.out.println("生成标签数据完成，长度：" + zmpclecommand.length);
        //endregion

        //region 发送数据给打印机
        zmprintoperate.PrintLabelCommandbyNet(thiszmprinter, zmpclecommand);//发送数据给打印机

    }
}
