package com.example.chatserver.repository;

import com.example.chatserver.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    Optional<ChatRoom> findByPropertyIdAndBidderIdAndSellerId(Long propertyId, Long bidderId, Long sellerId);

    Slice<ChatRoom> findBySellerIdOrBidderIdOrderByIdDesc(Long sellerId, Long bidderId, Pageable pageable);
}
