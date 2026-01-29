package com.example.chatserver.dto.response;

import com.example.chatserver.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FirstMessageResponse {

    private Long roomId;
    private LocalDateTime createdAt;

    public static FirstMessageResponse from(ChatRoom chatRoom) {
        return new FirstMessageResponse(chatRoom.getId(), chatRoom.getCreatedAt());
    }
}
