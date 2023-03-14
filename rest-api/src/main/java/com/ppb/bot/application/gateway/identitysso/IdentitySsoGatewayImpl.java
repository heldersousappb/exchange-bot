package com.ppb.bot.application.gateway.identitysso;


import com.ppb.bot.application.gateway.identitysso.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class IdentitySsoGatewayImpl implements IdentitySsoGateway {

    private final WebClient webClient;
    private final IdentitySsoGatewayConfig config;
    private final String applicationId;
    private final String applicationIpAddress;

    public IdentitySsoGatewayImpl(
        @Autowired final IdentitySsoGatewayConfig config,
        @Autowired final WebClient.Builder webClientBuilder,
        @Value("${com.betfair.bot.id}") final String applicationId,
        @Value("${com.betfair.bot.ip}") final String applicationIpAddress
    ) {
        this.config = config;
        this.webClient = webClientBuilder.baseUrl(config.getBaseUrl()).build();
        this.applicationId = applicationId;
        this.applicationIpAddress = applicationIpAddress;
    }

    @Override
    public Mono<LoginResponse> login() {

        String urlEncodedData = "?username=" + config.getUsername() + "&password=" +config.getPassword();

        return this.webClient
            .post()
            .uri("/login" + urlEncodedData)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .header("X-Application", applicationId)
            .header("X-IP", applicationIpAddress)
            .retrieve()
            .bodyToMono(LoginResponse.class);

    }

}
