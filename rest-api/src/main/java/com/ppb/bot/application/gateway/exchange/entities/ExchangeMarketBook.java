package com.ppb.bot.application.gateway.exchange.entities;

import com.ppb.bot.application.gateway.exchange.enums.ExchangeMarketStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeMarketBook {

    private String marketId;
    private Boolean isMarketDataDelayed;
    private ExchangeMarketStatus status;
    private Integer betDelay;
    private Boolean bspReconciled;
    private Boolean complete;
    private Boolean inplay;
    private Integer numberOfWinners;
    private Integer numberOfRunners;
    private Integer numberOfActiveRunners;
    private Double totalMatched;
    private Double totalAvailable;
    private Boolean crossMatching;
    private Boolean runnersVoidable;
    private Long version;
    private List<ExchangeMarketBookRunner> runners;

}
