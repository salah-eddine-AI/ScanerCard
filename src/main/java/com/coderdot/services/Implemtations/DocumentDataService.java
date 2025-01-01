package com.coderdot.services.Implemtations;

import com.coderdot.entities.DocumentData;
import com.coderdot.repository.DocumentDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentDataService {

    @Autowired
    private DocumentDataRepository repository;

    public DocumentData save(DocumentData data) {
        return repository.save(data);
    }
}
