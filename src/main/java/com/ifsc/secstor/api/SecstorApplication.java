package com.ifsc.secstor.api;

import com.ifsc.secstor.api.config.SecstorConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigurationProperties(SecstorConfig.class)
@EntityScan(basePackages = "com.ifsc.secstor.api.model")
@SpringBootApplication(scanBasePackages = "com.ifsc.secstor.api")
public class SecstorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecstorApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
