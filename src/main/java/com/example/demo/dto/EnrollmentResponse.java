package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnrollmentResponse {
    private Long id;
    private Long studentId;
    private Long sessionId;
    private LocalDateTime contollmentDate;
}
