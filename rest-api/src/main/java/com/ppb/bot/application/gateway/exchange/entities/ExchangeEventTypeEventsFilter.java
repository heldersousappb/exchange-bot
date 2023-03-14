package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeEventTypeEventsFilter {

    private Set<String> eventTypeIds;
    private ExchangeMarketStartTime marketStartTime;

}
