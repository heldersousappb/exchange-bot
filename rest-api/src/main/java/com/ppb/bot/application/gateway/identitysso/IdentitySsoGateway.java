package com.ppb.bot.application.gateway.identitysso;

import com.ppb.bot.application.gateway.identitysso.response.LoginResponse;
import reactor.core.publisher.Mono;

public interface IdentitySsoGateway {

    String APPLICATION_HEADER_NAME = "X-Application";
    String IP_HEADER_NAME = "X-IP";

    Mono<LoginResponse> login();

}
