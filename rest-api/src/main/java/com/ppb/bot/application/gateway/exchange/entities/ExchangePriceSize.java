package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangePriceSize {

    private Double price;
    private Double size;

}
