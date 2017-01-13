package sbe.web1.mvc;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import sbe.web1.mvc.interceptors.MyAttrBean;

@Controller
@RequestMapping("/attr-demo")
@SessionAttributes(names = { "mb4", "mb5" })
public class AttrDemoController {
    static final Logger LOG = LoggerFactory.getLogger(AttrDemoController.class);

    @ModelAttribute("mb1")
    MyAttrBean createMyAttrBean1() {
        // http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-ann-modelattrib-methods
        return new MyAttrBean("modelAttrBean1");
    }

    @ModelAttribute("mb2")
    MyAttrBean createMyAttrBean2() {
        return new MyAttrBean("modelAttrBean2");
    }

    @ModelAttribute("mb4")
    MyAttrBean createMyAttrBean4() {
        // http://blog.okazuki.jp/entry/2015/07/05/214538
        // http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc-ann-sessionattrib
        return new MyAttrBean("modelAttrBean4(with @SessionAttributes)");
    }

    @ModelAttribute("mb5")
    MyAttrBean createMyAttrBean5() {
        return new MyAttrBean("modelAttrBean5(with @SessionAttributes)");
    }

    @RequestMapping("/")
    public String index(@RequestAttribute MyAttrBean myAttrBean, @RequestAttribute("rb2") MyAttrBean rb2,
            @RequestAttribute(name = "rb3", required = false) MyAttrBean rb3, @ModelAttribute("mb2") MyAttrBean mb2,
            @ModelAttribute("mb3") MyAttrBean mb3, @ModelAttribute("mb4") MyAttrBean mb4,
            @ModelAttribute("mb5") MyAttrBean mb5, @SessionAttribute("myAttrBean") MyAttrBean sb1,
            @SessionAttribute("sb2") MyAttrBean sb2, Model m) {
        LOG.info("/attr-demo/, myAttrBean={}", myAttrBean);
        LOG.info("/attr-demo/, rb2={}", rb2);
        LOG.info("/attr-demo/, rb3={}", rb3);
        mb2.addString("hello");
        LOG.info("/attr-demo/, mb2={}", mb2);
        LOG.info("/attr-demo/, mb3={}", mb3);
        LOG.info("/attr-demo/, mb4={}", mb4);
        LOG.info("/attr-demo/, mb5={}", mb5);
        LOG.info("/attr-demo/, sb1={}", sb1);
        LOG.info("/attr-demo/, sb2={}", sb2);
        return "attr-demo/index";
    }

    @RequestMapping("/update")
    public String updateSessionAttr(@RequestAttribute MyAttrBean myAttrBean, @RequestAttribute("rb2") MyAttrBean rb2,
            @RequestAttribute(name = "rb3", required = false) MyAttrBean rb3, @ModelAttribute("mb2") MyAttrBean mb2,
            @ModelAttribute("mb3") MyAttrBean mb3, @ModelAttribute("mb4") MyAttrBean mb4,
            @ModelAttribute("mb5") MyAttrBean mb5, @SessionAttribute("myAttrBean") MyAttrBean sb1,
            @SessionAttribute("sb2") MyAttrBean sb2, Model m) {
        LOG.info("/attr-demo/update, myAttrBean={}", myAttrBean);
        LOG.info("/attr-demo/update, rb2={}", rb2);
        LOG.info("/attr-demo/update, rb3={}", rb3);
        mb2.addString("hello");
        LOG.info("/attr-demo/update, mb2={}", mb2);
        LOG.info("/attr-demo/update, mb3={}", mb3);
        mb4.addUUID();
        LOG.info("/attr-demo/update, mb4={}", mb4);
        mb5.addUUID();
        LOG.info("/attr-demo/update, mb5={}", mb5);
        sb1.addUUID();
        LOG.info("/attr-demo/update, sb1={}", sb1);
        sb2.addUUID();
        LOG.info("/attr-demo/update, sb2={}", sb2);
        return "attr-demo/index";
    }

    @RequestMapping("/clear")
    public String clearSessionAttributes(SessionStatus ss) {
        ss.setComplete();
        return "attr-demo/clear";
    }

    @RequestMapping("/invalidate")
    public String invalidateSession(HttpSession hs) {
        hs.invalidate();
        return "attr-demo/invalidate";
    }
}
