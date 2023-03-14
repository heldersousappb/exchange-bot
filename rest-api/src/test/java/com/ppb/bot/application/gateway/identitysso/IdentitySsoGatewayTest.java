//package com.ppb.bot.application.gateway.identitysso;
//
//import com.ppb.bot.BotApplication;
//import com.ppb.bot.application.services.login.LoginService;
//import com.ppb.bot.application.services.login.LoginServiceImpl;
//import com.ppb.bot.presentation.login.LoginController;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.reactive.server.WebTestClient;
//
//@SpringBootApplication
//@ExtendWith(SpringExtension.class)
//@WebFluxTest(controllers = LoginController.class)
//@Import({LoginServiceImpl.class, IdentitySsoGatewayImpl.class})
////@SpringBootTest(classes = BotApplication.class)
//public class IdentitySsoGatewayTest {
//
//    @Autowired
//    private WebTestClient webTestClient;
//
//
//    @Test
//    public void test() {
//
//        webTestClient.post().uri("/login").exchange().expectStatus().isOk();
//
//    }
//
//}
