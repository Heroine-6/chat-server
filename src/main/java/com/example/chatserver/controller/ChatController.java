package com.example.chatserver.controller;

import com.example.chatserver.dto.request.FirstMessageRequest;
import com.example.chatserver.dto.response.FirstMessageResponse;
import com.example.chatserver.dto.response.GetMyChatRoomResponse;
import com.example.chatserver.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    // 첫 메시지 전송 + 방 생성
    // TODO: 서버 연동 후 인증 추가
    @PostMapping("/v2")
    public ResponseEntity<FirstMessageResponse> firstMessage(@Valid @RequestBody FirstMessageRequest request) {

        FirstMessageResponse response = chatService.firstMessage(request);

        return ResponseEntity.ok(response);
    }

    // 내 채팅방 조회
    // TODO: 서버 연동 후 인증 추가
    @GetMapping("/v2/{userId}")
    public ResponseEntity<List<GetMyChatRoomResponse>> getRooms(@PathVariable Long userId) {

        List<GetMyChatRoomResponse> response = chatService.getRooms(userId);

        return ResponseEntity.ok(response);
    }
}
