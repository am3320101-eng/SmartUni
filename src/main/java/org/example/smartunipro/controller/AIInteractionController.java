package org.example.smartunipro.controller;

import lombok.RequiredArgsConstructor;
import org.example.smartunipro.dto.AIInteractionDto;
import org.example.smartunipro.service.AIInteractionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIInteractionController {

    private final AIInteractionService aiInteractionService;

    @PostMapping(value = "/ask", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AIInteractionDto> ask(
            @RequestParam("file") MultipartFile file,
            @RequestParam("studentId") Long studentId,
            @RequestParam("question") String question) {

        // بنجمع البيانات النصية جوه الـ Dto عشان نمررها للـ Service مع الملف
        AIInteractionDto requestDto = new AIInteractionDto();
        requestDto.setStudentId(studentId);
        requestDto.setQuestion(question);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aiInteractionService.ask(requestDto, file));
    }

    @GetMapping("/history/{studentId}")
    public ResponseEntity<List<AIInteractionDto>> getHistory(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(aiInteractionService.getHistory(studentId));
    }
}