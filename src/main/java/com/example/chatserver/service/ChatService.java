package com.example.chatserver.service;

import com.example.chatserver.dto.request.FirstMessageRequest;
import com.example.chatserver.dto.response.FirstMessageResponse;
import com.example.chatserver.entity.ChatRoom;
import com.example.chatserver.entity.Message;
import com.example.chatserver.repository.ChatRoomRepository;
import com.example.chatserver.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    // 첫 메시지 전송 + 방 생성
    @Transactional
    public FirstMessageResponse firstMessage(FirstMessageRequest request) {

        Long sellerId = request.getSellerId();
        Long bidderId = request.getBidderId();
        Long senderId = request.getSenderId();
        Long propertyId = request.getPropertyId();

        // TODO: Custom Exception
        if (!request.getSenderId().equals(request.getBidderId())) {
            throw new IllegalStateException("첫 메시지는 입찰자만 전송할 수 있습니다.");
        }

        if (sellerId.equals(bidderId)) {
            throw new IllegalStateException("판매자와 입찰자는 동일할 수 없습니다.");
        }

        if (chatRoomRepository.findByPropertyIdAndBidderIdAndSellerId(propertyId, bidderId, sellerId).isPresent()) {
            throw new IllegalStateException("이미 채팅방이 존재합니다.");
        }

        ChatRoom room = ChatRoom.create(sellerId, bidderId, propertyId);
        ChatRoom savedRoom = chatRoomRepository.save(room);

        Message message = Message.create(senderId, savedRoom, request.getContent());
        Message savedMessage = messageRepository.save(message);

        return FirstMessageResponse.from(savedRoom);
    }
}
