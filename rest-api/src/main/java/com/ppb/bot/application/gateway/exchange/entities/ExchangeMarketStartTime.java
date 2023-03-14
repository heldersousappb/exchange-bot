package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeMarketStartTime {

    // TODO: Change to Instant and make it work with jackson
    private String from;
    private String to;

}
