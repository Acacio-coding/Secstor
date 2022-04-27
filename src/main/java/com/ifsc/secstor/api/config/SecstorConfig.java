package com.ifsc.secstor.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("secstor")
public record SecstorConfig(String authSecret, String adminUsername, String adminPassword) {
}
