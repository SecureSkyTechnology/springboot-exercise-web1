package sbe.web1.mvc;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/thlf")
public class ThymeleafSampleController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("htmlstr", "<s>\"xss'&</s>");
        model.addAttribute("str1", "hello");
        model.addAttribute("str2", "morning");
        model.addAttribute("date", new Date());
        model.addAttribute("num", 123456789);
        model.addAttribute("sampleObjects1",
                Arrays.asList(
                        new SampleObject(1, "jon", 10),
                        new SampleObject(2, "bob", 20),
                        new SampleObject(3, "ken", 30)));
        Map<String, SampleObject> m = new HashMap<>();
        m.put("user1", new SampleObject(4, "tom", 40));
        m.put("user2", new SampleObject(5, "bez", 50));
        m.put("user3", new SampleObject(6, "bill", 60));
        model.addAttribute("sampleObjects2", m);
        return "thlf/index";
    }

    @RequestMapping("/fragments")
    public String fragments(@RequestParam(defaultValue = "false") boolean isAdmin, Model model) {
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("str1", "jon");
        model.addAttribute("str2", "bob");
        model.addAttribute("str3", "ken");
        return "thlf/fragments";
    }

    @RequestMapping("/fragment-only")
    public String fragmentOnly() {
        return "thlf/fragments-sub :: copyright";
    }

    @RequestMapping("/layout-demo")
    public String layoutDemo(@RequestParam(defaultValue = "thlf/layout1") String layout, Model model) {
        // https://github.com/ultraq/thymeleaf-layout-dialect
        model.addAttribute("layoutName", layout);
        model.addAttribute("contentTitle", "content title");
        model.addAttribute("str1", "jon");
        model.addAttribute("str2", "bob");
        model.addAttribute("str3", "ken");
        return "thlf/layout-demo";
    }

}

class SampleObject {
    public int id;
    public String name;
    public Date birthday;
    public int age;

    public SampleObject(int aId, String aName, int anAge) {
        this.id = aId;
        this.name = aName;
        this.birthday = new Date();
        this.age = anAge;
    }
}
