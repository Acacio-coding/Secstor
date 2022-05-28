package com.ifsc.secstor.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnonymizationDTO {

    private Object generalization_level;

    private Object attribute_config;

    private Object[] data;
}
