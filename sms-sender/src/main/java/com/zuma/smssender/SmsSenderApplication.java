package com.zuma.smssender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class SmsSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsSenderApplication.class, args);
	}
}
