package com.ifsc.secstor.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.model.ErrorModel;
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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/login")) {
            if (request.getParameter("username").isBlank()) {
                setResponse(response, BAD_REQUEST.value(), "Validation Error", "Username is missing", request.getServletPath());
            }

            if (request.getParameter("password").isBlank()) {
                setResponse(response, BAD_REQUEST.value(), "Validation Error", "Password is missing", request.getServletPath());
            }

            filterChain.doFilter(request, response);
        } else if (request.getServletPath().equals("/api/v1/token/refresh")) {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                setResponse(response, FORBIDDEN.value(), "Authorization Error", "Authorization header is missing", request.getServletPath());
            } else {
                filterChain.doFilter(request, response);
            }
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                setResponse(response, FORBIDDEN.value(), "Authorization Error", "Authorization header is missing", request.getServletPath());
            } else if (authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    JWTUtils jwtUtils = new JWTUtils();

                    try {
                        UsernamePasswordAuthenticationToken authenticationToken = jwtUtils.verifyToken(token);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    } catch (Exception exception) {
                        setResponse(response, FORBIDDEN.value(), "Authorization Error", exception.getMessage(), request.getServletPath());
                    }

                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    setResponse(response, INTERNAL_SERVER_ERROR.value(), "Error", exception.getMessage(), request.getServletPath());
                }
            } else {
                setResponse(response, FORBIDDEN.value(),"Authorization Error", "Authorization header is invalid", request.getServletPath());
            }
        }
    }

    private void setResponse(HttpServletResponse response, int status, String errorTitle, String errorDetail, String path) throws IOException {
        response.setStatus(status);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new ErrorModel(status, errorTitle, errorDetail, path));
    }
}
