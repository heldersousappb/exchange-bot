package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeEvent {
    private String id;
    private String name;
    private String countryCode;
    private String timezone;
    private Instant openDate;
}
