package com.ifsc.secstor.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FormDTO {
    @NotBlank(message = "Username must be provided")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @Pattern(regexp = "^(?=.{5,20}$)(?![_.-])(?!.*[_.-]{2})[a-z0-9._-]+(?<![_.-])$",
            message = "Username can only contain lower case letters, numbers, underscore, dash or dot with no white spaces")
    private String username;

    @NotBlank(message = "Password must be provided")
    @Size(min = 8, max = 12, message = "Password must be between 8 and 12 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}|_`´~\"',-./:;<>\\[\\]+?\\\\=]).{8,12}$",
            message = "Password must include at least one uppercase and lowercase letters, a number and a symbol with no white spaces")
    private String password;
}
