package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeEventTypeMarkets {

    private ExchangeEventType eventType;
    private Integer marketCount;

}
