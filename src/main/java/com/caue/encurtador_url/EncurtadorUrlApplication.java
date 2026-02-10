package com.caue.encurtador_url;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EncurtadorUrlApplication {
	public static void main(String[] args) {
		SpringApplication.run(EncurtadorUrlApplication.class, args);
	}
}
