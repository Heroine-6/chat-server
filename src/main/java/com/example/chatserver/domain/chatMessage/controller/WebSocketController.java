package com.example.chatserver.domain.chatMessage.controller;

import com.example.chatserver.common.entity.ChatMessage;
import com.example.chatserver.common.entity.ChatRoom;
import com.example.chatserver.domain.chatMessage.dto.payload.GetMessagePayload;
import com.example.chatserver.domain.chatMessage.dto.payload.SendMessagePayload;
import com.example.chatserver.domain.chatMessage.service.ChatMessageService;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.chatMessage.dto.payload.ReadStatePayload;
import com.example.chatserver.domain.chatMessage.dto.request.MarkReadRequest;
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
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 메시지 전송
     * TODO: 유저 인증 수정 (지금은 테스트)
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
    public void markRead(MarkReadRequest request) {

        Long roomId = request.getRoomId();
        Long readerId = request.getUserId();

        Long lastReadMessageId = readStateService.markReadAll(roomId, readerId);

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("채팅방이 존재하지 않습니다."));

        Long otherId = readerId.equals(room.getSellerId()) ? room.getBidderId() : room.getSellerId();

        messagingTemplate.convertAndSendToUser(
                otherId.toString(),
                "/queue/read",
                new ReadStatePayload(roomId, readerId, lastReadMessageId)
        );
    }
}
