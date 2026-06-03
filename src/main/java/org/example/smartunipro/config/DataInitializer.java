package org.example.smartunipro.config;

import org.example.smartunipro.entity.User;
import org.example.smartunipro.model.Role;
import org.example.smartunipro.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.count() == 0) {

                System.out.println("====== [CampusMind] Database is empty. Initializing default users... ======");


                User admin = new User();
                admin.setName("System Admin");
                admin.setEmail("admin@uni.com");
                admin.setRole(Role.valueOf("ADMIN"));

                admin.setPassword(passwordEncoder.encode("Admin@123"));
                userRepository.save(admin);


                User doctor = new User();
                doctor.setName("Dr. Mohamed");
                doctor.setEmail("mohamed@uni.com");
                doctor.setRole(Role.valueOf("DOCTOR"));
                doctor.setPassword(passwordEncoder.encode("Doc@123"));
                userRepository.save(doctor);


                User student = new User();
                student.setName("Aya Mohamed");
                student.setEmail("aya@student.uni.com");
                student.setRole(Role.valueOf("STUDENT"));
                student.setPassword(passwordEncoder.encode("Aya@123"));
                userRepository.save(student);

                System.out.println("====== [CampusMind] Default Users Created Successfully! ======");
            } else {

                System.out.println("====== [CampusMind] Database already has data. Skipping initialization. ======");
            }
        };
    }
}
