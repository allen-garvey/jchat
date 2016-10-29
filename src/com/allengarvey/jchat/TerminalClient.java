package com.allengarvey.jchat;

/**
 * Created by allen on 10/29/16.
 */
public class TerminalClient{
    //shared flag between threads so that listenAction thread can communicate to
    //broadcast action to quit
    boolean isQuitting;
    //terminal client's username
    String userName;
    //index of last message successful broadcast
    private int currentMessageIndex;


    public TerminalClient(String userName){
        setUserName(userName);
        //initialize variables
        isQuitting = false;
        currentMessageIndex = 0;
    }

    //set username for terminal client and add to username's hash
    private void setUserName(String userName){
        //going to assume this succeeds, since userName is hardcoded
        Main.addUserName(userName);
        this.userName = userName;
    }

    void listenAction(){

    }

    //loop that gets unread messages since last checked and broadcasts them to client
    void broadcastAction(){
        //check if should stop, because client sent \quit
        while(!isQuitting){
            //get all unread messages, broadcast them, and increment index
            ChatMessage[] unreadMessages = Main.addGetNewMessages(null, currentMessageIndex);
            for(ChatMessage message : unreadMessages){
                //print message to console - no println because message already ends in \n
                System.out.print(message.toString());
                //message now broadcast, so increment index
                currentMessageIndex++;
            }
        }
    }
}
