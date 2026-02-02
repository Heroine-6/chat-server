package com.example.chatserver.domain.readState.dto.payload;

import com.example.chatserver.common.entity.ReadState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadStatePayload {

    private Long roomId;
    private Long readerId;
    private Long lastReadMessageId;
}
