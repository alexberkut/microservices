package com.epam.songservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearValidator implements ConstraintValidator<ValidYear, String> {

    @Override
    public boolean isValid(String year, ConstraintValidatorContext context) {
        if (year == null) {
            return true;
        }
        if (!year.matches("\\d{4}")) {
            return false;
        }
        try {
            int yearInt = Integer.parseInt(year);
            return yearInt >= 1900 && yearInt <= 2099;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
