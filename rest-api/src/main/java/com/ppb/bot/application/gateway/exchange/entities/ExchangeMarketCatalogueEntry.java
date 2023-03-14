package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeMarketCatalogueEntry {

    private String marketId;
    private String marketName;
    private String marketStartTime;
    private Double totalMatched;
    private List<ExchangeRunner> runners;
    private ExchangeEventType eventType;
    private ExchangeCompetition competition;
    private ExchangeEvent event;

}
