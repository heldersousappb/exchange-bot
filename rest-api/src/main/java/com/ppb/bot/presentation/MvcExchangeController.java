package com.ppb.bot.presentation;

import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import com.ppb.bot.application.services.exchange.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/exchange/event")
public class MvcExchangeController {

    private final ExchangeService exchangeService;

    public MvcExchangeController(
        @Autowired final ExchangeService exchangeService
    ) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("/types")
    public List<ExchangeEventTypeMarkets> getEventTypes() {
        return this.exchangeService.listEventTypes();
    }

    @PostMapping("/list")
    public Map<String,List<ExchangeEventMarketCount>> getTypeEvents(
        @RequestParam(name = "from", required = false) final Optional<Instant> from,
        @RequestParam(name = "to", required = false) Optional<Instant> to,
        @RequestBody final Set<String> eventTypeIds
    ) {
        return this.exchangeService.listEvents(eventTypeIds, from, to);
    }

    @PostMapping("/market")
    public Map<String, List<ExchangeMarketCatalogueEntry>> getEventMarkets(
        @RequestBody final Set<String> eventIds
    ) {
        return this.exchangeService.listEventMarkets(eventIds);
    }

    @PostMapping("/market/books")
    public Map<String, List<ExchangeMarketBook>> getMarketBooks(
        @RequestBody final Set<String> marketIds
    ) {
        return this.exchangeService.listMarketBooks(marketIds);
    }

    @PostMapping("/market/bet")
    public ExchangePlaceOrdersResponse placeOrders(
        @RequestBody final ExchangePlaceOrdersRequest orders
    ) {
        return this.exchangeService.placeOrders(orders);
    }

}
