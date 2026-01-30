package com.example.chatserver.domain.chatRoom.dto.response;

import com.example.chatserver.common.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetMyRoomsResponse {

    // TODO: 채팅 제목, 최근 채팅 전송 시간 추가
    private Long roomId;
    private LocalDateTime createdAt;

    public static GetMyRoomsResponse from(ChatRoom room) {
        return new GetMyRoomsResponse(room.getId(), room.getCreatedAt());
    }
}
