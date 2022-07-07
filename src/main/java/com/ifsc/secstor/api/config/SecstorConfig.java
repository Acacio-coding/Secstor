package com.ifsc.secstor.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConfigurationProperties("secstor")
public record SecstorConfig(String authSecret, String adminUsername, String adminPassword) implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/api/v1/docs/**").setViewName("forward:/index.html");
        registry.addRedirectViewController("/", "/api/v1/docs");
    }
}
