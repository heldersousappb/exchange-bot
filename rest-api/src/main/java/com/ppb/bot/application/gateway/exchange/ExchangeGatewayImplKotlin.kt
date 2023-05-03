package com.ppb.bot.application.gateway.exchange

import com.ppb.bot.application.gateway.exchange.entities.*
import com.ppb.bot.application.gateway.exchange.enums.ExchangeMarketProjection
import com.ppb.bot.application.gateway.exchange.enums.ExchangePriceData
import com.ppb.bot.application.gateway.exchange.request.ExchangeEventMarketsRequest
import com.ppb.bot.application.gateway.exchange.request.ExchangeEventTypeEventsRequest
import com.ppb.bot.application.gateway.exchange.request.ExchangeMarketBookRequest
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*

import kotlin.collections.List
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.groupBy
import kotlin.collections.mapOf
import kotlin.collections.setOf
import kotlin.collections.toMutableMap

@Component
class ExchangeGatewayImplKotlin(
    @Value("\${com.betfair.bot.gateway.exchange.base-url}") exchangeBaseUrl: String,
    @Value("\${com.betfair.bot.id}") applicationId: String,
    @Autowired webClientBuilder: WebClient.Builder
) : ExchangeGateway {


    private val webClient = webClientBuilder.baseUrl(exchangeBaseUrl)
                                            .defaultHeader(ExchangeGateway.APPLICATION_HEADER_NAME, applicationId)
                                            .build()

    override fun listEventTypes(authenticationToken: String?): Mono<List<ExchangeEventTypeMarkets>> {
        return webClient
            .post()
            .uri("/listEventTypes/")
            .accept(MediaType.APPLICATION_JSON)
            .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
            .bodyValue(ExchangeRequestFilter.empty())
            .retrieve()
            .bodyToFlux(ExchangeEventTypeMarkets::class.java)
            .collectList()
    }

    override fun listEvents(
        authenticationToken: String,
        eventTypeIds: MutableSet<String>,
        from: Optional<Instant>,
        to: Optional<Instant>
    ): Mono<List<ExchangeEventMarketCount>> {

        val exchangeEventTypeEventsFilter = ExchangeEventTypeEventsFilter(eventTypeIds, ExchangeMarketStartTime())

        if (from.isPresent) {
            exchangeEventTypeEventsFilter.marketStartTime.from = from.get().toString()
        }

        if (to.isPresent) {
            exchangeEventTypeEventsFilter.marketStartTime.to = to.get().toString()
        }

        return webClient
            .post()
            .uri("/listEvents/")
            .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(ExchangeEventTypeEventsRequest(exchangeEventTypeEventsFilter))
            .retrieve()
            .bodyToFlux(ExchangeEventMarketCount::class.java)
            .collectList()
    }

    override fun listEventMarkets(
        authenticationToken: String,
        eventIds: MutableSet<String>
    ): Mono<MutableMap<String, List<ExchangeMarketCatalogueEntry>>> {

        val body = ExchangeEventMarketsRequest(
            mapOf(Pair("eventIds", eventIds)),
            "1000",
            setOf(
                ExchangeMarketProjection.COMPETITION,
                ExchangeMarketProjection.EVENT,
                ExchangeMarketProjection.EVENT_TYPE,
                ExchangeMarketProjection.RUNNER_DESCRIPTION,
                ExchangeMarketProjection.RUNNER_METADATA,
                ExchangeMarketProjection.MARKET_START_TIME
            )
        )

        return webClient
            .post()
            .uri("/listMarketCatalogue/")
            .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
            .bodyValue(body)
            .retrieve()
            .bodyToFlux(ExchangeMarketCatalogueEntry::class.java)
            .collectList().map { events ->
                events.groupBy { e -> e.event.id }.toMutableMap()
            }
    }

    override fun listMarketBooks(
        authenticationToken: String,
        marketIds: MutableSet<String>
    ): Mono<MutableMap<String, List<ExchangeMarketBook>>> {

        val body = ExchangeMarketBookRequest(
            marketIds,
            ExchangePriceProjection(
                setOf(ExchangePriceData.EX_TRADED, ExchangePriceData.EX_BEST_OFFERS),
                null,
                true,
                false
            )
        )

        return webClient
            .post()
            .uri("/listMarketBook/")
            .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
            .bodyValue(body)
            .retrieve()
            .bodyToFlux(ExchangeMarketBook::class.java)
            .collectList().map { marketBooks ->
                marketBooks.groupBy { b -> b.marketId }.toMutableMap()
            }
    }

    override fun placeOrders(
        authenticationToken: String,
        orders: ExchangePlaceOrdersRequest
    ): Mono<ExchangePlaceOrdersResponse> {
        return webClient
            .post()
            .uri("/placeOrders/")
            .header(ExchangeGateway.AUTHENTICATION_HEADER_NAME, authenticationToken)
            .bodyValue(orders)
            .retrieve()
            .bodyToMono(ExchangePlaceOrdersResponse::class.java)
    }
}