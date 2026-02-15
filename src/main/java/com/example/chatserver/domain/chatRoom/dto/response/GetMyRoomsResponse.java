package com.example.chatserver.domain.chatRoom.dto.response;

import com.example.chatserver.common.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetMyRoomsResponse {

    private Long roomId;
    private Long propertyId;
    private Long sellerId;
    private Long bidderId;
    private LocalDateTime lastMessageAt;
    private Long unreadCount;
    private Long lastReadMessageId;

    public static GetMyRoomsResponse from(ChatRoom room, Long unreadCount, Long lastReadMessageId) {
        return new GetMyRoomsResponse(room.getId(), room.getPropertyId(), room.getSellerId(), room.getBidderId(), room.getLastMessageAt(), unreadCount, lastReadMessageId);
    }
}
