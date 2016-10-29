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
        //replace extra newlines at end of message
        String cleanedBody = messageBody.replaceAll("[\\n\\s]+$", "");
        //message should be in format username> message
        String fullMessage = userName + "> " + cleanedBody;
        //trim message to be no more than max length
        //need an extra character for newline
        if(fullMessage.length() > Main.MESSAGE_MAX_LENGTH - 1){
            fullMessage = fullMessage.substring(0, Main.MESSAGE_MAX_LENGTH - 1);
        }
        //make sure to add newline at the end
        return fullMessage + "\n";

    }
}