package com.ppb.bot.application.gateway.exchange.response;

import com.ppb.bot.application.gateway.exchange.entities.ExchangePlaceInstructionReport;
import com.ppb.bot.application.gateway.exchange.enums.ExchangeInstructionReportStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExchangePlaceOrdersResponse {
    private String marketId;
    private List<ExchangePlaceInstructionReport> instructionReports;
    private ExchangeInstructionReportStatus status;
}
