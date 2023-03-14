package com.ppb.bot.application.services.exchange;

import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public interface ExchangeMemoryCache {

    Mono<List<ExchangeEventTypeMarkets>> getCachedEventTypes();
    Mono<List<ExchangeEventTypeMarkets>> cacheEventTypes(Mono<List<ExchangeEventTypeMarkets>> eventTypeMarkets);
    Mono<List<ExchangeEventMarketCount>> getCachedEventTypeEventMarkets(String eventType);
    Mono<List<ExchangeEventMarketCount>> cacheEventTypeEventMarkets(String eventType, List<ExchangeEventMarketCount> eventMarkets);
    Mono<Map<String,List<ExchangeMarketCatalogueEntry>>> getEventMarkets(Set<String> eventIds);
    Mono<Map<String, List<ExchangeMarketCatalogueEntry>>> cacheEventMarkets(Map<String, List<ExchangeMarketCatalogueEntry>> eventMarkets);
    Mono<Map<String,List<ExchangeMarketBook>>> getMarketBooks(Set<String> marketIds);
    Mono<Map<String,List<ExchangeMarketBook>>> cacheMarketBooks(Map<String,List<ExchangeMarketBook>> marketBooks);

}
