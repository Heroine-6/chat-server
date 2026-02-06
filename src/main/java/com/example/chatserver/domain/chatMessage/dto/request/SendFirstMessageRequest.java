package com.example.chatserver.domain.chatMessage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SendFirstMessageRequest {

    @NotBlank(message = "전송할 내용이 없습니다.")
    private String content;
}
