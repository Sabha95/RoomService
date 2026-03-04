package com.learn.roomseervice.whatsapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppService {

    @Value("${whatsapp.access.token}")
    private String accessToken;

    @Value("${whatsapp.base.url}")
    private String url;

    public void sendUtilityTemplate(String to, String name) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //"recipient_type": "individual",
        String body = """
        {
          "messaging_product": "whatsapp",
          "to": "%s",
          "type": "template",
          "template": {
            "name": "bin_status2",
            "language": { "code": "en_US" }
            }
        }
        """.formatted(to, name);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(url, request, String.class);
    }
}