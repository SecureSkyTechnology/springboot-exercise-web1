package sbe.web1.servlet;

import java.util.Locale;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

@WebListener
public class MyServletSessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    static final Logger LOG = LoggerFactory.getLogger(MyServletSessionListener.class);

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MessageSource messageSource;

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>!!+*>> MyServletSessionListener#attributeAdded(), ac check : {}, ms check : {}", appname, msg);
        HttpSession hs = se.getSession();
        LOG.info(">>!!+*>> MyServletSessionListener#attributeAdded(), session id: {}, bound name: {}, new value: {}",
                hs.getId(), se.getName(), se.getValue().toString());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>!!+*>> MyServletSessionListener#attributeRemoved(), ac check : {}, ms check : {}", appname, msg);
        HttpSession hs = se.getSession();
        LOG.info(
                ">>!!+*>> MyServletSessionListener#attributeRemoved(), session id: {}, bound name: {}, removed value: {}",
                hs.getId(), se.getName(), se.getValue().toString());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>!!+*>> MyServletSessionListener#attributeReplaced(), ac check : {}, ms check : {}", appname, msg);
        HttpSession hs = se.getSession();
        LOG.info(
                ">>!!+*>> MyServletSessionListener#attributeReplaced(), session id: {}, bound name: {}, replaced value: {}",
                hs.getId(), se.getName(), se.getValue().toString());
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>!!+*>> MyServletSessionListener#sessionCreated(), ac check : {}, ms check : {}", appname, msg);
        HttpSession hs = se.getSession();
        LOG.info(">>!!+*>> MyServletSessionListener#sessionCreated(), session id: {}", hs.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>!!+*>> MyServletSessionListener#sessionDestroyed(), ac check : {}, ms check : {}", appname, msg);
        HttpSession hs = se.getSession();
        LOG.info(">>!!+*>> MyServletSessionListener#sessionDestroyed(), session id: {}", hs.getId());
    }

}
