package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AIInteractionResponse {
    private Long id;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
    private Long studentId;

}
