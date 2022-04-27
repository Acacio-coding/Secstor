package com.ifsc.secstor.api.service;

import com.ifsc.secstor.api.dto.AnonymizationDTO;

import java.io.IOException;

public interface DataAnonymizationService {
    Object anonymize(AnonymizationDTO anonymizationDTO) throws IOException;
}
