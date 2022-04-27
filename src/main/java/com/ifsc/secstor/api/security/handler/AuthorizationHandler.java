package com.ifsc.secstor.api.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.model.ErrorModel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), new ErrorModel(FORBIDDEN.value(), "Authorization Error",
                "Insufficient permission to access this resource", request.getServletPath()));
    }
}
