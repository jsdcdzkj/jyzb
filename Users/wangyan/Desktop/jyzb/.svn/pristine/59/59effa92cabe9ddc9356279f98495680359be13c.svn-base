package com.jsdc.rfid.common.config;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/druid/*",
        initParams = {
                @WebInitParam(name = "loginUsername", value = "admin"),// 用户名
                @WebInitParam(name = "loginPassword", value = "jsdc@123"),// 密码
                @WebInitParam(name = "resetEnable", value = "false")
        }
)
public class DruidConfig extends StatViewServlet {
    private static final long serialVersionUID = 1L;
}
