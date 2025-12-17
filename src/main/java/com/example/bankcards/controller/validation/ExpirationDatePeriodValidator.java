package com.example.bankcards.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class ExpirationDatePeriodValidator implements ConstraintValidator<ValidExpirationDatePeriod, String> {
    @Autowired
    private DateTimeFormatter expiringDateFormatter;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            YearMonth expirationDate = YearMonth.parse(value, expiringDateFormatter);
            YearMonth now = YearMonth.now();
            if (!expirationDate.isBefore(now) && !expirationDate.isAfter(now.plusYears(6))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
