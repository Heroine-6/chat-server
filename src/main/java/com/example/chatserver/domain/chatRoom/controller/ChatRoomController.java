package com.example.chatserver.domain.chatRoom.controller;

import com.example.chatserver.common.security.JwtProvider;
import com.example.chatserver.domain.chatRoom.service.ChatRoomService;
import com.example.chatserver.domain.chatRoom.dto.request.FindRoomRequest;
import com.example.chatserver.domain.chatRoom.dto.response.GetMyRoomsResponse;
import com.example.chatserver.domain.chatRoom.dto.response.FindRoomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/chats/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JwtProvider jwtProvider;

    /**
     * 이미 존재하는 채팅방인지 검증
     */
    @PostMapping("/open")
    public ResponseEntity<FindRoomResponse> findRoom(@RequestHeader("Authorization") String authorization, @Valid @RequestBody FindRoomRequest request) {

        Long bidderId = jwtProvider.extractUserId(authorization);

        return chatRoomService.findRoom(bidderId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * 내 채팅방 조회
     */
    @GetMapping
    public ResponseEntity<Slice<GetMyRoomsResponse>> getMyRooms(@RequestHeader("Authorization") String authorization, @PageableDefault(size = 20) Pageable pageable) {

        Long userId = jwtProvider.extractUserId(authorization);
        Slice<GetMyRoomsResponse> response = chatRoomService.getMyRooms(userId, pageable);

        return ResponseEntity.ok(response);
    }
}
