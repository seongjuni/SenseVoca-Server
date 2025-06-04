package com.sensevoca.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ai.server.url}")
    private String aiServerUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(aiServerUrl) // AI 서버 기본 URL
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean("multipartWebClient")
    public WebClient multipartWebClient()
    {
        return WebClient.builder()
                .baseUrl(aiServerUrl)
                .build();
    }
}