package com.ppb.bot.application.services.exchange;

import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public interface ExchangeMemoryCache {

    List<ExchangeEventTypeMarkets> getCachedEventTypes();
    List<ExchangeEventTypeMarkets> cacheEventTypes(List<ExchangeEventTypeMarkets> eventTypeMarkets);
    List<ExchangeEventMarketCount> getCachedEventTypeEventMarkets(String eventType);
    List<ExchangeEventMarketCount> cacheEventTypeEventMarkets(String eventType, List<ExchangeEventMarketCount> eventMarkets);
    Map<String,List<ExchangeMarketCatalogueEntry>> getEventMarkets(Set<String> eventIds);
    Map<String, List<ExchangeMarketCatalogueEntry>> cacheEventMarkets(Map<String, List<ExchangeMarketCatalogueEntry>> eventMarkets);
    Map<String,List<ExchangeMarketBook>> getMarketBooks(Set<String> marketIds);
    Map<String,List<ExchangeMarketBook>> cacheMarketBooks(Map<String,List<ExchangeMarketBook>> marketBooks);

}
