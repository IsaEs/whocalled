package com.isaes.whocalled.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.stream.Collectors;

/**
 * We are creating randomId for each web socket session for communicate with a
 * specific websocket client. This will be set on the handshake. Spring will
 * handle randomId - session mapping its self and I will be handle randomId -- realUser mapping.
 */
@Slf4j
public class DeterminePrincipalHandshakeHandler extends DefaultHandshakeHandler{
    private static final String PRINCIPAL_KEY = "__principal__";

    public static final class StompPrincipal implements Principal {
        private final String name;
        public StompPrincipal(String name) {
            this.name = name;
        }
        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String name;
        log.info("DeterminePrincipal: Handshake...");
        try {
            String result =new BufferedReader(new InputStreamReader(request.getBody()))
                    .lines().collect(Collectors.joining("\n"));
            log.info(result);
        }catch (IOException e){
            log.error("IOException: determineUser");
        }
        attributes.forEach((k,v)->{
            log.error(k);
            log.info(v.toString());
        });

        // Set message mapping attributes if its not set
        if (!attributes.containsKey(PRINCIPAL_KEY)) {
            name = UUID.randomUUID().toString();
            attributes.put(PRINCIPAL_KEY, name);
        } else {
            name = (String) attributes.get(PRINCIPAL_KEY);
        }
        return new StompPrincipal(name);
    }
}