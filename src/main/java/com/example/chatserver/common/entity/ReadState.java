package com.example.chatserver.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "read_states", uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "user_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="last_read_message_id")
    private Long lastReadMessageId;

    @Column(name="unread_count", nullable = false)
    private long unreadCount;

    private ReadState(ChatRoom chatRoom, Long userId, Long lastReadMessageId, long unreadCount) {
        this.chatRoom = chatRoom;
        this.userId = userId;
        this.lastReadMessageId = lastReadMessageId;
        this.unreadCount = unreadCount;
    }

    public static ReadState create(ChatRoom chatRoom, Long userId) {
        return new ReadState(chatRoom, userId, null, 0);
    }

    public void increaseUnreadCount() {
        this.unreadCount++;
    }

    public void markRead(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
        this.unreadCount = 0;
    }
}
