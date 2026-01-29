package com.example.chatserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "bidder_id", nullable = false)
    private Long bidderId;

    @Column(name = "property_id", nullable = false)
    private Long propertyId;

    private ChatRoom(Long sellerId, Long bidderId, Long propertyId) {
        this.sellerId = sellerId;
        this.bidderId = bidderId;
        this.propertyId = propertyId;
    }

    public static ChatRoom create(Long sellerId, Long bidderId, Long propertyId) {
        return new ChatRoom(sellerId, bidderId, propertyId);
    }
}
