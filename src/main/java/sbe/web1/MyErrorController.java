package sbe.web1;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @see http://docs.spring.io/spring-boot/docs/1.4.3.RELEASE/api/org/springframework/boot/autoconfigure/web/BasicErrorController.html
 * @see http://docs.spring.io/spring-boot/docs/1.4.3.RELEASE/api/org/springframework/boot/autoconfigure/web/ErrorController.html
 * @see http://docs.spring.io/spring-boot/docs/1.4.3.RELEASE/api/org/springframework/boot/autoconfigure/web/ErrorAttributes.html
 * @see http://docs.spring.io/spring-boot/docs/1.4.3.RELEASE/reference/htmlsingle/#boot-features-error-handling
 */
@Controller
public class MyErrorController implements ErrorController {
    static final Logger LOG = LoggerFactory.getLogger(MyErrorController.class);

    @Autowired
    ErrorAttributes errorAttributes;

    @Value("${server.error.path:${error.path:/error}}")
    String errorPath = "";

    @Override
    public String getErrorPath() {
        return errorPath;
    }

    @RequestMapping("/error")
    public String error(HttpServletRequest req, HttpServletResponse res) {
        Throwable t = errorAttributes.getError(new ServletRequestAttributes(req));
        LOG.warn("MyErrorController#error()", t);
        Map<String, Object> m = errorAttributes.getErrorAttributes(new ServletRequestAttributes(req), false);
        for (Map.Entry<String, Object> e : m.entrySet()) {
            LOG.warn("MyErrorController#error() ErrorAttribute[{}] = {}", e.getKey(), e.getValue());
        }
        LOG.info("MyErrorController#error() : current HttpServletResponse#getStatus() = {}", res.getStatus());
        return "error";
    }
}
