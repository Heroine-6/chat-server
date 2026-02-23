package com.example.chatserver.common.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
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
    public boolean beforeHandshake(@NonNull ServerHttpRequest request,
                                   @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler,
                                   @NonNull Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {

            HttpServletRequest http = servletRequest.getServletRequest();

            /* Authorization 헤더에서 JWT 추출 */
            String authorization = http.getHeader("Authorization");

            /* 헤더가 실리지 않을 경우 쿼리 파라미터로 처리 (SockJS) */
            if (authorization == null) {
                String tokenParam = http.getParameter("token");
                if (tokenParam != null && !tokenParam.isBlank()) {
                    authorization = tokenParam.startsWith("Bearer ") ? tokenParam : "Bearer " + tokenParam;
                }
            }

            /* JWT 검증 후 userId 저장 */
            if (authorization != null && authorization.startsWith("Bearer ")) {
                Long userId = jwtProvider.extractUserId(authorization);
                attributes.put(ATTR_USER_ID, userId);
                return true;
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request,
                               @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler,
                               Exception exception) { }
}

