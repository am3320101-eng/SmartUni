package org.example.smartunipro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Academic number is required")
    @Size(max = 50, message = "Academic number must not exceed 50 characters")
    private String academicNumber;

    @NotBlank(message = "Level is required")
    @Size(max = 50, message = "Level must not exceed 50 characters")
    private String level;
}