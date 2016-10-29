package com.allengarvey.jchat;

/**
 * Created by allen on 10/29/16.
 */
//container for chat messages
//keeps track of message body and sender
public class ChatMessage {
    private String userName;
    private String messageBody;

    public ChatMessage(String userName, String messageBody) {
        this.userName = userName;
        this.messageBody = messageBody;
    }

    //formats message when used for sending
    @Override
    public String toString() {
        //replace extra newlines at end of message and make sure message ends in newline
        String cleanedBody = messageBody.replaceAll("[\\n\\s]+$", "") + "\n";
        return "MUSER: " + userName + "\tMBODY: " + cleanedBody;

    }
}
