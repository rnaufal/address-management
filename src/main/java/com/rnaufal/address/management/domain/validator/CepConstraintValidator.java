package com.rnaufal.address.management.domain.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by rnaufal on 14/11/16.
 */
public class CepConstraintValidator implements ConstraintValidator<CEP, String> {

    private static final String CEP_PATTERN = "\\d{8}";

    @Override
    public void initialize(CEP constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(CEP_PATTERN);
    }
}
