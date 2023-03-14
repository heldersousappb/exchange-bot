package com.ppb.bot.application.gateway.exchange.entities;

import com.ppb.bot.application.gateway.exchange.enums.ExchangeOrderType;
import com.ppb.bot.application.gateway.exchange.enums.ExchangeSide;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangePlaceInstruction {

    private ExchangeOrderType orderType;
    private long selectionId;
    private double handicap;
    private ExchangeSide side;
    private ExchangeLimitOrder limitOrder;
    private ExchangeLimitOnCloseOrder limitOnCloseOrder;
    private ExchangeMarketOnCloseOrder marketOnCloseOrder;

}
