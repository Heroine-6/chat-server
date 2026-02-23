package com.example.chatserver.common.response;

public record ChatServerResponse(

        Long propertyId,
        Long sellerId,
        Long bidderId,
        String propertyName,
        String propertyAddress,
        String bidderName
) {}

