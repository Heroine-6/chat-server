package com.example.chatserver.domain.chatRoom.service;

import com.example.chatserver.domain.chatRoom.dto.request.FindRoomRequest;
import com.example.chatserver.domain.chatRoom.dto.response.GetMyRoomsResponse;
import com.example.chatserver.domain.chatRoom.dto.response.FindRoomResponse;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.chatMessage.repository.ChatMessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

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

        return chatRoomRepository
                .findBySellerIdOrBidderIdOrderByLastMessageAtDesc(userId, userId, pageable)
                .map(GetMyRoomsResponse::from);
    }
}
