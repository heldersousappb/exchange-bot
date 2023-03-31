package com.ppb.bot.application.services.exchange;

import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ExchangeService {

    List<ExchangeEventTypeMarkets> listEventTypes();
    Map<String,List<ExchangeEventMarketCount>> listEvents(Set<String> eventTypeIds, Optional<Instant> from, Optional<Instant> to);
    Map<String,List<ExchangeMarketCatalogueEntry>> listEventMarkets(Set<String> eventIds);
    Map<String,List<ExchangeMarketBook>> listMarketBooks(Set<String> marketIds);
    ExchangePlaceOrdersResponse placeOrders(ExchangePlaceOrdersRequest orders);

}
