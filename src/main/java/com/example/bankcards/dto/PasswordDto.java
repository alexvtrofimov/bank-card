package com.example.bankcards.dto;

import com.example.bankcards.controller.validation.ValidPassword;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordDto {
    @NotNull
    private String oldPassword;

    @NotNull
    @ValidPassword
    private String newPassword;
}
