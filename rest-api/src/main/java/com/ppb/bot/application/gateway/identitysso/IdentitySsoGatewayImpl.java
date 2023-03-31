package com.ppb.bot.application.gateway.identitysso;


import com.ppb.bot.application.gateway.identitysso.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class IdentitySsoGatewayImpl implements IdentitySsoGateway {

    private final RestTemplate restTemplate;
    private final IdentitySsoGatewayConfig config;
    private final String applicationId;
    private final String applicationIpAddress;

    public IdentitySsoGatewayImpl(
        @Autowired final IdentitySsoGatewayConfig config,
        @Autowired final RestTemplateBuilder restTemplateBuilder,
        @Value("${com.betfair.bot.id}") final String applicationId,
        @Value("${com.betfair.bot.ip}") final String applicationIpAddress
    ) {
        this.config = config;
        this.restTemplate = restTemplateBuilder.rootUri(config.getBaseUrl()).build();
        this.applicationId = applicationId;
        this.applicationIpAddress = applicationIpAddress;
    }

    @Override
    public LoginResponse login() {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        requestHeaders.add(IdentitySsoGateway.APPLICATION_HEADER_NAME, applicationId);
        requestHeaders.add(IdentitySsoGateway.IP_HEADER_NAME, applicationIpAddress);

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, requestHeaders);

        String urlEncodedData = "?username=" + config.getUsername() + "&password=" +config.getPassword();

        return this.restTemplate.exchange("/login" + urlEncodedData, HttpMethod.POST, requestEntity, LoginResponse.class).getBody();
    }

}
