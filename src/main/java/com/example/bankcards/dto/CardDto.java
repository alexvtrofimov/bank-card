package com.example.bankcards.dto;

import com.example.bankcards.controller.validation.ValidCardStatus;
import com.example.bankcards.controller.validation.ValidExpirationDateFormat;
import com.example.bankcards.controller.validation.ValidExpirationDatePeriod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardDto {
    @NotNull
    @ValidExpirationDateFormat
    @ValidExpirationDatePeriod
    private String expiration;

    @ValidCardStatus
    private String status;

    private BigDecimal balance = BigDecimal.ZERO;
}
