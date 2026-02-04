package com.example.chatserver.domain.chatMessage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SendFirstMessageRequest {

    /**
     * TODO: PathVariable로 propertyId 받고, sellerId 자동으로 받을 수 있도록 수정
     */
    @NotNull
    private Long propertyId;

    @NotBlank(message = "전송할 내용이 없습니다.")
    private String content;
}
