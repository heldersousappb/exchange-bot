package com.ppb.bot.application.services.login;

import reactor.core.publisher.Mono;

public interface LoginService {

    Mono<String> getAuthenticationToken();

    Mono<String> refreshAuthenticationToken();

}
