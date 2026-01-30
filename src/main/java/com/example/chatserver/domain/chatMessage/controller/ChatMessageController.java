package com.example.chatserver.domain.chatMessage.controller;

import com.example.chatserver.domain.chatMessage.service.ChatMessageService;
import com.example.chatserver.domain.chatMessage.dto.request.FirstMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.FirstMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 첫 메시지 전송 + 방 생성
    // TODO: 서버 연동 후 인증 추가
    @PostMapping("/v2")
    public ResponseEntity<FirstMessageResponse> firstMessage(@Valid @RequestBody FirstMessageRequest request) {

        FirstMessageResponse response = chatMessageService.firstMessage(request);

        return ResponseEntity.ok(response);
    }
}
