package com.example.chatserver.domain.chatMessage.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendMessagePayload {

    private Long roomId;
    private String content;
}
