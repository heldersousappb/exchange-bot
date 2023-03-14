package com.ppb.bot.presentation;

import com.ppb.bot.application.services.login.exception.UnexpectedLoginStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UnexpectedLoginStatusException.class)
    public ResponseEntity unexpectedLoginStatus(UnexpectedLoginStatusException e) {
        Map<String,String> body = Map.of("message", e.getMessage());
        return ResponseEntity.internalServerError().body(body);
    }

}
