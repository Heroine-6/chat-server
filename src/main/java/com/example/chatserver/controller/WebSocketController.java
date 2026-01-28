package com.example.chatserver.controller;

import com.example.chatserver.dto.request.SendMessageRequest;
import com.example.chatserver.dto.response.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.send")
    public void send(@Payload SendMessageRequest request, Principal principal) {

        Long senderId = Long.valueOf(principal.getName());

        SendMessageResponse response = SendMessageResponse.of(senderId, request.getContent());

        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(request.getReceiverId()),
                "/queue/chat",
                response
        );
    }
}