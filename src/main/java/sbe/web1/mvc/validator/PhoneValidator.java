package sbe.web1.mvc.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author http://www.shuwasystem.co.jp/products/7980html/4565.html
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private boolean onlyNumber = false;

    @Override
    public void initialize(Phone constraintAnnotation) {
        onlyNumber = constraintAnnotation.onlyNumber();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (onlyNumber) {
            return value.matches("[0-9]*");
        } else {
            return value.matches("[0-9()-]*");
        }
    }
}
