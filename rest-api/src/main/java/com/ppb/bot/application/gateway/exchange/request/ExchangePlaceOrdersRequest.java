package com.ppb.bot.application.gateway.exchange.request;

import com.ppb.bot.application.gateway.exchange.entities.ExchangePlaceInstruction;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangePlaceOrdersRequest {

    private String marketId;
    private List<ExchangePlaceInstruction> instructions;

}
