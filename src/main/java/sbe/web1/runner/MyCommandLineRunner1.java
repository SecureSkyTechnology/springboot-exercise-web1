package sbe.web1.runner;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class MyCommandLineRunner1 extends MyCommandLineRunner {
}
