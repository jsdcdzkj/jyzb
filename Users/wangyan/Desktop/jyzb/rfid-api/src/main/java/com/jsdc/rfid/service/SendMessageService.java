//package com.jsdc.rfid.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.bouncycastle.tsp.TSPUtil;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import vo.SendMessageVo;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.SocketTimeoutException;
//import java.net.URL;
//
//@Slf4j
//@Service
//@Transactional
//public class SendMessageService {
////    private static String wx_access_token = "";
////
////
////    /**
////     * 获取微信小程序链接短码
////     * path 跳转小程序路径
////     *
////     * @param path
////     * @return
////     */
////    public  String getOpenLink(String path) {
////        //接口地址
////        String httpUrl = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=";
////        String AccessToken = wx_access_token;
////        if (StringUtils.isEmpty(AccessToken)) {
////            AccessToken = getAccessToken();
////            if (StringUtils.isEmpty(AccessToken)){
////                return null;
////            }
////        }
////        try {
////            String content = "";
////            JSONObject jsonParam = new JSONObject();
////            jsonParam.put("path", path);
////            String params = jsonParam.toString();
////            CloseableHttpClient httpClient = HttpClients.createDefault();
////            RequestConfig requestConfig = RequestConfig.custom()
////                    .setSocketTimeout(300 * 1000)
////                    .setConnectTimeout(300 * 1000)
////                    .build();
////            HttpPost post = new HttpPost(httpUrl + AccessToken);
////            post.setConfig(requestConfig);
////            post.setHeader("Content-Type", "application/json;charset=utf-8");
////            StringEntity postingString = new StringEntity(params, "utf-8");
////            post.setEntity(postingString);
////            CloseableHttpResponse response = httpClient.execute(post);
////            content = EntityUtils.toString(response.getEntity());
////            JSONObject resultUrl = JSONObject.parseObject(content);
////            String code = resultUrl.getString("errcode");
////            //证明token过期
////            if ("40001".equals(code)){
////                getAccessToken();
////                String value = getOpenLink(path);
////                return value;
////
////            }else {
////                String newUrl = resultUrl.getString("url_link");
//////                System.out.println("WeChatInterfaceUtil.getWeixinUrl--" + newUrl);
////                return newUrl;
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////            return null;
////        }
////    }
////
////
////    /**
////     * 通过appid 以及 secret 等参数获取微信的assess_token
////     *
////     * @return
////     */
////    public  String getAccessToken() {
////        String access_token = null;
////        HttpURLConnection con = null;
////        BufferedReader buffer = null;
////        StringBuffer resultBuffer = null;
////        try {
////            URL url = new URL("https://api.weixin.qq.com/cgi-bin/token?appid=wxc45a3bfd9815cee0&secret=370a08a8915c6bf4acb77c3be3c1228d&grant_type=client_credential");
////            //得到连接对象
////            con = (HttpURLConnection) url.openConnection();
////            //设置请求类型
////            con.setRequestMethod("GET");
////            //设置Content-Type，此处根据实际情况确定
////            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
////            //允许写出
////            con.setDoOutput(true);
////            //允许读入
////            con.setDoInput(true);
////            //不使用缓存
////            con.setUseCaches(false);
////            //得到响应码
////            int responseCode = con.getResponseCode();
////            if (responseCode == HttpURLConnection.HTTP_OK) {
////                //得到响应流
////                InputStream inputStream = con.getInputStream();
////                //将响应流转换成字符串
////                resultBuffer = new StringBuffer();
////                String line;
////                buffer = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
////                while ((line = buffer.readLine()) != null) {
////                    resultBuffer.append(line);
////                }
////
////                String result = resultBuffer.toString();
////                SendMessageVo sendMessageVo = JSONObject.parseObject(result, SendMessageVo.class);
////                access_token = sendMessageVo.getAccess_token();
////                wx_access_token = access_token;
////                return access_token;
////            }
////        } catch (Exception e) {
////            return access_token;
////        }
////        return access_token;
////    }
////
////
////
////
////    public static void main(String[] args) {
////        SendMessageService sendMessageService = new SendMessageService();
////
////        //需要跳转的小程序路径
////        String path= "pages/index/index";
////        String value = sendMessageService.getOpenLink(path);
////        System.out.println(value);
////
////
////
////
////    }
//}
