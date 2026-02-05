package com.example.chatserver.domain.chatMessage.service;

import com.example.chatserver.common.entity.ChatMessage;
import com.example.chatserver.common.entity.ChatRoom;
import com.example.chatserver.common.entity.ReadState;
import com.example.chatserver.domain.chatMessage.dto.result.MarkReadResult;
import com.example.chatserver.domain.chatMessage.repository.ChatMessageRepository;
import com.example.chatserver.domain.chatRoom.repository.ChatRoomRepository;
import com.example.chatserver.domain.chatMessage.repository.ReadStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadStateService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStateRepository readStateRepository;

    /**
     * ReadState 조회 또는 생성
     */
    public ReadState getOrCreateReadState(ChatRoom room, Long userId) {

        return readStateRepository.findByChatRoomAndUserId(room, userId)
                .orElseGet(() -> readStateRepository.save(ReadState.create(room, userId)));
    }

    /**
     * 수신자 ReadState 업데이트 (읽지 않음 카운트 증가)
     */
    public void increaseUnreadCount(ChatRoom room, Long userId) {

        ReadState state = getOrCreateReadState(room, userId);
        state.increaseUnreadCount();
    }

    /**
     * 송신자 ReadState 업데이트 (읽음 처리)
     */
    public void markAsRead(ChatRoom room, Long userId, Long messageId) {

        ReadState state = getOrCreateReadState(room, userId);
        state.markRead(messageId);
    }

    /**
     * 채팅방의 모든 메시지 읽음 처리
     */
    @Transactional
    public MarkReadResult markReadAll(Long roomId, Long userId) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("채팅방이 존재하지 않습니다."));

        if (!userId.equals(room.getSellerId()) && !userId.equals(room.getBidderId())) {
            throw new IllegalStateException("채팅방 참여자만 읽음 처리할 수 있습니다.");
        }

        Long lastMessageId = chatMessageRepository.findTopByChatRoomIdOrderByIdDesc(roomId)
                .map(ChatMessage::getId)
                .orElse(null);

        ReadState state = getOrCreateReadState(room, userId);
        state.markRead(lastMessageId);

        Long otherId = userId.equals(room.getSellerId()) ? room.getBidderId() : room.getSellerId();

        return new MarkReadResult(otherId, lastMessageId);
    }
}