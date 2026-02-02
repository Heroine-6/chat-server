package com.example.chatserver.domain.chatRoom.service;

import com.example.chatserver.common.entity.ChatRoom;
import com.example.chatserver.common.entity.ReadState;
import com.example.chatserver.domain.chatRoom.dto.request.FindRoomRequest;
import com.example.chatserver.domain.chatRoom.dto.response.GetMyRoomsResponse;
import com.example.chatserver.domain.chatRoom.dto.response.FindRoomResponse;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.readState.repository.ReadStateRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ReadStateRepository readStateRepository;

    // 이미 존재하는 채팅방인지 검증
    @Transactional
    public Optional<FindRoomResponse> findRoom(@Valid FindRoomRequest request) {

        return chatRoomRepository
                .findByPropertyIdAndBidderIdAndSellerId(request.getPropertyId(), request.getBidderId(), request.getSellerId())
                .map(FindRoomResponse::from);
    }

    // 내 채팅방 조회
    @Transactional(readOnly = true)
    public Slice<GetMyRoomsResponse> getMyRooms(Long userId, Pageable pageable) {

        Slice<ChatRoom> rooms = chatRoomRepository.findBySellerIdOrBidderIdOrderByLastMessageAtDesc(userId, userId, pageable);

        List<ChatRoom> roomList = rooms.getContent();

        if (roomList.isEmpty()) {
            return rooms.map(room -> GetMyRoomsResponse.from(room, 0L, null));
        }

        List<Long> roomIds = roomList.stream().map(ChatRoom::getId).toList();

        // 내 ReadState 조회
        List<ReadState> myReadStates = readStateRepository.findByChatRoomIdInAndUserId(roomIds, userId);

        Map<Long, ReadState> myReadStateMap = myReadStates.stream()
                .collect(toMap(
                        rs -> rs.getChatRoom().getId(),
                        rs -> rs
                ));

        // 상대 ReadState 조회
        List<Long> otherUserIds = roomList.stream()
                .map(room -> userId.equals(room.getSellerId()) ? room.getBidderId() : room.getSellerId())
                .distinct()
                .toList();

        List<ReadState> otherReadStates = readStateRepository.findByChatRoomIdInAndUserIdIn(roomIds, otherUserIds);

        // (roomId, userId) 조합으로 구분하기 위해 문자열 키 사용하여 합체
        Map<String, ReadState> otherReadStateMap = otherReadStates.stream()
                .collect(toMap(
                        rs -> key(rs.getChatRoom().getId(), rs.getUserId()),
                        rs -> rs
                ));

        return rooms.map(room -> {

            // 내가 읽지 않은 메시지 수
            ReadState myState = myReadStateMap.get(room.getId());
            long unreadCount = (myState == null) ? 0 : myState.getUnreadCount();

            // 상대가 어디까지 읽었는지
            Long otherUserId = userId.equals(room.getSellerId()) ? room.getBidderId() : room.getSellerId();

            ReadState otherState = otherReadStateMap.get(key(room.getId(), otherUserId));
            Long otherLastReadMessageId = (otherState == null) ? null : otherState.getLastReadMessageId();

            return GetMyRoomsResponse.from(room, unreadCount, otherLastReadMessageId);
        });
    }

    private String key(Long roomId, Long userId) {
        return roomId + ":" + userId;
    }
}
