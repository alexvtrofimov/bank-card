package com.example.bankcards.controller.validation;

import com.example.bankcards.service.CardStatusService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CardStatusValidation implements ConstraintValidator<ValidCardStatus, String> {
    @Autowired
    private CardStatusService cardStatusService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return cardStatusService.getAllCardStatuses().contains(value);
    }
}
