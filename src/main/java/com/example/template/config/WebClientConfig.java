package com.example.template.config;

import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.HttpProtocol;
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient ragWebClient(@Value("${rag-service.protocol}") String protocol,
                                  @Value("${rag-service.host}") String host,
                                  @Value("${rag-service.port}") int port) {

        HttpClient httpClient = HttpClient.create()
                .protocol(HttpProtocol.HTTP11);

        return WebClient.builder()
                .baseUrl(protocol + "://" + host + ":" + port)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> {
                            configurer.defaultCodecs().maxInMemorySize(50 * 1024 * 1024); // 50MB
                            configurer.defaultCodecs().enableLoggingRequestDetails(true);
                        })
                        .build())
                .defaultHeader(HttpHeaders.USER_AGENT, "SpringBoot-WebClient")
                .build();
    }
}
