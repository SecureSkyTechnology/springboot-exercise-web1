# springboot-exercise-web1
Spring Framework, Spring Boot を使ったWebアプリケーションの練習その1

## 開発環境, ビルド, 実行方法
JDK : jdk1.8.0_92, 64bit

STS:

```
Version: 3.8.3.RELEASE
Build Id: 201612191351
Platform: Eclipse Neon.2 (4.6.2)
```
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
* http://localhost:8081/actuator-manage/(各 Actuator Endpoint)
 * Basic認証をかけているため、 `actadmin`, パスワード `password` でログイン。

## 主な練習内容

* `sbe.web1.mvc.BasicController` : Spring Framework MVCの基本の練習
 * `@Controller`, `@RestController`, `@RequestMapping`, `@GetMapping`
 * メソッド引数による各種リクエストパラメータ・環境情報・Servlet関連オブジェクト(`HttpServletRequest/Response`, `HttpSession`, `ServletContext`) の取得
 * `@ResponseBody`, `@RequestBody` によるJSONリクエスト/レスポンス, 任意形式のレスポンスの処理
 * ファイルアップロード処理
 * 手動による任意の `Set-Cookie` 発行
 * リダイレクト, フォワード処理
 * `Locale` と `MessageSource` のJavaコード内での取得
 * ロギングの練習 (slf4j + logback利用, プロジェクトルート直下の `logback-spring.xml`, `logback.xml` も参照)
* `sbe.web1.mvc.AttrDemoController`
 * `@ModelAttribute`, `@RequestAttribute`, `@SessionAttribute` の練習
* `sbe.web1.mvc.ThymeleafSampleController` : Thymeleaf の練習
 * Thymeleaf + Spring での基本的な使い方の練習(`Model`, デフォルトオブジェクトの取得, リンクの生成, 条件分岐とループ処理, コメントアウト)
 * Thymeleaf テンプレート中からの `MessageSource` 取得, `Environment` によるプロファイルとプロパティの取得
 * fragment の練習, layout の練習
* `sbe.web1.mvc.ValidationDemoController` : Validation の練習
 * 基本的な Validation アノテーションの練習
 * 単項目のカスタム Validation アノテーション作成の練習
 * 複数項目のカスタム Validation アノテーション作成の練習
 * Web API (`@RestController`, `@ResponseBody + @RequestBody` ) での入力エラーをJSONに変換する練習
