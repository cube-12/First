package com.example.librarysystem.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class DeleteAccountDto {
    @NotBlank(message = "密码不能为空")
    private String password;
}
