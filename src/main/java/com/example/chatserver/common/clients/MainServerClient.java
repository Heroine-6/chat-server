package com.example.chatserver.common.clients;

import com.example.chatserver.common.clients.dto.ChatServerResponse;
import com.example.chatserver.common.response.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "mainServerClient", url = "${main.server.url}")
public interface MainServerClient {

    @GetMapping("/api/v2/internal/chat/{propertyId}")
    GlobalResponse<ChatServerResponse> getChatContext(@RequestHeader String authorization, @PathVariable Long propertyId);
}
