package com.ppb.bot.application.gateway.identitysso;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties("com.betfair.bot.gateway.identitysso")
public class IdentitySsoGatewayConfig {

    private String baseUrl;

    private String username;

    private String password;

}
