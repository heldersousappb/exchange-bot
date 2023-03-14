package com.ppb.bot.application.services.login.mapper;

import com.ppb.bot.application.gateway.identitysso.response.LoginResponse;
import com.ppb.bot.application.services.login.dto.LoginResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GatewayLoginResponseMapper {

    GatewayLoginResponseMapper INSTANCE = Mappers.getMapper(GatewayLoginResponseMapper.class);

    LoginResponseDto gatewayToService(LoginResponse gateway);

}
