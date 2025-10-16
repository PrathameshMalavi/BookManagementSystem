package com.prathameshmalavi.BookManagementSystem.user;
import com.prathameshmalavi.BookManagementSystem.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prathameshmalavi.BookManagementSystem.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User , Integer>{

    Optional<User> findByEmail(String username);

}
