package com.example.demo.service;

import com.example.demo.dto.AIInteractionRequest;
import com.example.demo.dto.AIInteractionResponse;
import com.example.demo.entity.AIInteraction;
import com.example.demo.entity.Student;
import com.example.demo.repository.AIInteractionRepository;
import com.example.demo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIInteractionService {

    private final AIInteractionRepository aiInteractionRepository;
    private final StudentRepository studentRepository;

    // CREATE
    public AIInteractionResponse createAIInteraction(AIInteractionRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        AIInteraction ai = new AIInteraction();
        ai.setQuestion(request.getQuestion());
        ai.setAnswer(request.getAnswer()); //
        ai.setStudent(student);

        AIInteraction saved = aiInteractionRepository.save(ai);
        return mapToResponse(saved);
    }

    // GET BY ID
    public AIInteractionResponse getById(Long id) {
        AIInteraction ai = aiInteractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AI Interaction not found with id: " + id));
        return mapToResponse(ai);
    }

    // GET ALL
    public List<AIInteractionResponse> getAll() {
        return aiInteractionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // UPDATE
    public AIInteractionResponse updateAIInteraction(Long id, AIInteractionRequest request) {
        AIInteraction ai = aiInteractionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AI Interaction not found with id: " + id));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        ai.setQuestion(request.getQuestion());
        ai.setAnswer(request.getAnswer());
        ai.setStudent(student);

        return mapToResponse(aiInteractionRepository.save(ai));
    }

    // DELETE
    public void delete(Long id) {
        if (!aiInteractionRepository.existsById(id)) {
            throw new RuntimeException("AI Interaction not found with id: " + id);
        }
        aiInteractionRepository.deleteById(id);
    }

    // MAPPER
    private AIInteractionResponse mapToResponse(AIInteraction ai) {
        AIInteractionResponse res = new AIInteractionResponse();
        res.setId(ai.getId());
        res.setQuestion(ai.getQuestion());
        res.setAnswer(ai.getAnswer());
        res.setCreatedAt(ai.getCreatedAt());

        if (ai.getStudent() != null) {
            res.setStudentId(ai.getStudent().getId());
        }

        return res;
    }
}