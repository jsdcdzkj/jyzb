package com.jsdc.rfid.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jsdc.rfid.common.G;
import com.jsdc.rfid.mapper.RFIDConfigMapper;
import com.jsdc.rfid.model.RFIDConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AliSmsUtil {

    //建议：提到yml配置文件中
    //固定资产驳回
    private static String pathRfidReject = "";
    //建议：提到yml配置文件中

    //固定资产审批
    private static String pathRfidSP = "";

    private static String pathRfidPass = "";


    @Autowired
    private RFIDConfigMapper rfidConfigMapper;

//
//    public static void main(String[] args) {
//////        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
//////        String val = s.format(new Date());
////////        httpClientRfidSP("领用", "张德泉", val,PathUtils.ZCSL_PANDING_APPROVE.getName(),"15262052123,15805204568");
//////        httpClientRfidReject("领用","节点一",val,"错别字太多",PathUtils.LYSQ_APPLY.getName(),"15262052123");
//        AliSmsUtil .passRfid("资产领用","pages/RFID/applyZc/index","15262052123");
//    }


    public  void getList(){
        List<RFIDConfig> list =  rfidConfigMapper.selectList(Wrappers.<RFIDConfig>lambdaQuery()
                .eq(RFIDConfig::getIs_del, G.ISDEL_NO)
        );
        if (CollectionUtils.isNotEmpty(list)){
            RFIDConfig rfidConfig = list.get(0);
            pathRfidReject = rfidConfig.getPathrfidreject();
            pathRfidSP = rfidConfig.getPathrfidsp();
            pathRfidPass= rfidConfig.getPathrfidpass();
        }
    }
    /**
     * 获取短信发送接口并传送参数（固定资产通知下一节点审批人）
     */
    public  void httpClientRfidSP(String title,String name, String time,String content, String sending_phone) {
        getList();
        if (StringUtils.isNotEmpty(pathRfidSP)){
            // 创建HttpClient实例
            HttpClient httpclient = HttpClients.createDefault();
            // 创建POST请求对象，并设置URL
            HttpPost httppost = new HttpPost(pathRfidSP);
            try {
                // 创建要传递的对象
                // 将对象转换为JSON格式的字符串
                JSONObject params = new JSONObject();
                params.put("title",title);
                params.put("name", name);
                params.put("time", time);
                params.put("sending_phone", sending_phone);
                params.put("content", content);
                // 设置请求头
                httppost.setHeader("Content-Type", "application/json");
                // 设置请求体
                StringEntity s = new StringEntity(params.toString(), "UTF-8");
                s.setContentType("application/json");
                httppost.setEntity(s);
                // 执行请求
                HttpResponse response = httpclient.execute(httppost);
                // 获取响应内容
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                // 处理响应结果
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取短信发送接口并传送参数（固定资产审批驳回）
     */
    public  void httpClientRfidReject(String title,String name, String time,String resons,String content, String sending_phone) {
        getList();

        if (StringUtils.isNotEmpty(pathRfidReject)){
            // 创建HttpClient实例
            HttpClient httpclient = HttpClients.createDefault();
            // 创建POST请求对象，并设置URL
            HttpPost httppost = new HttpPost(pathRfidReject);
            try {
                // 创建要传递的对象
                // 将对象转换为JSON格式的字符串
                JSONObject params = new JSONObject();
                params.put("title",title);
                params.put("name", name);
                params.put("time", time);
                params.put("resons",resons);
                params.put("sending_phone", sending_phone);
                params.put("content", content);
                // 设置请求头
                httppost.setHeader("Content-Type", "application/json");
                // 设置请求体
                StringEntity s = new StringEntity(params.toString(), "UTF-8");
                s.setContentType("application/json");
                httppost.setEntity(s);
                // 执行请求
                HttpResponse response = httpclient.execute(httppost);
                // 获取响应内容
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                // 处理响应结果
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    /**
     * 获取短信发送接口并传送参数（固定资产审批驳回）
     */
    public  void passRfid(String title,String content, String sending_phone) {
        getList();

        if (StringUtils.isNotEmpty(pathRfidPass)){
            // 创建HttpClient实例
            HttpClient httpclient = HttpClients.createDefault();
            // 创建POST请求对象，并设置URL
            HttpPost httppost = new HttpPost(pathRfidPass);
            try {
                // 创建要传递的对象
                // 将对象转换为JSON格式的字符串
                JSONObject params = new JSONObject();
                params.put("title",title);
                params.put("sending_phone", sending_phone);
                params.put("content", content);
                // 设置请求头
                httppost.setHeader("Content-Type", "application/json");
                // 设置请求体
                StringEntity s = new StringEntity(params.toString(), "UTF-8");
                s.setContentType("application/json");
                httppost.setEntity(s);
                // 执行请求
                HttpResponse response = httpclient.execute(httppost);
                // 获取响应内容
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                // 处理响应结果
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
