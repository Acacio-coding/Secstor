package com.ifsc.secstor.api.service;

import com.ifsc.secstor.api.advice.exception.ValidationException;
import com.ifsc.secstor.api.dto.AnonymizationDTO;
import com.ifsc.secstor.facade.AnonymizerEngine;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class DataAnonymizationImplementation implements DataAnonymizationService {

    private final AnonymizerEngine engine;

    public DataAnonymizationImplementation() {
        this.engine = new AnonymizerEngine();
    }

    @Override
    public Object anonymize(AnonymizationDTO anonymizationDTO) throws IOException {
        if (anonymizationDTO.getData() == null)
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Data must be provided",
                    "/api/v1/data-anonymization/anonymize");

        return this.engine.anonymize(anonymizationDTO);
    }
}
