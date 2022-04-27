package com.ifsc.secstor.api.controller;

import com.ifsc.secstor.api.dto.AnonymizationDTO;
import com.ifsc.secstor.api.service.DataAnonymizationImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/data-anonymization")
public class DataAnonymizationController {

    private final DataAnonymizationImplementation dataAnonymizationService;

    public DataAnonymizationController() {
        this.dataAnonymizationService = new DataAnonymizationImplementation();
    }

    @PostMapping("/anonymize")
    public ResponseEntity<Object> anonymize(@RequestBody @Validated AnonymizationDTO anonymizationDTO) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(this.dataAnonymizationService.anonymize(anonymizationDTO));
    }
}
