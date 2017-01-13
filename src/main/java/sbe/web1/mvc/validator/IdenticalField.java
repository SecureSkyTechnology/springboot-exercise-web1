package sbe.web1.mvc.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author http://www.shoeisha.co.jp/book/detail/9784798142470
 */
@Documented
@Constraint(validatedBy = { IdenticalFieldValidator.class })
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdenticalField {
    String message() default "{com.sst.mvc.validator.IdenticalField.message}.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String src();

    String dest();

    @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {
        IdenticalField[] value();
    }
}
