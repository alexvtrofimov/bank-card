package com.example.bankcards.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
public @interface ValidPassword {
    String message() default "Require Strong Passwords: \n" +
            "- least 8 chars \n" +
            "- mix of uppercase, lowercase, numbers, and symbols";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

