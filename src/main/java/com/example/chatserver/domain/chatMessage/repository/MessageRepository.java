package com.example.chatserver.domain.chatMessage.repository;

import com.example.chatserver.common.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<ChatMessage,Long> {
}
