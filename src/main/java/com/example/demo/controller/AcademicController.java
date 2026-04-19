package com.example.demo.controller;
import com.example.demo.entity.*;
import com.example.demo.service.AcademicService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/academic")
public class AcademicController {
    private final AcademicService academicService;
    public AcademicController(AcademicService academicService)
    { this.academicService = academicService; }

    @PostMapping("/course")
    public Course createCourse(@RequestBody Course course)
    { return academicService.saveCourse(course); }

    @PostMapping("/session")
    public Session createSession(@RequestBody Session session)
    { return academicService.saveSession(session); }
}