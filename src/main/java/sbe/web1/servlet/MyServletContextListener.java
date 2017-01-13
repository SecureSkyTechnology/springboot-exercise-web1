package sbe.web1.servlet;

import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    static final Logger LOG = LoggerFactory.getLogger(MyServletContextListener.class);

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MessageSource messageSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>!!!!>> MyServletContextListener#contextInitialized(), ac check : {}, ms check : {}", appname, msg);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>!!!!>> MyServletContextListener#contextDestroyed(), ac check : {}, ms check : {}", appname, msg);
    }

}
