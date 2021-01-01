package com.isaes.whocalled.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app") // @MessageMapping Prefix for STOMP client
                .enableSimpleBroker("/queue"); // Special keyword for sending specific user
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/wsocket") // Websocket communication will be continue over this endpoint.
                .setAllowedOriginPatterns("*") // https://github.com/spring-projects/spring-framework/issues/26111
                .setHandshakeHandler(new DeterminePrincipalHandshakeHandler())
                .withSockJS();

    }

}
