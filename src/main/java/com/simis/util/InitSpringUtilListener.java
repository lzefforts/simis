package com.simis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by 一拳超人 on 17/3/31.
 */
public class InitSpringUtilListener implements ServletContextListener {
    private Logger log = LoggerFactory.getLogger(InitSpringUtilListener.class);

    public InitSpringUtilListener() {
    }

    public void contextDestroyed(ServletContextEvent evt) {
    }

    public void contextInitialized(ServletContextEvent evt) {
        this.log.info("开始初始化SpringUtil...");
        ServletContext ctx = evt.getServletContext();
        SpringUtil.init(ctx);
        String webRootAbsPath = evt.getServletContext().getRealPath("/");
        this.log.info("web root abs path=" + webRootAbsPath);
        SpringUtil.setWebRootAbsPath(webRootAbsPath);
        this.log.info("SpringUtil初始化完成.");
    }
}

