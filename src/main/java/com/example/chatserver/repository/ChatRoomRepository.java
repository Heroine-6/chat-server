package com.example.chatserver.repository;

import com.example.chatserver.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    Optional<ChatRoom> findByPropertyIdAndBidderIdAndSellerId(Long propertyId, Long bidderId, Long sellerId);
}
