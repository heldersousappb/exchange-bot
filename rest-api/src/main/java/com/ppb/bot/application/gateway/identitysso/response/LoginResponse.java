package com.ppb.bot.application.gateway.identitysso.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String product;
    private LoginStatus status;
    private String error;

}
