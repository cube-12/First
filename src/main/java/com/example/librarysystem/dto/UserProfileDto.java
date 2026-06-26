package com.example.librarysystem.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Date createdAt;

    public UserProfileDto(Long id, String username, String email, String firstName,
                          String lastName, String role, Date createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = createdAt;
    }
}