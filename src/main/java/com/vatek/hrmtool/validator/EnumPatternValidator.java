package com.vatek.hrmtool.validator;

import com.vatek.hrmtool.validator.anotation.EnumPatternConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author itsol.hungtt on 2/1/2021
 */
public class EnumPatternValidator implements ConstraintValidator<EnumPatternConstraint, String> {
    private Pattern pattern;
    private boolean nullable;

    @Override
    public void initialize(EnumPatternConstraint constraintAnnotation) {
        try {
            pattern = Pattern.compile(constraintAnnotation.regexp());
            nullable = constraintAnnotation.nullable();
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Given regex is invalid", e);
        }
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (nullable && s == null) {
            return true;
        }
        if (s == null) {
            return false;
        }
        Matcher m = pattern.matcher(s);
        return m.matches();
    }
}
