package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeMarketPrices {

    private List<ExchangePriceSize> availableToBack;
    private List<ExchangePriceSize> availableToLay;
    private List<ExchangePriceSize> tradedVolume;

}
