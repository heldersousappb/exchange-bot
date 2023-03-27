package com.ppb.bot.presentation

import com.ppb.bot.presentation.handler.ExchangeControllerHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
open class FunctionalWebExchangeController(
    @Autowired private val exchangeControllerHandler: ExchangeControllerHandler
) {

    @Bean
    open fun routing() = router {

        "/functional/exchange/event".nest {

            GET { ServerResponse.ok().bodyValue("HELLO!") }

            "/market".nest {
                POST("/bet", accept(MediaType.APPLICATION_JSON), exchangeControllerHandler::handleMarketBetRequest)
                POST("/books", accept(MediaType.APPLICATION_JSON), exchangeControllerHandler::handleMarketBooksRequest)
                POST(accept(MediaType.APPLICATION_JSON), exchangeControllerHandler::handleMarketRequest)
            }

            POST("/types", exchangeControllerHandler::handleTypesRequest)
            POST("/list", accept(MediaType.APPLICATION_JSON), exchangeControllerHandler::handleListRequest)


        }

    }

}