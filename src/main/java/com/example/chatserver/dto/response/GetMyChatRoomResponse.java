package com.example.chatserver.dto.response;

import com.example.chatserver.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetMyChatRoomResponse {

    // TODO: 채팅 제목, 최근 채팅 전송 시간 추가
    private Long roomId;
    private LocalDateTime createdAt;

    public static GetMyChatRoomResponse from(ChatRoom room) {
        return new GetMyChatRoomResponse(room.getId(), room.getCreatedAt());
    }
}
