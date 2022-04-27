package com.ifsc.secstor.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.advice.exception.*;
import com.ifsc.secstor.api.dto.UserDTO;
import com.ifsc.secstor.api.model.ErrorModel;
import com.ifsc.secstor.api.model.Role;
import com.ifsc.secstor.api.model.UserModel;
import com.ifsc.secstor.api.repository.UserRepository;
import com.ifsc.secstor.api.security.jwt.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = this.userRepository.findByUsername(username);

        if (user == null)
            throw new ValidationException(HttpStatus.NOT_FOUND, "User not found with provided credentials", "/api/v1/login");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            response.setStatus(FORBIDDEN.value());
            new ObjectMapper().writeValue(response.getWriter(),
                    new ErrorModel(FORBIDDEN.value(), "Authorization Error", "Authorization header is missing", request.getServletPath()));
        } else if (authorizationHeader.startsWith("Bearer ")) {
            try {
                JWTUtils jwtUtils = new JWTUtils();

                String refreshToken = authorizationHeader.substring("Bearer ".length());
                String username = jwtUtils.getTokenUsername(refreshToken);

                var user = this.userRepository.findByUsername(username);

                return jwtUtils.createTokenByRefreshToken(refreshToken, user, request);
            } catch (Exception exception) {
                response.setStatus(FORBIDDEN.value());
                new ObjectMapper().writeValue(response.getOutputStream(),
                        new ErrorModel(FORBIDDEN.value(), "Authorization Error", exception.getMessage(), request.getServletPath()));
            }
        }

        throw new ValidationException(HttpStatus.BAD_REQUEST, "Authorization header is invalid", request.getServletPath());
    }

    @Override
    public Page<UserModel> findAllUsers(Pageable pageable) {
        var toReturn = this.userRepository.findAllByRole(Role.CLIENT, pageable);

        if (toReturn == null)
            return null;

        toReturn.forEach(model -> model.setPassword("Hashed"));

        return toReturn;
    }

    @Override
    public UserModel findUserByUsername(String username) {
        var user = this.userRepository.findByRoleAndUsername(Role.CLIENT, username);

        if (user == null)
            throw new ValidationException(HttpStatus.NOT_FOUND, "User not found with provided username", "/api/v1/user/" + username);

        user.setPassword("Hashed");

        return user;
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        if (this.userRepository.existsByUsername(userDTO.getUsername()))
            throw new ValidationException(HttpStatus.CONFLICT, "User is already registered", "/api/v1/user/create");

        var user = new UserModel();

        if (userDTO.getRole().equalsIgnoreCase("CLIENT"))
            user.setRole(Role.CLIENT);
        else if (userDTO.getRole().equalsIgnoreCase("ADMINISTRATOR"))
            user.setRole(Role.ADMINISTRATOR);
        else
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Role provided is invalid, it must be either CLIENT or ADMINISTRATOR", "/api/v1/user/create");

        BeanUtils.copyProperties(userDTO, user);

        user.setPassword(encoder.encode(user.getPassword()));

        this.userRepository.save(user);
    }

    @Override
    public void updateUser(String username, UserDTO userDTO) {
        var user = this.userRepository.findByUsername(username);

        if (user == null)
            throw new ValidationException(HttpStatus.NOT_FOUND, "User not found with provided username", "/api/v1/user/" + username);

        if (userDTO.getRole().equalsIgnoreCase("CLIENT"))
            user.setRole(Role.CLIENT);
        else if (userDTO.getRole().equalsIgnoreCase("ADMINISTRATOR"))
            user.setRole(Role.ADMINISTRATOR);
        else
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Role provided is invalid, it must be either CLIENT or ADMINISTRATOR", "/api/v1/" + username);

        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));

        this.userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        var user = this.userRepository.findByUsername(username);

        if (user == null)
            throw new ValidationException(HttpStatus.NOT_FOUND, "User not found with provided username", "/api/v1/user/" + username);

        this.userRepository.delete(user);
    }
}
