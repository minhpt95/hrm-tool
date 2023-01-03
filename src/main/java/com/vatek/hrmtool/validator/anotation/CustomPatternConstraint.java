package com.vatek.hrmtool.validator.anotation;

import com.vatek.hrmtool.validator.CustomPatternValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomPatternValidators.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CustomPatternConstraint {

    String value();

    String message() default "Must match value defined";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
