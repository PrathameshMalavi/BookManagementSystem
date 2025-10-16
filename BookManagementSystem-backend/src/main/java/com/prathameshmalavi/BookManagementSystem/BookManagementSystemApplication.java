package com.prathameshmalavi.BookManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagementSystemApplication.class, args);
	}

}
