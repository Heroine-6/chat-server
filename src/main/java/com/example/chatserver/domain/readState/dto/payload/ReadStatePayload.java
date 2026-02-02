package com.example.chatserver.domain.readState.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadStatePayload {

    private Long roomId;
    private Long readerId;
    private Long lastReadMessageId;
}
