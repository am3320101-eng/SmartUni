package com.example.demo.entity;
import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String academicNumber;
    private String level;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAcademicNumber() { return academicNumber; }
    public void setAcademicNumber(String academicNumber) { this.academicNumber = academicNumber; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}