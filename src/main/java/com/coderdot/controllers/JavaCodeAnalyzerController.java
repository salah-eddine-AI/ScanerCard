package com.coderdot.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class JavaCodeAnalyzerController {

    @PostMapping("analyze-java-code")
    public Map<String, Object> analyzeJavaCode(@RequestBody String javaCode) {
        Map<String, Object> response = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/api/code/analyze/";  // L'URL de l'API Django

            // Envoi de la chaîne de Java Code directement
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, javaCode, Map.class);
            response = responseEntity.getBody();  // Obtenez les recommandations retournées par l'API Django

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to call Django API: " + e.getMessage());
        }

        return response;
    }
    @PostMapping("generate-java-pattern")
    public Map<String, Object> generateJavaPattern(@RequestBody String javaCode) {
        Map<String, Object> response = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/api/code/generate/";  // L'URL de l'API Django
            // Envoi de la chaîne de Java Code directement
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, javaCode, Map.class);
            response = responseEntity.getBody();  // Obtenez les recommandations retournées par l'API Django

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to call Django API: " + e.getMessage());
        }

        return response;
    }
    @PostMapping("analyze_coupling")
    public Map<String, Object> detect_coupling(@RequestBody String javaCode) {
        Map<String, Object> response = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/api/code/analyze_coupling/";  // L'URL de l'API Django
            // Envoi de la chaîne de Java Code directement
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, javaCode, Map.class);
            response = responseEntity.getBody();  // Obtenez les recommandations retournées par l'API Django

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to call Django API: " + e.getMessage());
        }

        return response;
    }
}
