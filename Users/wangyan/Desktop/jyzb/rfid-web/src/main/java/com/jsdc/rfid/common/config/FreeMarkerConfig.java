//package com.jsdc.rfid.common.config;
//
//import com.jagregory.shiro.freemarker.ShiroTags;
//import com.jsdc.core.common.utils.FreeMarkerUtils;
//import freemarker.template.TemplateModelException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.ServletContext;
//
//@Configuration
//public class FreeMarkerConfig {
//    @Autowired
//    protected FreeMarkerConfigurer freeMarkerConfigurer;
//
//    @Autowired
//    private ServletContext servletContext;
//
//    @PostConstruct
//    public void setSharedVariable() throws TemplateModelException {
//        freeMarkerConfigurer.getConfiguration().setSharedVariable("utils", new FreeMarkerUtils());
//        freeMarkerConfigurer.getConfiguration().setSharedVariable("shiro", new ShiroTags());
//        freeMarkerConfigurer.getConfiguration().setSharedVariable("ctx", servletContext.getContextPath());
//    }
//
//}
