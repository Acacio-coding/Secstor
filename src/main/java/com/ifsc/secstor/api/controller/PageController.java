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
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import static com.ifsc.secstor.api.advice.paths.Paths.REGISTER;
import static com.ifsc.secstor.api.advice.paths.Paths.REGISTER_BASE;
import static com.ifsc.secstor.api.util.Constants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(REGISTER_BASE)
public class PageController {

    private final UserServiceImplementation userService;

    @GetMapping(REGISTER)
    public String registerForm(Model model) {
        List<String> usernames = userService.findAllUsernames();
        FormDTO user = new FormDTO();

        model.addAttribute(USER, user);
        model.addAttribute(USERNAMES, usernames);

        return REGISTER_TEMPLATE;
    }

    @PostMapping(REGISTER)
    public String submitForm(@ModelAttribute(USER) @Validated FormDTO user, Model model) {
        UserDTO toSave = new UserDTO(user.getUsername(), user.getPassword(), CLIENT.toUpperCase());

        try {
            userService.saveUser(toSave);
            return SUCCESS_TEMPLATE;
        } catch (Exception exception) {
            model.addAttribute(ERROR, exception.getMessage());
            return ERROR_TEMPLATE;
        }
    }
}
