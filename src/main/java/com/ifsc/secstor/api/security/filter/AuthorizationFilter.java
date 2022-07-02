package com.ifsc.secstor.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.model.UserErrorModel;
import com.ifsc.secstor.api.security.jwt.JWTUtils;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.*;
import static com.ifsc.secstor.api.advice.paths.Paths.*;
import static com.ifsc.secstor.api.util.Constants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(LOGIN_ROUTE)) {
            if (request.getParameter(USERNAME).isBlank()) {
                setResponse(response, BAD_REQUEST.value(), VALIDATION_ERROR, NULL_USERNAME, request.getServletPath());
            }

            if (request.getParameter(PASSWORD).isBlank()) {
                setResponse(response, BAD_REQUEST.value(), VALIDATION_ERROR, NULL_PASSWORD, request.getServletPath());
            }

            filterChain.doFilter(request, response);
        } else if (request.getServletPath().equals(REFRESH_TOKEN_ROUTE_AUTH)) {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                setResponse(response, FORBIDDEN.value(), AUTH_ERROR, NULL_AUTH_HEADER, request.getServletPath());
            } else {
                filterChain.doFilter(request, response);
            }
        } else if (request.getServletPath().equals(REGISTER_ROUTE_AUTH)
                || request.getServletPath().equals(SAVE_USER_AUTH)) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                setResponse(response, FORBIDDEN.value(), AUTH_ERROR, NULL_AUTH_HEADER, request.getServletPath());
            } else if (authorizationHeader.startsWith(TOKEN_BEARER)) {
                try {
                    String token = authorizationHeader.substring(TOKEN_BEARER.length());
                    JWTUtils jwtUtils = new JWTUtils();

                    try {
                        UsernamePasswordAuthenticationToken authenticationToken = jwtUtils.verifyToken(token);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } catch (Exception exception) {
                        setResponse(response, FORBIDDEN.value(),
                                AUTH_ERROR, exception.getMessage(), request.getServletPath());
                    }

                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    setResponse(response, INTERNAL_SERVER_ERROR.value(), "Error", exception.getMessage(),
                            request.getServletPath());
                }
            } else {
                setResponse(response, FORBIDDEN.value(),AUTH_ERROR, INVALID_AUTH_HEADER, request.getServletPath());
            }
        }
    }

    private void setResponse(HttpServletResponse response, int status, String errorTitle, String errorDetail, String path) throws IOException {
        response.setStatus(status);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new UserErrorModel(status, errorTitle, errorDetail, path));
    }
}
