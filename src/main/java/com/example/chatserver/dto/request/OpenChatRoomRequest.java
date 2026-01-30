package com.example.chatserver.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OpenChatRoomRequest {

    @NotNull
    private Long bidderId;

    @NotNull
    private Long sellerId;

    @NotNull
    private Long propertyId;
}
