package com.example.chatserver.domain.chatMessage.controller;

import com.example.chatserver.common.entity.ChatMessage;
import com.example.chatserver.common.entity.ChatRoom;
import com.example.chatserver.domain.chatMessage.dto.payload.ChatMessagePayload;
import com.example.chatserver.domain.chatMessage.dto.request.SendMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.GetMessageResponse;
import com.example.chatserver.domain.chatMessage.service.ChatMessageService;
import com.example.chatserver.domain.chatMessage.dto.request.SendFirstMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.SendFirstMessageResponse;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.readState.dto.payload.ReadStatePayload;
import com.example.chatserver.domain.readState.dto.request.MarkReadRequest;
import com.example.chatserver.domain.readState.service.ReadStateService;
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
     * 메시지 전송
     */
    @MessageMapping("/message")
    public void sendMessage(SendMessageRequest request) {

        ChatMessage savedMessage = chatMessageService.sendMessage(request);

        Long receiverId = chatMessageService.getReceiverId(savedMessage);

        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/chat",
                ChatMessagePayload.from(savedMessage)
        );

        messagingTemplate.convertAndSendToUser(
                request.getSenderId().toString(),
                "/queue/chat",
                ChatMessagePayload.from(savedMessage)
        );
    }

    /**
     * 메시지 읽음
     */
    @MessageMapping("/read")
    public void markRead(MarkReadRequest request) {

        Long roomId = request.getRoomId();
        Long readerId = request.getUserId();

        Long lastReadMessageId = readStateService.markReadAll(roomId, readerId);

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("채팅방이 존재하지 않습니다."));

        Long otherId = readerId.equals(room.getSellerId()) ? room.getBidderId() : room.getSellerId();

        messagingTemplate.convertAndSendToUser(
                otherId.toString(),
                "/queue/read",
                new ReadStatePayload(roomId, readerId, lastReadMessageId)
        );
    }

    /**
     * 채팅 메시지 조회
     */
    @GetMapping("/rooms/{roomId}/messages/{userId}")
    public ResponseEntity<Slice<GetMessageResponse>> getMessages(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @PageableDefault(size = 30) Pageable pageable) {

        Slice<GetMessageResponse> response = chatMessageService.getMessages(roomId, userId, pageable);

        return ResponseEntity.ok(response);
    }
}
