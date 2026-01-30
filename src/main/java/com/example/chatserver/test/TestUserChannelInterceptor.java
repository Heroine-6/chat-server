package com.example.chatserver.test;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class TestUserChannelInterceptor implements ChannelInterceptor {

    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String userId = accessor.getFirstNativeHeader("userId");

            if (userId != null && !userId.isBlank()) {
                accessor.setUser(new StompPrincipal(userId));
            }
        }

        return message;
    }
}