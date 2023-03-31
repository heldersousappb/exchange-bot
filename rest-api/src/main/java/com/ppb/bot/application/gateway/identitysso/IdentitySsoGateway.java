package com.ppb.bot.application.gateway.identitysso;

import com.ppb.bot.application.gateway.identitysso.response.LoginResponse;

public interface IdentitySsoGateway {

    String APPLICATION_HEADER_NAME = "X-Application";
    String IP_HEADER_NAME = "X-IP";

    LoginResponse login();

}
