package com.ppb.bot.presentation.handler

import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest
import com.ppb.bot.application.services.exchange.ExchangeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.time.Instant

@Component
class ExchangeControllerHandler(
    @Autowired private val exchangeService: ExchangeService
) {

    fun handleTypesRequest(request: ServerRequest) : Mono<ServerResponse> {
        return ServerResponse.ok().bodyValue(this.exchangeService::listEventTypes)
    }

    fun handleListRequest(request: ServerRequest) : Mono<ServerResponse> {

        return request.bodyToMono(object : ParameterizedTypeReference<Set<String>>(){}).flatMap { eventTypeIds ->

            val from = request.queryParam("from").map(Instant::parse)
            val to = request.queryParam("to").map(Instant::parse)

            this.exchangeService.listEvents(eventTypeIds, from, to).flatMap { events ->
                ServerResponse.ok().bodyValue(events)
            }
        }

    }

    fun handleMarketRequest(request: ServerRequest) : Mono<ServerResponse> {

        return request.bodyToMono(object : ParameterizedTypeReference<Set<String>>(){}).flatMap { eventIds ->
            this.exchangeService.listEventMarkets(eventIds).flatMap { eventMarkets ->
                ServerResponse.ok().bodyValue(eventMarkets)
            }
        }

    }

    fun handleMarketBooksRequest(request: ServerRequest) : Mono<ServerResponse> {

        return request.bodyToMono(object : ParameterizedTypeReference<Set<String>>(){}).flatMap { marketIds ->
            this.exchangeService.listMarketBooks(marketIds).flatMap { marketBooks ->
                ServerResponse.ok().bodyValue(marketBooks)
            }
        }

    }

    fun handleMarketBetRequest(request: ServerRequest) : Mono<ServerResponse> {

        return request.bodyToMono(ExchangePlaceOrdersRequest::class.java).flatMap { orders ->
            this.exchangeService.placeOrders(orders).flatMap { betSlip ->
                ServerResponse.ok().bodyValue(betSlip)
            }
        }

    }

}