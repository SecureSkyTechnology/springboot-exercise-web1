package sbe.web1.servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

@WebServlet(name = "MyServlet", urlPatterns = { "/servlet/myservlet" }, loadOnStartup = 1)
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(MyServlet.class);

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MessageSource messageSource;

    @Override
    public void init() throws ServletException {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>>>>>>> MyServlet#init(), ac check : {}, ms check : {}", appname, msg);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>>>>>>> MyServlet#service(), ac check : {}, ms check : {}", appname, msg);
        resp.getWriter().write("ok");
    }

    @Override
    public void destroy() {
        String msg = messageSource.getMessage("msg.sample1", null, Locale.getDefault());
        String appname = applicationContext.getApplicationName();
        LOG.info(">>>>>>>> MyServlet#destroy(), ac check : {}, ms check : {}", appname, msg);
    }
}
