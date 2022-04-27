package com.ifsc.secstor.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.dto.UserDTO;

import com.ifsc.secstor.api.model.UserModel;
import com.ifsc.secstor.api.service.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1")
public class UserController {

    private final UserServiceImplementation userService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserModel>> getAllUsers(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAllUsers(pageable));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Object> getUser(@PathVariable(value = "username") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findUserByUsername(username));
    }

    @PostMapping("/user/save")
    public ResponseEntity<String> saveUser(@RequestBody @Validated UserDTO userDTO) {
        this.userService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User saved successfully");
    }

    @PutMapping("/user/{username}")
    public ResponseEntity<String> updateUser(@PathVariable(value = "username") String username,
                                             @RequestBody @Validated UserDTO userDTO) {
        this.userService.updateUser(username, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "username") String username) {
        this.userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    //Refresh token
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> tokens = this.userService.refreshToken(request, response);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
