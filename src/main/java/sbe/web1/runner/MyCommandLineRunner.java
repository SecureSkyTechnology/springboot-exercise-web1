package sbe.web1.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * "23.8 Using the ApplicationRunner or CommandLineRunner" demo.
 * 
 * @see http://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/htmlsingle/#boot-features-command-line-runner
 */
@Component
public class MyCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.info("##########" + this.getClass().getName() + ", running...");
        for (int i = 0; i < args.length; i++) {
            log.info("##########" + this.getClass().getName() + ", args[" + i + "]=" + args[i]);
        }
    }
}
