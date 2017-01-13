package sbe.web1.mvc.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MyIntercept1 implements HandlerInterceptor {
    static final Logger LOG = LoggerFactory.getLogger(MyIntercept1.class);

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        LOG.info("#1######## preHandle(), handler = {}", handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView mav)
            throws Exception {
        LOG.info("#1######## postHandle(), handler = {}, ModelAndView = {}", handler, mav);
        res.addHeader("X-MyIntercept1", "morning");
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex)
            throws Exception {
        LOG.info("#1######## afterCompletion(), handler = {}, Exception = {}", handler, ex);
    }
}
