package com.ifsc.secstor.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SplitDTO {

    private Object data;

    @NotBlank(message = "Algorithm must be provided")
    private String algorithm;
}
