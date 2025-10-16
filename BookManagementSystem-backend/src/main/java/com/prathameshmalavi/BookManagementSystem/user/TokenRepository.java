package com.prathameshmalavi.BookManagementSystem.user;

import com.prathameshmalavi.BookManagementSystem.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TokenRepository extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);

}
