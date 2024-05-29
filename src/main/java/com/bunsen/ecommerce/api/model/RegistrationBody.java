package com.bunsen.ecommerce.api.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationBody {
    @NotNull
    @NotBlank
    private String firstname;

    @NotNull
    @NotBlank
    private String lastname;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @Size(max = 32, min = 8)
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
    private String password;

    @NotNull
    @NotBlank
    @Size(max = 255, min = 3)
    private String username;

}
