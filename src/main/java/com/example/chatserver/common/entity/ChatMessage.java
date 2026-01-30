package com.example.chatserver.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id",  nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    private ChatMessage(Long senderId, ChatRoom chatRoom, String content, Boolean isRead) {
        this.senderId = senderId;
        this.chatRoom = chatRoom;
        this.content = content;
        this.isRead = isRead;
    }

    public static ChatMessage create(Long senderId, ChatRoom chatRoom, String content) {
        return new ChatMessage(senderId, chatRoom, content, false);
    }
}
