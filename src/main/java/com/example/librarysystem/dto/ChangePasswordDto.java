package com.example.librarysystem.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ChangePasswordDto {
    @NotBlank(message = "当前密码不能为空")
    private String currentPassword;
    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, message = "新密码至少需要8个字符")
    private String newPassword;
}
