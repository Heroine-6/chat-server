package com.example.chatserver.domain.chatMessage.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SendMessageRequest {

    @NotNull
    private Long roomId;

    @NotNull
    private Long senderId;

    @NotNull
    private String content;
}