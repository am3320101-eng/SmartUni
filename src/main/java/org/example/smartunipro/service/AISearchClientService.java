package org.example.smartunipro.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public AISearchClientService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String askAI(String question) {
        try {
            log.info("Sending Clean JSON to AI URL: {}/chat with question: {}", aiServiceUrl, question);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("ngrok-skip-browser-warning", "true");

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("question", question);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    aiServiceUrl + "/chat",
                    entity,
                    String.class
            );

            log.info("AI service raw response: {}", response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());
            if (root.has("answer")) {
                JsonNode answerNode = root.get("answer");
                if (answerNode.isTextual()) {
                    return answerNode.asText();
                } else if (answerNode.isArray()) {
                    StringBuilder sb = new StringBuilder();
                    for (JsonNode node : answerNode) {
                        sb.append(node.asText()).append(" ");
                    }
                    return sb.toString().trim();
                } else {
                    return answerNode.toString();
                }
            }

            return "لم أتمكن من الحصول على إجابة من سيرفر الـ AI.";

        } catch (Exception e) {
            log.error("AI connection failed. Error Details: ", e);
            return "عذراً، خدمة الذكاء الاصطناعي غير متوفرة حالياً بسبب: " + e.getMessage();
        }
    }
}