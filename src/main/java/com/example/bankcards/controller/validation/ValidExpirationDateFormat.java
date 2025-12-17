package com.example.bankcards.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ExpirationDateFormatValidator.class})
public @interface ValidExpirationDateFormat {
    String message() default "Expiration date format error (required format: 'MM/yyyy')";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
