package com.ppb.bot.application.services.login;

public interface LoginService {

    String getAuthenticationToken();

    String refreshAuthenticationToken();

}
