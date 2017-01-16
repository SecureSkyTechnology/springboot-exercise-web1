# springboot-exercise-web1
Spring Framework, Spring Boot を使ったWebアプリケーションの練習その1

## 開発環境, ビルド, 実行方法

Spring Boot : 1.4.2.RELEASE (2017-01時点, 詳細はpom.xml参照)

JDK : jdk1.8.0_92, 64bit

STS:

```
Version: 3.8.3.RELEASE
Build Id: 201612191351
Platform: Eclipse Neon.2 (4.6.2)
```
プロジェクトルート直下の `springboot-exercise-web1-cleanup.xml`, `springboot-exercise-web1-formatter.xml` はそれぞれEclipse用の Clean Up 設定ファイル, Java Editor の Code Formatter 設定ファイルになりますので、それぞれImportして使ってください。

ビルド:

```
unix(shell):
$ ./mvnw package

Win(command prompt):
> mvnw.cmd package
```
実行: JMX機能を使うため、JMXの認証設定が本来なら必要。サンプルとしてだけなので、認証無効化のJMXオプションを渡す。

```
java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -jar target/springboot-exercise-web1-0.0.1-SNAPSHOT.jar

or

./mvnw -Drun.jvmArguments="-Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false" spring-boot:run
```

デフォルトでは以下のURLでアクセスできるようになる。

* http://localhost:8080/sb1/
* http://localhost:8080/sb1/my-h2-console/
 * h2db の Webコンソール。Basic認証をかけているため、 `h2admin`, パスワード `password` でログイン。
 * 使用するJDBCのURLについては、 `application.properties` 中の `spring.datasource.url` 参照。
 * production profileで起動したときは無効になります。
* http://localhost:8081/actuator-manage/(各 Actuator Endpoint)
 * Basic認証をかけているため、 `actadmin`, パスワード `password` でログイン。

デフォルトのprofileは default です。profileを変更したり、使用するh2dbを変更したい場合は以下のように環境変数を調整します。

```
export SPRING_PROFILES_ACTIVE=production
# 例 : localhost の 8182 で起動したh2dbにTCP接続
export SPRING_DATASOURCE_URL=jdbc:h2:tcp://localhost:8182/test2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
export SPRING_DATASOURCE_USERNAME=sa
java ... -jar target/springboot-exercise-web1-0.0.1-SNAPSHOT.jar
```

## 参考資料

Spring 公式

* Docs
 * https://spring.io/docs/reference

Thymeleaf 公式

* Tutorial: Using Thymeleaf
 * http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html
* Tutorial: Thymeleaf + Spring
 * http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html
* Thymeleaf
 * http://www.thymeleaf.org/

書籍

* Spring Framework 4 プログラミング入門｜書籍情報｜秀和システム
 * http://www.shuwasystem.co.jp/products/7980html/4156.html
* Spring Boot プログラミング入門｜書籍情報｜秀和システム
 * http://www.shuwasystem.co.jp/products/7980html/4565.html
* Spring徹底入門 Spring FrameworkによるJavaアプリケーション開発（株式会社NTTデータ） ｜ 翔泳社の本
 * http://www.shoeisha.co.jp/book/detail/9784798142470

Web記事

* Spring Bootハンズオン
 * http://jsug-spring-boot-handson.readthedocs.io/en/latest/index.html
* [随時更新]SpringBoot(with Thymeleaf)チートシート - Qiita
 * http://qiita.com/uzresk/items/31a4585f7828c4a9334f
* Spring-Bootの設定プロパティと環境変数 - Qiita
 * http://qiita.com/NewGyu/items/d51f527c7199b746c6b6
* Spring Boot 使い方メモ - Qiita
 * http://qiita.com/opengl-8080/items/05d9490d6f0544e2351a
* Spring MVC(+Spring Boot)上でのリクエスト共通処理の実装方法を理解する - Qiita
 * http://qiita.com/kazuki43zoo/items/757b557c05f548c6c5db
* Spring Framework (Spring Boot)のTips - Qiita
 * http://qiita.com/rubytomato@github/items/d5c68a95900d52cbd5f4
* SpringBootを使っていてハマった事[随時更新] - Qiita
 * http://qiita.com/yakumo/items/026fc4274ac2692e4947
* Spring Boot で Boot した後に作る Web アプリケーション基盤/spring-boot-application-infrastructure // Speaker Deck
 * https://speakerdeck.com/sinsengumi/spring-boot-application-infrastructure

## 主な練習内容

### sbe.web1.mvc パッケージ : Spring Framework, Web関連

* `/basic/`, BasicController : Spring Framework MVCの基本の練習
 * `@Controller`, `@RestController`, `@RequestMapping`, `@GetMapping`
 * メソッド引数による各種リクエストパラメータ・環境情報・Servlet関連オブジェクト(HttpServletRequest/Response, HttpSession, ServletContext) の取得
 * `@ResponseBody`, `@RequestBody` によるJSONリクエスト/レスポンス, 任意形式のレスポンスの処理
 * ファイルアップロード処理
 * 手動による任意の Set-Cookie 発行
 * リダイレクト, フォワード処理
 * Locale と MessageSource のJavaコード内での取得
 * LocaleChangeInterceptor による Locale と messages.properties の切り替え
 * ロギングの練習 (slf4j + logback利用, プロジェクトルート直下の `logback-spring.xml`, `logback.xml` も参照)
* `/attr-demo/`, AttrDemoController
 * `@ModelAttribute`, `@RequestAttribute`, `@SessionAttribute` の練習
* `/thlf/`, ThymeleafSampleController : Thymeleaf の練習
 * Thymeleaf + Spring での基本的な使い方の練習(Model, デフォルトオブジェクトの取得, リンクの生成, 条件分岐とループ処理, コメントアウト)
 * Thymeleaf テンプレート中からの MessageSource 取得, Environment によるプロファイルとプロパティの取得
 * fragment の練習, layout の練習
* `/validation-demo/`, ValidationDemoController : Validation の練習
 * 基本的な Validation アノテーションの練習
 * 単項目のカスタム Validation アノテーション作成の練習
 * 複数項目のカスタム Validation アノテーション作成の練習
 * Web API (`@RestController`, `@ResponseBody + @RequestBody` ) での入力エラーをJSONに変換する練習
* `/exception-demo/`, ExceptionDemoController : 例外の取扱とエラーページのカスタマイズの練習
 * コントローラのメソッド内で発生した例外の扱い
 * リクエストパラメータのBinding時の例外の扱い
 * HandlerInterceptor 内で発生した例外の扱い
 * Servlet Filter 内で発生した例外の扱い
 * Tomcat などの組み込みコンテナのデフォルトエラー画面のカスタマイズ
  * `application.properties` の `server.error.path` プロパティに注目 -> sbe.web1.MyErrorController コントローラへの連携
 * 例外クラスに応じたエラー画面の切り替え : `sbe.web1.MyExceptionHandler` 参照
* `/jdbc/`, JdbcTemplateDemoController
 * JdbcTemplate の練習
* `/misc/`, MiscSampleController
 * Controller の Javaコードからの ApplicationContext, Environment, ApplicationArguments インスタンスの取得と操作の練習
 * profile, プロパティの取り出しの練習
 * MyIntercept3, 独自追加した MyServlet を動かすURLリンク
* `/security-demo/`, SecurityDemoController, `/security-demo-api/`, SecurityDemoApiController
 * Spring Security の練習(後述)

### sbe.web1 パッケージ直下 + 他 : TIPS

* MyApplication
 * HttpServletRequest, ServletContext, ServerProperties のダンプ出力メソッドの練習
* IndexController
 * webjarを使ったjqueryをThymeleafのテンプレートで利用する練習
 * Spring Security でのログインタイムアウト, CSRFエラー画面の練習
* MyContextEventListener
 * Contextのライフサイクルイベントの観察の練習
* MySecurityEventListener
 * Spring Security で発行されるイベントの観察の練習
* MyWebMvcConfigurerAdapter
 * Locale をCookieに保存する CookieLocaleResolver の練習
 * 明示的に DataSource をBean定義する練習
 * tomcat-catalina-jmx-remote によりコンテナ設定でJMX RMI RegistryPort/ServerPort を固定する練習
 * sbe.web1.interceptors パッケージ以下の HandlerInterceptor の登録
* MyWebSecurityConfig
 * Spring Security の練習(後述)

* sbe.web1.runner パッケージ以下
 * ApplicationRunner の練習
 * CommandLineRunner の練習

* sbe.web1.servlet パッケージ以下
 * 独自の Servlet, Servlet Filter, ServletContextListener, HttpSessionListener, HttpSessionAttributeListener の練習

* src/main/resources/db/ 以下
 * flyway migrationの練習
 * Spring Boot による schema.sql, data.sql の練習(`application.properties` 中の `spring.datasource.initialize` 参照。)

### Spring Security の練習

* sbe.web1.MyWebSecurityConfig : 以下の5種類の認証設定を混在させるデモ
 * h2db の Web コンソールに対してBasic認証
 * actuator の各エンドポイントに対してBaisc認証
 * `/security-demo/basic-auth` に対してBaisc認証
 * `/security-demo-api/` に対してDBを使ったフォーム認証
 * `/security-demo/` に対してDBを使ったフォーム認証

* `/security-demo-api/` の詳細
 * Web API用にログイン時にJSONでユーザ情報を返したり、ステータスコードで認証系のエラー状態を返せるようカスタマイズ
 * In-Memoryを使ったRemember-Me
 * `/security-demo-api/regen-csrftoken` へのアクセスでCSRFトークンを再生成してJSONで返すデモ
 * CSRFトークンエラー時のレスポンスのカスタマイズ
 * ログアウト時により細かくオプションを指定してセッションID Cookieを削除できるようカスタマイズ
 * レスポンスヘッダのカスタマイズ

* `/security-demo/` の詳細
 * DBを使ったRemember-Me
 * CSRFトークンエラー時のレスポンスのカスタマイズ
 * ログアウト時により細かくオプションを指定してセッションID Cookieを削除できるようカスタマイズ
 * Spring Security がサポートしている、セキュリティ系のレスポンスヘッダーのカスタマイズ
 * 初期化データ用の暗号化パスワード生成のデモ
 * src/main/resources/static 以下の静的リロースに対する認可チェックのカスタマイズ

### 開発, 運用関連

* Spring Boot の Actuator 組み込みの練習 (ポートを分離, Context Path 設定, 独自Basic認証設定, `shutdown` Endpoint の有効化)
* spring-boot-devtools 導入による、STS上で保存 -> 即時リブートによるインクリメンタル開発の練習 + jarビルド時には除外する設定組み込み
* JMX接続受付 + tomcat-catalina-jmx-remote 導入によるJMX RMI RegistryPort/ServerPort の固定 : MyWebMvcConfigurerAdapter 参照。
