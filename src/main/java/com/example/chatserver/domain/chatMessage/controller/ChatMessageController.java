package com.example.chatserver.domain.chatMessage.controller;

import com.example.chatserver.domain.chatMessage.dto.response.GetMessageResponse;
import com.example.chatserver.domain.chatMessage.service.ChatMessageService;
import com.example.chatserver.domain.chatMessage.dto.request.SendFirstMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.SendFirstMessageResponse;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.readState.service.ReadStateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/chats")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ReadStateService readStateService;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 첫 메시지 전송 + 방 생성
     * TODO: 유저 인증 (only bidder)
     */
    @PostMapping("/messages/{bidderId}")
    public ResponseEntity<SendFirstMessageResponse> sendFirstMessage(@PathVariable Long bidderId, @Valid @RequestBody SendFirstMessageRequest request) {

        SendFirstMessageResponse response = chatMessageService.sendFirstMessage(bidderId, request);

        return ResponseEntity.ok(response);
    }

    /**
     * 채팅 메시지 조회
     * TODO: 유저 인증
     */
    @GetMapping("/rooms/{roomId}/messages/{userId}")
    public ResponseEntity<Slice<GetMessageResponse>> getMessages(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Slice<GetMessageResponse> response = chatMessageService.getMessages(roomId, userId, pageable);

        return ResponseEntity.ok(response);
    }
}
