package com.simis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * Created by 一拳超人 on 17/3/31.
 */
public class SpringUtil {
    private static Logger log = LoggerFactory.getLogger(SpringUtil.class);
    private static ApplicationContext context;
    static String webRootAbsPath;

    private SpringUtil() {
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static void init(ServletContext sc) {
        log.info("加载上下文");
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
    }

    public static void init(String... configLocations) {
        log.info("加载配置");
        context = new ClassPathXmlApplicationContext(configLocations);
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static void init(ApplicationContext actx) {
        context = actx;
    }

    public static void setWebRootAbsPath(String webRootAbsPath) {
        webRootAbsPath = webRootAbsPath;
    }

    public static String getWebRootAbsPath() {
        return webRootAbsPath;
    }
}
