package com.zuma;

import com.zuma.config.LogServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class LogStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogStorageApplication.class, args);
	}
}
