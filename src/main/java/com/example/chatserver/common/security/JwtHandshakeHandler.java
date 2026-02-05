package com.example.chatserver.common.security;

import lombok.NonNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class JwtHandshakeHandler extends DefaultHandshakeHandler {

    /**
     * Principal 생성 (name = userId)
     */
    @Override
    protected Principal determineUser(@NonNull ServerHttpRequest request,
                                      @NonNull WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        Object userId = attributes.get(JwtHandshakeInterceptor.ATTR_USER_ID);

        if (userId == null) {
            return null;
        }

        return () -> String.valueOf(userId);
    }
}

