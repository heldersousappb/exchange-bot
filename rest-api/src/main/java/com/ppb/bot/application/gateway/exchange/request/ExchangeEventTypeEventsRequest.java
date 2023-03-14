package com.ppb.bot.application.gateway.exchange.request;

import com.ppb.bot.application.gateway.exchange.entities.ExchangeEventTypeEventsFilter;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeEventTypeEventsRequest {

    private ExchangeEventTypeEventsFilter filter;

}
