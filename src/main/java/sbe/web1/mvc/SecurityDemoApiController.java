package sbe.web1.mvc;

import java.util.UUID;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/security-demo-api")
public class SecurityDemoApiController {
    @RequestMapping("/")
    public String index() {
        return "security-demo-api/index";
    }

    /**
     * CSRFトークンを再生成するフィルタ処理用の、(404にならないためだけの)ダミーのエンドポイント。
     * @param csrfToken
     * @return
     */
    @RequestMapping("/regen-csrftoken")
    @ResponseBody
    public CsrfToken regenCsrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }

    /**
     * USER, ADMIN 両方のロールでアクセス可能なエンドポイント。UUIDのランダム値を生成するデモ。
     * 
     * @param method HTTPメソッド名を取り出すおまけ。
     * @param csrfToken Spring Security により、メソッドパラメータで {@link CsrfToken}を受け取れることを示すデモ。
     * @return
     */
    @RequestMapping("/common")
    @ResponseBody
    public String common(HttpMethod method, CsrfToken csrfToken) {
        String uuid = UUID.randomUUID().toString();
        return method.toString() + ":common:" + uuid + ":csrfToken=" + csrfToken.getToken();
    }

    /**
     * USERロールでアクセス可能なエンドポイント。UUIDのランダム値を生成するデモ。
     * 
     * @param method HTTPメソッド名を取り出すおまけ。
     * @return
     */
    @RequestMapping("/user")
    @ResponseBody
    public String user(HttpMethod method) {
        String uuid = UUID.randomUUID().toString();
        return method.toString() + ":user:" + uuid;
    }

    /**
     * ADMINロールでアクセス可能なエンドポイント。UUIDのランダム値を生成するデモ。
     * 
     * @param method HTTPメソッド名を取り出すおまけ。
     * @return
     */
    @RequestMapping("/admin")
    @ResponseBody
    public String admin(HttpMethod method) {
        String uuid = UUID.randomUUID().toString();
        return method.toString() + ":admin:" + uuid;
    }
}
