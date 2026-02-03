package com.example.chatserver.domain.chatRoom.controller;

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

    /**
     * 이미 존재하는 채팅방인지 검증
     * TODO: 유저 인증
     */
    @PostMapping("/open/{bidderId}")
    public ResponseEntity<FindRoomResponse> findRoom(@PathVariable Long bidderId, @Valid @RequestBody FindRoomRequest request) {

        return chatRoomService.findRoom(bidderId, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * 내 채팅방 조회
     * TODO: 유저 인증
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Slice<GetMyRoomsResponse>> getMyRooms(@PathVariable Long userId, @PageableDefault(size = 20) Pageable pageable) {

        Slice<GetMyRoomsResponse> response = chatRoomService.getMyRooms(userId, pageable);

        return ResponseEntity.ok(response);
    }
}
