package com.ifsc.secstor.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.NULL_ALGORITHM;

@Getter
@Setter
public class SplitDTO {

    private Object data;

    @NotBlank(message = NULL_ALGORITHM)
    private String algorithm;
}
