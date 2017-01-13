package sbe.web1.mvc.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

/**
 * @author http://www.shuwasystem.co.jp/products/7980html/4565.html
 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Phone {
    String message() default "{com.sst.mvc.validator.Phone.message}.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean onlyNumber() default false;
}
