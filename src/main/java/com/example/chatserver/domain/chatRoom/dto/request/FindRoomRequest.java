package com.example.chatserver.domain.chatRoom.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FindRoomRequest {

    @NotNull
    private Long bidderId;

    @NotNull
    private Long sellerId;

    @NotNull
    private Long propertyId;
}
