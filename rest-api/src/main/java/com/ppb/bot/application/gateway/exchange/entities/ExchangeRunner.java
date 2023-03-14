package com.ppb.bot.application.gateway.exchange.entities;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangeRunner {

    private Long selectionId;
    private String runnerName;
    private Double handicap;
    private Integer sortPriority;
    private Map<String, Object> metadata;

}
