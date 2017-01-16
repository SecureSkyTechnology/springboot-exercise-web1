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
 * `production` profileで起動したときは無効になります。
* http://localhost:8081/actuator-manage/(各 Actuator Endpoint)
 * Basic認証をかけているため、 `actadmin`, パスワード `password` でログイン。

デフォルトのprofileは `default` です。profileを変更したり、使用するh2dbを変更したい場合は以下のように環境変数を調整します。

```
export SPRING_PROFILES_ACTIVE=production
# 例 : localhost の 8182 で起動したh2dbにTCP接続
export SPRING_DATASOURCE_URL=jdbc:h2:tcp://localhost:8182/test2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
export SPRING_DATASOURCE_USERNAME=sa
java ... -jar target/springboot-exercise-web1-0.0.1-SNAPSHOT.jar
```

## 主な練習内容

### Spring Framework, Web関連

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

### 開発, 運用関連

* Spring Boot の Actuator 組み込みの練習 (ポートを分離, Context Path 設定, 独自Basic認証設定, `shutdown` Endpoint の有効化)
* `spring-boot-devtools` 導入による、STS上で保存 -> 即時リブートによるインクリメンタル開発の練習 + jarビルド時には除外する設定組み込み
* JMX接続受付 + `tomcat-catalina-jmx-remote` 導入によるJMX RMI RegistryPort/ServerPort の固定 : `MyWebMvcConfigurerAdapter` 参照。
