package com.ppb.bot.application.services.login;

import com.ppb.bot.application.gateway.identitysso.IdentitySsoGateway;
import com.ppb.bot.application.gateway.identitysso.response.LoginStatus;
import com.ppb.bot.application.services.login.exception.UnexpectedLoginStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private String authenticationToken;
    private final IdentitySsoGateway identitySsoGateway;

    static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    public LoginServiceImpl(@Autowired IdentitySsoGateway identitySsoGateway) {
        this.identitySsoGateway = identitySsoGateway;
    }


    private String login() {

        final var loginResponse = this.identitySsoGateway.login();

        if (loginResponse.getStatus() == LoginStatus.SUCCESS) {
            return loginResponse.getToken();
        } else {
            throw new UnexpectedLoginStatusException(loginResponse.getStatus());
        }

    }

    @Override
    public String getAuthenticationToken() {
        if(this.authenticationToken == null) {
            return this.refreshAuthenticationToken();
        } else {
            return this.authenticationToken;
        }
    }

    @Override
    public String refreshAuthenticationToken() {
        LOGGER.info("Refreshing authentication token...");
        this.authenticationToken = this.login();
        return this.authenticationToken;
    }

    @Scheduled(fixedDelay = 3600000) // Refresh authentication token every hour
    public void refreshTokenJob() {
        this.refreshAuthenticationToken();
    }
}
