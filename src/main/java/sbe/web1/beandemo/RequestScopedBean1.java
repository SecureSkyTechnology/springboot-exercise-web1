package sbe.web1.beandemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class RequestScopedBean1 {
    static final Logger LOG = LoggerFactory.getLogger(RequestScopedBean1.class);
    String id;
    String scName;
    String[] profiles;
    String[] params;

    public RequestScopedBean1(ApplicationContext ac, ServletContext sc, HttpServletRequest sr) {
        LOG.info("====== constructor.");
        this.id = UUID.randomUUID().toString();
        this.scName = sc.getServletContextName();
        this.profiles = ac.getEnvironment().getDefaultProfiles();
        List<String> parameters = new ArrayList<>();
        for (Map.Entry<String, String[]> param : sr.getParameterMap().entrySet()) {
            String pname = param.getKey();
            for (String v : param.getValue()) {
                parameters.add("<" + pname + ">:<" + v + ">");
            }
        }
        params = parameters.toArray(new String[parameters.size()]);
        LOG.info("====== " + this.toString());
    }

    @PostConstruct
    public void postConstruct() {
        LOG.info("====== postConstruct, " + this.toString());
    }

    @PreDestroy
    public void preDestroy() {
        LOG.info("====== preDestroy, " + this.toString());
    }

    @Override
    public String toString() {
        return "RequestScopedBean1 [id=" + id + ", scName=" + scName + ", profiles=" + Arrays.toString(profiles) + ", params=" + Arrays.toString(params) + "]";
    }
}
