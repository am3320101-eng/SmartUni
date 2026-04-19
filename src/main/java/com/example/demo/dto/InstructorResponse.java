package com.example.demo.dto;

import lombok.Data;

@Data
public class InstructorResponse {
    private Long id;
    private String name;
    private Long userId;

    public void setDepartment(String department) {

    }
}
