package com.ppb.bot.presentation.exchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppb.bot.application.gateway.exchange.entities.*;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import com.ppb.bot.application.services.exchange.ExchangeService;
import com.ppb.bot.application.services.login.LoginService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
class ExchangeControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ExchangeService exchangeService;

    @MockBean
    LoginService loginService;

    @BeforeEach
    void setupAuthenticationToken() {
        Mockito.when(loginService.getAuthenticationToken()).thenReturn(Mono.just(""));
    }

    @Test
    void getEventTypes() throws IOException {

        List<ExchangeEventTypeMarkets> mockData = this.objectMapper.readValue(
                this.getClass().getClassLoader().getResourceAsStream("test-data/test-event-types-data.json"),
                new TypeReference<List<ExchangeEventTypeMarkets>>(){}
        );

        Mockito.when(exchangeService.listEventTypes()).thenReturn(Mono.just(mockData));

        this.webTestClient.post()
                .uri("/exchange/event/types")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ExchangeEventTypeMarkets.class)
                .equals(mockData);
    }

    @Test
    void getTypeEvents() throws IOException {

        Map<String,List<ExchangeEventMarketCount>> mockData = this.objectMapper.readValue(
                this.getClass().getClassLoader().getResourceAsStream("test-data/test-event-list-data.json"),
                new TypeReference<Map<String,List<ExchangeEventMarketCount>>>() {}
        );

        Mockito.when(
                exchangeService.listEvents(mockData.keySet(), Optional.empty(), Optional.empty())
        ).thenReturn(Mono.just(mockData));

        this.webTestClient.post()
                .uri("/exchange/event/list")
                .bodyValue(mockData.keySet())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Map<String,List<ExchangeEventMarketCount>>>(){})
                .equals(mockData);

    }

    @Test
    void getEventMarkets() throws IOException {

        Map<String, List<ExchangeMarketCatalogueEntry>> mockData = this.objectMapper.readValue(
                this.getClass().getClassLoader().getResourceAsStream("test-data/test-event-markets-data.json"),
                new TypeReference<Map<String, List<ExchangeMarketCatalogueEntry>>>(){}
        );

        Mockito.when(
                exchangeService.listEventMarkets(mockData.keySet())
        ).thenReturn(Mono.just(mockData));

        this.webTestClient.post()
                .uri("/exchange/event/market")
                .bodyValue(mockData.keySet())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Map<String,List<ExchangeMarketCatalogueEntry>>>(){})
                .equals(mockData);

    }

    @Test
    void getMarketBooks() throws IOException {

        Map<String, List<ExchangeMarketBook>> mockData = this.objectMapper.readValue(
                this.getClass().getClassLoader().getResourceAsStream("test-data/test-market-books-data.json"),
                new TypeReference<Map<String, List<ExchangeMarketBook>>>() {}
        );

        Mockito.when(
                exchangeService.listMarketBooks(mockData.keySet())
        ).thenReturn(Mono.just(mockData));

        this.webTestClient.post()
                .uri("/exchange/event/market/books")
                .bodyValue(mockData.keySet())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Map<String,List<ExchangeMarketBook>>>(){})
                .equals(mockData);

    }

//    @Test
//    void placeOrders() throws IOException {
//
//        ExchangePlaceOrdersRequest placeBetMockData = this.objectMapper.readValue(
//                this.getClass().getClassLoader().getResourceAsStream("test-data/test-place-bet-data.json"),
//                ExchangePlaceOrdersRequest.class
//        );
//
//        ExchangePlaceOrdersResponse betPlacedMockData = this.objectMapper.readValue(
//                this.getClass().getClassLoader().getResourceAsStream("test-data/test-bet-placed-data.json"),
//                ExchangePlaceOrdersResponse.class
//        );
//
//        Mockito.when(
//                exchangeService.placeOrders("", placeBetMockData)
//        ).thenReturn(Mono.just(betPlacedMockData));
//
//        this.webTestClient.post()
//                .uri("/exchange/event/market/bet")
//                .bodyValue(placeBetMockData)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(ExchangePlaceOrdersResponse.class)
//                .equals(betPlacedMockData);
//
//    }
}