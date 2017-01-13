package sbe.web1.mvc.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author http://ziqoo.com/wiki/index.php?Spring%20Boot%20%C6%FE%CE%CF%A5%C1%A5%A7%A5%C3%A5%AF
 */
public class PeriodValidator implements ConstraintValidator<Period, Object> {
    private String from;
    private String to;
    private String message;

    @Override
    public void initialize(Period constraintAnnotation) {
        this.from = constraintAnnotation.from();
        this.to = constraintAnnotation.to();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Date dateFrom = (Date) beanWrapper.getPropertyValue(from);
        Date dateTo = (Date) beanWrapper.getPropertyValue(to);
        if (dateFrom != null && dateTo != null && dateFrom.compareTo(dateTo) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(from).addConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(to).addConstraintViolation();
            return false;
        }
        return true;
    }

}
