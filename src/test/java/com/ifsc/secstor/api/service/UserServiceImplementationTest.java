package com.ifsc.secstor.api.service;

import com.ifsc.secstor.api.advice.exception.ValidationException;
import com.ifsc.secstor.api.config.SecstorConfig;
import com.ifsc.secstor.api.dto.UserDTO;
import com.ifsc.secstor.api.model.Role;
import com.ifsc.secstor.api.model.UserModel;
import com.ifsc.secstor.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecstorConfig secstorConfig;

    private UserServiceImplementation userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserServiceImplementation(this.userRepository, this.passwordEncoder, this.secstorConfig);
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void refreshToken() {
    }

    @Test
    void findAllUsers_Null() {
        //given
        Pageable pageable = PageRequest.of(0, 8);

        //when
        var result = this.userService.findAllUsers(pageable);

        //then
        verify(this.userRepository).findAllByRole(Role.CLIENT, pageable);

        assertThat(result).isNull();
    }

    @Test
    void findAllUsers_PageOfUserModel() {
        //given
        Pageable pageable = PageRequest.of(0, 8);

        given(this.userRepository.findAllByRole(any(), any())).willReturn(new PageImpl<>(new ArrayList<>()));

        //when
        var result = this.userService.findAllUsers(pageable);

        //then
        verify(this.userRepository).findAllByRole(Role.CLIENT, pageable);

        assertThat(result).isNotNull();
    }

    @Test
    void findUserByUsername_RegisteredUsername_UserModel() {
        //given
        String username = "admin";

        given(this.userRepository.findByRoleAndUsername(Role.CLIENT, username)).willReturn(
                new UserModel(1L, "admin", "admin", Role.ADMINISTRATOR));

        //when
        var result = this.userService.findUserByUsername(username);

        //then
        assertThat(result).isExactlyInstanceOf(UserModel.class);
    }

    @Test
    void findUserByUsername_NotRegisteredUsername_ValidationException() {
        //given
        String username = "admin";

        //then
        assertThatThrownBy(() -> this.userService.findUserByUsername(username))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessageContaining("User not found with provided username",
                        "/api/v1/user/" + username);

        verify(this.userRepository, never()).findByUsername(any());
    }

    @Test
    void saveUser_ValidUserDTOAdminRole_UserModel() {
        //given
        var admin = new UserDTO("admin", "admin", "ADMINISTRATOR");

        //when
        this.userService.saveUser(admin);

        //then
        ArgumentCaptor<UserModel> userModelArgumentCaptor = ArgumentCaptor.forClass(UserModel.class);

        verify(this.userRepository).save(userModelArgumentCaptor.capture());

        var capturedUser = userModelArgumentCaptor.getValue();

        assertThat(capturedUser).isExactlyInstanceOf(UserModel.class);
    }

    @Test
    void saveUser_ValidUserDTOClientRole_UserModel() {
        //given
        var client = new UserDTO("client", "client", "CLIENT");

        //when
        this.userService.saveUser(client);

        //then
        ArgumentCaptor<UserModel> userModelArgumentCaptor = ArgumentCaptor.forClass(UserModel.class);

        verify(this.userRepository).save(userModelArgumentCaptor.capture());

        var capturedUser = userModelArgumentCaptor.getValue();

        assertThat(capturedUser).isExactlyInstanceOf(UserModel.class);
    }

    @Test
    void saveUser_AlreadyRegisteredUser_ValidationException() {
        //given
        var admin = new UserDTO("admin", "admin", "ADMINISTRATOR");

        given(this.userRepository.existsByUsername(admin.getUsername())).willReturn(true);

        //then
        assertThatThrownBy(() -> this.userService.saveUser(admin))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessageContaining("User is already registered", "/api/v1/user/create");

        verify(this.userRepository, never()).save(any());
    }

    @Test
    void saveUser_InvalidRole_ValidationException() {
        //given
        var admin = new UserDTO("admin", "admin", "TESTING");

        //then
        assertThatThrownBy(() -> this.userService.saveUser(admin))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Role provided is invalid, it must be either CLIENT or ADMINISTRATOR",
                        "/api/v1/user/create");

        verify(this.userRepository, never()).save(any());
    }

    @Test
    void updateUser_RegisteredUser() {
        //given
        var adminDto = new UserDTO("newAdmin", "admin", "ADMINISTRATOR");
        var username = "admin";

        given(this.userRepository.findByUsername(username)).willReturn(
                new UserModel(1L, "admin", "admin", Role.ADMINISTRATOR));

        //when
        this.userService.updateUser(username, adminDto);

        //then
        ArgumentCaptor<UserModel> userModelArgumentCaptor = ArgumentCaptor.forClass(UserModel.class);

        verify(this.userRepository).save(userModelArgumentCaptor.capture());

        var capturedUser = userModelArgumentCaptor.getValue();

        assertThat(capturedUser).isExactlyInstanceOf(UserModel.class);
    }

    @Test
    void deleteUser() {
    }
}