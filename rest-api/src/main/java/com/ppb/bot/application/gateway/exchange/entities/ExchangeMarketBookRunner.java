package com.ppb.bot.application.gateway.exchange.entities;

import com.ppb.bot.application.gateway.exchange.enums.ExchangeRunnerStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeMarketBookRunner {

    private Long selectionId;
    private Double handicap;
    private ExchangeRunnerStatus status;
    private ExchangeMarketPrices ex;

}
