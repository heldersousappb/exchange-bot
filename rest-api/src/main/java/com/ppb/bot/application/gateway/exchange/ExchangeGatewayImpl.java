package com.ppb.bot.application.gateway.exchange;

import com.ppb.bot.application.gateway.exchange.enums.ExchangeMarketProjection;
import com.ppb.bot.application.gateway.exchange.enums.ExchangePriceData;
import com.ppb.bot.application.gateway.exchange.request.ExchangeEventMarketsRequest;
import com.ppb.bot.application.gateway.exchange.entities.ExchangeRequestFilter;
import com.ppb.bot.application.gateway.exchange.entities.*;
import com.ppb.bot.application.gateway.exchange.request.ExchangeEventTypeEventsRequest;
import com.ppb.bot.application.gateway.exchange.request.ExchangeMarketBookRequest;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExchangeGatewayImpl implements ExchangeGateway {
    private final RestTemplate restTemplate;

    public ExchangeGatewayImpl(
        @Value("${com.betfair.bot.gateway.exchange.base-url}") final String exchangeBaseUrl,
        @Value("${com.betfair.bot.id}") final String applicationId,
        @Autowired final RestTemplateBuilder restTemplateBuilder
    ) {
        this.restTemplate = restTemplateBuilder
                .rootUri(exchangeBaseUrl)
                .defaultHeader(ExchangeGateway.APPLICATION_HEADER_NAME, applicationId)
                .build();
    }


    @Override
    public List<ExchangeEventTypeMarkets> listEventTypes(final String authenticationToken) {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken);

        HttpEntity<ExchangeRequestFilter> requestBody = new HttpEntity<>(ExchangeRequestFilter.empty(), requestHeaders);
        return this.restTemplate.exchange("/listEventTypes/", HttpMethod.POST, requestBody, new ParameterizedTypeReference<List<ExchangeEventTypeMarkets>>(){}).getBody();
    }

    @Override
    // TODO: Make sure that jackson plays nice with Instant
    public List<ExchangeEventMarketCount> listEvents(
        final String authenticationToken, final Set<String> eventTypeIds, final Optional<Instant> from, final Optional<Instant> to
    ) {

        ExchangeEventTypeEventsFilter exchangeEventTypeEventsFilter = new ExchangeEventTypeEventsFilter(eventTypeIds, new ExchangeMarketStartTime());

        if(from.isPresent()) {
            exchangeEventTypeEventsFilter.getMarketStartTime().setFrom(from.get().toString());
        }

        if(to.isPresent()) {
            exchangeEventTypeEventsFilter.getMarketStartTime().setTo(to.get().toString());
        }

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken);

        HttpEntity<ExchangeEventTypeEventsRequest> requestBody = new HttpEntity<>(new ExchangeEventTypeEventsRequest(exchangeEventTypeEventsFilter), requestHeaders);

        return this.restTemplate.exchange("/listEvents/", HttpMethod.POST, requestBody, new ParameterizedTypeReference<List<ExchangeEventMarketCount>>(){}).getBody();
    }

    @Override
    // TODO: Try using collector group by
    public Map<String, List<ExchangeMarketCatalogueEntry>> listEventMarkets(String authenticationToken, Set<String> eventIds) {

        ExchangeEventMarketsRequest exchangeEventMarketsRequest = new ExchangeEventMarketsRequest(
                Map.of("eventIds", eventIds),
                "1000",
                Set.of(ExchangeMarketProjection.COMPETITION, ExchangeMarketProjection.EVENT, ExchangeMarketProjection.EVENT_TYPE, ExchangeMarketProjection.RUNNER_DESCRIPTION, ExchangeMarketProjection.RUNNER_METADATA, ExchangeMarketProjection.MARKET_START_TIME)
        );

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken);

        HttpEntity<ExchangeEventMarketsRequest> requestBody = new HttpEntity<>(exchangeEventMarketsRequest, requestHeaders);

        Map<String, List<ExchangeMarketCatalogueEntry>> exchangeEventsMarketsMap = new HashMap<>();
        this.restTemplate.exchange("/listMarketCatalogue/", HttpMethod.POST, requestBody, new ParameterizedTypeReference<List<ExchangeMarketCatalogueEntry>>(){}).getBody().stream().forEach(exchangeEventMarket -> {

            if(exchangeEventsMarketsMap.containsKey(exchangeEventMarket.getEvent().getId())) {
                exchangeEventsMarketsMap.get(exchangeEventMarket.getEvent().getId()).add(exchangeEventMarket);
            } else {
                exchangeEventsMarketsMap.put(exchangeEventMarket.getEvent().getId(), new ArrayList<>(Arrays.asList(exchangeEventMarket)));
            }

        });

        return exchangeEventsMarketsMap;
    }

    @Override
    public Map<String, List<ExchangeMarketBook>> listMarketBooks(String authenticationToken, Set<String> marketIds) {

        ExchangeMarketBookRequest exchangeMarketBookRequest = new ExchangeMarketBookRequest(marketIds, new ExchangePriceProjection(Set.of(ExchangePriceData.EX_TRADED, ExchangePriceData.EX_BEST_OFFERS), null, true, false));

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken);

        HttpEntity<ExchangeMarketBookRequest> requestBody = new HttpEntity<>(exchangeMarketBookRequest, requestHeaders);

        return this.restTemplate.exchange("/listMarketBook/", HttpMethod.POST, requestBody, new ParameterizedTypeReference<List<ExchangeMarketBook>>(){}).getBody().stream().collect(Collectors.groupingBy(ExchangeMarketBook::getMarketId));
    }

    @Override
    public ExchangePlaceOrdersResponse placeOrders(String authenticationToken, ExchangePlaceOrdersRequest orders) {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken);

        HttpEntity<ExchangePlaceOrdersRequest> requestBody = new HttpEntity<>(orders, requestHeaders);

        return this.restTemplate.exchange("/placeOrders/", HttpMethod.POST, requestBody, ExchangePlaceOrdersResponse.class).getBody();
    }
}
