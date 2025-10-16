package com.prathameshmalavi.BookManagementSystem.auth;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "authentication")
public class AuthenticationController {

    private AuthenticationService service;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterationRequest request
    )  {
        service.register(request);
        return ResponseEntity.accepted().build();


    }
}
