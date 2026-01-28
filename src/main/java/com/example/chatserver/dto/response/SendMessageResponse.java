package com.example.chatserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SendMessageResponse {

    private Long senderId;
    private String content;
    private LocalDateTime sentAt;

    public static SendMessageResponse of(Long senderId, String content) {
        return new SendMessageResponse(senderId, content, LocalDateTime.now());
    }
}