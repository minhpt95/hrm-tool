package com.vatek.hrmtool.validator.anotation;

import com.vatek.hrmtool.constant.DateConstant;
import com.vatek.hrmtool.validator.DateFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormatConstraint {

    String message() default "Invalid date";

    String datePattern() default DateConstant.DATE_PATTERN_DD_MM_YYYY;

    boolean required() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
