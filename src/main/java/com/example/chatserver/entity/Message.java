package com.example.chatserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

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

    private Message(Long senderId, ChatRoom chatRoom, String content, Boolean isRead) {
        this.senderId = senderId;
        this.chatRoom = chatRoom;
        this.content = content;
        this.isRead = isRead;
    }

    public static Message create(Long senderId, ChatRoom chatRoom, String content) {
        return new Message(senderId, chatRoom, content, false);
    }
}
