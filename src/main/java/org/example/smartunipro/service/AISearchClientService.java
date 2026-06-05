package org.example.smartunipro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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

    public String askAI(String question, MultipartFile file) {
        try {
            log.info("Sending Multipart File to AI URL: {}/chat", aiServiceUrl);

            // 1. تظبيط الـ Headers الأساسية لرفع الملفات Multipart
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add("ngrok-skip-browser-warning", "true"); // لتخطي حماية نجروك

            // 2. تجهيز الـ Body من نوع MultiValueMap المخصص للملفات
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("question", question);

            // تحويل الـ MultipartFile لكائن يفهمه الـ RestTemplate أثناء الرفع أونلاين
            if (file != null && !file.isEmpty()) {
                requestBody.add("file", file.getResource());
            } else {
                return "خطأ: لم يتم رفع ملف PDF.";
            }

            // دمج الـ Body والـ Headers
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 3. إرسال الطلب الفعلي أونلاين لسيرفر البايثون
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiServiceUrl + "/chat",
                    entity,
                    Map.class
            );

            // 4. قراءة الإجابة وفك الـ Map المرتجع
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