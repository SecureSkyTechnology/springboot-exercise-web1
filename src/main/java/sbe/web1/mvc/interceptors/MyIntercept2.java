package sbe.web1.mvc.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * {@link org.springframework.web.servlet.HandlerInterceptor}
 * の実装例として、独自のレスポンスヘッダーを追加してみる + postHandleで ModelAndViewのビュー名をログ出力してみる。
 */
public class MyIntercept2 implements HandlerInterceptor {
    static final Logger LOG = LoggerFactory.getLogger(MyIntercept2.class);

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        LOG.info("#2######## preHandle(), handler = {}", handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView mav)
            throws Exception {
        LOG.info("#2######## postHandle(), handler = {}, ModelAndView = {}", handler, mav);
        if (null != mav) {
            LOG.info("#2######## postHandle(), view = " + mav.getViewName());
        }
        res.addHeader("X-MyIntercept2", "hello");
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex)
            throws Exception {
        LOG.info("#2######## afterCompletion(), handler = {}, Exception = {}", handler, ex);
    }
}
