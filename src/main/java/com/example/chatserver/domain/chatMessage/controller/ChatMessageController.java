package com.example.chatserver.domain.chatMessage.controller;

import com.example.chatserver.common.entity.ChatMessage;
import com.example.chatserver.domain.chatMessage.dto.payload.ChatMessagePayload;
import com.example.chatserver.domain.chatMessage.dto.request.SendMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.GetMessageResponse;
import com.example.chatserver.domain.chatMessage.service.ChatMessageService;
import com.example.chatserver.domain.chatMessage.dto.request.SendFirstMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.SendFirstMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/chats")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    // 첫 메시지 전송 + 방 생성
    // TODO: 서버 연동 후 인증 추가
    @PostMapping("/messages")
    public ResponseEntity<SendFirstMessageResponse> sendFirstMessage(@Valid @RequestBody SendFirstMessageRequest request) {

        SendFirstMessageResponse response = chatMessageService.sendFirstMessage(request);

        return ResponseEntity.ok(response);
    }

    // 메시지 전송
    @MessageMapping("/message")
    public void sendMessage(SendMessageRequest request) {

        ChatMessage savedMessage = chatMessageService.sendMessage(request);

        Long receiverId = chatMessageService.getReceiverId(savedMessage);

        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/chat",
                ChatMessagePayload.from(savedMessage)
        );
    }

    // 채팅 메시지 조회
    @GetMapping("/rooms/{roomId}/messages/{userId}")
    public ResponseEntity<Slice<GetMessageResponse>> getMessages(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @PageableDefault(size = 30) Pageable pageable) {

        Slice<GetMessageResponse> response = chatMessageService.getMessages(roomId, userId, pageable);

        return ResponseEntity.ok(response);
    }
}
