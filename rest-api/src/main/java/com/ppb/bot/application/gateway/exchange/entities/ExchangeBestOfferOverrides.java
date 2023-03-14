package com.ppb.bot.application.gateway.exchange.entities;

import com.ppb.bot.application.gateway.exchange.enums.ExchangeRollupModel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeBestOfferOverrides {

    private int bestPricesDepth;
    private ExchangeRollupModel rollupModel;
    private int rollupLimit;
    private double rollupLiabilityThreshold;
    private int rollupLiabilityFactor;

}
