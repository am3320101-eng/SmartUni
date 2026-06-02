package org.example.smartunipro.repository;

import org.example.smartunipro.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByAcademicNumber(String academicNumber);

    boolean existsByAcademicNumber(String academicNumber);

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);
}