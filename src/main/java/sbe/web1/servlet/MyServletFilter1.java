package sbe.web1.servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

@WebFilter(filterName = "MyServletFilter1", urlPatterns = "/*")
public class MyServletFilter1 implements Filter {
    static final Logger LOG = LoggerFactory.getLogger(MyServletFilter1.class);

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MessageSource messageSource;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>##>>>> MyServletFilter1#init(), ac check : {}, ms check : {}", appname, msg);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>##>>>> MyServletFilter1#doFilter(), ac check : {}, ms check : {}", appname, msg);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>##>>>> MyServletFilter1#destroy(), ac check : {}, ms check : {}", appname, msg);
    }

}
