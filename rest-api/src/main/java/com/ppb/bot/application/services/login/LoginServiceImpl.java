package com.ppb.bot.application.services.login;

import com.ppb.bot.application.gateway.identitysso.IdentitySsoGateway;
import com.ppb.bot.application.gateway.identitysso.response.LoginStatus;
import com.ppb.bot.application.services.login.exception.UnexpectedLoginStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginServiceImpl implements LoginService {

    private String authenticationToken;
    private final IdentitySsoGateway identitySsoGateway;

    static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    public LoginServiceImpl(@Autowired IdentitySsoGateway identitySsoGateway) {
        this.identitySsoGateway = identitySsoGateway;
    }


    private Mono<String> login() {
        return this.identitySsoGateway.login().map(loginResponse -> {

            if(loginResponse.getStatus() == LoginStatus.SUCCESS) {
                return loginResponse.getToken();
            } else {
                throw new UnexpectedLoginStatusException(loginResponse.getStatus());
            }

        });
    }

    @Override
    public Mono<String> getAuthenticationToken() {
        if(this.authenticationToken == null) {
            return this.refreshAuthenticationToken();
        } else {
            return Mono.just(this.authenticationToken);
        }
    }

    @Override
    public Mono<String> refreshAuthenticationToken() {
        return this.login().map(authenticationToken -> {
            LOGGER.info("Refreshing authentication token...");
            this.authenticationToken = authenticationToken;
            return authenticationToken;
        });
    }

    @Scheduled(fixedDelay = 3600000) // Refresh authentication token every hour
    public void refreshTokenJob() {
        this.refreshAuthenticationToken().subscribe();
    }
}
