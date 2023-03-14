package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeLimitOnCloseOrder {

    private double liability;
    private double price;

}
