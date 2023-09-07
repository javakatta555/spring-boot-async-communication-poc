package com.example.webclient.service;

import com.example.webclient.model.Rating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.net.URI;

@Service
@Slf4j
public class WebClientService {

    @Autowired
    private WebClient webClient;

    public Mono<Rating> getRating(String url){
        return webClient.get().uri(URI.create(url))
                .retrieve()
                .bodyToMono(Rating.class)
                //.block()  //we can use block to work web client as synchronous
                .doOnError(err->
                        log.info("Received error is :{}", err.getMessage())
                ).doOnSuccess(res->
                        log.info("Received response is :{}",res)
                );
    }

    public <T, S> Mono<S> postMono(String url, T body, Class<S> clasz, HttpHeaders httpHeaders){
        log.info("Request Body - {}",body);
        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(body)
                .headers(h->h.addAll(httpHeaders))
                .retrieve()
                .bodyToMono(clasz)
                .doOnError(err->{
                    if(err instanceof WebClientResponseException)
                        log.error("Exception in post mono for url  - {} Body  - {} Headers - {} Exception - {} Response - {}",url,body,httpHeaders,err,((WebClientResponseException)err).getResponseBodyAsString());
                    else
                        log.error("Exception in post mono for url  - {} Body  - {}  Headers - {} Exception - {}",url,body,httpHeaders,err);
                }).doOnSuccess(resp->{
                    log.info("Received response - {}",resp);
                });
    }

}
