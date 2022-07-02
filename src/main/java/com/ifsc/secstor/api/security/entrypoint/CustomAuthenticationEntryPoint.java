package com.ifsc.secstor.api.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.model.UserErrorModel;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.AUTHENTICATION_ERROR;
import static com.ifsc.secstor.api.advice.messages.ErrorMessages.NOT_AUTHENTICATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), new UserErrorModel(UNAUTHORIZED.value(), AUTHENTICATION_ERROR,
                NOT_AUTHENTICATED, request.getServletPath()));
    }
}
