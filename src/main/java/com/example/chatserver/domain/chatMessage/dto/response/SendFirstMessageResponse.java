package com.example.chatserver.domain.chatMessage.dto.response;

import com.example.chatserver.common.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SendFirstMessageResponse {

    private Long roomId;
    private LocalDateTime createdAt;

    public static SendFirstMessageResponse from(ChatRoom chatRoom) {
        return new SendFirstMessageResponse(chatRoom.getId(), chatRoom.getCreatedAt());
    }
}
