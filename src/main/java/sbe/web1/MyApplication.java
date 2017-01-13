package sbe.web1;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.SessionCookieConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

    public static void logdumpHttpServletRequest(Logger log, HttpServletRequest sr) {
        log.info("===================== logdumpHttpServletRequest START");
        log.info("ServletRequest.getCharacterEncoding()=" + sr.getCharacterEncoding());
        log.info("ServletRequest.getContentLength()=" + sr.getContentLength());
        log.info("ServletRequest.getContentType()=" + sr.getContentType());
        log.info("ServletRequest.getLocale()=" + sr.getLocale());
        for (Locale loc : Collections.list(sr.getLocales())) {
            log.info("ServletRequest.getLocales()=" + loc);
        }
        log.info("ServletRequest.getLocalAddr()=" + sr.getLocalAddr());
        log.info("ServletRequest.getLocalName()=" + sr.getLocalName());
        log.info("ServletRequest.getLocalPort()=" + sr.getLocalPort());
        log.info("ServletRequest.getProtocol()=" + sr.getProtocol());
        log.info("ServletRequest.getRemoteAddr()=" + sr.getRemoteAddr());
        log.info("ServletRequest.getRemoteHost()=" + sr.getRemoteHost());
        log.info("ServletRequest.getRemotePort()=" + sr.getRemotePort());
        log.info("ServletRequest.getScheme()=" + sr.getScheme());
        log.info("ServletRequest.getServerName()=" + sr.getServerName());
        log.info("ServletRequest.getServerPort()=" + sr.getServerPort());
        log.info("ServletRequest.isSecure()=" + sr.isSecure());
        log.info("ServletRequest.isAsyncSupported()=" + sr.isAsyncSupported());
        for (String name : Collections.list(sr.getAttributeNames())) {
            log.info("ServletRequest.getAttribute('" + name + "')=" + sr.getAttribute(name).toString());
        }
        for (Map.Entry<String, String[]> e : sr.getParameterMap().entrySet()) {
            String[] items = e.getValue();
            for (int i = 0; i < items.length; i++) {
                log.info("ServletRequest.getParameterMap('" + e.getKey() + "')[" + i + "]=" + items[i]);
            }
        }
        log.info("HttpServletRequest.getAuthType()=" + sr.getAuthType());
        log.info("HttpServletRequest.getContextPath()=" + sr.getContextPath());
        log.info("HttpServletRequest.getMethod()=" + sr.getMethod());
        log.info("HttpServletRequest.getPathInfo()=" + sr.getPathInfo());
        log.info("HttpServletRequest.getPathTranslated()=" + sr.getPathTranslated());
        log.info("HttpServletRequest.getQueryString()=" + sr.getQueryString());
        log.info("HttpServletRequest.getRemoteUser()=" + sr.getRemoteUser());
        log.info("HttpServletRequest.getRequestedSessionId()=" + sr.getRequestedSessionId());
        log.info("HttpServletRequest.getRequestURI()=" + sr.getRequestURI());
        log.info("HttpServletRequest.getRequestURL()=" + sr.getRequestURL());
        log.info("HttpServletRequest.getServletPath()=" + sr.getServletPath());
        log.info("HttpServletRequest.isRequestedSessionIdFromCookie()=" + sr.isRequestedSessionIdFromCookie());
        log.info("HttpServletRequest.isRequestedSessionIdFromURL()=" + sr.isRequestedSessionIdFromURL());
        log.info("HttpServletRequest.isRequestedSessionIdValid()=" + sr.isRequestedSessionIdValid());
        Cookie[] cookies = sr.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                log.info("HttpServletRequest.getCookies()[" + i + "]=" + cookies[i].getName() + ":"
                        + cookies[i].getValue());
            }
        }
        for (String name : Collections.list(sr.getHeaderNames())) {
            log.info("HttpServletRequest.getHeader('" + name + "')=" + sr.getHeader(name));
        }
        log.info("===================== logdumpHttpServletRequest END");
    }

    public static void logdumpServletContext(Logger log, ServletContext sc) {
        log.info("===================== logdumpServletContext START");
        for (String name : Collections.list(sc.getAttributeNames())) {
            log.info("ServletContext.getAttribute('" + name + "')=" + sc.getAttribute(name).toString());
        }
        log.info("ServletContext.getContextPath()=" + sc.getContextPath());
        log.info("ServletContext.getEffectiveMajorVersion()=" + sc.getEffectiveMajorVersion());
        log.info("ServletContext.getEffectiveMinorVersion()=" + sc.getEffectiveMinorVersion());
        for (String name : Collections.list(sc.getInitParameterNames())) {
            log.info("ServletContext.getInitParameter('" + name + "')=" + sc.getInitParameter(name));
        }
        log.info("ServletContext.getMajorVersion()=" + sc.getMajorVersion());
        log.info("ServletContext.getMinorVersion()=" + sc.getMinorVersion());
        log.info("ServletContext.getRealPath('')=" + sc.getRealPath(""));
        log.info("ServletContext.getRealPath('/')=" + sc.getRealPath("/"));
        log.info("ServletContext.getServerInfo()=" + sc.getServerInfo());
        log.info("ServletContext.getServletContextName()=" + sc.getServletContextName());
        SessionCookieConfig scc = sc.getSessionCookieConfig();
        if (null != scc) {
            log.info("ServletContext.getSessionCookieConfig().getName()=" + scc.getName());
            log.info("ServletContext.getSessionCookieConfig().getPath()=" + scc.getPath());
            log.info("ServletContext.getSessionCookieConfig().getDomain()=" + scc.getDomain());
            log.info("ServletContext.getSessionCookieConfig().getMaxAge()=" + scc.getMaxAge());
            log.info("ServletContext.getSessionCookieConfig().isHttpOnly()=" + scc.isHttpOnly());
            log.info("ServletContext.getSessionCookieConfig().isSecure()=" + scc.isSecure());
            log.info("ServletContext.getSessionCookieConfig().getComment()=" + scc.getComment());
        }
        log.info("ServletContext.getVirtualServerName()=" + sc.getVirtualServerName());
        log.info("===================== logdumpServletContext END");
    }

    public static void logdumpServerProperties(Logger log, ServerProperties sp) {
        log.info("===================== logdumpServerProperties START");
        log.info("ServerProperties.getAddress()=" + sp.getAddress());
        log.info("ServerProperties.getConnectionTimeout()=" + sp.getConnectionTimeout());
        for (Map.Entry<String, String> e : sp.getContextParameters().entrySet()) {
            log.info("ServerProperties.getContextParameters()['" + e.getKey() + "']=" + e.getValue());
        }
        log.info("ServerProperties.getContextPath()=" + sp.getContextPath());
        log.info("ServerProperties.getDisplayName()=" + sp.getDisplayName());
        log.info("ServerProperties.getMaxHttpHeaderSize()=" + sp.getMaxHttpHeaderSize());
        log.info("ServerProperties.getMaxHttpPostSize()=" + sp.getMaxHttpPostSize());
        log.info("ServerProperties.getOrder()=" + sp.getOrder());
        log.info("ServerProperties.getPath('')=" + sp.getPath(""));
        log.info("ServerProperties.getPath('/')=" + sp.getPath("/"));
        log.info("ServerProperties.getPort()=" + sp.getPort());
        log.info("ServerProperties.getServerHeader()=" + sp.getServerHeader());
        log.info("ServerProperties.getServletMapping()=" + sp.getServletMapping());
        log.info("ServerProperties.getServletPath()=" + sp.getServletPath());
        log.info("ServerProperties.getServletPrefix()=" + sp.getServletPrefix());
        log.info("ServerProperties.isUseForwardHeaders()=" + sp.isUseForwardHeaders());
        log.info("===================== logdumpServerProperties END");
    }
}
