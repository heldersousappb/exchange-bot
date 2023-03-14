package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeEventMarketCount {

    private ExchangeEvent event;
    private Integer marketCount;

}
