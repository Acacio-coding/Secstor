package com.ifsc.secstor.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.model.UserErrorModel;
import com.ifsc.secstor.api.security.jwt.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.AUTHENTICATION_ERROR;
import static com.ifsc.secstor.api.advice.messages.ErrorMessages.USER_NOT_FOUND;
import static com.ifsc.secstor.api.util.Constants.PASSWORD;
import static com.ifsc.secstor.api.util.Constants.USERNAME;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return this.authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        JWTUtils algorithmUtils = new JWTUtils();

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), algorithmUtils.createTokens(user, request));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(),
                new UserErrorModel(UNAUTHORIZED.value(), AUTHENTICATION_ERROR, USER_NOT_FOUND, request.getServletPath()));
    }
}
