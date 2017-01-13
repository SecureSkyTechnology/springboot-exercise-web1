package sbe.web1.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import sbe.web1.MyApplication;
import sbe.web1.beandemo.ApplicationScopedBean1;
import sbe.web1.beandemo.RequestScopedBean1;
import sbe.web1.beandemo.SessionScopedBean1;

@Controller
@RequestMapping("/basic")
public class BasicController {

    static final Logger LOG = LoggerFactory.getLogger(BasicController.class);

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ApplicationScopedBean1 asBean1;

    @Autowired
    RequestScopedBean1 rsBean1;

    @Autowired
    SessionScopedBean1 ssBean1;

    @RequestMapping("/")
    public String index() {
        return "basic/index";
    }

    @RequestMapping("/dump-servlet")
    public String dumpServletParameter(HttpServletRequest sr, HttpSession sess, Locale argLocale, TimeZone argTz,
            HttpMethod argHttpMethod, InputStream ins, Model m) throws IOException {
        m.addAttribute("argRequest", sr);
        m.addAttribute("argLocale", argLocale);
        m.addAttribute("argTz", argTz);
        m.addAttribute("argHttpMethod", argHttpMethod.toString());
        String insResult = StreamUtils.copyToString(ins, Charset.defaultCharset());
        m.addAttribute("argInputStream", insResult);
        Integer i = (Integer) sess.getAttribute("cnt");
        if (null == i) {
            i = new Integer(1);
        }
        i = new Integer(i.intValue() + 1);
        sess.setAttribute("cnt", i);
        m.addAttribute("argSession", sess);
        LOG.info(asBean1.toString());
        LOG.info(rsBean1.toString());
        LOG.info(ssBean1.toString());
        m.addAttribute("asBean1", asBean1);
        m.addAttribute("rsBean1", rsBean1);
        m.addAttribute("ssBean1", ssBean1);
        return "basic/dump-servlet :: result";
    }

    @RequestMapping("/typical-param")
    public String typicalParameters(@RequestParam String qp1, @RequestParam String qp2, @RequestParam String[] qp3,
            @RequestParam(defaultValue = "xxxx") String qp4, TypicalParamForm fp, @RequestBody byte[] requestBody,
            @CookieValue(name = "JSESSIONID", required = false) String jsessionId,
            @CookieValue(name = "xxxx", required = false) String nonExistCookie,
            @RequestHeader("Accept-Encoding") String acceptEncoding, @RequestHeader("Host") String host,
            @RequestHeader(name = "X-Custom-XXXX", required = false) String nonExistHeader, Model m) {
        m.addAttribute("qp1", qp1);
        m.addAttribute("qp2", qp2);
        m.addAttribute("qp3", qp3);
        m.addAttribute("qp4", qp4);
        m.addAttribute("fp", fp);
        String body = new String(requestBody, Charset.defaultCharset());
        m.addAttribute("body", body);
        m.addAttribute("jsessionId", jsessionId);
        m.addAttribute("nonExistCookie", nonExistCookie);
        m.addAttribute("acceptEncoding", acceptEncoding);
        m.addAttribute("host", host);
        m.addAttribute("nonExistHeader", nonExistHeader);
        return "basic/typical-param :: result";
    }

    @ModelAttribute
    public AutoPopulatedModel1 populateModel1(@RequestParam(defaultValue = "0") int id) {
        AutoPopulatedModel1 m = new AutoPopulatedModel1();
        m.setId(id);
        m.setName("model1-" + id);
        m.setAge(id * 10);
        return m; // key name = "autoPopulatedModel1"
    }

    @ModelAttribute("model1List")
    public List<AutoPopulatedModel1> populateModel1List(@RequestParam(defaultValue = "0") int id) {
        List<AutoPopulatedModel1> l = new ArrayList<>();
        int newId = id * 20;
        for (int i = newId; i < newId + 3; i++) {
            AutoPopulatedModel1 m = new AutoPopulatedModel1();
            m.setId(i);
            m.setName("model1-" + i);
            m.setAge(i * 10);
            l.add(m);
        }
        return l;
    }

    @ModelAttribute
    public void populateModelAuto(@RequestParam(defaultValue = "0") int id, Model m) {
        AutoPopulatedModel1 m1 = new AutoPopulatedModel1();
        m1.setId(id + 1);
        m1.setName("model1-" + id + 1);
        m1.setAge(id * 10 + 1);
        AutoPopulatedModel2 m2 = new AutoPopulatedModel2();
        m2.setId(id + 2);
        m2.setName("model2-" + id + 2);
        m2.setHobbies(Arrays.asList("cooking", "programming"));
        m.addAttribute("model1", m1);
        m.addAttribute("model2", m2);
    }

    @RequestMapping("/modelattr-on-method")
    public String modelattrOnMethod() {
        return "basic/modelattr-on-method";
    }

    @RequestMapping("/json-mapped-param")
    @ResponseBody
    public JsonMappedModel echoJsonMappedModel(@RequestBody JsonMappedModel jsonMapped) {
        return jsonMapped;
    }

    @PostMapping("/fileupload-result")
    public String fileuploadResult(@RequestParam String comment, @RequestParam Part file1, @RequestParam Part file2,
            Model m) throws IOException {
        m.addAttribute("file1", file1);
        m.addAttribute("file2", file2);
        byte[] file1data = StreamUtils.copyToByteArray(file1.getInputStream());
        byte[] file2data = StreamUtils.copyToByteArray(file2.getInputStream());
        m.addAttribute("file1base64", Base64Utils.encodeToString(file1data));
        m.addAttribute("file2base64", Base64Utils.encodeToString(file2data));
        return "basic/fileupload-result";
    }

    @GetMapping("/render-text")
    @ResponseBody
    public String renderText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("日本語テキスト:" + i + "\n");
        }
        return sb.toString();
    }

    @GetMapping(path = "/render-text-html", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String renderTextHtml() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("日本語テキスト:" + i + "\n");
        }
        return sb.toString();
    }

    @GetMapping(path = "/render-image", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] renderImage() throws IOException {
        return StreamUtils
                .copyToByteArray(applicationContext.getResource("classpath:static/image/sample.png").getInputStream());
    }

    @GetMapping(path = "/download-image")
    @ResponseBody
    public ResponseEntity<byte[]> downloadImage() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).header("Content-Disposition", "attachment; filename=foo.png")
                .contentType(MediaType.IMAGE_PNG).body(StreamUtils.copyToByteArray(
                        applicationContext.getResource("classpath:static/image/sample.png").getInputStream()));
    }

    @GetMapping(path = "/download-binary1", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] downloadBinary1() throws IOException {
        return StreamUtils
                .copyToByteArray(applicationContext.getResource("classpath:static/512_0to255.bin").getInputStream());
    }

    @GetMapping(path = "/download-binary2")
    @ResponseBody
    public ResponseEntity<byte[]> downloadBinary2() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).header("Content-Disposition", "attachment; filename=512_0to255.bin")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(StreamUtils.copyToByteArray(
                        applicationContext.getResource("classpath:static/512_0to255.bin").getInputStream()));
    }

    @GetMapping(path = "/download-text")
    @ResponseBody
    public ResponseEntity<byte[]> downloadTextWithCharset(@RequestParam String cs) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("日本語テキスト:" + i + "\n");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Disposition", "attachment; filename=sample_text_" + cs + ".txt")
                .contentType(MediaType.parseMediaType("text/plain; charset=" + cs)).body(sb.toString().getBytes(cs));
    }

    @GetMapping(path = "/empty-404")
    @ResponseBody
    public ResponseEntity<Void> empty404() throws IOException {
        return ResponseEntity.notFound().header("X-Custom-Foo", "foo").build();
    }

    @GetMapping(path = "/programmed-404")
    @ResponseBody
    public ResponseEntity<String> programmed404() throws IOException {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("custom 404");
    }

    @RequestMapping("/set-cookie")
    public String setcookie(@RequestParam String name, @RequestParam String value,
            @RequestParam(defaultValue = "-1") int maxage, @RequestParam(required = false) String domain,
            @RequestParam(required = false) String path, @RequestParam(defaultValue = "false") boolean isSecure,
            @RequestParam(defaultValue = "false") boolean isHttpOnly, @RequestParam(required = false) String comment,
            @RequestParam(defaultValue = "false") boolean delete, HttpServletResponse sr) {
        Cookie c = new Cookie(name, value);
        if (delete) {
            c.setMaxAge(0);
        } else if (maxage > 0) {
            c.setMaxAge(maxage);
        }
        if (!StringUtils.isEmpty(domain)) {
            c.setDomain(domain);
        }
        if (!StringUtils.isEmpty(path)) {
            c.setPath(path);
        }
        c.setHttpOnly(isHttpOnly);
        c.setSecure(isSecure);
        if (!StringUtils.isEmpty(comment)) {
            c.setComment(comment);
        }
        sr.addCookie(c);
        return "basic/set-cookie";
    }

    @PostMapping("/redirect1/{pathp1}/{pathp2}")
    public String redirect1(@PathVariable int pathp1, @PathVariable int pathp2,
            @RequestParam MultiValueMap<String, String> mm, UriComponentsBuilder builder) {
        mm.add("addp1", "1001");
        mm.add("addp2", "1002");
        mm.add("addp2", "1003");
        mm.add("addp3", "日本語");
        String query = builder.queryParams(mm).build().encode().getQuery();
        return "redirect:/basic/redirect1to/{pathp1}/{pathp2}?" + query;
    }

    @GetMapping("/redirect1to/{pathp1}/{pathp2}")
    public String redirect1to(@PathVariable int pathp1, @PathVariable int pathp2, BasicControllerForm form,
            Model model) {
        model.addAttribute("requestForm", form);
        model.addAttribute("pathp1", pathp1);
        model.addAttribute("pathp2", pathp2);
        return "basic/redirect1to";
    }

    @GetMapping("/redirect2")
    public String redirect2(RedirectAttributes ra) {
        ra.addFlashAttribute("flashp1", "hello");
        ra.addFlashAttribute("flashp2", "morning");
        return "redirect:/basic/redirect2to";
    }

    @GetMapping("/redirect2to")
    public String redirect2to(@RequestParam MultiValueMap<String, String> mvm, Model model) {
        String flashp1 = mvm.getFirst("flashp1");
        String flashp2 = mvm.getFirst("flashp2");
        model.addAttribute("mvmflashp1", flashp1); // will be empty. (already
                                                   // got-and-cleared)
        model.addAttribute("mvmflashp2", flashp2); // will be empty. (already
                                                   // got-and-cleared)
        return "basic/redirect2to";
    }

    @PostMapping("/redirect3/{pathp1}/{pathp2}")
    public String redirect3(@PathVariable int pathp1, @PathVariable int pathp2,
            @RequestParam MultiValueMap<String, String> mm, UriComponentsBuilder builder) {
        mm.add("addp1", "1001");
        mm.add("addp2", "1002");
        mm.add("addp2", "1003");
        mm.add("addp3", "日本語");
        String query = builder.queryParams(mm).build().encode().getQuery();
        return "redirect:http://localhost:18080/foo/bar/{pathp1}/{pathp2}?" + query;
    }

    @PostMapping("/forwarder/{pathp1}/{pathp2}")
    public String forwarder(@PathVariable int pathp1, @PathVariable int pathp2,
            @RequestParam MultiValueMap<String, String> mm) {
        mm.add("addp1", "1001");
        mm.add("addp2", "1002");
        mm.add("addp2", "1003");
        mm.add("addp3", "日本語");
        return "forward:/basic/forwarded/{pathp1}/{pathp2}";
    }

    @RequestMapping("/forwarded/{pathp1}/{pathp2}")
    public String forwarded(@PathVariable String pathp1, @PathVariable String pathp2, BasicControllerForm form,
            @RequestParam MultiValueMap<String, String> mm, Model model) {
        model.addAttribute("requestForm", form);
        // addpX are not inherited.
        model.addAttribute("addp1", "1001");
        model.addAttribute("addp2", Arrays.asList("1002", "1003"));
        model.addAttribute("addp3", "日本語");
        return "basic/forwarded";
    }

    @Autowired
    MessageSource messageSource;

    @GetMapping("/locale_tz_messages")
    public String localeTzMessages(Locale requestLocale, TimeZone requestTz, Model model) {
        model.addAttribute("defaultLocale", Locale.getDefault());
        model.addAttribute("defaultTz", TimeZone.getDefault());
        model.addAttribute("requestLocale", requestLocale);
        model.addAttribute("requestTz", requestTz);

        // from http://blog.okazuki.jp/entry/2015/07/13/203328
        Locale holderedLocale = LocaleContextHolder.getLocale();
        TimeZone holderedTz = LocaleContextHolder.getTimeZone();
        model.addAttribute("holderedLocale", holderedLocale);
        model.addAttribute("holderedTz", holderedTz);

        // retrieve message from java program
        // see: 7.15.1 Internationalization using MessageSource
        // http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#context-functionality-messagesource
        model.addAttribute("msg1", messageSource.getMessage("msg.basic.sample1", null, requestLocale));
        model.addAttribute("msg2",
                messageSource.getMessage("msg.basic.sample2", new Object[] { "val1", "val2" }, requestLocale));
        return "basic/locale_tz_messages";
    }

    @GetMapping("/change_locale")
    public String changeLocale(Locale requestLocale, TimeZone requestTz, Model model) {
        return this.localeTzMessages(requestLocale, requestTz, model);
    }

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ServerProperties serverProps;

    @RequestMapping("/dump-server-info")
    public String dumpServerInfo(HttpServletRequest sr, Model m) throws IOException {
        MyApplication.logdumpHttpServletRequest(LOG, sr);
        MyApplication.logdumpServletContext(LOG, servletContext);
        MyApplication.logdumpServerProperties(LOG, serverProps);
        m.addAttribute("sr", sr);
        m.addAttribute("sc", servletContext);
        m.addAttribute("sp", serverProps);
        return "basic/dump-server-info :: result";
    }

    @RequestMapping("/logger-demo")
    @ResponseBody
    public String loggerDemo(@RequestParam String loggerName, @RequestParam int count) throws IOException {
        Logger log = LoggerFactory.getLogger(loggerName);
        String filler = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < count; i++) {
            log.trace("trace log demo [" + i + "]" + filler);
            log.debug("debug log demo [" + i + "]" + filler);
            log.info("info log demo [" + i + "]" + filler);
            log.warn("warn log demo [" + i + "]" + filler);
            log.error("error log demo [" + i + "]" + filler);
        }
        return "see log files.\ntry : logging.config property or LOGGING_CONFIG env.";
    }
}

class TypicalParamForm {
    String fp1;
    List<String> fp2;
    String fp3;

    public String getFp1() {
        return fp1;
    }

    public void setFp1(String fp1) {
        this.fp1 = fp1;
    }

    public List<String> getFp2() {
        return fp2;
    }

    public void setFp2(List<String> fp2) {
        this.fp2 = fp2;
    }

    public String getFp3() {
        return fp3;
    }

    public void setFp3(String fp3) {
        this.fp3 = fp3;
    }
}

class AutoPopulatedModel1 {
    int id;
    String name;
    int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class AutoPopulatedModel2 {
    int id;
    String name;
    List<String> hobbies;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }
}

class JsonMappedModel {
    AutoPopulatedModel1 model1;
    List<AutoPopulatedModel2> listOfModel2;

    public AutoPopulatedModel1 getModel1() {
        return model1;
    }

    public void setModel1(AutoPopulatedModel1 model1) {
        this.model1 = model1;
    }

    public List<AutoPopulatedModel2> getListOfModel2() {
        return listOfModel2;
    }

    public void setListOfModel2(List<AutoPopulatedModel2> listOfModel2) {
        this.listOfModel2 = listOfModel2;
    }
}

class BasicControllerForm {
    public String qp1;
    public List<String> qp2;
    public String qp3;
    public String fp1;
    public List<String> fp2;
    public String fp3;

    public String getQp1() {
        return qp1;
    }

    public void setQp1(String qp1) {
        this.qp1 = qp1;
    }

    public List<String> getQp2() {
        return qp2;
    }

    public void setQp2(List<String> qp2) {
        this.qp2 = qp2;
    }

    public String getQp3() {
        return qp3;
    }

    public void setQp3(String qp3) {
        this.qp3 = qp3;
    }

    public String getFp1() {
        return fp1;
    }

    public void setFp1(String fp1) {
        this.fp1 = fp1;
    }

    public List<String> getFp2() {
        return fp2;
    }

    public void setFp2(List<String> fp2) {
        this.fp2 = fp2;
    }

    public String getFp3() {
        return fp3;
    }

    public void setFp3(String fp3) {
        this.fp3 = fp3;
    }
}