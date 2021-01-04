package com.isaes.whocalled.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
public class FilterChannelInterceptor  implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
        //log.error( headerAccessor.getMessageHeaders().toString());
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            log.error( headerAccessor.getMessageHeaders().toString());
            if(null!=headerAccessor.getHeader("Authorization")){
                //https://stackoverflow.com/questions/30887788/json-web-token-jwt-with-spring-based-sockjs-stomp-web-socket/39456274#39456274
                log.error("AuthHeader {}",headerAccessor.getNativeHeader("Authorization").toString());
            }
        }
        return message;
    }
}
