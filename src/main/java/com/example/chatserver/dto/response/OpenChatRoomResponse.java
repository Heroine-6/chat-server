package com.example.chatserver.dto.response;

import com.example.chatserver.entity.ChatRoom;
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
