package com.ifsc.secstor.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.secstor.api.dto.UserDTO;

import com.ifsc.secstor.api.model.UserModel;
import com.ifsc.secstor.api.service.UserServiceImplementation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.ifsc.secstor.api.advice.messages.SuccessMessages.*;
import static com.ifsc.secstor.api.advice.paths.Paths.*;
import static com.ifsc.secstor.api.advice.paths.Paths.REFRESH_TOKEN;
import static com.ifsc.secstor.api.advice.paths.Paths.USER;
import static com.ifsc.secstor.api.util.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(USER_BASE)
public class UserController {

    private final UserServiceImplementation userService;

    @GetMapping(USERS)
    @Operation(hidden = true)
    public ResponseEntity<Page<UserModel>> getAllUsers(
            @PageableDefault(sort = ID, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAllUsers(pageable));
    }

    @GetMapping(USER)
    @Operation(hidden = true)
    public ResponseEntity<Object> getUser(@PathVariable(value = USERNAME) String username) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findUserByUsername(username));
    }

    @PostMapping(SAVE_USER)
    @Operation(summary = REGISTER_TITLE, description = REGISTER_DESCRIPTION, tags = USER_TAG)
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200_CODE, description = HTTP_200_DESCRIPTION,
                    content = @Content(schema = @Schema(example = REGISTER_SUCCESS))),
            @ApiResponse(responseCode = HTTP_400_CODE, description = HTTP_400_DESCRIPTION,
                    content = @Content(schema = @Schema(example = REGISTER_ERROR)))
    })
    public ResponseEntity<String> saveUser(@RequestBody @Validated UserDTO userDTO) {
        this.userService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(SAVE_SUCCESSFUL);
    }

    @PutMapping(USER)
    @Operation(hidden = true)
    public ResponseEntity<String> updateUser(@PathVariable(value = USERNAME) String username,
                                             @RequestBody @Validated UserDTO userDTO) {
        this.userService.updateUser(username, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(UPDATE_SUCCESSFUL);
    }

    @DeleteMapping(USER)
    @Operation(hidden = true)
    public ResponseEntity<String> deleteUser(@PathVariable(value = USERNAME) String username) {
        this.userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUCCESSFUL);
    }

    //Refresh token
    @GetMapping(REFRESH_TOKEN)
    @Operation(summary = REFRESH_TOKEN_TITLE, description = REFRESH_TOKEN_DESCRIPTION,
            tags = AUTHENTICATION_TAG, security = @SecurityRequirement(name = BEARER_AUTH))
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200_CODE, description = HTTP_200_DESCRIPTION,
                    content = @Content(schema = @Schema(example = LOGIN_RESPONSE_SUCCESS))),
            @ApiResponse(responseCode = HTTP_400_CODE, description = HTTP_400_DESCRIPTION,
                    content = @Content(schema = @Schema(example = REFRESH_TOKEN_RESPONSE_ERROR)))
    })
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> tokens = this.userService.refreshToken(request, response);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @PostMapping(LOGIN)
    @Operation(summary = LOGIN_TITLE, description =  LOGIN_DESCRIPTION, tags = AUTHENTICATION_TAG)
    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTP_200_CODE, description = HTTP_200_DESCRIPTION,
                    content = @Content(schema = @Schema(example = LOGIN_RESPONSE_SUCCESS))),
            @ApiResponse(responseCode = HTTP_400_CODE, description = LOGIN_400_RESPONSE,
                    content = @Content(schema = @Schema(example = LOGIN_RESPONSE_ERROR)))
    })
    public void login(HttpServletRequest request, String username, String password) throws ServletException {
        request.login(username, password);
    }
}
