package sbe.web1.runner;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class MyApplicationRunner2 extends MyApplicationRunner {
}
