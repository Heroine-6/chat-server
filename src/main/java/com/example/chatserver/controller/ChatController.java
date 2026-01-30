package com.example.chatserver.controller;

import com.example.chatserver.dto.request.FirstMessageRequest;
import com.example.chatserver.dto.request.OpenChatRoomRequest;
import com.example.chatserver.dto.response.FirstMessageResponse;
import com.example.chatserver.dto.response.GetMyChatRoomResponse;
import com.example.chatserver.dto.response.OpenChatRoomResponse;
import com.example.chatserver.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    // 이미 존재하는 채팅방인지 검증
    @PostMapping("/v2/open")
    public ResponseEntity<OpenChatRoomResponse> openRoom(@Valid @RequestBody OpenChatRoomRequest request) {

        return chatService.openRoom(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

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
    public ResponseEntity<Slice<GetMyChatRoomResponse>> getRooms(@PathVariable Long userId, @PageableDefault(size = 20) Pageable pageable) {

        Slice<GetMyChatRoomResponse> response = chatService.getRooms(userId, pageable);

        return ResponseEntity.ok(response);
    }
}
