package com.example.demo.service;

import com.example.demo.dto.StudentRequest;
import com.example.demo.dto.StudentResponse;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.model.Role;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    // CREATE
    public StudentResponse createStudent(StudentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));


        user.setRole(Role.STUDENT);

        Student student = new Student();
        student.setAcademicNumber(request.getAcademicNumber());
        student.setLevel(request.getLevel());
        student.setUser(user);

        student = studentRepository.save(student);
        return mapToResponse(student);
    }

    // GET ALL
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return mapToResponse(student);
    }

    // UPDATE
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));


        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        student.setAcademicNumber(request.getAcademicNumber());
        student.setLevel(request.getLevel());
        student.setUser(user);

        student = studentRepository.save(student);
        return mapToResponse(student);
    }

    // DELETE
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    // MAPPING
    private StudentResponse mapToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setAcademicNumber(student.getAcademicNumber());
        response.setLevel(student.getLevel());
        if (student.getUser() != null) {
            response.setUserId(student.getUser().getId());
        }
        return response;
    }
}