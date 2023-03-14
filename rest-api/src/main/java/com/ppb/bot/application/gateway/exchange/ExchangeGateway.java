package com.ppb.bot.application.gateway.exchange;

import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ExchangeGateway {

    String APPLICATION_HEADER_NAME = "X-Application";
    String AUTHENTICATION_HEADER_NAME = "X-Authentication";

    Mono<List<ExchangeEventTypeMarkets>> listEventTypes(String authenticationToken);
    Mono<List<ExchangeEventMarketCount>> listEvents(String authenticationToken, Set<String> eventTypeIds, Optional<Instant> from, Optional<Instant> to);
    Mono<Map<String,List<ExchangeMarketCatalogueEntry>>> listEventMarkets(String authenticationToken, Set<String> eventIds);
    Mono<Map<String,List<ExchangeMarketBook>>> listMarketBooks(String authenticationToken, Set<String> marketIds);
    Mono<ExchangePlaceOrdersResponse> placeOrders(String authenticationToken, ExchangePlaceOrdersRequest orders);

}
