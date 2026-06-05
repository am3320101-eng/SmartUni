package org.example.smartunipro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AISearchClientService {

    @Value("${AI_SERVICE_URL}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate;

    public AISearchClientService() {
        this.restTemplate = new RestTemplate();
    }

    public String askAI(String question) {
        try {
            log.info("Sending Clean JSON to AI URL: {}/chat with question: {}", aiServiceUrl, question);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("ngrok-skip-browser-warning", "true");

            // بنبعت السؤال النصي القصير في الـ body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("question", question);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiServiceUrl + "/chat",
                    entity,
                    Map.class
            );

            if (response.getBody() != null && response.getBody().containsKey("answer")) {
                return (String) response.getBody().get("answer");
            }

            return "لم أتمكن من الحصول على إجابة من سيرفر الـ AI.";

        } catch (Exception e) {
            log.error("AI connection failed. Error Details: ", e);
            return "عذراً، خدمة الذكاء الاصطناعي غير متوفرة حالياً بسبب: " + e.getMessage();
        }
    }
}