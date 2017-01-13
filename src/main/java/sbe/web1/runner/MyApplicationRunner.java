package sbe.web1.runner;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * "23.8 Using the ApplicationRunner or CommandLineRunner" demo.
 * 
 * @see http://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/htmlsingle/#boot-features-command-line-runner
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.info("##########" + this.getClass().getName() + ", running...");
        Set<String> opts = args.getOptionNames();
        for (String opt : opts) {
            List<String> vals = args.getOptionValues(opt);
            for (String val : vals) {
                log.info("##########" + this.getClass().getName() + ", Opt[" + opt + "]=" + val);
            }
        }
        List<String> noopts = args.getNonOptionArgs();
        for (int i = 0; i < noopts.size(); i++) {
            log.info("##########" + this.getClass().getName() + ", NonOpt[" + i + "]=" + noopts.get(i));
        }
        String[] raws = args.getSourceArgs();
        for (int i = 0; i < raws.length; i++) {
            log.info("##########" + this.getClass().getName() + ", Source[" + i + "]=" + raws[i]);
        }
    }
}
