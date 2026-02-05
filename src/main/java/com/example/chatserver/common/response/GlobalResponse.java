package com.example.chatserver.common.response;

import java.time.LocalDateTime;

public record GlobalResponse<T>(boolean success, T data, LocalDateTime timestamp) { }
