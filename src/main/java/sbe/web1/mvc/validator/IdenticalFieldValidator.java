package sbe.web1.mvc.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

public class IdenticalFieldValidator implements ConstraintValidator<IdenticalField, Object> {
    private String src;
    private String dest;
    private String message;

    @Override
    public void initialize(IdenticalField constraintAnnotation) {
        this.src = constraintAnnotation.src();
        this.dest = constraintAnnotation.dest();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Object srcVal = beanWrapper.getPropertyValue(src);
        Object destVal = beanWrapper.getPropertyValue(dest);
        boolean matched = ObjectUtils.nullSafeEquals(srcVal, destVal);
        if (matched) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(src).addConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addPropertyNode(dest).addConstraintViolation();
        return false;
    }

}
