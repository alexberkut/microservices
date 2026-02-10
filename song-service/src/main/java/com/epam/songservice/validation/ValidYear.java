package com.epam.songservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearValidator.class)
public @interface ValidYear {
    String message() default "Year must be between 1900 and 2099";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
