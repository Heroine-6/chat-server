package com.example.chatserver.domain.chatMessage.service;

import com.example.chatserver.common.entity.ReadState;
import com.example.chatserver.domain.chatMessage.dto.payload.ChatMessagePayload;
import com.example.chatserver.domain.chatMessage.dto.request.SendFirstMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.request.SendMessageRequest;
import com.example.chatserver.domain.chatMessage.dto.response.GetMessageResponse;
import com.example.chatserver.domain.chatMessage.dto.response.SendFirstMessageResponse;
import com.example.chatserver.common.entity.ChatRoom;
import com.example.chatserver.common.entity.ChatMessage;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.chatMessage.repository.ChatMessageRepository;
import com.example.chatserver.domain.readState.repository.ReadStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStateRepository readStateRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 첫 메시지 전송 + 방 생성
    @Transactional
    public SendFirstMessageResponse sendFirstMessage(SendFirstMessageRequest request) {

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

        Optional<ChatRoom> existingRoom = chatRoomRepository.findByPropertyIdAndBidderIdAndSellerId(propertyId, bidderId, sellerId);

        if (existingRoom.isPresent()) {
            throw new IllegalStateException("이미 채팅방이 존재합니다.");
        }

        try {
            ChatRoom room = ChatRoom.create(sellerId, bidderId, propertyId);
            ChatRoom savedRoom = chatRoomRepository.save(room);

            ChatMessage message = ChatMessage.create(senderId, savedRoom, content);
            ChatMessage savedMessage = chatMessageRepository.save(message);

            savedRoom.updateLastMessageAt(savedMessage.getCreatedAt());

            ReadState sellerState = readStateRepository.save(ReadState.create(savedRoom, sellerId));
            sellerState.increaseUnreadCount();

            ReadState bidderState = readStateRepository.save(ReadState.create(savedRoom, bidderId));
            bidderState.markRead(savedMessage.getId());

            simpMessagingTemplate.convertAndSendToUser(
                    sellerId.toString(),
                    "/queue/chat",
                    ChatMessagePayload.from(savedMessage)
            );

            return SendFirstMessageResponse.from(savedRoom);

        } catch (DataIntegrityViolationException e) {

            throw new IllegalStateException("이미 생성되었습니다.");
        }
    }

    // 메시지 전송
    @Transactional
    public ChatMessage sendMessage(SendMessageRequest request) {

        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalStateException("채팅방이 존재하지 않습니다."));

        Long senderId = request.getSenderId();

        if (!senderId.equals(room.getSellerId()) && !senderId.equals(room.getBidderId())) {
            throw new IllegalStateException("채팅방 참여자만 메시지를 보낼 수 있습니다.");
        }

        ChatMessage message = ChatMessage.create(senderId, room, request.getContent());
        ChatMessage savedMessage = chatMessageRepository.save(message);

        room.updateLastMessageAt(savedMessage.getCreatedAt());

        Long receiverId = senderId.equals(room.getSellerId()) ? room.getBidderId() : room.getSellerId();

        ReadState receiverState = readStateRepository.findByChatRoomAndUserId(room, receiverId)
                .orElseGet(() -> readStateRepository.save(ReadState.create(room, receiverId)));
        receiverState.increaseUnreadCount();

        ReadState senderState = readStateRepository.findByChatRoomAndUserId(room, senderId)
                .orElseGet(() -> readStateRepository.save(ReadState.create(room, senderId)));
        senderState.markRead(savedMessage.getId());

        return savedMessage;
    }

    public Long getReceiverId(ChatMessage message) {
        ChatRoom room = message.getChatRoom();
        Long senderId = message.getSenderId();

        if (senderId.equals(room.getSellerId())) {
            return room.getBidderId();
        }
        return room.getSellerId();
    }

    // 채팅 메시지 조회
    @Transactional(readOnly = true)
    public Slice<GetMessageResponse> getMessages(Long roomId, Long userId, Pageable pageable) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("채팅방이 존재하지 않습니다."));

        if (!userId.equals(room.getSellerId()) && !userId.equals(room.getBidderId())) {
            throw new IllegalStateException("채팅방 참여자만 메시지를 보낼 수 있습니다.");
        }

        return chatMessageRepository
                .findByChatRoomIdOrderByCreatedAtDesc(roomId, pageable)
                .map(GetMessageResponse::from);
    }
}
