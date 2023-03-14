package com.ppb.bot.application.gateway.exchange.request;

import com.ppb.bot.application.gateway.exchange.enums.ExchangeMarketProjection;
import lombok.*;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeEventMarketsRequest {

    private Map<String, Object> filter;
    private String maxResults;
    private Set<ExchangeMarketProjection> marketProjection;

}
