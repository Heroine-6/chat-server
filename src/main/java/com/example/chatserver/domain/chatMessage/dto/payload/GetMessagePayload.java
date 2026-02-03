package com.example.chatserver.domain.chatMessage.dto.payload;

import com.example.chatserver.common.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetMessagePayload {

    private Long messageId;
    private Long roomId;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;

    public static GetMessagePayload from(ChatMessage message) {
        return new GetMessagePayload(message.getId(), message.getChatRoom().getId(), message.getSenderId(), message.getContent(), message.getCreatedAt());
    }
}
