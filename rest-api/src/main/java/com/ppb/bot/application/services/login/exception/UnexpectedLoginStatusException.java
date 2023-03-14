package com.ppb.bot.application.services.login.exception;

import com.ppb.bot.application.services.login.dto.LoginStatus;

public class UnexpectedLoginStatusException extends RuntimeException {

    public UnexpectedLoginStatusException(LoginStatus status) {
        super("Unexpected login status '" + status.toString() + "'!");
    }

}
