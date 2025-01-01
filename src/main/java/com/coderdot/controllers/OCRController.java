package com.coderdot.controllers;

import com.coderdot.services.Implemtations.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
@CrossOrigin(origins = "*") // Autoriser les requêtes CORS pour Angular
public class OCRController {

    @Autowired
    private OCRService ocrService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            String extractedText = ocrService.extractText(file);
            return ResponseEntity.ok().body(extractedText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'extraction OCR");
        }
    }
}
