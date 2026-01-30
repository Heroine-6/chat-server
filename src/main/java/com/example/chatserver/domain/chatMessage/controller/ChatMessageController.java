package com.example.chatserver.domain.chatMessage.controller;

import com.example.chatserver.domain.chatMessage.service.ChatMessageService;
import com.example.chatserver.domain.chatMessage.dto.request.SendFirstMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.SendFirstMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/chats/messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 첫 메시지 전송 + 방 생성
    // TODO: 서버 연동 후 인증 추가
    @PostMapping
    public ResponseEntity<SendFirstMessageResponse> sendFirstMessage(@Valid @RequestBody SendFirstMessageRequest request) {

        SendFirstMessageResponse response = chatMessageService.sendFirstMessage(request);

        return ResponseEntity.ok(response);
    }
}
