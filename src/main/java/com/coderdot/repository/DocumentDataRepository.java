package com.coderdot.repository;

import com.coderdot.entities.DocumentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDataRepository extends JpaRepository<DocumentData, Long> {
}