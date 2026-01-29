package com.example.chatserver.dto.payload;

import com.example.chatserver.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessagePayload {

    private Long roomId;
    private Long senderId;
    private String content;

    public static ChatMessagePayload from(ChatMessage message) {
        return new ChatMessagePayload(message.getChatRoom().getId(), message.getSenderId(), message.getContent());
    }
}
