package com.company.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;

public class WebAppInit implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(ApplicationContextConfig.class);
        annotationConfigApplicationContext.refresh();
        ApplicationContextConfig applicationContextConfig = annotationConfigApplicationContext.getBean(ApplicationContextConfig.class);
        container.setAttribute("applicationContext", applicationContextConfig);
    }

}