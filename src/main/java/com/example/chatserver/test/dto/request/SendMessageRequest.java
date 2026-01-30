package com.example.chatserver.test.dto.request;

import lombok.Getter;

@Getter
public class SendMessageRequest {

    private Long receiverId;
    private String content;
}