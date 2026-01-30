package com.example.chatserver.domain.chatRoom.dto.response;

import com.example.chatserver.common.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindRoomResponse {

    private Long roomId;

    public static FindRoomResponse from(ChatRoom chatRoom) {
        return new FindRoomResponse(chatRoom.getId());
    }
}
