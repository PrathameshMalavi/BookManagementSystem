package com.prathameshmalavi.BookManagementSystem.auth;

import com.prathameshmalavi.BookManagementSystem.role.RoleRepository;
import com.prathameshmalavi.BookManagementSystem.user.Token;
import com.prathameshmalavi.BookManagementSystem.user.TokenRepository;
import com.prathameshmalavi.BookManagementSystem.user.User;
import com.prathameshmalavi.BookManagementSystem.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterationRequest request) {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role User was not Initialized"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        saveValidationEmail(user);


    }

    private void saveValidationEmail(User user) {
        var newToken = generateAndSendActivationToken(user);
        //Send Email
    }

    private Token generateAndSendActivationToken(User user) {
        var generatedToken = generateActivationToken(6);

        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .user(user)
                .build();

        tokenRepository.save(token);

        return token;
    }

    private String generateActivationToken(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for(int i =0 ; i< length ; i++){
            int randoemIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randoemIndex));
        }

        return codeBuilder.toString();
    }

}
