package sbe.web1.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName = "MyServletFilter3", urlPatterns = "/*")
public class MyServletFilter3 implements Filter {
    static final Logger LOG = LoggerFactory.getLogger(MyServletFilter3.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String fdiv0 = request.getParameter("filter-div0");
        if (null != fdiv0 && fdiv0.length() > 0) {
            int div0 = Integer.parseInt(fdiv0);
            String fdiv1 = request.getParameter("filter-div1");
            if (null != fdiv1 && fdiv1.length() > 0) {
                int div1 = Integer.parseInt(fdiv1);
                int divr = div1 / div0;
                LOG.info(">>#####>> MyServletFilter3#doFilter(), div1 / div0 = {}", divr);
            }
        }
        String ioex = request.getParameter("filter-ioex");
        if (null != ioex) {
            throw new IOException("from MyServletFilter3#doFilter()()");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to do.
    }

}
