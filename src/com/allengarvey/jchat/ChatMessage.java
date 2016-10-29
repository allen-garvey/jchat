package com.allengarvey.jchat;

/**
 * Created by allen on 10/29/16.
 */
public class ChatMessage {
    private String userName;
    private String messageBody;

    public ChatMessage(String userName, String messageBody) {
        this.userName = userName;
        this.messageBody = messageBody;
    }

    @Override
    public String toString() {
        //replace extra newlines at end of message
        String cleanedBody = messageBody.replaceAll("[\\n\\s]+$", "");
        return "UNAME: " + userName + " MBODY: " + cleanedBody;

    }
}
