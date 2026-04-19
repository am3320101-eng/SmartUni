package com.example.demo.controller;

import com.example.demo.dto.AIInteractionRequest;
import com.example.demo.dto.AIInteractionResponse;
import com.example.demo.service.AIInteractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-interactions")
@RequiredArgsConstructor
public class AIInteractionController {

    private final AIInteractionService aiInteractionService;

    // CREATE
    @PostMapping
    public ResponseEntity<AIInteractionResponse> create(@Valid @RequestBody AIInteractionRequest request) {
        return ResponseEntity.ok(aiInteractionService.createAIInteraction(request));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AIInteractionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(aiInteractionService.getById(id));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<AIInteractionResponse>> getAll() {
        return ResponseEntity.ok(aiInteractionService.getAll());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AIInteractionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AIInteractionRequest request) {

        return ResponseEntity.ok(aiInteractionService.updateAIInteraction(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        aiInteractionService.delete(id);
        return ResponseEntity.ok("AI Interaction deleted successfully");
    }
}