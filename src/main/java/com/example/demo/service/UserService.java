package com.example.demo.service;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    public UserService(UserRepository userRepository, StudentRepository studentRepository, InstructorRepository instructorRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
    }

    public User saveUser(User user) { return userRepository.save(user); }
    public Student saveStudent(Student student) { return studentRepository.save(student); }
    public Instructor saveInstructor(Instructor instructor) { return instructorRepository.save(instructor); }
}