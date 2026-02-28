package com.example.demo.service;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final AttendanceRepository attendanceRepository;
    private final MaterialRepository materialRepository;
    private final AIInteractionRepository aiInteractionRepository;

    public TransactionService(AttendanceRepository attendanceRepository, MaterialRepository materialRepository, AIInteractionRepository aiInteractionRepository) {
        this.attendanceRepository = attendanceRepository;
        this.materialRepository = materialRepository;
        this.aiInteractionRepository = aiInteractionRepository;
    }

    public Attendance markAttendance(Attendance attendance) {
        attendance.setTimestamp(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }
    public Material saveMaterial(Material material) { return materialRepository.save(material); }
    public AIInteraction saveAIInteraction(AIInteraction interaction) {
        interaction.setCreatedAt(LocalDateTime.now());
        return aiInteractionRepository.save(interaction);
    }
}