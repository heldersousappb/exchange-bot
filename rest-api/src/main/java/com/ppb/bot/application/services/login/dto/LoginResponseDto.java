package com.ppb.bot.application.services.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {

    private String token;

    private String error;

    private LoginStatus status;

}
