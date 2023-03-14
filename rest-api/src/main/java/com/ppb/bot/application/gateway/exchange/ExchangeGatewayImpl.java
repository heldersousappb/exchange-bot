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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExchangeGatewayImpl implements ExchangeGateway {
    private final WebClient webClient;

    public ExchangeGatewayImpl(
        @Value("${com.betfair.bot.gateway.exchange.base-url}") final String exchangeBaseUrl,
        @Value("${com.betfair.bot.id}") final String applicationId,
        @Autowired final WebClient.Builder webClientBuilder
    ) {
        this.webClient = webClientBuilder.baseUrl(exchangeBaseUrl).defaultHeader(ExchangeGateway.APPLICATION_HEADER_NAME, applicationId).build();
    }


    @Override
    public Mono<List<ExchangeEventTypeMarkets>> listEventTypes(final String authenticationToken) {
        return this.webClient
                .post()
                .uri("/listEventTypes/")
                .accept(MediaType.APPLICATION_JSON)
                .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
                .bodyValue(ExchangeRequestFilter.empty())
                .retrieve()
                .bodyToFlux(ExchangeEventTypeMarkets.class)
                .collectList();
    }

    @Override
    // TODO: Make sure that jackson plays nice with Instant
    public Mono<List<ExchangeEventMarketCount>> listEvents(
        final String authenticationToken, final Set<String> eventTypeIds, final Optional<Instant> from, final Optional<Instant> to
    ) {

        ExchangeEventTypeEventsFilter exchangeEventTypeEventsFilter = new ExchangeEventTypeEventsFilter(eventTypeIds, new ExchangeMarketStartTime());

        if(from.isPresent()) {
            exchangeEventTypeEventsFilter.getMarketStartTime().setFrom(from.get().toString());
        }

        if(to.isPresent()) {
            exchangeEventTypeEventsFilter.getMarketStartTime().setTo(to.get().toString());
        }

        return this.webClient
                .post()
                .uri("/listEvents/")
                .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new ExchangeEventTypeEventsRequest(exchangeEventTypeEventsFilter))
                .retrieve()
                .bodyToFlux(ExchangeEventMarketCount.class)
                .collectList();
    }

    @Override
    // TODO: Try use collector group by
    public Mono<Map<String, List<ExchangeMarketCatalogueEntry>>> listEventMarkets(String authenticationToken, Set<String> eventIds) {

        ExchangeEventMarketsRequest body = new ExchangeEventMarketsRequest(
                Map.of("eventIds", eventIds),
                "1000",
                Set.of(ExchangeMarketProjection.COMPETITION, ExchangeMarketProjection.EVENT, ExchangeMarketProjection.EVENT_TYPE, ExchangeMarketProjection.RUNNER_DESCRIPTION, ExchangeMarketProjection.RUNNER_METADATA, ExchangeMarketProjection.MARKET_START_TIME)
        );

        return this.webClient
                .post()
                .uri("/listMarketCatalogue/")
                .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(ExchangeMarketCatalogueEntry.class)
                .collectList()
                .map(exchangeEventsMarkets -> {

                    Map<String, List<ExchangeMarketCatalogueEntry>> exchangeEventsMarketsMap = new HashMap<>();

                    exchangeEventsMarkets.stream().forEach(exchangeEventMarket -> {

                        if(exchangeEventsMarketsMap.containsKey(exchangeEventMarket.getEvent().getId())) {
                            exchangeEventsMarketsMap.get(exchangeEventMarket.getEvent().getId()).add(exchangeEventMarket);
                        } else {
                            exchangeEventsMarketsMap.put(exchangeEventMarket.getEvent().getId(), new ArrayList<>(Arrays.asList(exchangeEventMarket)));
                        }

                    });

                    return exchangeEventsMarketsMap;
                });
    }

    @Override
    public Mono<Map<String, List<ExchangeMarketBook>>> listMarketBooks(String authenticationToken, Set<String> marketIds) {

        ExchangeMarketBookRequest body = new ExchangeMarketBookRequest(marketIds, new ExchangePriceProjection(Set.of(ExchangePriceData.EX_TRADED, ExchangePriceData.EX_BEST_OFFERS), null, true, false));

        return this.webClient
                .post()
                .uri("/listMarketBook/")
                .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(ExchangeMarketBook.class)
                .collectList()
                .map(exchangeMarketBooks -> exchangeMarketBooks.stream().collect(Collectors.groupingBy(ExchangeMarketBook::getMarketId)));
    }

    @Override
    public Mono<ExchangePlaceOrdersResponse> placeOrders(String authenticationToken, ExchangePlaceOrdersRequest orders) {
        return this.webClient
                .post()
                .uri("/placeOrders/")
                .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
                .bodyValue(orders)
                .retrieve()
                .bodyToMono(ExchangePlaceOrdersResponse.class);
    }
}
