package com.service.BOOKJEOK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;

@EnableJpaAuditing
@SpringBootApplication
public class BookjeokApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookjeokApplication.class, args);
	}

}
