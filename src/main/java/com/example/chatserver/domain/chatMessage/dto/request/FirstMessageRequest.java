package com.example.chatserver.domain.chatMessage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FirstMessageRequest {

    // TODO: 서버 연동 후 content만 받도록 수정
    @NotNull
    private Long propertyId;

    @NotNull
    private Long bidderId;

    @NotNull
    private Long sellerId;

    @NotNull
    private Long senderId;

    @NotBlank(message = "전송할 내용이 없습니다.")
    private String content;
}
