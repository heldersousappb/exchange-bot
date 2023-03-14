package com.ppb.bot.application.gateway.exchange.entities;

import com.ppb.bot.application.gateway.exchange.enums.ExchangeInstructionReportErrorCode;
import com.ppb.bot.application.gateway.exchange.enums.ExchangeInstructionReportStatus;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangePlaceInstructionReport {

    private ExchangeInstructionReportStatus status;
    private ExchangeInstructionReportErrorCode errorCode;
    private ExchangePlaceInstruction instruction;
    private String betId;
    private Instant placedDate;
    private double averagePriceMatched;
    private double sizeMatched;

}
