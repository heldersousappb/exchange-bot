package com.ppb.bot.application.services.exchange;

import com.ppb.bot.application.gateway.exchange.ExchangeGateway;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeGateway exchangeGateway;
    private final ExchangeMemoryCache exchangeMemoryCache;

    public ExchangeServiceImpl(
        @Autowired ExchangeGateway exchangeGateway,
        @Autowired ExchangeMemoryCache exchangeMemoryCache
    ) {
        this.exchangeGateway = exchangeGateway;
        this.exchangeMemoryCache = exchangeMemoryCache;
    }

    @Override
    public Mono<List<ExchangeEventTypeMarkets>> listEventTypes(String authenticationToken) {
        return this.exchangeMemoryCache.getCachedEventTypes() // Try to get from cache
                .switchIfEmpty(this.exchangeMemoryCache.cacheEventTypes(this.exchangeGateway.listEventTypes(authenticationToken))); // Get and cache
    }

    @Override
    public Mono<Map<String,List<ExchangeEventMarketCount>>> listEvents(String authenticationToken, Set<String> eventTypeIds, Optional<Instant> from, Optional<Instant> to) {
        return Mono.from(
            Flux.fromStream(
                eventTypeIds.stream().map(eventTypeId ->
                    this.exchangeMemoryCache.getCachedEventTypeEventMarkets(eventTypeId) // Trying to get from cache
                        .switchIfEmpty(this.exchangeGateway.listEvents(authenticationToken, Set.of(eventTypeId), from, to).flatMap(gatewayEvents -> this.exchangeMemoryCache.cacheEventTypeEventMarkets(eventTypeId, gatewayEvents))) // Get and cache
                        .map(eventTypeIdMarketCount -> Map.of(eventTypeId, eventTypeIdMarketCount)))
            ).flatMap(x -> x)
        );
    }

    @Override
    public Mono<Map<String, List<ExchangeMarketCatalogueEntry>>> listEventMarkets(String authenticationToken, Set<String> eventIds) {

        return this.exchangeMemoryCache.getEventMarkets(eventIds).flatMap(cachedEventMarkets -> {

            // Removing cache hits
            eventIds.removeAll(cachedEventMarkets.keySet());

            if(eventIds.isEmpty()) {
                // No cache misses
                return Mono.just(cachedEventMarkets);
            } else {
                // Getting cache misses from exchange gateway
                return this.exchangeGateway.listEventMarkets(authenticationToken, eventIds).flatMap(this.exchangeMemoryCache::cacheEventMarkets).map(gatewayEventMarkets -> {
                    gatewayEventMarkets.putAll(cachedEventMarkets);
                    return gatewayEventMarkets;
                });
            }

        });

    }

    @Override
    public Mono<Map<String, List<ExchangeMarketBook>>> listMarketBooks(String authenticationToken, Set<String> marketIds) {

        return this.exchangeMemoryCache.getMarketBooks(marketIds).flatMap(cachedMarketBooks -> {

            // Removing cache hits
            marketIds.removeAll(cachedMarketBooks.keySet());

            if(marketIds.isEmpty()) {
                // No cache misses
                return Mono.just(cachedMarketBooks);
            } else {
                // Getting cache misses from exchange gateway
                return this.exchangeGateway.listMarketBooks(authenticationToken, marketIds).flatMap(this.exchangeMemoryCache::cacheMarketBooks).map(gatewayEventMakers -> {
                    gatewayEventMakers.putAll(cachedMarketBooks);
                    return gatewayEventMakers;
                });
            }

        });
    }

    @Override
    public Mono<ExchangePlaceOrdersResponse> placeOrders(String authenticationToken, ExchangePlaceOrdersRequest orders) {
        return this.exchangeGateway.placeOrders(authenticationToken, orders);
    }
}
