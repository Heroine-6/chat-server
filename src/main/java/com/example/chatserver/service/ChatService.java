package com.example.chatserver.service;

import com.example.chatserver.dto.payload.ChatMessagePayload;
import com.example.chatserver.dto.request.FirstMessageRequest;
import com.example.chatserver.dto.request.OpenChatRoomRequest;
import com.example.chatserver.dto.response.FirstMessageResponse;
import com.example.chatserver.dto.response.GetMyChatRoomResponse;
import com.example.chatserver.dto.response.OpenChatRoomResponse;
import com.example.chatserver.entity.ChatRoom;
import com.example.chatserver.entity.ChatMessage;
import com.example.chatserver.repository.ChatRoomRepository;
import com.example.chatserver.repository.MessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

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

    // 첫 메시지 전송 + 방 생성
    @Transactional
    public FirstMessageResponse firstMessage(FirstMessageRequest request) {

        Long sellerId = request.getSellerId();
        Long bidderId = request.getBidderId();
        Long senderId = request.getSenderId();
        Long propertyId = request.getPropertyId();
        String content = request.getContent();

        // TODO: Custom Exception
        if (!request.getSenderId().equals(request.getBidderId())) {
            throw new IllegalStateException("첫 메시지는 입찰자만 전송할 수 있습니다.");
        }

        if (sellerId.equals(bidderId)) {
            throw new IllegalStateException("판매자와 입찰자는 동일할 수 없습니다.");
        }

        Optional<ChatRoom> existingRoom = chatRoomRepository.findByPropertyIdAndBidderIdAndSellerId(propertyId, bidderId, senderId);

        if (existingRoom.isPresent()) {
            throw new IllegalStateException("이미 채팅방이 존재합니다.");
        }

        try {
            ChatRoom room = ChatRoom.create(sellerId, bidderId, propertyId);
            ChatRoom savedRoom = chatRoomRepository.save(room);

            ChatMessage message = ChatMessage.create(senderId, savedRoom, content);
            ChatMessage savedMessage = messageRepository.save(message);

            simpMessagingTemplate.convertAndSendToUser(
                    sellerId.toString(),
                    "/queue/chat",
                    ChatMessagePayload.from(savedMessage)
            );

            return FirstMessageResponse.from(savedRoom);

        } catch (DataIntegrityViolationException e) {

            throw new IllegalStateException("이미 생성되었습니다.");
        }
    }

    // 내 채팅방 조회
    @Transactional(readOnly = true)
    public List<GetMyChatRoomResponse> getRooms(Long userId) {

        List<ChatRoom> rooms = chatRoomRepository.findBySellerIdOrBidderId(userId, userId);
        List<GetMyChatRoomResponse> response = new ArrayList<>();

        for (ChatRoom room : rooms) {
            response.add(GetMyChatRoomResponse.from(room));
        }

        return response;
    }
}
