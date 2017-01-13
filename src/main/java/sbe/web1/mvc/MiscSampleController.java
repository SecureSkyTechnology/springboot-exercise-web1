package sbe.web1.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/misc")
public class MiscSampleController {
    static final Logger LOG = LoggerFactory.getLogger(MiscSampleController.class);

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Environment appEnv;

    @Autowired
    ApplicationArguments args;

    @RequestMapping("/")
    public String index(Model m) {
        Environment e = applicationContext.getEnvironment();
        m.addAttribute("activeProfiles1", e.getActiveProfiles());
        m.addAttribute("defaultProfiles1", e.getDefaultProfiles());
        m.addAttribute("activeProfiles2", appEnv.getActiveProfiles());
        m.addAttribute("defaultProfiles2", appEnv.getDefaultProfiles());
        m.addAttribute("foo_bar", appEnv.getProperty("foo.bar"));
        m.addAttribute("foo_baz", appEnv.getProperty("foo.baz", "defaultValue"));
        m.addAttribute("appArgs", args);
        return "misc/index";
    }

    @RequestMapping("/intercept3")
    @ResponseBody
    public String intercept3() {
        return "ok";
    }
}
