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

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate;

    public AISearchClientService() {
        this.restTemplate = new RestTemplate();
    }

    public String askAI(String question, Long studentId) {
        // 1. تجهيز الـ Request Body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("question", question);

        try {
            log.info("Sending to AI URL: {}/chat with question: {}", aiServiceUrl, question);

            // 2. تظبيط الـ Headers الأساسية للـ JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            //  السطر السحري لتخطي حماية Ngrok عشان يمرر الطلب مباشرة للبايثون
            headers.add("ngrok-skip-browser-warning", "true");

            // دمج الـ Body مع الـ Headers في كائن واحد
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 3. إرسال الطلب للسيرفر
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiServiceUrl + "/chat",
                    entity,
                    Map.class
            );

            // 4. قراءة الإجابة المرتجعة
            if (response.getBody() != null && response.getBody().containsKey("answer")) {
                return (String) response.getBody().get("answer");
            }

            return "لم أتمكن من الحصول على إجابة من السيرفر.";

        } catch (Exception e) {
            // طباعة تفاصيل الخطأ كاملة في الـ Console لو حصلت أي مشكلة
            log.error("AI connection failed. Error Details: ", e);
            return "عذراً، خدمة الذكاء الاصطناعي غير متوفرة حالياً.";
        }
    }
}