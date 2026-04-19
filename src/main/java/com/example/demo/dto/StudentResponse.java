package com.example.demo.dto;

import lombok.Data;

@Data
public class StudentResponse {
    private Long id;
    private String academicNumber;
    private String level;
    private Long userId;
}
