package com.example.chatserver.domain.chatMessage.repository;

import com.example.chatserver.common.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

    Slice<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(Long roomId, Pageable pageable);

    Optional<ChatMessage> findTopByChatRoomIdOrderByIdDesc(Long roomId);
}
