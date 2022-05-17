package com.ifsc.secstor.api.controller;

import com.ifsc.secstor.api.dto.FormDTO;
import com.ifsc.secstor.api.dto.UserDTO;
import com.ifsc.secstor.api.service.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PageController {

    private final UserServiceImplementation userService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        List<String> usernames = userService.findAllUsernames();
        FormDTO user = new FormDTO();

        model.addAttribute("user", user);
        model.addAttribute("usernames", usernames);

        return "register";
    }

    @PostMapping("/register")
    public String submitForm(@ModelAttribute("user") @Validated FormDTO user, Model model) {
        UserDTO toSave = new UserDTO(user.getUsername(), user.getPassword(), "CLIENT");

        try {
            userService.saveUser(toSave);
            return "success";
        } catch (Exception exception) {
            model.addAttribute("error", exception.getMessage());
            return "error";
        }
    }
}
