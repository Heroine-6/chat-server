package com.example.chatserver.domain.readState.repository;

import com.example.chatserver.common.entity.ChatRoom;
import com.example.chatserver.common.entity.ReadState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReadStateRepository extends JpaRepository<ReadState,Long> {

    Optional<ReadState> findByChatRoomAndUserId(ChatRoom chatRoom, Long userId);

    List<ReadState> findByChatRoomIdInAndUserId(Collection<Long> roomIds, Long userId);

    List<ReadState> findByChatRoomIdInAndUserIdIn(Collection<Long> roomIds, Collection<Long> userIds);
}
