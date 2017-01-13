package sbe.web1.mvc;

import java.io.IOException;
import java.util.Calendar;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sbe.web1.MyCustomException1;
import sbe.web1.MyCustomException2;

@Controller
@RequestMapping("/exception-demo")
public class ExceptionDemoController {

    @RequestMapping("/")
    public String index() {
        return "exception-demo/index";
    }

    @PostMapping("/post")
    public String post(@RequestParam int div0, @RequestParam int div1,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Calendar dateTime, Model m) {
        int r = div1 / div0;
        m.addAttribute("div0", div0);
        m.addAttribute("div1", div1);
        m.addAttribute("divr", r);
        m.addAttribute("dateTime", dateTime);
        return "exception-demo/index";
    }

    @RequestMapping("/custom-ex")
    public String customEx(@RequestParam String exname) throws Exception {
        switch (exname) {
        case "forbidden":
            throw new MyCustomException1("from Controller method");
        case "conflict":
            throw new MyCustomException2("from Controller method");
        }
        throw new IOException("from Controller method");
    }

    @PostMapping("/intercept-div")
    public String interceptDiv() {
        return "exception-demo/index";
    }

    @GetMapping("/intercept-ioex")
    public String interceptIoex() {
        return "exception-demo/index";
    }

    @PostMapping("/filter-div")
    public String filterDiv() {
        return "exception-demo/index";
    }

    @GetMapping("/filter-ioex")
    public String filterIoex() {
        return "exception-demo/index";
    }

}
