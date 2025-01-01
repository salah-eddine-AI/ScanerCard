package com.coderdot.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DocumentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastNameFr;
    private String firstNameFr;
    private String lastNameAr;
    private String firstNameAr;
    private LocalDate dateOfBirth;
    private String birthPlace;
    private LocalDate validity;
    private String cin;
}
