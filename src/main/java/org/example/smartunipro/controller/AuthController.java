package org.example.smartunipro.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartunipro.dto.AuthResponse;
import org.example.smartunipro.dto.LoginRequest;
import org.example.smartunipro.dto.RegisterRequest;
import org.example.smartunipro.entity.Student;
import org.example.smartunipro.entity.User;
import org.example.smartunipro.exception.CustomException;
import org.example.smartunipro.model.Role;
import org.example.smartunipro.repository.StudentRepository;
import org.example.smartunipro.repository.UserRepository;
import org.example.smartunipro.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // ==================== REGISTER ====================
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {


        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already exists: " + request.getEmail(), HttpStatus.CONFLICT);
        }


        if (studentRepository.existsByAcademicNumber(request.getAcademicNumber())) {
            throw new CustomException("Academic number already exists: " + request.getAcademicNumber(), HttpStatus.CONFLICT);
        }


        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);

        User savedUser = userRepository.save(user);


        Student student = new Student();
        student.setAcademicNumber(request.getAcademicNumber());
        student.setLevel(request.getLevel());
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setUser(savedUser);

        studentRepository.save(student);


        String token = jwtService.generateToken(savedUser.getEmail());


        AuthResponse response = AuthResponse.builder()
                .token(token)
                .message("Registration successful")
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .role(savedUser.getRole())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==================== LOGIN ====================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );


            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));


            String token = jwtService.generateToken(user.getEmail());


            AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .message("Login successful")
                    .userId(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .build();

            return ResponseEntity.ok(response);

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new CustomException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}