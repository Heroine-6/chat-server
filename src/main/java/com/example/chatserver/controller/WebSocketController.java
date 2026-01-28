package com.example.chatserver.controller;

import com.example.chatserver.dto.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.send")
    public void send(SendMessageRequest request) {

        simpMessagingTemplate.convertAndSend(
                "/queue/test",
                "서버에서 받은 메시지: " + request.getContent()
        );
    }
}