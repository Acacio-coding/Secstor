package com.ifsc.secstor.api.service;

import com.ifsc.secstor.api.dto.UserDTO;
import com.ifsc.secstor.api.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface UserService {
    Page<UserModel> findAllUsers(Pageable pageable);

    UserModel findUserByUsername(String username);

    void saveUser(UserDTO userDTO);

    void updateUser(String username, UserDTO userDTO);

    void deleteUser(String username);

    Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    List<String> findAllUsernames();

    boolean isAuthenticated();

    boolean isAdmin();

    String getAuthenticatedUsername();
}
