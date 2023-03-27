package com.ppb.bot.application.services.exchange;

import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ExchangeService {

    Mono<List<ExchangeEventTypeMarkets>> listEventTypes();
    Mono<Map<String,List<ExchangeEventMarketCount>>> listEvents(Set<String> eventTypeIds, Optional<Instant> from, Optional<Instant> to);
    Mono<Map<String,List<ExchangeMarketCatalogueEntry>>> listEventMarkets(Set<String> eventIds);
    Mono<Map<String,List<ExchangeMarketBook>>> listMarketBooks(Set<String> marketIds);
    Mono<ExchangePlaceOrdersResponse> placeOrders(ExchangePlaceOrdersRequest orders);

}
