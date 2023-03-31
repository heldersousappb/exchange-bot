package com.ppb.bot.infrastructure;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplateBuilder restTemplate() { return new RestTemplateBuilder(); }

}
