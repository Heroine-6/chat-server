package com.example.chatserver.domain.chatMessage.controller;

import com.example.chatserver.common.entity.ChatMessage;
import com.example.chatserver.common.entity.ChatRoom;
import com.example.chatserver.domain.chatMessage.dto.payload.GetMessagePayload;
import com.example.chatserver.domain.chatMessage.dto.payload.SendMessagePayload;
import com.example.chatserver.domain.chatMessage.dto.result.MarkReadResult;
import com.example.chatserver.domain.chatMessage.service.ChatMessageService;
import com.example.chatserver.domain.chatMessage.dto.payload.ReadStatePayload;
import com.example.chatserver.domain.chatMessage.dto.payload.MarkReadPayload;
import com.example.chatserver.domain.chatMessage.service.ReadStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChatMessageService chatMessageService;
    private final ReadStateService readStateService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 메시지 전송
     */
    @MessageMapping("/message")
    public void sendMessage(SendMessagePayload payload, Principal principal) {

        Long senderId = Long.valueOf(principal.getName());

        ChatMessage savedMessage = chatMessageService.sendMessage(senderId, payload);
        ChatRoom room = savedMessage.getChatRoom();

        Long receiverId = senderId.equals(room.getSellerId()) ? room.getBidderId() : room.getSellerId();

        GetMessagePayload out = GetMessagePayload.from(savedMessage);

        messagingTemplate.convertAndSendToUser(receiverId.toString(), "/queue/chat", out);
        messagingTemplate.convertAndSendToUser(senderId.toString(), "/queue/chat", out);
    }

    /**
     * 메시지 읽음
     */
    @MessageMapping("/read")
    public void markRead(MarkReadPayload payload, Principal principal) {

        Long roomId = payload.getRoomId();
        Long userId = Long.valueOf(principal.getName());

        MarkReadResult result = readStateService.markReadAll(roomId, userId);

        ReadStatePayload out = new ReadStatePayload(roomId, userId, result.getLastReadMessageId());

        messagingTemplate.convertAndSendToUser(result.getOtherId().toString(), "/queue/read", out);
    }
}
