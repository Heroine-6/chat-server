package com.example.chatserver.domain.chatRoom.dto.response;

import com.example.chatserver.common.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OpenChatRoomResponse {

    private Long roomId;

    public static OpenChatRoomResponse from(ChatRoom chatRoom) {
        return new OpenChatRoomResponse(chatRoom.getId());
    }
}
