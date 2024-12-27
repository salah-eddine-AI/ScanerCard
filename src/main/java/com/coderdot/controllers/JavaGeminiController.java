package com.coderdot.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class JavaGeminiController {

    @PostMapping("/analyze_repoGit")
    public Map<String, Object> analyzeJavaCode(@RequestBody String repoUrl) {
        Map<String, Object> response = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "http://localhost:8000/api/code/analyze_repo/";  // L'URL de l'API Django

            // Header pour indiquer le type de contenu JSON
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // Envoi de la requête POST avec entête et payload JSON
            HttpEntity<String> requestEntity = new HttpEntity<>(repoUrl, headers);
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);

            // Récupération des données renvoyées par l'API Django
            response = responseEntity.getBody();

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erreur lors de l'appel à l'API Django : " + e.getMessage());
        }

        return response;
    }


}
