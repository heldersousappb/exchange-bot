package com.ppb.bot;

import com.ppb.bot.application.gateway.exchange.entities.*;
import com.ppb.bot.application.gateway.exchange.request.ExchangeEventMarketsRequest;
import com.ppb.bot.application.gateway.exchange.request.ExchangeEventTypeEventsRequest;
import com.ppb.bot.application.gateway.exchange.request.ExchangeMarketBookRequest;
import com.ppb.bot.application.gateway.exchange.request.ExchangePlaceOrdersRequest;
import com.ppb.bot.application.gateway.exchange.response.ExchangePlaceOrdersResponse;
import com.ppb.bot.application.gateway.identitysso.response.LoginResponse;
import com.ppb.bot.application.gateway.identitysso.response.LoginStatus;
import com.ppb.bot.infrastructure.MyRuntimeHints;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@EnableConfigurationProperties
@RegisterReflectionForBinding(
	{	LoginResponse.class, LoginStatus.class, ExchangePlaceOrdersResponse.class, ExchangeEventMarketsRequest.class,
		ExchangeEventTypeEventsRequest.class, ExchangeMarketBookRequest.class, ExchangePlaceOrdersRequest.class,
		ExchangeBestOfferOverrides.class, ExchangeCompetition.class, ExchangeEvent.class, ExchangeEventMarketCount.class,
		ExchangeEventType.class, ExchangeEventTypeEventsFilter.class, ExchangeEventTypeMarkets.class,
		ExchangeLimitOnCloseOrder.class, ExchangeLimitOrder.class, ExchangeMarketBook.class, ExchangeMarketBookRunner.class,
		ExchangeMarketCatalogueEntry.class, ExchangeMarketOnCloseOrder.class, ExchangeMarketPrices.class, ExchangeMarketStartTime.class,
		ExchangePlaceInstruction.class, ExchangePlaceInstructionReport.class, ExchangePriceProjection.class, ExchangePriceSize.class,
		ExchangeRequestFilter.class, ExchangeRunner.class
	}
)
@ImportRuntimeHints(MyRuntimeHints.class)
@SpringBootApplication
public class BotApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotApplication.class, args);
	}

}
