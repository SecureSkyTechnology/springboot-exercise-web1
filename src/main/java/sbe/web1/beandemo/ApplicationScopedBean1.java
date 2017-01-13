package sbe.web1.beandemo;

import java.util.Arrays;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@ApplicationScope
@Component
public class ApplicationScopedBean1 {
    static final Logger LOG = LoggerFactory.getLogger(ApplicationScopedBean1.class);
    String id;
    String scName;
    String[] profiles;

    public ApplicationScopedBean1(ApplicationContext ac, ServletContext sc) {
        LOG.info("====== constructor.");
        this.id = UUID.randomUUID().toString();
        this.scName = sc.getServletContextName();
        this.profiles = ac.getEnvironment().getDefaultProfiles();
        LOG.info("====== " + this.toString());
    }

    @PostConstruct
    public void postConstruct() {
        LOG.info("====== postConstruct, " + this.toString());
    }

    @PreDestroy
    public void preDestroy() {
        // NOTE : this does NOT called on actuator's shutdown endpoint nor ctrl-c shutdown :(
        LOG.info("====== preDestroy, " + this.toString());
    }

    @Override
    public String toString() {
        return "ApplicationScopedBean1 [id=" + id + ", scName=" + scName + ", profiles=" + Arrays.toString(profiles) + "]";
    }
}
