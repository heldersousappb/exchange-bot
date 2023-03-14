package com.ppb.bot.presentation.exchange;

import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import com.ppb.bot.application.services.exchange.ExchangeService;
import com.ppb.bot.application.services.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/exchange/event")
public class ExchangeController {


    private final ExchangeService exchangeService;
    private final LoginService loginService;

    public ExchangeController(
        @Autowired final ExchangeService exchangeService,
        @Autowired final LoginService loginService
    ) {
        this.exchangeService = exchangeService;
        this.loginService = loginService;
    }

    @PostMapping("/types")
    public Mono<List<ExchangeEventTypeMarkets>> getEventTypes() {
        return this.loginService.getAuthenticationToken().flatMap(authenticationToken -> this.exchangeService.listEventTypes(authenticationToken));
    }

    @PostMapping("/list")
    public Mono<Map<String,List<ExchangeEventMarketCount>>> getTypeEvents(
        @RequestParam(name = "from", required = false) final Optional<Instant> from,
        @RequestParam(name = "to", required = false) Optional<Instant> to,
        @RequestBody final Set<String> eventTypeIds
    ) {
        return this.loginService.getAuthenticationToken().flatMap(authenticationToken -> this.exchangeService.listEvents(authenticationToken, eventTypeIds, from, to));
    }

    @PostMapping("/market")
    public Mono<Map<String, List<ExchangeMarketCatalogueEntry>>> getEventMarkets(
        @RequestBody final Set<String> eventIds
    ) {
        return this.loginService.getAuthenticationToken().flatMap(authenticationToken -> this.exchangeService.listEventMarkets(authenticationToken, eventIds));
    }

    @PostMapping("/market/books")
    public Mono<Map<String, List<ExchangeMarketBook>>> getMarketBooks(
        @RequestBody final Set<String> marketIds
    ) {
        return this.loginService.getAuthenticationToken().flatMap(authenticationToken -> this.exchangeService.listMarketBooks(authenticationToken, marketIds));
    }

    @PostMapping("/market/bet")
    public Mono<ExchangePlaceOrdersResponse> placeOrders(
        @RequestBody final ExchangePlaceOrdersRequest orders
    ) {
        return this.loginService.getAuthenticationToken().flatMap(authenticationToken -> this.exchangeService.placeOrders(authenticationToken, orders));
    }

}
