package com.ppb.bot.infrastructure.caching;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import com.ppb.bot.application.services.exchange.ExchangeMemoryCache;
import com.ppb.bot.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExchangeMemoryCacheGuavaImpl implements ExchangeMemoryCache {

    private final Duration CACHE_TTL = Duration.ofMinutes(1L); // TODO: Put me if config if needed
    private final Cache<String,List<ExchangeEventTypeMarkets>> eventTypesCache;
    private final Cache<String,List<ExchangeEventMarketCount>> eventTypeMarketsCache;
    private final Cache<String,List<ExchangeMarketCatalogueEntry>> eventMarketsCache;
    private final Cache<String,List<ExchangeMarketBook>> marketBooksCache;

    static final Logger LOGGER = LoggerFactory.getLogger(ExchangeMemoryCacheGuavaImpl.class);

    public ExchangeMemoryCacheGuavaImpl() {
        this.eventTypesCache = CacheBuilder.newBuilder().maximumSize(1).expireAfterWrite(CACHE_TTL).build();
        this.eventTypeMarketsCache = CacheBuilder.newBuilder().expireAfterWrite(CACHE_TTL).build();
        this.eventMarketsCache = CacheBuilder.newBuilder().expireAfterWrite(CACHE_TTL).build();
        this.marketBooksCache = CacheBuilder.newBuilder().expireAfterWrite(CACHE_TTL).build();
    }

    @Override
    public Mono<List<ExchangeEventTypeMarkets>> getCachedEventTypes() {

        List<ExchangeEventTypeMarkets> cached = this.eventTypesCache.getIfPresent("");

        if(cached == null) {
            LOGGER.debug("Cache MISS for event types!");
            return Mono.empty();
        } else {
            LOGGER.debug("Cache HIT for event types!");
            return Mono.just(cached);
        }

    }

    @Override
    public Mono<List<ExchangeEventTypeMarkets>> cacheEventTypes(Mono<List<ExchangeEventTypeMarkets>> eventTypeMarkets) {

        return eventTypeMarkets.map(eventTypeMarketsList -> {
            this.eventTypesCache.put("", eventTypeMarketsList);
            LOGGER.debug("Event types were cached!");
            return eventTypeMarketsList;
        });

    }

    @Override
    public Mono<List<ExchangeEventMarketCount>> getCachedEventTypeEventMarkets(String eventType) {

        List<ExchangeEventMarketCount> cached = this.eventTypeMarketsCache.getIfPresent(eventType);

        if(cached == null) {
            LOGGER.debug("Cache MISS for event type {}!", eventType);
            return Mono.empty();
        } else {
            LOGGER.debug("Cache HIT for event type {}!", eventType);
            return Mono.just(cached);
        }

    }

    @Override
    public Mono<List<ExchangeEventMarketCount>> cacheEventTypeEventMarkets(String eventType, List<ExchangeEventMarketCount> eventMarkets) {
        this.eventTypeMarketsCache.put(eventType, eventMarkets);
        LOGGER.debug("Event type {} was cached!", eventType);
        return Mono.just(eventMarkets);
    }

    @Override
    public Mono<Map<String, List<ExchangeMarketCatalogueEntry>>> getEventMarkets(Set<String> eventIds) {
        return Mono.just(
                eventIds.stream()
                    .map(eventId -> new Pair<>(eventId, this.eventMarketsCache.getIfPresent(eventId))) // Getting from cache
                    .filter(cachedEventMarkets -> cachedEventMarkets.getSecond() != null) // Filtering cache misses
                    .map(eventMarket -> {
                        LOGGER.debug("Cache HIT for event {} markets", eventMarket.getFirst());
                        return eventMarket;
                    })
                    .collect(Collectors.toMap(eventMarket -> eventMarket.getFirst(), eventMarket -> eventMarket.getSecond()))
        );
    }

    @Override
    public Mono<Map<String, List<ExchangeMarketCatalogueEntry>>> cacheEventMarkets(Map<String, List<ExchangeMarketCatalogueEntry>> eventMarkets) {
        this.eventMarketsCache.putAll(eventMarkets);
        LOGGER.debug("Markets cached for events {}", Arrays.toString(eventMarkets.keySet().toArray()));
        return Mono.just(eventMarkets);
    }

    @Override
    public Mono<Map<String, List<ExchangeMarketBook>>> getMarketBooks(Set<String> marketIds) {
        return Mono.just(
                marketIds.stream()
                        .map(marketId -> new Pair<>(marketId, this.marketBooksCache.getIfPresent(marketId)))
                        .filter(cachedMarketBooks -> cachedMarketBooks.getSecond() != null)
                        .map(marketBooks -> {
                            LOGGER.debug("Cache HIT for market {} books", marketBooks.getFirst());
                            return marketBooks;
                        })
                        .collect(Collectors.toMap(cachedMarketBooks -> cachedMarketBooks.getFirst(), cachedMarketBooks -> cachedMarketBooks.getSecond()))
        );
    }

    @Override
    public Mono<Map<String, List<ExchangeMarketBook>>> cacheMarketBooks(Map<String, List<ExchangeMarketBook>> marketBooks) {
        this.marketBooksCache.putAll(marketBooks);
        LOGGER.debug("Books cached for markets {}", Arrays.toString(marketBooks.keySet().toArray()));
        return Mono.just(marketBooks);
    }
}
