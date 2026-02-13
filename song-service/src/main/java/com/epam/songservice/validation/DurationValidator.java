package com.epam.songservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DurationValidator implements ConstraintValidator<ValidDuration, String> {

    private static final String DURATION_PATTERN = "^[0-5]\\d:[0-5]\\d$";

    @Override
    public boolean isValid(String duration, ConstraintValidatorContext context) {
        if (duration == null) {
            return false;
        }
        return duration.matches(DURATION_PATTERN);
    }
}
