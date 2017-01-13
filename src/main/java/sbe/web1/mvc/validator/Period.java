package sbe.web1.mvc.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author http://ziqoo.com/wiki/index.php?Spring%20Boot%20%C6%FE%CE%CF%A5%C1%A5%A7%A5%C3%A5%AF
 */
@Documented
@Constraint(validatedBy = { PeriodValidator.class })
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Period {
    String message() default "{com.sst.mvc.validator.Period.message}.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String from();

    String to();

    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public static @interface List {
        Period[] value();
    }
}
