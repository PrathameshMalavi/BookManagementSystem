package com.prathameshmalavi.BookManagementSystem.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @Email(message = "Email is not formatter")
    @NotEmpty(message = "Email Name is Mandatory")
    @NotBlank(message = "Email Name is Mandatory")
    private String email;

    @NotEmpty(message = "Password Name is Mandatory")
    @NotBlank(message = "Password Name is Mandatory")
    @Size(min = 8 , message = "Password should be 8 characters minimum")
    private String password;

}