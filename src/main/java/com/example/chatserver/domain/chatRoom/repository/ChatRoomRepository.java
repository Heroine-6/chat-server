package com.example.chatserver.domain.chatRoom.repository;

import com.example.chatserver.common.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    Optional<ChatRoom> findByBidderIdAndPropertyId(Long bidderId, Long propertyId);

    Slice<ChatRoom> findBySellerIdOrBidderIdOrderByLastMessageAtDesc(Long sellerId, Long bidderId, Pageable pageable);
}
