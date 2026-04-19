package com.example.demo.service;

import com.example.demo.dto.InstructorRequest;
import com.example.demo.dto.InstructorResponse;
import com.example.demo.entity.Instructor;
import com.example.demo.entity.User;
import com.example.demo.model.Role;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;

    // CREATE
    public InstructorResponse createInstructor(InstructorRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Instructor instructor = new Instructor();
        instructor.setDepartment(request.getDepartment());
        instructor.setUser(user);


         user.setRole(Role.INSTRUCTOR);

        Instructor saved = instructorRepository.save(instructor);
        return mapToResponse(saved);
    }

    // GET ALL
    public List<InstructorResponse> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public InstructorResponse getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
        return mapToResponse(instructor);
    }

    // UPDATE
    public InstructorResponse updateInstructor(Long id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        instructor.setDepartment(request.getDepartment());
        instructor.setUser(user);

        return mapToResponse(instructorRepository.save(instructor));
    }

    // DELETE
    public void deleteInstructor(Long id) {
        if (!instructorRepository.existsById(id)) {
            throw new RuntimeException("Instructor not found with id: " + id);
        }
        instructorRepository.deleteById(id);
    }

    // MAPPING
    private InstructorResponse mapToResponse(Instructor instructor) {
        InstructorResponse response = new InstructorResponse();
        response.setId(instructor.getId());
        response.setDepartment(instructor.getDepartment());
        if (instructor.getUser() != null) {
            response.setUserId(instructor.getUser().getId());
        }
        return response;
    }
}