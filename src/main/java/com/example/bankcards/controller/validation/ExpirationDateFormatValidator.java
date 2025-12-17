package com.example.bankcards.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class ExpirationDateFormatValidator implements ConstraintValidator<ValidExpirationDateFormat, String> {
    @Autowired
    private DateTimeFormatter expiringDateFormatter;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            YearMonth.parse(value, expiringDateFormatter);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
