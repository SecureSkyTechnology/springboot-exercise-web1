package sbe.web1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/error/login-timeout")
    public String loginTimeout() {
        return "error_login_timeout";
    }

    @RequestMapping("/error/csrf")
    public String csrfError() {
        return "error_csrf";
    }
}
