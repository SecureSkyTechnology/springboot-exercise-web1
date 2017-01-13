package sbe.web1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

/**
 *
 * Spring Framework's ApplicationContext Event :
 * 7.15.2 Standard and Custom Events
 * @see http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#context-functionality-events
 *
 * Spring Boot's Application Event :
 * 23.5 Application events and listeners
 * @see http://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/htmlsingle/#boot-features-application-events-and-listeners
 */
@Component
public class MyContextEventListener {
    static final Logger LOG = LoggerFactory.getLogger(MyContextEventListener.class);

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent e) {
        LOG.info("========== " + e.toString());
    }

    @EventListener
    public void onContextStarted(ContextStartedEvent e) {
        LOG.info("========== " + e.toString());
    }

    @EventListener
    public void onContextStopped(ContextStoppedEvent e) {
        LOG.info("========== " + e.toString());
    }

    @EventListener
    public void onContextClosed(ContextClosedEvent e) {
        LOG.info("========== " + e.toString());
    }

    @EventListener
    public void onRequestHandled(RequestHandledEvent e) {
        LOG.info("========== " + e.toString());
    }

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent e) {
        LOG.info("=>>======= " + e.toString());
    }

    @EventListener
    public void onApplicationEnvironmentPrepared(ApplicationEnvironmentPreparedEvent e) {
        LOG.info("=>>======= " + e.toString());
    }

    @EventListener
    public void onApplicationPrepared(ApplicationPreparedEvent e) {
        LOG.info("=>>======= " + e.toString());
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent e) {
        LOG.info("=>>======= " + e.toString());
    }

    @EventListener
    public void onApplicationFailed(ApplicationFailedEvent e) {
        LOG.info("=>>======= " + e.toString());
    }
}
