package com.ppb.bot.application.gateway.exchange.request;

import com.ppb.bot.application.gateway.exchange.entities.ExchangePriceProjection;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeMarketBookRequest {

    private Set<String> marketIds;
    private ExchangePriceProjection priceProjection;

}
