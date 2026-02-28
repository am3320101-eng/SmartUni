package com.example.demo.entity;
import jakarta.persistence.*;

@Entity
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String department;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}