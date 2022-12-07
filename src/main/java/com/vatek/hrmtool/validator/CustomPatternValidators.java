package com.vatek.hrmtool.validator;

import com.vatek.hrmtool.validator.anotation.CustomPatternConstraint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author itsol.hungtt on 1/25/2021
 */
public class CustomPatternValidators implements ConstraintValidator<CustomPatternConstraint, String>
{

    private final Environment environment;

    public CustomPatternValidators(Environment environment) {
        this.environment = environment;
    }

    private String rawValue;

    @Override
    public void initialize(CustomPatternConstraint constraintAnnotation)
    {
        rawValue = environment.getProperty(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext)
    {
        if (StringUtils.isBlank(s))
        {
            return false;
        }
        return s.equals(rawValue);
    }
}
