package com.example.chatserver.domain.chatRoom.service;

import com.example.chatserver.domain.chatRoom.dto.request.OpenChatRoomRequest;
import com.example.chatserver.domain.chatRoom.dto.response.GetMyChatRoomResponse;
import com.example.chatserver.domain.chatRoom.dto.response.OpenChatRoomResponse;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.chatMessage.repository.MessageRepository;
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
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 이미 존재하는 채팅방인지 검증
    @Transactional
    public Optional<OpenChatRoomResponse> openRoom(@Valid OpenChatRoomRequest request) {

        return chatRoomRepository
                .findByPropertyIdAndBidderIdAndSellerId(request.getPropertyId(), request.getBidderId(), request.getSellerId())
                .map(OpenChatRoomResponse::from);
    }

    // 내 채팅방 조회
    @Transactional(readOnly = true)
    public Slice<GetMyChatRoomResponse> getRooms(Long userId, Pageable pageable) {

        return chatRoomRepository
                .findBySellerIdOrBidderIdOrderByIdDesc(userId, userId, pageable)
                .map(GetMyChatRoomResponse::from);
    }
}
