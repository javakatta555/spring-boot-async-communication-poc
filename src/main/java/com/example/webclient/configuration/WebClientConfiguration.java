package com.example.webclient.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    WebClient webClient(){
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()))
                .build();
    }
}
