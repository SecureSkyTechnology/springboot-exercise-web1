package sbe.web1;

import java.util.Locale;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.apache.catalina.mbeans.JmxRemoteLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import sbe.web1.mvc.interceptors.MyAttrsIntercept;
import sbe.web1.mvc.interceptors.MyIntercept1;
import sbe.web1.mvc.interceptors.MyIntercept2;
import sbe.web1.mvc.interceptors.MyIntercept3;
import sbe.web1.mvc.interceptors.MyIntercept4;

@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    static final Logger LOG = LoggerFactory.getLogger(MyWebMvcConfigurerAdapter.class);

    @Value("${tomcat.jmx.rmiRegistryPort:7970}")
    int rmiRegistryPort;

    @Value("${tomcat.jmx.rmiServerPort:7971}")
    int rmiServerPort;

    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setDefaultLocale(Locale.getDefault());
        clr.setDefaultTimeZone(TimeZone.getDefault());
        clr.setCookieName("mylocale");
        clr.setCookieMaxAge(3600);
        clr.setCookieHttpOnly(true);
        return clr;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        // equivalent to Spring Boot's default data source.
        // see: http://docs.spring.io/spring-boot/docs/1.4.3.RELEASE/reference/htmlsingle/#howto-two-datasources
        return DataSourceBuilder.create().build();
    }

    @Bean
    public EmbeddedServletContainerCustomizer cookieProcessorCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    LOG.info("####+++ Tomcat Container Customize, RMI RegistryPort set to {}, RMI ServerPort set to {}",
                            rmiRegistryPort, rmiServerPort);
                    JmxRemoteLifecycleListener lifecycleListener = new JmxRemoteLifecycleListener();
                    lifecycleListener.setRmiRegistryPortPlatform(rmiRegistryPort);
                    lifecycleListener.setRmiServerPortPlatform(rmiServerPort);
                    ((TomcatEmbeddedServletContainerFactory) container).addContextLifecycleListeners(lifecycleListener);
                }
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor i = new LocaleChangeInterceptor();
        i.setParamName("newLocale");
        registry.addInterceptor(i).addPathPatterns("/basic/change_locale");
        registry.addInterceptor(new MyIntercept1());
        registry.addInterceptor(new MyIntercept2()).addPathPatterns("/misc/*");
        registry.addInterceptor(new MyIntercept3()).addPathPatterns("/misc/intercept3");
        registry.addInterceptor(new MyIntercept4()).addPathPatterns("/exception-demo/*");
        registry.addInterceptor(new MyAttrsIntercept()).addPathPatterns("/attr-demo/*");
    }
}
