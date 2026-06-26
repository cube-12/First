package com.example.librarysystem.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateProfileDto {
    @NotBlank(message = "名字不能为空")
    private String firstName;
    @NotBlank(message = "姓氏不能为空")
    private String lastName;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "用户名不能为空")
    private String username;
}
