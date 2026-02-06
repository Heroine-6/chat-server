package com.example.chatserver.domain.chatRoom.service;

import com.example.chatserver.common.clients.MainServerClient;
import com.example.chatserver.common.response.ChatServerResponse;
import com.example.chatserver.common.entity.ChatRoom;
import com.example.chatserver.common.entity.ReadState;
import com.example.chatserver.common.response.GlobalResponse;
import com.example.chatserver.domain.chatMessage.service.ReadStateService;
import com.example.chatserver.domain.chatRoom.dto.request.FindRoomRequest;
import com.example.chatserver.domain.chatRoom.dto.response.GetMyRoomsResponse;
import com.example.chatserver.domain.chatRoom.dto.response.FindRoomResponse;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.chatMessage.repository.ReadStateRepository;
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
    private final ReadStateService readStateService;
    private final MainServerClient mainServerClient;

    /**
     * 이미 존재하는 채팅방인지 검증
     */
    @Transactional
    public Optional<FindRoomResponse> findRoom(Long bidderId, FindRoomRequest request) {

        return chatRoomRepository
                .findByPropertyIdAndBidderIdAndSellerId(request.getPropertyId(), bidderId, request.getSellerId())
                .map(FindRoomResponse::from);
    }

    /**
     * 외부 API에서 채팅 컨텍스트 조회
     */
    @Transactional(readOnly = true)
    public ChatServerResponse fetchChatContext(Long propertyId, String authorization) {

        GlobalResponse<ChatServerResponse> response = mainServerClient.getChatContext(authorization, propertyId);

        if (response == null || !response.success() || response.data() == null) {
            throw new RuntimeException("외부 API 조회에 실패했습니다.");
        }

        return response.data();
    }

    /**
     * 내 채팅방 조회
     */
    @Transactional(readOnly = true)
    public Slice<GetMyRoomsResponse> getMyRooms(Long userId, Pageable pageable) {

        Slice<ChatRoom> rooms = chatRoomRepository.findBySellerIdOrBidderIdOrderByLastMessageAtDesc(userId, userId, pageable);

        List<ChatRoom> roomList = rooms.getContent();

        if (roomList.isEmpty()) {
            return rooms.map(room -> GetMyRoomsResponse.from(room, 0L, null));
        }

        Map<Long, ReadState> myReadStateMap = readStateService.loadMyReadStates(roomList, userId);
        Map<String, ReadState> otherReadStateMap = readStateService.loadOtherReadStates(roomList, userId);

        return rooms.map(room -> mapToResponse(room, userId, myReadStateMap, otherReadStateMap));
    }

    /**
     * ChatRoom을 GetMyRoomsResponse로 매핑
     */
    private GetMyRoomsResponse mapToResponse(ChatRoom room, Long userId, Map<Long, ReadState> myReadStateMap, Map<String, ReadState> otherReadStateMap) {

        /* 내가 읽지 않은 메시지 수 */
        ReadState myState = myReadStateMap.get(room.getId());
        long unreadCount = (myState == null) ? 0 : myState.getUnreadCount();

        /* 상대가 마지막으로 읽은 메시지 */
        Long otherUserId = userId.equals(room.getSellerId()) ? room.getBidderId() : room.getSellerId();
        ReadState otherState = otherReadStateMap.get(readStateService.createKey(room.getId(), otherUserId));
        Long otherLastReadMessageId = (otherState == null) ? null : otherState.getLastReadMessageId();

        return GetMyRoomsResponse.from(room, unreadCount, otherLastReadMessageId);
    }
}