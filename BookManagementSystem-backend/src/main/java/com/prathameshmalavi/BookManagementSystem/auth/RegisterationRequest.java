package com.prathameshmalavi.BookManagementSystem.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegisterationRequest {

    @NotEmpty(message = "First Name is Mandatory")
    @NotBlank(message = "First Name is Mandatory")
    private String firstname;

    @NotEmpty(message = "Last Name is Mandatory")
    @NotBlank(message = "Last Name is Mandatory")
    private String lastname;

    @Email(message = "Email is not formatter")
    @NotEmpty(message = "Email Name is Mandatory")
    @NotBlank(message = "Email Name is Mandatory")
    private String email;

    @NotEmpty(message = "Password Name is Mandatory")
    @NotBlank(message = "Password Name is Mandatory")
    @Size(min = 8 , message = "Password should be 8 characters minimum")
    private String password;

}
