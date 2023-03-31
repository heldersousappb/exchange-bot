package com.ppb.bot.application.services.exchange;

import com.ppb.bot.application.gateway.exchange.ExchangeGateway;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventMarketCount;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeMarkets;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketBook;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeMarketCatalogueEntry;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import com.ppb.bot.application.services.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeGateway exchangeGateway;
    private final ExchangeMemoryCache exchangeMemoryCache;
    private final LoginService loginService;

    public ExchangeServiceImpl(
        @Autowired ExchangeGateway exchangeGateway,
        @Autowired ExchangeMemoryCache exchangeMemoryCache,
        @Autowired LoginService loginService
    ) {
        this.exchangeGateway = exchangeGateway;
        this.exchangeMemoryCache = exchangeMemoryCache;
        this.loginService = loginService;
    }

    @Override
    public List<ExchangeEventTypeMarkets> listEventTypes() {

        final var cachedEventTypes = this.exchangeMemoryCache.getCachedEventTypes();

        if(cachedEventTypes == null) {
            final var exchangeEventTypes = this.exchangeGateway.listEventTypes(this.loginService.getAuthenticationToken());
            this.exchangeMemoryCache.cacheEventTypes(exchangeEventTypes);
            return exchangeEventTypes;
        } else {
            return cachedEventTypes;
        }

    }

    @Override
    public Map<String,List<ExchangeEventMarketCount>> listEvents(final Set<String> eventTypeIds, final Optional<Instant> from, final Optional<Instant> to) {

        var eventTypeIdMarkets = new HashMap<String,List<ExchangeEventMarketCount>>();

        eventTypeIds.stream().forEach(eventTypeId -> {

                var eventTypeMarkets = this.exchangeMemoryCache.getCachedEventTypeEventMarkets(eventTypeId); // Trying to get from cache

                if(eventTypeMarkets == null) {
                    eventTypeMarkets = this.exchangeMemoryCache.cacheEventTypeEventMarkets(eventTypeId, this.exchangeGateway.listEvents(this.loginService.getAuthenticationToken(), Set.of(eventTypeId), from, to));
                }

                eventTypeIdMarkets.put(eventTypeId, eventTypeMarkets);

            }
        );

        return eventTypeIdMarkets;
    }

    @Override
    public Map<String, List<ExchangeMarketCatalogueEntry>> listEventMarkets(final Set<String> eventIds) {

        final var cachedEventMarketEntries = this.exchangeMemoryCache.getEventMarkets(eventIds);

        // Removing cache hits
        eventIds.removeAll(cachedEventMarketEntries.keySet());

        if(!eventIds.isEmpty()) {
            // Getting cache misses from exchange gateway and caching them
            cachedEventMarketEntries.putAll(
                    this.exchangeMemoryCache.cacheEventMarkets(this.exchangeGateway.listEventMarkets(this.loginService.getAuthenticationToken(), eventIds))
            );
        }

        return cachedEventMarketEntries;
    }

    @Override
    public Map<String, List<ExchangeMarketBook>> listMarketBooks(final Set<String> marketIds) {

        final var cachedMarketIdBooks = this.exchangeMemoryCache.getMarketBooks(marketIds);

        // Removing cache hits
        marketIds.removeAll(cachedMarketIdBooks.keySet());

        if(!marketIds.isEmpty()) {
            // Getting cache misses from exchange gateway and caching them
            cachedMarketIdBooks.putAll(
                    this.exchangeMemoryCache.cacheMarketBooks(this.exchangeGateway.listMarketBooks(this.loginService.getAuthenticationToken(), marketIds))
            );
        }

        return cachedMarketIdBooks;
    }

    @Override
    public ExchangePlaceOrdersResponse placeOrders(final ExchangePlaceOrdersRequest orders) {
        return this.exchangeGateway.placeOrders(this.loginService.getAuthenticationToken(), orders);
    }
}
