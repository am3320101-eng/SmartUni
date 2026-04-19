package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstructorRequest {
    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "User ID is required")
    private Long userId;
}
