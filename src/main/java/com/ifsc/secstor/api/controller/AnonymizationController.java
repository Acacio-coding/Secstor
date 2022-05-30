package com.ifsc.secstor.api.controller;

import com.ifsc.secstor.api.dto.AnonymizationDTO;
import com.ifsc.secstor.api.service.AnonymizationServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ifsc.secstor.api.advice.paths.Paths.DATA_ANONYMIZATION_ANONYMIZE;
import static com.ifsc.secstor.api.advice.paths.Paths.DATA_ANONYMIZATION_BASE;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(DATA_ANONYMIZATION_BASE)
public class AnonymizationController {

    private final AnonymizationServiceImplementation anonymizationService;

    public AnonymizationController() {
        this.anonymizationService = new AnonymizationServiceImplementation();
    }

    @PostMapping(DATA_ANONYMIZATION_ANONYMIZE)
    public ResponseEntity<Object> anonymize(@RequestBody AnonymizationDTO anonymizationDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.anonymizationService.anonymize(anonymizationDTO));
    }
}
