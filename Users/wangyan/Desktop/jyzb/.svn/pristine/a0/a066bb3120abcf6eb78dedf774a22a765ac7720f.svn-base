package com.jsdc.rfid;


import cn.hutool.core.date.DateUtil;
import net.hasor.spring.boot.EnableHasor;
import net.hasor.spring.boot.EnableHasorWeb;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

@EnableHasor()
@EnableHasorWeb()
@SpringBootApplication
@EnableScheduling
@CrossOrigin
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        System.setProperty("spring.devtools.restart.enabled", "true");
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Application.class, args);
        Environment environment = configurableApplicationContext.getBean(Environment.class);
        System.out.println("\n============> 系统启动成功！后台地址：http://localhost:" + environment.getProperty("server.port")
                + "\n============> 数据库连接地址: " + environment.getProperty("spring.datasource.url"));
    }

    @Bean
    public TomcatServletWebServerFactory tomcatEmbeddedServletContainerFactory(){
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if(connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>){
                ( (AbstractHttp11Protocol<?>)connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return factory;
    }
}
