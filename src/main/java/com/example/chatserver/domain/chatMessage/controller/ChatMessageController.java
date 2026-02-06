package com.example.chatserver.domain.chatMessage.controller;

import com.example.chatserver.common.security.JwtProvider;
import com.example.chatserver.domain.chatMessage.dto.response.GetMessageResponse;
import com.example.chatserver.domain.chatMessage.service.ChatMessageService;
import com.example.chatserver.domain.chatMessage.dto.request.SendFirstMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.SendFirstMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/chats")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final JwtProvider jwtProvider;

    /**
     * 첫 메시지 전송 + 방 생성
     */
    @PostMapping("/messages/{propertyId}")
    public ResponseEntity<SendFirstMessageResponse> sendFirstMessage(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long propertyId,
            @Valid @RequestBody SendFirstMessageRequest request) {

        Long bidderId = jwtProvider.extractUserId(authorization);
        SendFirstMessageResponse response = chatMessageService.sendFirstMessage(bidderId, authorization, propertyId, request);

        return ResponseEntity.ok(response);
    }

    /**
     * 채팅 메시지 조회
     */
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<Slice<GetMessageResponse>> getMessages(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long roomId,
            @PageableDefault(size = 30, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Long userId = jwtProvider.extractUserId(authorization);
        Slice<GetMessageResponse> response = chatMessageService.getMessages(roomId, userId, pageable);

        return ResponseEntity.ok(response);
    }
}
