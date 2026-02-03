package com.example.chatserver.domain.chatMessage.dto.request;

import lombok.Getter;

@Getter
public class MarkReadRequest {

    private Long roomId;
    private Long userId;
}
