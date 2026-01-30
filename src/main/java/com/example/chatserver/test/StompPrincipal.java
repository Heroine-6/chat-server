package com.example.chatserver.test;

import lombok.Getter;

import java.security.Principal;

@Getter
public class StompPrincipal implements Principal {

    private final String name;

    public StompPrincipal(String name) {
        this.name = name;
    }
}