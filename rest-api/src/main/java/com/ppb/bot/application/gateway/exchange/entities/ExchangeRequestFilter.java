package com.ppb.bot.application.gateway.exchange.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequestFilter {

    private Map<String, Object> filter;

    static public ExchangeRequestFilter empty() {
        return new ExchangeRequestFilter(Map.of());
    }

}
