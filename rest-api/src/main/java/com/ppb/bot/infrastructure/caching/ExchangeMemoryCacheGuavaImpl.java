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
    public List<ExchangeEventTypeMarkets> getCachedEventTypes() {

        List<ExchangeEventTypeMarkets> cached = this.eventTypesCache.getIfPresent("");

        if(cached == null) {
            LOGGER.debug("Cache MISS for event types!");
        } else {
            LOGGER.debug("Cache HIT for event types!");
        }

        return cached;

    }

    @Override
    public List<ExchangeEventTypeMarkets> cacheEventTypes(List<ExchangeEventTypeMarkets> eventTypeMarkets) {

        this.eventTypesCache.put("", eventTypeMarkets);
        LOGGER.debug("Event types were cached!");
        return eventTypeMarkets;

    }

    @Override
    public List<ExchangeEventMarketCount> getCachedEventTypeEventMarkets(String eventType) {

        final List<ExchangeEventMarketCount> cached = this.eventTypeMarketsCache.getIfPresent(eventType);

        if(cached == null) {
            LOGGER.debug("Cache MISS for event type {}!", eventType);
        } else {
            LOGGER.debug("Cache HIT for event type {}!", eventType);
        }

        return cached;

    }

    @Override
    public List<ExchangeEventMarketCount> cacheEventTypeEventMarkets(String eventType, List<ExchangeEventMarketCount> eventMarkets) {
        this.eventTypeMarketsCache.put(eventType, eventMarkets);
        LOGGER.debug("Event type {} was cached!", eventType);
        return eventMarkets;
    }

    @Override
    public Map<String, List<ExchangeMarketCatalogueEntry>> getEventMarkets(Set<String> eventIds) {
        return eventIds
            .stream().map(eventId -> new Pair<>(eventId, this.eventMarketsCache.getIfPresent(eventId))) // Getting from cache
            .filter(cachedEventMarkets -> cachedEventMarkets.getSecond() != null) // Filtering cache misses
            .map(eventMarket -> {
                LOGGER.debug("Cache HIT for event {} markets", eventMarket.getFirst());
                return eventMarket;
            })
            .collect(Collectors.toMap(eventMarket -> eventMarket.getFirst(), x -> x.getSecond())); // Collecting as Map<String, List<ExchangeMarketCatalogueEntry>>
    }

    @Override
    public Map<String, List<ExchangeMarketCatalogueEntry>> cacheEventMarkets(Map<String, List<ExchangeMarketCatalogueEntry>> eventMarkets) {
        this.eventMarketsCache.putAll(eventMarkets);
        LOGGER.debug("Markets cached for events {}", Arrays.toString(eventMarkets.keySet().toArray()));
        return eventMarkets;
    }

    @Override
    public Map<String, List<ExchangeMarketBook>> getMarketBooks(Set<String> marketIds) {
        return marketIds
            .stream().map(marketId -> new Pair<>(marketId, this.marketBooksCache.getIfPresent(marketId)))
            .filter(cachedMarketBooks -> cachedMarketBooks.getSecond() != null)
            .map(marketBooks -> {
                LOGGER.debug("Cache HIT for market {} books", marketBooks.getFirst());
                return marketBooks;
            })
            .collect(Collectors.toMap(cachedMarketBooks -> cachedMarketBooks.getFirst(), cachedMarketBooks -> cachedMarketBooks.getSecond()));
    }

    @Override
    public Map<String, List<ExchangeMarketBook>> cacheMarketBooks(Map<String, List<ExchangeMarketBook>> marketBooks) {
        this.marketBooksCache.putAll(marketBooks);
        LOGGER.debug("Books cached for markets {}", Arrays.toString(marketBooks.keySet().toArray()));
        return marketBooks;
    }
}
