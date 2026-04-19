package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StudentRequest {

    @NotBlank(message = "Academic number is required")
    private String academicNumber;
    @NotBlank(message = "Level is required")
    private String level;
    @NotNull(message = "User ID is required")
    private Long userId;
}
