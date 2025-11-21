package com.jsdc.rfid.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.github.pagehelper.PageHelper;
import com.jsdc.rfid.common.interceptor.MySqlConverInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Properties;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType("mysql");
        return paginationInterceptor;
    }

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        properties.setProperty("aggregateFunctions", "FORCE,INDEX,CONCAT");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

//    @Bean
//    public SqlSessionFactory sqlSessionFactory(SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
//        // 设置拦截器
//        sqlSessionFactoryBean.setPlugins(Collections.singletonList(new MySqlConverInterceptor()));
//        return sqlSessionFactoryBean.getObject();
//    }
    @Bean
    public MySqlConverInterceptor mySqlConverInterceptor(){
        MySqlConverInterceptor mySqlConverInterceptor = new MySqlConverInterceptor();
        return mySqlConverInterceptor;
    }
}
