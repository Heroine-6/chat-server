package com.example.chatserver.domain.chatRoom.controller;

import com.example.chatserver.domain.chatRoom.service.ChatRoomService;
import com.example.chatserver.domain.chatRoom.dto.request.OpenChatRoomRequest;
import com.example.chatserver.domain.chatRoom.dto.response.GetMyChatRoomResponse;
import com.example.chatserver.domain.chatRoom.dto.response.OpenChatRoomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 이미 존재하는 채팅방인지 검증
    @PostMapping("/v2/open")
    public ResponseEntity<OpenChatRoomResponse> openRoom(@Valid @RequestBody OpenChatRoomRequest request) {

        return chatRoomService.openRoom(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    // 내 채팅방 조회
    // TODO: 서버 연동 후 인증 추가
    @GetMapping("/v2/{userId}")
    public ResponseEntity<Slice<GetMyChatRoomResponse>> getRooms(@PathVariable Long userId, @PageableDefault(size = 20) Pageable pageable) {

        Slice<GetMyChatRoomResponse> response = chatRoomService.getRooms(userId, pageable);

        return ResponseEntity.ok(response);
    }
}
