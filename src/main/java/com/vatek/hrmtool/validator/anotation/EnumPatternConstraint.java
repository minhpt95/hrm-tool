package com.vatek.hrmtool.validator.anotation;

import com.vatek.hrmtool.validator.EnumPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumPatternValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface EnumPatternConstraint {
    String regexp();

    boolean nullable() default false;

    String message() default "Must match \"{regexp}\"";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
