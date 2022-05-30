package com.ifsc.secstor.api.advice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.VALIDATION_ERROR;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {
    private final HttpStatus status;
    private final String title = VALIDATION_ERROR;
    private final String message;
    private final String path;
}
