package com.ppb.bot.application.gateway.exchange.entities;

import com.ppb.bot.application.gateway.exchange.enums.ExchangePriceData;
import lombok.*;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangePriceProjection {

    private Set<ExchangePriceData> priceData;
    private ExchangeBestOfferOverrides exBestOfferOverRides;
    private boolean virtualise;
    private boolean rolloverStakes;

}
