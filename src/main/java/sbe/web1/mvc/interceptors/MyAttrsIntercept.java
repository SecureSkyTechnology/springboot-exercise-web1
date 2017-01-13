package sbe.web1.mvc.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class MyAttrsIntercept extends HandlerInterceptorAdapter {
    static final Logger LOG = LoggerFactory.getLogger(MyAttrsIntercept.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LOG.info("#4######## MyAttrsIntercept#preHandle()");
        ServletWebRequest wr = new ServletWebRequest(request);
        if (null == wr.getAttribute("myAttrBean", WebRequest.SCOPE_REQUEST)) {
            MyAttrBean rb1 = new MyAttrBean("requestAttr1");
            wr.setAttribute("myAttrBean", rb1, WebRequest.SCOPE_REQUEST);
            LOG.info("#4######## MyAttrsIntercept#preHandle() : rb1 set {}", rb1);
        }
        if (null == wr.getAttribute("rb2", WebRequest.SCOPE_REQUEST)) {
            MyAttrBean rb2 = new MyAttrBean("requestAttr2");
            wr.setAttribute("rb2", rb2, WebRequest.SCOPE_REQUEST);
            LOG.info("#4######## MyAttrsIntercept#preHandle() : rb2 set {}", rb2);
        }
        String sid = wr.getSessionId();
        if (null == wr.getAttribute("myAttrBean", WebRequest.SCOPE_SESSION)) {
            MyAttrBean sb1 = new MyAttrBean("sessionAttr1:" + sid);
            wr.setAttribute("myAttrBean", sb1, WebRequest.SCOPE_SESSION);
            LOG.info("#4######## MyAttrsIntercept#preHandle() : sb1 set {}", sb1);
        }
        if (null == wr.getAttribute("sb2", WebRequest.SCOPE_SESSION)) {
            MyAttrBean sb2 = new MyAttrBean("sessionAttr2:" + sid);
            wr.setAttribute("sb2", sb2, WebRequest.SCOPE_SESSION);
            LOG.info("#4######## MyAttrsIntercept#preHandle() : sb2 set {}", sb2);
        }
        return super.preHandle(request, response, handler);
    }

}
