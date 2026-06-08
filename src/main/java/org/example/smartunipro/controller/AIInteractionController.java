package org.example.smartunipro.controller;

import lombok.RequiredArgsConstructor;
import org.example.smartunipro.dto.AIInteractionDto;
import org.example.smartunipro.service.AIInteractionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIInteractionController {

    private final AIInteractionService aiInteractionService;

    @PostMapping(value = "/ask", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AIInteractionDto> ask(@RequestBody AIInteractionDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(aiInteractionService.ask(requestDto));
    }

    @GetMapping(value = "/history/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AIInteractionDto>> getHistory(@PathVariable Long studentId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(aiInteractionService.getHistory(studentId));
    }
}