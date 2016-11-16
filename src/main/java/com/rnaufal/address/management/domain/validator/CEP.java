package com.rnaufal.address.management.domain.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by rnaufal on 14/11/16.
 */
@Documented
@Constraint(validatedBy = CepConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CEP {

    String message() default "{error.address.cep.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}