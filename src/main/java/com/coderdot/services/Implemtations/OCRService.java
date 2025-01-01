package com.coderdot.services.Implemtations;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class OCRService {

    private final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    public String extractText(MultipartFile file) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C://Users//ayoub//Desktop//S_9//PFA//Project//Backend//tessdata"); // Spécifiez le chemin du dossier tessdata
        tesseract.setLanguage("fra");    // Langue (par exemple, anglais)

        try {
            // Convertir MultipartFile en fichier temporaire
            Path tempFile = Files.createTempFile(Paths.get(TEMP_DIR), "ocr_", "_" + file.getOriginalFilename());
            file.transferTo(tempFile);

            // Extraire le texte avec Tesseract
            String extractedText = tesseract.doOCR(tempFile.toFile());

            // Supprimer le fichier temporaire après traitement
            Files.delete(tempFile);

            return extractedText;

        } catch (IOException | TesseractException e) {
            throw new RuntimeException("Erreur lors de l'extraction OCR", e);
        }
    }
}
