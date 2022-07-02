package com.ifsc.secstor.api.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.model.ErrorModel;
import com.ifsc.secstor.api.model.UserErrorModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.AUTH_ERROR;
import static com.ifsc.secstor.api.advice.messages.ErrorMessages.INSUFFICIENT_PERMISSION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), new UserErrorModel(FORBIDDEN.value(), AUTH_ERROR,
                INSUFFICIENT_PERMISSION, request.getServletPath()));
    }
}
