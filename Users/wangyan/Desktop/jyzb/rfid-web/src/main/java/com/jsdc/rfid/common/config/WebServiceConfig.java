package com.jsdc.rfid.common.config;

import com.jsdc.rfid.service.DataService;
import com.jsdc.rfid.service.DataServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

  /**
   * cxf前缀
   * */
  @Bean("cxfServletRegistration")
  public ServletRegistrationBean dispatcherServlet(){
    return new ServletRegistrationBean(new CXFServlet(),"/ws/*");
  }

  @Bean
  public DataService dataService() {
    return new DataServiceImpl();
  }

  @Bean(name = Bus.DEFAULT_BUS_ID)
  public SpringBus springBus() {return new SpringBus();}

  /**
   * 对外发布地址
   * @return1
   */
  @Bean
  public Endpoint endpoint( ) {
    EndpointImpl endpoint = new EndpointImpl(springBus(), dataService());
    endpoint.publish("/api");

    return endpoint;
  }
}