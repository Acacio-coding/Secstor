package com.ifsc.secstor.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.ifsc.secstor.api.advice.messages.ErrorMessages.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    @NotBlank(message = NULL_USERNAME)
    @Size(min = 5, max = 20, message = INVALID_USERNAME_LENGTH)
    @Pattern(regexp = "^(?=.{5,20}$)(?![_.-])(?!.*[_.-]{2})[a-z0-9._-]+(?<![_.-])$",
            message = INVALID_USERNAME)
    private String username;

    @NotBlank(message = NULL_PASSWORD)
    @Size(min = 8, max = 12, message = INVALID_PASSWORD_LENGTH)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}|_`´~\"',-./:;<>\\[\\]+?\\\\=]).{8,12}$",
            message = INVALID_PASSWORD)
    private String password;

    @Schema(hidden = true)
    private String role;
}
