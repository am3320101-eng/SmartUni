package com.example.demo.controller;

import com.example.demo.dto.InstructorRequest;
import com.example.demo.dto.InstructorResponse;
import com.example.demo.service.InstructorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    // CREATE
    @PostMapping
    public ResponseEntity<InstructorResponse> create(@Valid @RequestBody InstructorRequest request) {
        return ResponseEntity.ok(instructorService.createInstructor(request));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<InstructorResponse>> getAll() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(instructorService.getInstructorById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<InstructorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InstructorRequest request) {
        return ResponseEntity.ok(instructorService.updateInstructor(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.ok("Instructor deleted successfully");
    }
}