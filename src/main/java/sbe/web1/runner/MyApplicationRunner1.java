package sbe.web1.runner;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.boot.ApplicationRunner} を複数作った場合に、 Order
 * アノテーションで順序を制御するデモ
 */
@Component
@Order(1)
public class MyApplicationRunner1 extends MyApplicationRunner {
}
