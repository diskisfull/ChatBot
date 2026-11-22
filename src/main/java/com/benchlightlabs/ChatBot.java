package com.benchlightlabs;

public class ChatBot {

    public String reply(String message) {
        if (message == null || message.isBlank()) {
            return "Say something else!";
        }
        if (message.toLowerCase().contains("hello")) {
            return "Hello! How can I help?";
        }
        return "You said: " + message;
    }
}