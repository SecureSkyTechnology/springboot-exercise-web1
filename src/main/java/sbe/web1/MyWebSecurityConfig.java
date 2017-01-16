package sbe.web1;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import sbe.web1.security.LoginUserDetail;
import sbe.web1.security.LoginUserDetailsService;

/**
 * h2-console, actuator, および一部URLについてのみBasic認証(in-memoryでそれぞれ変えている) + それ以外はDB認証を組み合わせたサンプル。
 * 
 * @see http://docs.spring.io/spring-security/site/docs/4.2.1.RELEASE/reference/htmlsingle/#multiple-httpsecurity
 * @see http://docs.spring.io/spring-security/site/docs/4.2.1.RELEASE/reference/htmlsingle/#basic-config
 * @see http://docs.spring.io/spring-boot/docs/1.4.4.BUILD-SNAPSHOT/reference/htmlsingle/#boot-features-sql-h2-console-securing
 * @see http://docs.spring.io/spring-boot/docs/1.4.4.BUILD-SNAPSHOT/reference/htmlsingle/#production-ready-sensitive-endpoints
 */
@EnableWebSecurity
public class MyWebSecurityConfig {
    static final Logger LOG = LoggerFactory.getLogger(MyWebSecurityConfig.class);

    @Configuration
    @Order(1)
    public static class H2ConsoleSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        Environment env;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher(env.getProperty("spring.h2.console.path", "/h2-console") + "/**");
            http.csrf()
                    .disable();
            http.authorizeRequests()
                    .anyRequest().hasRole("H2CONSOLE");
            http.httpBasic()
                    .realmName("My Spring Boot h2-console realm");
            http.headers()
                    .defaultsDisabled()
                    .addHeaderWriter(new StaticHeadersWriter("X-My-Custom-Spring-Security-Header", "h2console"));
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    // ROLEが足りなかったらどうなるかの確認用
                    .withUser("user").password("password").roles("BOOO").and()
                    // こちらが本来のアカウント
                    .withUser("h2admin").password("password").roles("H2CONSOLE");
        }
    }

    @Configuration
    @Order(2)
    public static class ActuatorSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        Environment env;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher(env.getProperty("management.context-path", "") + "/**");
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER);
            http.csrf()
                    .disable();
            http.authorizeRequests()
                    .anyRequest().hasRole("ACTUATOR");
            http.httpBasic()
                    .realmName("My Spring Boot actuator realm");
            http.headers()
                    .defaultsDisabled()
                    .addHeaderWriter(new StaticHeadersWriter("X-My-Custom-Spring-Security-Header", "actuator"));
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    // ROLEが足りなかったらどうなるかの確認用
                    .withUser("user").password("password").roles("BOOO").and()
                    // こちらが本来のアカウント
                    .withUser("actadmin").password("password").roles("ACTUATOR");
        }
    }

    @Configuration
    @Order(3)
    public static class BasicAuthSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        Environment env;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/security-demo/basic-auth");
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER);
            http.csrf()
                    .disable();
            http.authorizeRequests()
                    .anyRequest().hasRole("USER");
            http.httpBasic()
                    .realmName("My Spring Boot basic auth realm");
            http.headers()
                    .defaultsDisabled()
                    .addHeaderWriter(new StaticHeadersWriter("X-My-Custom-Spring-Security-Header", "basic-auth"));
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
        }
    }

    @Configuration
    @Order(4)
    public static class ApiSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        Environment env;

        @Override
        public void configure(WebSecurity web) throws Exception {
            boolean useDebug = env.getProperty("custom.spring-security.enabledebug", Boolean.class, false);
            web.debug(useDebug);
            super.configure(web);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/security-demo-api/**");

            // リクエスト中のCSRFトークン認識用のヘッダー名をカスタマイズ
            HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
            csrfTokenRepository.setHeaderName("X-My-Api-Csrf-Token");
            http.csrf()
                    .csrfTokenRepository(csrfTokenRepository);

            /* 特定のURLでCSRFトークンを強制的に再生成するためのフィルタ。
             * @see http://www.codesandnotes.be/2015/02/05/spring-securitys-csrf-protection-for-rest-services-the-client-side-and-the-server-side/
             * @see https://patrickgrimard.io/2014/01/03/spring-security-csrf-protection-in-a-backbone-single-page-app/
             */
            Filter regenCsrfTokenFilter = new OncePerRequestFilter() {
                RequestMatcher targetMatcher = new AntPathRequestMatcher("/security-demo-api/regen-csrftoken");

                @Override
                protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
                    if (targetMatcher.matches(req)) {
                        // re-generate csrf token enforcing, and save to repository.
                        CsrfToken csrfToken = csrfTokenRepository.generateToken(req);
                        csrfTokenRepository.saveToken(csrfToken, req, res);
                        res.setHeader("X-My-Api-New-CsrfToken", csrfToken.getToken());
                    }
                    filterChain.doFilter(req, res);
                }
            };
            // デフォルトで追加される CsrfFilter の後に、フィルタを追加する。
            http.addFilterAfter(regenCsrfTokenFilter, CsrfFilter.class);

            http.sessionManagement()
                    .invalidSessionStrategy(new InvalidSessionStrategy() {
                        @Override
                        public void onInvalidSessionDetected(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
                            // APIなので、ステータスコードとレスポンスヘッダーのみをセットする。
                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
                            res.setHeader("X-My-Api-Result", "Invalid Session");
                        }
                    });

            http.authorizeRequests()
                    .antMatchers("/security-demo-api/common").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/security-demo-api/user").hasRole("USER")
                    .antMatchers("/security-demo-api/admin").hasRole("ADMIN")
                    .anyRequest().permitAll();

            http.formLogin()
                    .loginProcessingUrl("/security-demo-api/login")
                    .usernameParameter("login_user")
                    .passwordParameter("login_pwd")
                    .successHandler(new SimpleUrlAuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException, ServletException {
                            // 親クラスがもともと持ってて使ってる、認証エラーなどrequest attributeをクリーンアップするメソッドを呼び出す。
                            super.clearAuthenticationAttributes(req);

                            /*
                             * NOTE : ヘッダーや文字コード設定は、 OutputStream や Writer への出力の前に呼ぶ。
                             * 順番を逆にすると、 setHeader()したものが出力されなかったり、 setCharacterEncoding() が反映されないなどトラブルの原因となる。
                             */
                            res.setHeader("X-My-Api-Result", "Login Success");
                            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            res.setContentType("application/json;charset=UTF-8");

                            // CSRFトークンがこの時点で更新されているため、レスポンスヘッダーとしてクライアントに通知する。
                            CsrfToken token = csrfTokenRepository.loadToken(req);
                            LOG.info("API Login Success, new CSRF Token : {}", token.getToken());
                            res.setHeader("X-My-Api-New-CsrfToken", token.getToken());

                            // APIのレスポンスJSONとして使うモデル
                            class RepresentLoginUser {
                                public String username = "";
                                public String nickname = "";
                                public int age = 0;
                                public Date birthDay = Calendar.getInstance().getTime();
                            }
                            RepresentLoginUser rlu = new RepresentLoginUser();
                            if (auth.getPrincipal() instanceof LoginUserDetail) {
                                // セッション中のLoginUserDetailから、必要なフィールドをレスポンス用のモデルにコピー。
                                LoginUserDetail lud = LoginUserDetail.class.cast(auth.getPrincipal());
                                LOG.info("API Login Success, auth.getPrincipal() as LoginUserDetail: {}", lud);
                                rlu.username = lud.getUsername();
                                rlu.nickname = lud.getNickname();
                                rlu.age = lud.getAge();
                                rlu.birthDay = lud.getBirthDay();
                            }
                            LOG.info("API Login Success, auth.getPrincipal() : {}", auth.getPrincipal());
                            LOG.info("API Login Success, auth.getCredentials() : {}", auth.getCredentials());
                            LOG.info("API Login Success, auth.getDetails() : {}", auth.getDetails());
                            LOG.info("API Login Success, auth.getName() : {}", auth.getName());
                            LOG.info("API Login Success, auth.getAuthorities() : {}", auth.getAuthorities());

                            // JSONレスポンスを生成
                            ObjectMapper jsonMapper = new ObjectMapper();
                            String json = jsonMapper.writeValueAsString(rlu);
                            OutputStream os = res.getOutputStream();
                            os.write(json.getBytes(StandardCharsets.UTF_8));
                            os.flush();
                            os.close();

                            /*
                             * NOTE : HttpServletResponse#setStatus()は OutputStream や  Writer への出力が終わってから呼ぶ。
                             * 順番を逆にすると、OutputStream / Writer への出力が無視されてしまう。
                             */
                            res.setStatus(HttpStatus.NO_CONTENT.value());
                        }
                    })
                    .failureHandler(new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res,
                                AuthenticationException e) throws IOException, ServletException {
                            // APIなので、ステータスコードとレスポンスヘッダーのみをセットする。
                            res.setStatus(HttpStatus.BAD_REQUEST.value());
                            res.setHeader("X-My-Api-Result", e.getMessage());
                        }
                    })
                    .permitAll();

            // デフォルトの TokenBasedRememberMeServices (in-memory管理) によるremember-me
            http.rememberMe()
                    .alwaysRemember(false)
                    .key(env.getProperty("custom.spring-security.remember-me.key", "12345"))
                    .rememberMeCookieDomain(env.getProperty("custom.spring-security.remember-me.cookie.domain", "localhost"))
                    .rememberMeCookieName(env.getProperty("custom.spring-security.remember-me.cookie.name", "remtok"))
                    .rememberMeParameter(env.getProperty("custom.spring-security.remember-me.parameter", "rememberme"))
                    .tokenValiditySeconds(60);

            http.logout()
                    .logoutUrl("/security-demo-api/logout")
                    .logoutSuccessHandler(new LogoutSuccessHandler() {
                        @Override
                        public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException, ServletException {
                            // APIなので、ステータスコードとレスポンスヘッダーのみをセットする。
                            res.setStatus(HttpStatus.NO_CONTENT.value());
                            res.setHeader("X-My-Api-Result", "Logouted");
                        }
                    })
                    .invalidateHttpSession(true)
                    /* deleteCookies() は org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler 
                     * のショートカットだが、max-age以外の domain, path, secure属性を細くカスタマイズできないため、
                     * "server.session.cookie.*" のプロパティ設定とズレが生じ、正しくクライアント側で Cookie が削除できない。
                     * そのため、"server.session.cookie.*" のプロパティ設定を使った独自の
                     * FineControlledSessionCookieClearingLogoutHandler
                     * クラスを独自に作成し、それを addLogoutHandler() で登録してみた。
                     */
                    .addLogoutHandler(new FineControlledSessionCookieClearingLogoutHandler(env))
                    .permitAll();

            http.exceptionHandling()
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    .accessDeniedHandler(new AccessDeniedHandler() {
                        @Override
                        public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
                            // APIなので、ステータスコードとレスポンスヘッダーのみをセットする。
                            if (e instanceof CsrfException) {
                                res.setStatus(HttpStatus.BAD_REQUEST.value());
                                res.setHeader("X-My-Api-Result", "CSRF Token Error");
                            } else {
                                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                                res.setHeader("X-My-Api-Result", e.getMessage());
                            }
                        }
                    });

            http.headers()
                    .defaultsDisabled()
                    // HttpSecurity#headers()からの、特定 AntMatcher に絞った範囲での静的なレスポンスヘッダー出力の例。
                    .addHeaderWriter(new StaticHeadersWriter("X-My-Custom-Spring-Security-Header", "api"));
        }

        @Autowired
        LoginUserDetailsService loginUserDetailsService;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(loginUserDetailsService).passwordEncoder(passwordEncoder());
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Configuration
    public static class DefaultSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        Environment env;

        @Override
        public void configure(WebSecurity web) throws Exception {
            boolean useDebug = env.getProperty("custom.spring-security.enabledebug", Boolean.class, false);
            web.debug(useDebug);
            super.configure(web);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            // リクエスト中のCSRFトークン認識用のヘッダー名をカスタマイズ
            HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
            csrfTokenRepository.setHeaderName("X-My-Custom-Csrf-Token");
            http.csrf()
                    .ignoringAntMatchers(
                            "/",
                            "/attr-demo/**",
                            "/basic/**",
                            "/exception-demo/**",
                            "/misc/**",
                            "/jdbc/**",
                            "/thlf/**",
                            "/validation-demo/**")
                    .csrfTokenRepository(csrfTokenRepository);

            http.sessionManagement()
                    .invalidSessionUrl("/error/login-timeout");

            http.authorizeRequests()
                    .antMatchers(
                            "/security-demo/menu",
                            "/security-demo/change-password",
                            "/security-demo/data-ajax")
                    .hasAnyRole("USER", "ADMIN")
                    .antMatchers("/security-demo/menu/user", "/image-user/**").hasRole("USER")
                    .antMatchers("/security-demo/menu/admin", "/image-admin/**").hasRole("ADMIN")
                    .anyRequest().permitAll();

            AuthenticationFailureHandler afh = new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res,
                        AuthenticationException e) throws IOException, ServletException {
                    SimpleUrlAuthenticationFailureHandler delegate = new SimpleUrlAuthenticationFailureHandler(
                            "/security-demo/login?errorReason=unknown");
                    if (e instanceof BadCredentialsException) {
                        delegate.setDefaultFailureUrl("/security-demo/login?errorReason=badcredential");
                    }
                    if (e instanceof LockedException) {
                        delegate.setDefaultFailureUrl("/security-demo/login?errorReason=locked");
                    }
                    if (e instanceof AccountExpiredException) {
                        delegate.setDefaultFailureUrl("/security-demo/login?errorReason=account-expired");
                    }
                    if (e instanceof CredentialsExpiredException) {
                        delegate.setDefaultFailureUrl("/security-demo/login?errorReason=credential-expired");
                    }
                    if (e instanceof DisabledException) {
                        delegate.setDefaultFailureUrl("/security-demo/login?errorReason=disabled");
                    }
                    delegate.onAuthenticationFailure(req, res, e);
                }
            };
            http.formLogin()
                    .loginPage("/security-demo/login")
                    .usernameParameter("login_user")
                    .passwordParameter("login_pwd")
                    .loginProcessingUrl("/security-demo/dologin")
                    .defaultSuccessUrl("/security-demo/menu")
                    .failureHandler(afh)
                    .permitAll();

            // JDBCのDBを使ったremember-me トークン管理の例。
            JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
            jdbcTokenRepository.setDataSource(dataSource);
            http.rememberMe()
                    .alwaysRemember(false)
                    .key(env.getProperty("custom.spring-security.remember-me.key", "12345"))
                    .rememberMeCookieDomain(env.getProperty("custom.spring-security.remember-me.cookie.domain", "localhost"))
                    .rememberMeCookieName(env.getProperty("custom.spring-security.remember-me.cookie.name", "remtok"))
                    .rememberMeParameter(env.getProperty("custom.spring-security.remember-me.parameter", "rememberme"))
                    .tokenValiditySeconds(60)
                    // これによりJDBC管理になる。これを呼ばなければ、デフォルトでは TokenBasedRememberMeServices を使う。
                    .tokenRepository(jdbcTokenRepository);

            http.logout()
                    .logoutUrl("/security-demo/dologout")
                    .logoutSuccessUrl("/security-demo/logouted")
                    .invalidateHttpSession(true)
                    /* deleteCookies() は org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler 
                     * のショートカットだが、max-age以外の domain, path, secure属性を細くカスタマイズできないため、
                     * "server.session.cookie.*" のプロパティ設定とズレが生じ、正しくクライアント側で Cookie が削除できない。
                     * そのため、"server.session.cookie.*" のプロパティ設定を使った独自の
                     * FineControlledSessionCookieClearingLogoutHandler
                     * クラスを独自に作成し、それを addLogoutHandler() で登録してみた。
                     */
                    .addLogoutHandler(new FineControlledSessionCookieClearingLogoutHandler(env))
                    .permitAll();

            LoginUrlAuthenticationEntryPoint customAuthEndPoint = new LoginUrlAuthenticationEntryPoint("/security-demo/login?reason=authEPDemo");
            // CSRFトークンエラーの場合は画面を出し分けできるような AccessDeniedHandlerの例。
            AccessDeniedHandler ah = new AccessDeniedHandler() {
                @Override
                public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
                    LOG.warn("<+>##SECURITY EVENT##<-> : AccessDeniedHandler", e);
                    AccessDeniedHandlerImpl adh = new AccessDeniedHandlerImpl();
                    if (e instanceof CsrfException) {
                        // CSRFのチェックエラーの画面カスタマイズ例。
                        // see:
                        // http://progmemo.wp.xdomain.jp/archives/847
                        // http://progmemo.wp.xdomain.jp/archives/858
                        adh.setErrorPage("/error/csrf");
                    }
                    adh.handle(req, res, e);
                }

            };
            http.exceptionHandling()
                    .authenticationEntryPoint(customAuthEndPoint)
                    .accessDeniedHandler(ah);

            http.headers()
                    .defaultsDisabled()
                    // HttpSecurity#headers()からの、特定 AntMatcher に絞った範囲での静的なレスポンスヘッダー出力の例。
                    .addHeaderWriter(new StaticHeadersWriter("X-Custom-Security-Header", "aaa", "bbb", "ccc"))
                    // これはSpring Security 4.2 まででは含まれてないので、自分で追加した。
                    .addHeaderWriter(new StaticHeadersWriter("X-Download-Options", "noopen"))
                    .cacheControl().and()
                    .contentSecurityPolicy("default-src *; script-src * 'unsafe-inline'; img-src * data: blob:;style-src * 'unsafe-inline'").and()
                    .contentTypeOptions().and()
                    .xssProtection().block(true).and()
                    .frameOptions().sameOrigin();
        }

        @Autowired
        DataSource dataSource;

        @Autowired
        LoginUserDetailsService loginUserDetailsService;

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            PasswordEncoder pe = passwordEncoder();
            LOG.info("password :=> {}", pe.encode("password"));
            LOG.info("password1 :=> {}", pe.encode("password1"));
            LOG.info("password2 :=> {}", pe.encode("password2"));
            LOG.info("password3 :=> {}", pe.encode("password3"));
            LOG.info("password4 :=> {}", pe.encode("password4"));
            LOG.info("password5 :=> {}", pe.encode("password5"));
            auth.userDetailsService(loginUserDetailsService).passwordEncoder(pe);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
}

class FineControlledSessionCookieClearingLogoutHandler implements LogoutHandler {
    final String name;
    final String path;
    final String domain;
    final boolean secure;

    public FineControlledSessionCookieClearingLogoutHandler(Environment env) {
        this.name = env.getProperty("server.session.cookie.name", "JSESSIONID");
        this.path = env.getProperty("server.session.cookie.path", "/");
        this.domain = env.getProperty("server.session.cookie.domain", "");
        this.secure = Boolean.valueOf(env.getProperty("server.session.cookie.secure", "false"));
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
        Cookie cookie = new Cookie(this.name, null);
        if (StringUtils.hasLength(this.path)) {
            cookie.setPath(this.path);
        }
        if (StringUtils.hasLength(this.domain)) {
            cookie.setDomain(this.domain);
        }
        cookie.setSecure(this.secure);
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }
}