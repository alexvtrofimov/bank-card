package com.example.bankcards.dto;

import com.example.bankcards.controller.validation.ValidPassword;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class UserDto {
    @NotNull
    @Length(min = 3, max = 256, message = "Username length over 3 and less 256")
    private String username;

    @NotNull
    @ValidPassword
    private String password;

    private String role;
}
