package com.example.chatserver.common.config;

import com.example.chatserver.common.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    public static final String ATTR_USER_ID = "USER_ID";

    private final JwtProvider jwtProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest http = servletRequest.getServletRequest();

            String authorization = http.getHeader("Authorization");

            // 헤더가 실리지 않을 경우
            if (authorization == null) {
                String tokenParam = http.getParameter("token");
                if (tokenParam != null && !tokenParam.isBlank()) {
                    authorization = tokenParam.startsWith("Bearer ") ? tokenParam : "Bearer " + tokenParam;
                }
            }

            if (authorization != null && authorization.startsWith("Bearer ")) {
                Long userId = jwtProvider.extractUserId(authorization);
                attributes.put(ATTR_USER_ID, userId);
                return true;
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}

