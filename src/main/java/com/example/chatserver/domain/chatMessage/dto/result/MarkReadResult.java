package com.example.chatserver.domain.chatMessage.dto.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarkReadResult {

    private Long otherId;
    private Long lastReadMessageId;
}

