package com.jsdc.rfid.common.security;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import net.hasor.web.annotation.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vo.ResultInfo;

import javax.servlet.http.HttpServletRequest;


@Configuration
public class SatokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            SaRouter.notMatch("/main.do")
                    .check(r -> {
                        HttpServletRequest request =
                                ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
                        String token = request.getHeader("accessToken");
                        if(StrUtil.isEmpty(token)){
                            SaRouter.back(JSONObject.toJSON(ResultInfo.errorToken("无访问权限")));
                        }
                    });        // 要执行的校验动作，可以写完整的 lambda 表达式
        })).addPathPatterns("/**").excludePathPatterns(
                HttpMethod.OPTIONS.toString(), "/css/**", "/js/**", "/images/**", "/layuiadmin/**", "/fonts/**",
                "/homenew/**", "/hoem/**", "/layer_v2.1/**", "/libs/**", "/Plugin/**",
                "/pos/**", "/login.do","/loginNumber.do", "/main.do", "/post/toTree.do").order(-10000);
    }

    // 获取配置Bean (以代码的方式配置Sa-Token, 此配置会覆盖yml中的配置)
    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();
        config.setTokenName("accessToken");             // token名称 (同时也是cookie名称)
        config.setTimeout(30 * 24 * 60 * 60);       // token有效期，单位s 默认2天
        config.setActivityTimeout(-1);              // token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒
        config.setIsConcurrent(true);               // 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)
        config.setIsShare(false);                    // 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)
        config.setTokenStyle("uuid");               // token风格
        config.setIsReadHead(true);                 // 是否尝试从header里读取token
        config.setIsReadCookie(false);              // 是否尝试从cookie里读取token
        config.setIsLog(true);                     // 是否输出操作日志
        config.setAutoRenew(true);                  // 自动续签，指定时间内有操作，则会自动续签
        return config;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("accessToken")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
