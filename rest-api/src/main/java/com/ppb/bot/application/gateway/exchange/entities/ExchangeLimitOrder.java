package com.ppb.bot.application.gateway.exchange.entities;

import com.ppb.bot.application.gateway.exchange.enums.ExchangePersistenceType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeLimitOrder {

    private double size;
    private double price;
    private ExchangePersistenceType persistenceType;

}
