package com.example.mcpro;

public class ChatMessage {
    public String message;
    public boolean isUser; // true = user, false = counsellor

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }
}
