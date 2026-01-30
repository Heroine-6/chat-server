package com.example.chatserver.domain.chatMessage.dto.response;

import com.example.chatserver.common.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendMessageResponse {

    private Long messageId;
    private Long roomId;

    public static SendMessageResponse from(ChatMessage message) {
        return new SendMessageResponse(message.getId(), message.getChatRoom().getId());
    }
}