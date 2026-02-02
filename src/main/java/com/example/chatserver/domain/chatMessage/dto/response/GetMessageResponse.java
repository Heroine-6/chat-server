package com.example.chatserver.domain.chatMessage.dto.response;

import com.example.chatserver.common.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetMessageResponse {

    private Long messageId;
    private Long senderId;
    private String content;
    private LocalDateTime createdAt;

    public static GetMessageResponse from(ChatMessage message) {
        return new GetMessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
