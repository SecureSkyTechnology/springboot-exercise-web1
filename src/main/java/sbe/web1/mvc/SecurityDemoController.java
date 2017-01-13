package sbe.web1.mvc;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sbe.web1.mvc.form.ChangePasswordForm;
import sbe.web1.security.LoginUserDetail;
import sbe.web1.security.LoginUserDetailsService;

@Controller
@RequestMapping("/security-demo")
public class SecurityDemoController {
    static final Logger LOG = LoggerFactory.getLogger(SecurityDemoController.class);

    @Autowired
    MessageSource messageSource;

    @RequestMapping("/")
    public String index() {
        return "security-demo/index";
    }

    @RequestMapping("/basic-auth")
    public String basicAuth() {
        return "security-demo/basic-auth";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(name = "errorReason", required = false) String errorReason, Locale loc, Model m) {
        if (null != errorReason) {
            String msg = messageSource.getMessage("mycustomautherr." + errorReason, null, loc);
            if (null == msg) {
                msg = messageSource.getMessage("mycustomautherr.unknown", null, loc);
            }
            m.addAttribute("loginErrorMsg", msg);
        }
        return "security-demo/login";
    }

    @RequestMapping("/logouted")
    public String logouted() {
        return "security-demo/logouted";
    }

    @RequestMapping("/menu")
    public String menu(@ModelAttribute("form") ChangePasswordForm form) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof LoginUserDetail) {
            LoginUserDetail lud = LoginUserDetail.class.cast(auth.getPrincipal());
            LOG.info("SecurityContextHolder.getContext().getAuthentication().getPrincipal() as LoginUserDetail: {}", lud);
        }
        LOG.info("SecurityContextHolder.getContext().getAuthentication().getPrincipal() : {}", auth.getPrincipal());
        LOG.info("SecurityContextHolder.getContext().getAuthentication().getCredentials() : {}", auth.getCredentials());
        LOG.info("SecurityContextHolder.getContext().getAuthentication().getDetails() : {}", auth.getDetails());
        LOG.info("SecurityContextHolder.getContext().getAuthentication().getName() : {}", auth.getName());
        LOG.info("SecurityContextHolder.getContext().getAuthentication().getAuthorities() : {}", auth.getAuthorities());
        return "security-demo/menu";
    }

    @RequestMapping("/data-ajax")
    @ResponseBody
    public String dataAjax() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            String uuid = UUID.randomUUID().toString();
            sb.append("日本語テキスト:" + uuid + "\n");
        }
        return sb.toString();
    }

    @RequestMapping("/menu/user")
    public String menuUser() {
        return "security-demo/menu-user";
    }

    @RequestMapping("/menu/admin")
    public String menuAdmin() {
        return "security-demo/menu-admin";
    }

    @Autowired
    LoginUserDetailsService loginUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("form") @Validated ChangePasswordForm form, HttpServletRequest req, BindingResult result, Model m) {
        for (FieldError fe : result.getFieldErrors()) {
            LOG.info("/change-password : field error[] = {}", fe);
        }
        if (result.hasErrors()) {
            return "security-demo/menu";
        }
        // 現在のログイン情報を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUserDetail lud = LoginUserDetail.class.cast(auth.getPrincipal());
        UserDetails ud = loginUserDetailsService.loadUserByUsername(lud.getUsername());
        if (passwordEncoder.matches(form.getCurrentPassword(), ud.getPassword())) {
            // パスワード更新
            loginUserDetailsService.updatePassword(lud.getUsername(), form.getNewPassword1(), passwordEncoder);
            // セッションID変更 (>= Servlet 3.1)
            req.changeSessionId();
            LOG.info("/change-password : current password match, updated to new password.");
            return "redirect:/security-demo/menu";
        } else {
            // カスタムの入力チェックエラー生成
            result.addError(new FieldError(
                    "form",
                    "currentPassword",
                    form.getCurrentPassword(),
                    false,
                    new String[] { "PasswordConfirmationError" },
                    new Object[] {},
                    "current password confirmation error."));
            return "security-demo/menu";
        }
    }

}
